package com.cb.vmss;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoseeAddressActivity extends Activity implements OnClickListener{

	
	private Toolbar toolbar;
	private ImageView closeImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chosee_address);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_add_address));
			closeImageView=(ImageView)toolbar.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.imgeCloseTopBar:
			finish();
			break;
		}
		
	}
	
}
