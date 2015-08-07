package com.sabziatdoor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.sabziatdoor.R;
import com.sabziatdoor.database.VegAppDatabaseHelper;
import com.sabziatdoor.util.ConnectionDetector;
import com.sabziatdoor.util.Constant;
import com.sabziatdoor.util.Pref;
import com.sabziatdoor.util.ServerConnector;
import com.sabziatdoor.util.TimePickerFragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CheckOutActivity extends ActionBarActivity implements OnClickListener {

	private Toolbar toolbar;

	private TextView subTotalTextView;
	private TextView deliveryChargesTextView;
	private TextView totalTextView;
	private TextView timeTextView;
	private TextView promocodeTextView;

	private Button btnToday;
	private Button btnTomorrow;
	private Button btnDayAfter;
	private Button btnPlaceOrder;
	private Button btnPromoCodeFirst;
	private Button btnPromoCodeSecond;

	private EditText mPromoEditText;

	private Float totalAmount;

	private LinearLayout timerLinearLayout;
	private LinearLayout promocodeLinearLayout;

	private ViewFlipper promoCodeViewFlipper;

	private ProgressDialog mProgressDialog;
	ServerConnector connector;

	private String mServiceUrl;
	private String mOrderData;

	private VegAppDatabaseHelper mDatabaseHelper;
	private Context mContext;
	private ConnectionDetector cd;

	// DatePickerFragment mDatePicker;
	TimePickerFragment mTimePicker;

	private String parameterDate;
	private String mDialogTitle;
	private int mYear, mMonth, mDay;

	private int mHours, mMinute;
	private int timeMode;
	private String TimeValue;
	private String AM_PM;
	private String dayShift;

	Calendar calender;

	private Float totalValueAfterDiscount = 0f;

	private boolean hasText;
	private String validCouponCode = "";

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

		promoCodeViewFlipper = (ViewFlipper) findViewById(R.id.flipperPromoCode);

		timerLinearLayout = (LinearLayout) findViewById(R.id.timeLinerLayout);
		promocodeLinearLayout = (LinearLayout) findViewById(R.id.llPromocodeView);

		subTotalTextView = (TextView) findViewById(R.id.subTotalTextView);
		deliveryChargesTextView = (TextView) findViewById(R.id.deliveryChargeTextView);
		totalTextView = (TextView) findViewById(R.id.totalTextView);
		timeTextView = (TextView) findViewById(R.id.timeTextViewCheckOut);
		promocodeTextView = (TextView) findViewById(R.id.promocodeDiscountTextView);

		// btnPromoCode = (Button) findViewById(R.id.btnPromoCodeCheckOut);
		btnToday = (Button) findViewById(R.id.btnTodayCheckOut);
		btnTomorrow = (Button) findViewById(R.id.btnTommorowCheckOut);
		btnDayAfter = (Button) findViewById(R.id.btnDayAfterCheckOut);
		btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrderCheckOut);

		btnPromoCodeFirst = (Button) findViewById(R.id.btnApplyPromocodeFirst);
		btnPromoCodeSecond = (Button) findViewById(R.id.btnApplyPromocodeSecond);
		mPromoEditText = (EditText) findViewById(R.id.promoCodeEditTextCheckOut);

		// btnPromoCode.setOnClickListener(this);
		btnToday.setOnClickListener(this);
		btnTomorrow.setOnClickListener(this);
		btnDayAfter.setOnClickListener(this);
		btnPlaceOrder.setOnClickListener(this);
		btnPromoCodeFirst.setOnClickListener(this);
		btnPromoCodeSecond.setOnClickListener(this);

		timerLinearLayout.setOnClickListener(this);

		calender = Calendar.getInstance();
		calender.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

		mTimePicker = new TimePickerFragment(getApplicationContext());

		// mDatePicker = new DatePickerFragment(getApplicationContext());

		mDialogTitle = getString(R.string.lbl_today);
		setDateOnStartup();

		mPromoEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mPromoEditText.getText().length() > 2) {
					hasText = true;
					btnPromoCodeSecond.setText(getString(R.string.lbl_promo_apply));
				} else {
					hasText = false;
					btnPromoCodeSecond.setText(getString(R.string.lbl_promo_cancel));
				}
			}
		});

		totalAmount = Float.parseFloat(Pref.getValue(Constant.PREF_TOTAL_AMOUT, "0"));
		subTotalTextView.setText(totalAmount + "");
		totalTextView.setText(totalAmount + "");

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

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		/*
		 * case R.id.btnPromoCodeCheckOut: break;
		 */
		case R.id.btnTodayCheckOut:
			dayShift = "TODAY";
			setTodayDrawable();
			selectDate(getString(R.string.lbl_today));
			break;
		case R.id.btnTommorowCheckOut:
			dayShift = "TOMORROW";
			setTomorrowDrawable();
			selectDate(getString(R.string.lbl_tomorrow));
			break;
		case R.id.btnDayAfterCheckOut:
			dayShift = "DAYAFTER";
			setDayAfterDrawable();
			selectDate(getString(R.string.lbl_dayafter));
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
		case R.id.btnApplyPromocodeFirst:
			promoCodeViewFlipper.showNext();
			break;
		case R.id.btnApplyPromocodeSecond:
			if (hasText) {
				String promoCodeServiceUrl = Constant.HOST + Constant.SERVICE_PROMO_CODE;
				String promoCode = "od_chkuser_id=" + Pref.getValue(Constant.PREF_USER_ID, "") + "&od_chkpromocode="
						+ mPromoEditText.getText();
				new CheckPromoCodeTask().execute(promoCodeServiceUrl, promoCode);

			} else {
				promoCodeViewFlipper.showPrevious();
				promocodeLinearLayout.setVisibility(View.GONE);
				totalTextView.setText("" + totalAmount);
			}
			break;
		}
	}

	private class OrderPlacedTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
			mProgressDialog.setMessage("Please wait...");

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

					Intent orderSuccessIntent = new Intent(getApplicationContext(), OrderSuccessActivity.class);
					orderSuccessIntent.putExtra("time", timeTextView.getText().toString());
					orderSuccessIntent.putExtra("shift", dayShift);
					startActivityForResult(orderSuccessIntent, Constant.CODE_MAIN_LOGIN);

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
						try {
							String orderDataEncoded = URLEncoder.encode(orderData, "utf-8");
							
							mOrderData = "usr_id=" + Pref.getValue(Constant.PREF_USER_ID, "") + "&add_id="
									+ Pref.getValue(Constant.PREF_ADD_ID, "") + "&prd_data=" + orderDataEncoded
									+ "&od_deliverytype=" + dayShift + "&od_delivertytime=" + TimeValue + "&od_promocode="
									+ validCouponCode + "&gcm_regid="
									+ Pref.getValue(Constant.PREF_GCM_REGISTRATION_ID, "");

							mDatabaseHelper.close();
							new OrderPlacedTask().execute(mServiceUrl, mOrderData);
							
							
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						

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

	private void onPromoCodeFail(final JSONObject result) {
		try {
			String message = result.getString("MESSAGES").toString();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckOutActivity.this);
			alertDialogBuilder.setTitle("Error! Wrong Code");

			// set dialog message
			alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mPromoEditText.setText("");
							btnPromoCodeSecond.setText(getString(R.string.lbl_promo_cancel));
							promocodeLinearLayout.setVisibility(View.GONE);

						}
					});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void onPromoCodeSuccess(final JSONObject result) {

		try {
			// String message =
			// result.getJSONArray("MESSAGES").get(0).toString();
			String message = result.getString("MESSAGES").toString();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckOutActivity.this);
			alertDialogBuilder.setTitle("Congratulation!");
			// set dialog message
			alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							proccesPromoCode(result);
						}
					});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void proccesPromoCode(JSONObject result) {

		JSONObject promoCodeObject;
		try {
			promoCodeObject = result.getJSONObject("DATA");

			String couponType = promoCodeObject.getString("Coupon_type");
			validCouponCode = promoCodeObject.getString("Coupon_code");

			if (couponType.equals("amount")) {

				int discountAmount = promoCodeObject.getInt("Coupon_price");

				int couponMinPrice = promoCodeObject.getInt("coupon_min_price");
				if (totalAmount >= couponMinPrice) {
					promocodeLinearLayout.setVisibility(View.VISIBLE);
					totalValueAfterDiscount = totalAmount - discountAmount;
					promocodeTextView.setText(discountAmount + "");
					totalTextView.setText("" + totalValueAfterDiscount);
				} else {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckOutActivity.this);
					alertDialogBuilder.setTitle("Error!");
					// set dialog message
					String message = String.format(getString(R.string.lbl_promocode_minimum_validation),
							couponMinPrice + "");
					alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

								}
							});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}

			} else if (couponType.equals("discount")) {
				int couponDiscount = promoCodeObject.getInt("Coupon_discount");

				Float discountValue = (totalAmount * couponDiscount) / 100;
				totalValueAfterDiscount = totalAmount - discountValue;
				promocodeTextView.setText(discountValue + "");
				totalTextView.setText("" + totalValueAfterDiscount);
				promocodeLinearLayout.setVisibility(View.VISIBLE);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		mDialogTitle = mTitle;
		mTimePicker.setArguments(args);

		// mDatePicker.setArguments(args);
		/**
		 * Set Call back to capture selected date
		 */
		mTimePicker.setCallBack(mTimeSetListener);
		mTimePicker.show(getSupportFragmentManager(), "Time Picker");

	}

	OnTimeSetListener mTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub

			mHours = hourOfDay;
			mMinute = minute;

			if (dayShift.equalsIgnoreCase("TODAY")) {
				Calendar cal = Calendar.getInstance();
				int hours = cal.get(Calendar.HOUR_OF_DAY);
				if ((mHours - hours) < 1) {
					mHours++;
				}
			}

			if (isValidTime(mHours,mMinute)) {

				if (mHours < 10) {
					TimeValue = "0" + mHours;
				} else {
					TimeValue = "" + mHours;
				}

				if (mMinute < 10) {
					TimeValue = TimeValue + ":0" + mMinute;
				} else {
					TimeValue = TimeValue + ":" + mMinute;
				}

				if (hourOfDay < 12) {
					timeTextView.setText(TimeValue + " AM");
					AM_PM = "AM";
				} else {
					timeTextView.setText(TimeValue + " PM");
					AM_PM = "PM";
				}

			} else {
				timeOutError();
			}

		}
	};

	public void setDateOnStartup() {

		mHours = calender.get(Calendar.HOUR);
		mMinute = calender.get(Calendar.MINUTE);
		timeMode = calender.get(Calendar.AM_PM);

		dayShift = "TODAY";

		setTodayDrawable();

		if (timeMode == Calendar.AM) {
			if (mHours <= 9) {
				// calender.add(Calendar.HOUR,1);
				// mHours = calender.get(Calendar.HOUR);
				mHours = 11;
				mMinute = 00;
			}
		} else {
			if (mHours >= 6) {
				dayShift = "TOMORROW";
				btnToday.setEnabled(false);
				btnToday.setClickable(false);
				setTomorrowDrawable();
				mHours = 11;
				mMinute = 00;
				timeMode = 0;
			}
		}

		if (mHours < 10) {
			TimeValue = "0" + mHours;
		} else {
			TimeValue = "" + mHours;
		}

		if (mMinute < 10) {
			TimeValue = TimeValue + ":0" + mMinute;
		} else {
			TimeValue = TimeValue + ":" + mMinute;
		}

		if (timeMode == Calendar.AM) {
			timeTextView.setText(TimeValue + " AM");

		} else {
			timeTextView.setText(TimeValue + " PM");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Check which request we're responding to
		if (resultCode == Constant.CODE_MAIN_LOGIN) {
			// Make sure the request was successful
			setResult(Constant.CODE_MAIN_LOGIN);
			finish();
		} else if (resultCode == Constant.CODE_BACK_WITH_CHECK_ORDER) {
			setResult(Constant.CODE_BACK_WITH_CHECK_ORDER);
			finish();
		}
	}

	@SuppressWarnings("deprecation")
	public void setTodayDrawable() {
		btnToday.setBackground(getResources().getDrawable(R.drawable.button_background_with_fill_color));
		btnTomorrow.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnDayAfter.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));

		btnToday.setTextColor(Color.WHITE);
		btnTomorrow.setTextColor(Color.BLACK);
		btnDayAfter.setTextColor(Color.BLACK);

	}

	@SuppressWarnings("deprecation")
	public void setTomorrowDrawable() {
		btnToday.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnTomorrow.setBackground(getResources().getDrawable(R.drawable.button_background_with_fill_color));
		btnDayAfter.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));

		btnToday.setTextColor(Color.BLACK);
		btnTomorrow.setTextColor(Color.WHITE);
		btnDayAfter.setTextColor(Color.BLACK);

	}

	@SuppressWarnings("deprecation")
	public void setDayAfterDrawable() {
		btnToday.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnTomorrow.setBackground(getResources().getDrawable(R.drawable.button_background_with_border_color));
		btnDayAfter.setBackground(getResources().getDrawable(R.drawable.button_background_with_fill_color));

		btnToday.setTextColor(Color.BLACK);
		btnTomorrow.setTextColor(Color.BLACK);
		btnDayAfter.setTextColor(Color.WHITE);

	}

	private class CheckPromoCodeTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
			mProgressDialog.setMessage("Wait checking promo code...");
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
					onPromoCodeSuccess(result);
				} else {
					onPromoCodeFail(result);
					// mPromoEditText.setError("Invalid promo code");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isValidTime(int hourOfDay,int minute) {

		if (hourOfDay < 9 || hourOfDay > 18) {
			
			return false;
		} else {
			
			if(hourOfDay==18&&minute>0){
				return false;
			}else{
				return true;	
			}
		}
	}

	public void timeOutError() {

		AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this);
		builder.setTitle("Error!!!");
		builder.setMessage("Please choose delivery time between 9 am to 6 pm ");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
