package com.cb.vmss;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyCartActivity extends Activity implements OnClickListener{
	private Toolbar toolbar;
	private ImageView closeImageView;
	
	private ListView myCartListView;
	private LinearLayout checkOutOutLinearLayout,nextCheckoutLinearLayout;
	
	Context mContext;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_cart);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText("My Cart (0)");
			closeImageView = (ImageView) toolbar
					.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}

		mContext = this;
		myCartListView=(ListView)findViewById(R.id.myCartListView);
		checkOutOutLinearLayout=(LinearLayout)findViewById(R.id.bottomBarMyCart);
		checkOutOutLinearLayout.setOnClickListener(this);
		
		
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imgeCloseTopBar:
			finish();
			break;
		}
	}
}
