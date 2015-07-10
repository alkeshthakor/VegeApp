package com.cb.vmss;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

public class AddAddressActivity extends Activity implements OnClickListener {

	private Toolbar toolbar;
	private ImageView closeImageView;

	private EditText nameEditText;
	private EditText houseEditText;
	private EditText streetEditText;
	private EditText areaEditText;
	private EditText cityEditText;
	private EditText zipEditText;

	private Button createButton;
	private String mServiceUrl;
	private String addressBody;
	

	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_add_address));
			closeImageView = (ImageView) toolbar
					.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}

		mContext = this;

		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		mProgressDialog = new ProgressDialog(AddAddressActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        
		
		nameEditText = (EditText) findViewById(R.id.addNameEditText);
		houseEditText = (EditText) findViewById(R.id.addHouseEditText);
		streetEditText = (EditText) findViewById(R.id.addStreetEditText);
		areaEditText = (EditText) findViewById(R.id.addAreaEditText);
		cityEditText = (EditText) findViewById(R.id.addCityEditText);
		zipEditText = (EditText) findViewById(R.id.addZipEditText);

		createButton = (Button) findViewById(R.id.btnCreateAdd);
		createButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imgeCloseTopBar:
			finish();
			break;
		case R.id.btnCreateAdd:
			if(cd.isConnectingToInternet()){
				createAddress();	
			}else{
				Toast.makeText(mContext,"Internet connection not available",Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void createAddress(){
		if(!isEmptyField()){
			String userId = Pref.getValue(Constant.PREF_USER_ID, "0");
			if(!userId.equals("0")) {
				addressBody="usr_id=" + userId + "&add_id=&add_fullname="+nameEditText.getText().toString()+"&add_phone=2147483647"+"&add_address1="+houseEditText.getText().toString()
						+"&add_address2="+areaEditText.getText().toString()+"&add_landmark="+cityEditText.getText().toString()+"&add_zipcode="+zipEditText.getText().toString();
				 mServiceUrl=Constant.HOST+Constant.SERVICE_ADD_ADDRESS;
				 new addAddressOnServerTask().execute(mServiceUrl,addressBody);
			}
		} else {
			Toast.makeText(mContext,"Field should not be blank",Toast.LENGTH_SHORT).show();
		}
	}
	
	private class addAddressOnServerTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			
			return connector.getDataFromServer(params[0],params[1]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			try {
				if(result!=null&&result.getString("STATUS").equalsIgnoreCase("SUCCESS")){
					Toast.makeText(mContext,"Address added successfully",Toast.LENGTH_SHORT).show();;
					finish();
				}else{
					Toast.makeText(mContext,"Add address fail",Toast.LENGTH_SHORT).show();;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isEmptyField(){
		if(nameEditText.getText().toString().length()>0 && houseEditText.getText().toString().length()>0 && streetEditText.getText().toString().length()>0 && areaEditText.getText().toString().length()>0 && cityEditText.getText().toString().length()>0 && zipEditText.getText().toString().length()>0){
			return false;
		}else{
			return true;
		}
	}
}