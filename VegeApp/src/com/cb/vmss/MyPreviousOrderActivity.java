package com.cb.vmss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cb.vmss.adapter.AddressAdapter;
import com.cb.vmss.adapter.PreviousOrderAdapter;
import com.cb.vmss.model.Address;
import com.cb.vmss.model.PreviousOrder;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyPreviousOrderActivity extends ActionBarActivity implements OnClickListener,OnItemClickListener
{
	
	private Toolbar toolbar;
	private ProgressDialog mProgressDialog;
	ServerConnector connector;
	private String mServiceUrl;
	ListView previousOrderListView;
	public List<PreviousOrder> mPreviousOrderList;
	
	Context mContext;
	ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pervious_order);
		Constant.CONTEXT=this;
		
		mContext = this;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_previous_order));
			
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
			
		}
		
		mProgressDialog = new ProgressDialog(MyPreviousOrderActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        
		previousOrderListView = (ListView) findViewById(R.id.previousOrderListView);
		
		previousOrderListView.setOnItemClickListener(this);
		
	
		
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
	protected void onResume() {
		super.onResume();
		Constant.CONTEXT=this;
		
		if(cd.isConnectingToInternet()){
			fetchPreviousOrder();
		} else {
		    Toast.makeText(mContext,getString(R.string.lbl_network_connection_fail),Toast.LENGTH_SHORT).show();
		}
	}
	
	private void fetchPreviousOrder(){
		mServiceUrl=Constant.HOST+Constant.SERVICE_FETCH_PREVIOUS_ORDER;
		String parameter = Pref.getValue(Constant.PREF_USER_ID, "0");
		parameter = "13";
		if(!parameter.equals("0")) {
			new getPreviousOrderTask().execute(mServiceUrl,"usr_id=" + parameter);
		}
	}
	
	private class getPreviousOrderTask extends AsyncTask<String, Void, JSONObject> {

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
					JSONArray dataJSONArray = result.getJSONArray("DATA");
					mPreviousOrderList = new ArrayList<PreviousOrder>();
					for (int i=0; i< dataJSONArray.length(); i++) {
						JSONObject orderJSONObject = dataJSONArray.getJSONObject(i);
						PreviousOrder previousOrderItem = new PreviousOrder();
						previousOrderItem.setOrderId(orderJSONObject.getString("od_id")== null?"23":orderJSONObject.getString("od_id"));
						previousOrderItem.setOrderDate(orderJSONObject.getString("date") + " " + orderJSONObject.getString("time"));
						previousOrderItem.setTotalItem(""+orderJSONObject.getJSONArray("item").length());
						previousOrderItem.setOrderTotalPrice(orderJSONObject.getString("od_finalprice")== null?"2300":orderJSONObject.getString("od_finalprice"));
						previousOrderItem.setJsonObject(orderJSONObject.toString());
						mPreviousOrderList.add(previousOrderItem);
					}
					previousOrderListView.setAdapter(new PreviousOrderAdapter(MyPreviousOrderActivity.this, mPreviousOrderList));

					/*for (int i = 0; i < addressJsonArray.length(); i++) {
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
						mPreviousOrderList.add(addressItem);
					}
					previousOrderListView.setAdapter(new AddressAdapter(MyPreviousOrderActivity.this, mPreviousOrderList));
				*/}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	
		
	}

	@Override
	public void onClick(View v) {
		
	}
}