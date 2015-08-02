package com.sabziatdoor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sabziatdoor.R;
import com.sabziatdoor.adapter.AddressAdapter;
import com.sabziatdoor.model.Address;
import com.sabziatdoor.util.ConnectionDetector;
import com.sabziatdoor.util.Constant;
import com.sabziatdoor.util.Pref;
import com.sabziatdoor.util.ServerConnector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAddressActivity extends ActionBarActivity implements OnClickListener,OnItemClickListener
{
	
	private Toolbar toolbar;
	private Button addAddressBtn;
	private ProgressDialog mProgressDialog;
	private String mServiceUrl;
	private String mFromScreenName;
	private LinearLayout llEmptyAddress;
	public List<Address> mAddresstList;
	
	ListView addressListView;
	
	Context mContext;
	ConnectionDetector cd;
	ServerConnector connector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_address);
		Constant.CONTEXT=this;
		
		mContext = this;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_choose_address));
			
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
			
		}
		
		mFromScreenName=getIntent().getStringExtra("fromscreen");
		
		mProgressDialog = new ProgressDialog(ChooseAddressActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        
        addAddressBtn =(Button) findViewById(R.id.btnAddAddressChose);
		addAddressBtn.setOnClickListener(this);
		addressListView = (ListView) findViewById(R.id.addressListView);
		llEmptyAddress = (LinearLayout) findViewById(R.id.llEmptyAddress);
		addressListView.setOnItemClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
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
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
	
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
		
		if(cd.isConnectingToInternet()){
			fetchAddress();
		    }else{
		    	Toast.makeText(mContext,getString(R.string.lbl_network_connection_fail),Toast.LENGTH_SHORT).show();
		    }
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
						addressItem.setAddZipCodeName(addressJsonArray.getJSONObject(i).getString("zip_code"));
						addressItem.setAddZipCode(addressJsonArray.getJSONObject(i).getString("add_zipcode"));
						addressItem.setAddCreatedDate(addressJsonArray.getJSONObject(i).getString("add_createddate"));
						addressItem.setAddUpdatedDate(addressJsonArray.getJSONObject(i).getString("add_updateddate"));
						addressItem.setAddStatus(addressJsonArray.getJSONObject(i).getString("add_status"));
						mAddresstList.add(addressItem);
					}
					if(mAddresstList.size() > 0) {
						llEmptyAddress.setVisibility(View.GONE);
						addressListView.setVisibility(View.VISIBLE);
						addressListView.setAdapter(new AddressAdapter(ChooseAddressActivity.this, mAddresstList));
					} else {
						llEmptyAddress.setVisibility(View.VISIBLE);
						addressListView.setVisibility(View.GONE);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		if(!mFromScreenName.equalsIgnoreCase(MainActivity.class.getCanonicalName().toString())){
			
			Address mAddress=mAddresstList.get(position);
			
			String addressId=mAddress.getAddId();
			String defaultAddresss= mAddress.getAddAddress2()+", "+mAddress.getAddLandmark();
			
			Pref.setValue(Constant.PREF_ADD_ID,addressId);
			Pref.setValue(Constant.PREF_ADDRESS, defaultAddresss);
			
			Intent mCheckOutIntent=new Intent(getApplicationContext(),CheckOutActivity.class);
			startActivityForResult(mCheckOutIntent,Constant.CODE_MAIN_LOGIN);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Check which request we're responding to
	    if (resultCode == Constant.CODE_MAIN_LOGIN) {
	        // Make sure the request was successful
	        	setResult(Constant.CODE_MAIN_LOGIN);
	        	finish();    
	    }else if (resultCode == Constant.CODE_BACK_WITH_CHECK_ORDER) {
	    	   setResult(Constant.CODE_BACK_WITH_CHECK_ORDER);
        	   finish(); 
	    }
	}
}