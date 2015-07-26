package com.cb.vmss;

import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cb.vmss.util.Constant;

public class AboutUsActivity2 extends ActionBarActivity {

	private Toolbar toolbar;
	int mPaddingRigh=50;
	private RelativeLayout rlPrivacyPolicy;
	private RelativeLayout rlTermsConditions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us2);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_about_us_title));

			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
		}
		rlPrivacyPolicy = (RelativeLayout) findViewById(R.id.rlPrivacyPolicy);
		rlTermsConditions = (RelativeLayout) findViewById(R.id.rlTermsConditions);
		
		rlPrivacyPolicy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent aboutIntent=new Intent(getApplicationContext(),PrivacyPolicyActivity.class);
				startActivity(aboutIntent);				
			}
		});

		rlTermsConditions.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				Intent aboutIntent=new Intent(getApplicationContext(),TermsConditionsActivity.class);
				startActivity(aboutIntent);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
}
