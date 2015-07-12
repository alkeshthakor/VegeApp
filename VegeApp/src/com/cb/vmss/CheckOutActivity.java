package com.cb.vmss;

import org.json.JSONException;
import org.json.JSONObject;

import com.cb.vmss.database.VegAppDatabaseHelper;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CheckOutActivity extends ActionBarActivity implements OnClickListener{

	private Toolbar toolbar;

	private TextView subTotalTextView;
	private TextView deliveryChargesTextView;
	private TextView totalTextView;
	private TextView timeTextView;
	
	private Button btnToday;
	private Button btnTomorrow;
	private Button btnDayAfter;
	private Button btnPlaceOrder;
	private Button btnPromoCode;
	
	private int totalAmount;
	
	private LinearLayout timerLinearLayout;
	
	private ProgressDialog mProgressDialog;
	ServerConnector connector;
	
	private String mServiceUrl;
	private String mOrderData;
	
	private VegAppDatabaseHelper mDatabaseHelper;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_out);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		mContext=this;
		Constant.CONTEXT=mContext;
		
		
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_check_out));
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
			
			
		}
				
		mDatabaseHelper=new VegAppDatabaseHelper(mContext);
		
		mProgressDialog = new ProgressDialog(CheckOutActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        connector = new ServerConnector();
		timerLinearLayout=(LinearLayout)findViewById(R.id.timeLinerLayout);
		
		subTotalTextView=(TextView)findViewById(R.id.subTotalTextView);
		deliveryChargesTextView=(TextView)findViewById(R.id.deliveryChargeTextView);
		totalTextView=(TextView)findViewById(R.id.totalTextView);
		timeTextView=(TextView)findViewById(R.id.timeTextViewCheckOut);
		
		btnPromoCode=(Button)findViewById(R.id.btnPromoCodeCheckOut);
		btnToday=(Button)findViewById(R.id.btnTodayCheckOut);
		btnTomorrow=(Button)findViewById(R.id.btnTommorowCheckOut);
		btnDayAfter=(Button)findViewById(R.id.btnDayAfterCheckOut);
		btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrderCheckOut);
		
		
		btnPromoCode.setOnClickListener(this);
		btnToday.setOnClickListener(this);
		btnTomorrow.setOnClickListener(this);
		btnDayAfter.setOnClickListener(this);
		btnPlaceOrder.setOnClickListener(this);
		
		timerLinearLayout.setOnClickListener(this);
			
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
		subTotalTextView.setText(totalAmount+"");
		totalTextView.setText(totalAmount+"");
		
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btnPromoCodeCheckOut:
			break;
		case R.id.btnTodayCheckOut:
			break;
		case R.id.btnTommorowCheckOut:
			break;
		case R.id.btnDayAfterCheckOut:
			break;
		case R.id.btnPlaceOrderCheckOut:
			ConfirmOrderPlaced();
			break;	
		case R.id.timeLinerLayout:
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
			
			return connector.submitOrder(params[0],params[1]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			try {
				if(result!=null&&result.getString("STATUS").equalsIgnoreCase("SUCCESS")){
					mDatabaseHelper.open();
					mDatabaseHelper.clearMayCart();
					mDatabaseHelper.close();
					
					Pref.setValue(Constant.PREF_QTY_COUNT,"0");
					Pref.setValue(Constant.PREF_TOTAL_AMOUT,"0");
					
					orderSuccessInfom();
					
				}else{
					Toast.makeText(mContext,"Order submiting fail.",Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void ConfirmOrderPlaced() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				CheckOutActivity.this);
			// set title
			alertDialogBuilder.setTitle("Order Placed");
			// set dialog message
			alertDialogBuilder
				.setMessage("Are you sure, Do you want to place order?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						mServiceUrl=Constant.HOST+Constant.SERVICE_ADD_ORDER;
						mDatabaseHelper.open();
						String orderData=mDatabaseHelper.getOrderItem().toString();
						
						mOrderData="usr_id="+Pref.getValue(Constant.PREF_USER_ID,"")+
							    "&add_id="+Pref.getValue(Constant.PREF_ADD_ID,"")+
							    "&prd_data=\""+orderData+"\"";
						mDatabaseHelper.close();
						new OrderPlacedTask().execute(mServiceUrl,mOrderData);
						
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						dialog.cancel();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
	}
	
	
	private void orderSuccessInfom(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				CheckOutActivity.this);
			// set dialog message
			alertDialogBuilder
				.setMessage("Your order successfully placed!!")
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						//MainActivity.this.finish();
						
						setResult(Constant.CODE_MAIN_LOGIN);
						finish();
					}
				  });
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
}
