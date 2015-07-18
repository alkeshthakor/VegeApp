package com.cb.vmss;

import com.cb.vmss.util.Constant;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OrderSuccessActivity extends ActionBarActivity implements OnClickListener {

	private Toolbar toolbar;
	
	
	private Button checkOrderStatusButton;
	private Button coutinueShoppingButton;
	
	private TextView shiftTextView;
	private TextView timeTextView;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_success);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_thanks));

			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}
		
		
		shiftTextView=(TextView)findViewById(R.id.dayShiftTextViewOS);
		timeTextView=(TextView)findViewById(R.id.timeTextViewOS);
		
		checkOrderStatusButton=(Button)findViewById(R.id.btnCheckOrderStatus);
		coutinueShoppingButton=(Button)findViewById(R.id.btnCountinueShoppin);
		
		checkOrderStatusButton.setOnClickListener(this);
		coutinueShoppingButton.setOnClickListener(this);
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(Constant.CODE_MAIN_LOGIN);
        	finish(); 
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnCountinueShoppin:
			setResult(Constant.CODE_BACK_WITH_COUTINUE_SHOPPING);
			finish();
			break;
		case R.id.btnCheckOrderStatus:
			setResult(Constant.CODE_BACK_WITH_CHECK_ORDER);
			finish();
			break;
		}
	}
	
	
}
