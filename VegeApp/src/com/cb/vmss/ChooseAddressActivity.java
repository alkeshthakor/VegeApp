package com.cb.vmss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseAddressActivity extends Activity implements OnClickListener{

	
	private Toolbar toolbar;
	private ImageView closeImageView;
	private Button addAddressBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chosee_address);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_choose_address));
			closeImageView=(ImageView)toolbar.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}
		
		addAddressBtn =(Button) findViewById(R.id.btnAddAddressChose);
		addAddressBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.imgeCloseTopBar:
			finish();
			break;
		case R.id.btnAddAddressChose:
			Intent addressIntent=new Intent(getApplicationContext(), AddAddressActivity.class);
			startActivity(addressIntent);
			
			break;
		}
		
	}
	
}
