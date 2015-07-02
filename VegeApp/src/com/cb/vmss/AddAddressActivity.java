package com.cb.vmss;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class AddAddressActivity extends Activity implements OnClickListener{

	private Toolbar toolbar;
	private ImageView closeImageView;

	private EditText nameEditText;
	private EditText houseEditText;
	private EditText streetEditText;
	private EditText areaEditText;
	private EditText cityEditText;
	
	private Button createButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_add_address));
			closeImageView=(ImageView)toolbar.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}
	
		nameEditText=(EditText)findViewById(R.id.addNameEditText);
		houseEditText=(EditText)findViewById(R.id.addHouseEditText);
		streetEditText=(EditText)findViewById(R.id.addStreetEditText);
		areaEditText=(EditText)findViewById(R.id.addAreaEditText);
		cityEditText=(EditText)findViewById(R.id.addCityEditText);
		
		createButton=(Button)findViewById(R.id.btnCreateAdd);
		createButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.imgeCloseTopBar:
			finish();
			break;
		case R.id.btnCreateAdd:
			break;
			
		}
		
	}

}
