package com.cb.vmss;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cb.vmss.util.Constant;
import com.cb.vmss.util.ServerConnector;

public class ChooseAddressActivity extends Activity implements OnClickListener{
	
	private Toolbar toolbar;
	private ImageView closeImageView;
	private Button addAddressBtn;
	private ProgressDialog mProgressDialog;
	ServerConnector connector;
	private String mServiceUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chosee_address);
		
		connector = new ServerConnector();
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_choose_address));
			closeImageView=(ImageView)toolbar.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}
		
		mProgressDialog = new ProgressDialog(ChooseAddressActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        
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
	
	@Override
	protected void onResume() {
		super.onResume();
		fetchAddress();
	}
	
	private void fetchAddress(){
		mServiceUrl=Constant.HOST+Constant.SERVICE_FETCH_ADDRESS;
		new addAddressOnServerTask().execute(mServiceUrl,"usr_id=47");
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
				if(result!=null&&result.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
					//Toast.makeText(ChooseAddressActivity.this,"" + result.toString(),Toast.LENGTH_SHORT).show();
				} else {
					//Toast.makeText(ChooseAddressActivity.this,"Add address fail",Toast.LENGTH_SHORT).show();;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}