package com.cb.vmss;

import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckOutActivity extends Activity implements OnClickListener{

	private Toolbar toolbar;
	private ImageView closeImageView;

	private TextView subTotalTextView;
	private TextView deliveryChargesTextView;
	private TextView totalTextView;
	private TextView timeTextView;
	
	private Button btnToday;
	private Button btnTomorrow;
	private Button btnDayAfter;
	private Button btnBack;
	private Button btnPlaceOrder;
	private Button btnPromoCode;
	
	private int totalAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_out);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_check_out));
			closeImageView=(ImageView)toolbar.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}
		
		
		subTotalTextView=(TextView)findViewById(R.id.subTotalTextView);
		deliveryChargesTextView=(TextView)findViewById(R.id.deliveryChargeTextView);
		totalTextView=(TextView)findViewById(R.id.totalTextView);
		timeTextView=(TextView)findViewById(R.id.timeTextViewCheckOut);
		
		btnPromoCode=(Button)findViewById(R.id.btnPromoCodeCheckOut);
		btnToday=(Button)findViewById(R.id.btnTodayCheckOut);
		btnTomorrow=(Button)findViewById(R.id.btnTommorowCheckOut);
		btnDayAfter=(Button)findViewById(R.id.btnDayAfterCheckOut);
		btnBack=(Button)findViewById(R.id.btnBackCheckOut);
		btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrderCheckOut);
		
		
		btnBack.setOnClickListener(this);
		btnPromoCode.setOnClickListener(this);
		btnToday.setOnClickListener(this);
		btnTomorrow.setOnClickListener(this);
		btnDayAfter.setOnClickListener(this);
		btnPlaceOrder.setOnClickListener(this);
		
		

				
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
		case R.id.imgeCloseTopBar:
			finish();
			break;
		case R.id.btnPromoCodeCheckOut:
			break;
		case R.id.btnTodayCheckOut:
			break;
		case R.id.btnTommorowCheckOut:
			break;
		case R.id.btnDayAfterCheckOut:
			break;
		case R.id.btnBackCheckOut:
			finish();
			
			break;
		case R.id.btnPlaceOrderCheckOut:
			break;			
		}
	}
}
