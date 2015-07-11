package com.cb.vmss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cb.vmss.adapter.AddressAdapter;
import com.cb.vmss.model.Address;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseAddressActivity extends Activity implements OnClickListener,OnItemClickListener
{
	
	private Toolbar toolbar;
	private ImageView closeImageView;
	private Button addAddressBtn;
	private ProgressDialog mProgressDialog;
	ServerConnector connector;
	private String mServiceUrl;
	ListView addressListView;
	public List<Address> mAddresstList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chosee_address);
		Constant.CONTEXT=this;
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
		addressListView = (ListView) findViewById(R.id.addressListView);
		
		addressListView.setOnItemClickListener(this);
		
	
		
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
		Constant.CONTEXT=this;
		fetchAddress();
		
	}
	
	private void fetchAddress(){
		mServiceUrl=Constant.HOST+Constant.SERVICE_FETCH_ADDRESS;
		String parameter = Pref.getValue(Constant.PREF_USER_ID, "0");
		if(!parameter.equals("0")) {
			new addAddressOnServerTask().execute(mServiceUrl,"usr_id=" + parameter);
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
				if(result!=null&&result.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
					JSONArray addressJsonArray = result.getJSONArray("DATA");
					mAddresstList = new ArrayList<Address>();
					for (int i = 0; i < addressJsonArray.length(); i++) {
						Address addressItem = new Address();
						addressItem.setAddId(addressJsonArray.getJSONObject(i).getString("add_id"));
						addressItem.setAddUserId(addressJsonArray.getJSONObject(i).getString("add_usr_id"));
						addressItem.setAddFullName(addressJsonArray.getJSONObject(i).getString("add_fullname"));
						addressItem.setAddPhone(addressJsonArray.getJSONObject(i).getString("add_phone"));
						addressItem.setAddAddress1(addressJsonArray.getJSONObject(i).getString("add_address1"));
						addressItem.setAddAddress2(addressJsonArray.getJSONObject(i).getString("add_address2"));
						addressItem.setAddLandmark(addressJsonArray.getJSONObject(i).getString("add_landmark"));
						addressItem.setAddCity(addressJsonArray.getJSONObject(i).getString("add_city"));
						addressItem.setAddZipCode(addressJsonArray.getJSONObject(i).getString("add_zipcode"));
						addressItem.setAddCreatedDate(addressJsonArray.getJSONObject(i).getString("add_createddate"));
						addressItem.setAddUpdatedDate(addressJsonArray.getJSONObject(i).getString("add_updateddate"));
						addressItem.setAddStatus(addressJsonArray.getJSONObject(i).getString("add_status"));
						mAddresstList.add(addressItem);
					}
					addressListView.setAdapter(new AddressAdapter(ChooseAddressActivity.this, mAddresstList));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		    
		Address mAddress=mAddresstList.get(position);
		
		String addressId=mAddress.getAddId();
		
		String defaultAddresss=mAddress.getAddFullName()+"\n"+
		mAddress.getAddAddress1()+",\n"+ mAddress.getAddAddress2()+",\n"+
		mAddress.getAddLandmark()+", "+mAddress.getAddCity()+",\n"+mAddress.getAddZipCode();
				
		Pref.setValue(Constant.PREF_ADD_ID,addressId);
		Pref.setValue(Constant.PREF_ADDRESS, defaultAddresss);
		
		
	}
}