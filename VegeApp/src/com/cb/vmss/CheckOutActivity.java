package com.cb.vmss;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.cb.vmss.database.VegAppDatabaseHelper;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.DatePickerFragment;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;
import com.cb.vmss.util.TimePickerFragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CheckOutActivity extends ActionBarActivity implements OnClickListener {

	private Toolbar toolbar;

	private TextView subTotalTextView;
	private TextView deliveryChargesTextView;
	private TextView totalTextView;
	private TextView timeTextView;

	private Button btnToday;
	private Button btnTomorrow;
	private Button btnDayAfter;
	private Button btnPlaceOrder;
	//private Button btnPromoCode;

	private EditText mPromoEditText;
	
	private int totalAmount;

	private LinearLayout timerLinearLayout;

	private ProgressDialog mProgressDialog;
	ServerConnector connector;

	private String mServiceUrl;
	private String mOrderData;

	private VegAppDatabaseHelper mDatabaseHelper;
	private Context mContext;

	private ConnectionDetector cd;

	//DatePickerFragment mDatePicker;
	TimePickerFragment mTimePicker;
	

	private String parameterDate;
	private String mDialogTitle;
	private int mYear, mMonth, mDay;
	
	private int mHours, mMinute;
	private int timeMode;
	private String TimeValue;
	private String AM_PM ;
	private String dayShift;
	
	
	Calendar calender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_out);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		mContext = this;
		Constant.CONTEXT = mContext;
		cd = new ConnectionDetector(mContext);

		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_check_out));
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}

		mDatabaseHelper = new VegAppDatabaseHelper(mContext);

		mProgressDialog = new ProgressDialog(CheckOutActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		connector = new ServerConnector();
		timerLinearLayout = (LinearLayout) findViewById(R.id.timeLinerLayout);

		subTotalTextView = (TextView) findViewById(R.id.subTotalTextView);
		deliveryChargesTextView = (TextView) findViewById(R.id.deliveryChargeTextView);
		totalTextView = (TextView) findViewById(R.id.totalTextView);
		timeTextView = (TextView) findViewById(R.id.timeTextViewCheckOut);

		//btnPromoCode = (Button) findViewById(R.id.btnPromoCodeCheckOut);
		btnToday = (Button) findViewById(R.id.btnTodayCheckOut);
		btnTomorrow = (Button) findViewById(R.id.btnTommorowCheckOut);
		btnDayAfter = (Button) findViewById(R.id.btnDayAfterCheckOut);
		btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrderCheckOut);

		mPromoEditText=(EditText)findViewById(R.id.promoCodeEditTextCheckOut);
		
		
		//btnPromoCode.setOnClickListener(this);
		btnToday.setOnClickListener(this);
		btnTomorrow.setOnClickListener(this);
		btnDayAfter.setOnClickListener(this);
		btnPlaceOrder.setOnClickListener(this);

		timerLinearLayout.setOnClickListener(this);

		calender = Calendar.getInstance();
		calender.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		
		mTimePicker=new TimePickerFragment(getApplicationContext());
		
		//mDatePicker = new DatePickerFragment(getApplicationContext());
		
		
		mDialogTitle=getString(R.string.lbl_today);
		setDateOnStartup();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(Constant.CODE_BACK);
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		totalAmount = Integer.parseInt(Pref.getValue(Constant.PREF_TOTAL_AMOUT, "0"));
		subTotalTextView.setText(totalAmount + "");
		totalTextView.setText(totalAmount + "");

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		/*case R.id.btnPromoCodeCheckOut:
			break;*/
		case R.id.btnTodayCheckOut:
			setTodayDrawable();
			selectDate(getString(R.string.lbl_today));
			dayShift="TODAY";
			break;
		case R.id.btnTommorowCheckOut:
			setTomorrowDrawable();
			selectDate(getString(R.string.lbl_tomorrow));
			dayShift="TOMORROW";
			break;
		case R.id.btnDayAfterCheckOut:
			setDayAfterDrawable();
			selectDate(getString(R.string.lbl_dayafter));
			dayShift="DAYAFTER";
			break;
		case R.id.btnPlaceOrderCheckOut:
			if (cd.isConnectingToInternet()) {
				ConfirmOrderPlaced();
			} else {
				Toast.makeText(mContext, getString(R.string.lbl_network_connection_fail), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.timeLinerLayout:
			selectDate(mDialogTitle);
			break;
		}
	}

	private class OrderPlacedTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			return connector.submitOrder(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			try {
				if (result != null && result.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
					mDatabaseHelper.open();
					mDatabaseHelper.clearMayCart();
					mDatabaseHelper.close();

					Pref.setValue(Constant.PREF_QTY_COUNT, "0");
					Pref.setValue(Constant.PREF_TOTAL_AMOUT, "0");

					Intent orderSuccessIntent=new Intent(getApplicationContext(), OrderSuccessActivity.class);
					orderSuccessIntent.putExtra("time",timeTextView.getText().toString());
					orderSuccessIntent.putExtra("shift",dayShift);
					startActivityForResult(orderSuccessIntent,Constant.CODE_MAIN_LOGIN);
					
				} else {
					Toast.makeText(mContext, "Order submiting fail.", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void ConfirmOrderPlaced() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckOutActivity.this);
		// set title
		alertDialogBuilder.setTitle("Order Placed");
		// set dialog message
		alertDialogBuilder.setMessage("Are you sure, Do you want to place order?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mServiceUrl = Constant.HOST + Constant.SERVICE_ADD_ORDER;
						mDatabaseHelper.open();
						String orderData = mDatabaseHelper.getOrderItem().toString();

						mOrderData = "usr_id=" + Pref.getValue(Constant.PREF_USER_ID, "") + "&add_id="
								+ Pref.getValue(Constant.PREF_ADD_ID, "") + "&prd_data=\"" + orderData + "\""
								+"&od_deliverytype="+dayShift+"&od_delivertytime="+TimeValue+"&od_promocode="+mPromoEditText.getText().toString();
						
						mDatabaseHelper.close();
						new OrderPlacedTask().execute(mServiceUrl, mOrderData);

					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

	private void orderSuccessInfom() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckOutActivity.this);
		// set dialog message
		alertDialogBuilder.setMessage("Your order successfully placed!!").setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						// MainActivity.this.finish();

						setResult(Constant.CODE_MAIN_LOGIN);
						finish();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void selectDate(String mTitle) {

		/**
		 * Set Up Current Date Into dialog
		 */
		// Calendar calender = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("hours", mHours);
		args.putInt("minute", mMinute);
		args.putString("title", mTitle);
		mDialogTitle=mTitle;
		mTimePicker.setArguments(args);
		
		//mDatePicker.setArguments(args);
		/**
		 * Set Call back to capture selected date
		 */
		mTimePicker.setCallBack(mTimeSetListener);
		mTimePicker.show(getSupportFragmentManager(), "Time Picker");
	    
	}

	OnTimeSetListener mTimeSetListener=new OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHours=hourOfDay;
			mMinute=minute;
			
			TimeValue=mHours+":"+mMinute;
			
			if(hourOfDay < 12){
				timeTextView.setText(TimeValue+" AM");
				AM_PM = "AM";
			}else{
				timeTextView.setText(TimeValue+" PM");
				AM_PM = "PM";
			}
						  
		}
	};
	

	public void setDateOnStartup() {

		mHours=calender.get(Calendar.HOUR);
		mMinute=calender.get(Calendar.MINUTE);
		
		timeMode=calender.get(Calendar.AM);
		
		TimeValue=mHours+":"+mMinute;
		
		if(timeMode==0){			
			timeTextView.setText(TimeValue+" AM");
			
		}else{
			timeTextView.setText(TimeValue+" PM");
		}
		
		dayShift="TODAY";
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Check which request we're responding to
	    if (resultCode == Constant.CODE_MAIN_LOGIN) {
	        // Make sure the request was successful
	        	setResult(Constant.CODE_MAIN_LOGIN);
	        	finish();    
	    }else if (resultCode == Constant.CODE_BACK_WITH_CHECK_ORDER) {
	    	   setResult(Constant.CODE_BACK_WITH_CHECK_ORDER);
        	   finish(); 
	    }else if (resultCode == Constant.CODE_BACK_WITH_COUTINUE_SHOPPING) {
	    	   setResult(Constant.CODE_BACK_WITH_COUTINUE_SHOPPING);
        	   finish(); 
	    }
	    
	}
	
	@SuppressWarnings("deprecation")
	public void setTodayDrawable(){
		btnToday.setBackground(getResources().getDrawable(R.drawable.button_background_with_fill_color));
		btnTomorrow.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnDayAfter.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		
		btnToday.setTextColor(Color.WHITE);
		btnTomorrow.setTextColor(Color.BLACK);
		btnDayAfter.setTextColor(Color.BLACK);

		
	}
	
	@SuppressWarnings("deprecation")
	public void setTomorrowDrawable(){
		btnToday.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnTomorrow.setBackground(getResources().getDrawable(R.drawable.button_background_with_fill_color));
		btnDayAfter.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		
		btnToday.setTextColor(Color.BLACK);
		btnTomorrow.setTextColor(Color.WHITE);
		btnDayAfter.setTextColor(Color.BLACK);
		
	}
	
	@SuppressWarnings("deprecation")
	public void setDayAfterDrawable(){
		btnToday.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnTomorrow.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnDayAfter.setBackground(getResources().getDrawable(R.drawable.button_background_with_fill_color));
		
		btnToday.setTextColor(Color.BLACK);
		btnTomorrow.setTextColor(Color.BLACK);
		btnDayAfter.setTextColor(Color.WHITE);
		
	}
	
}
