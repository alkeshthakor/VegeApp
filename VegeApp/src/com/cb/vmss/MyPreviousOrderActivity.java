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
import android.widget.LinearLayout;
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
	private LinearLayout llEmptyOrder;
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
			mTitle.setText(getResources().getString(R.string.lbl_title_my_order));
			
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
        
        llEmptyOrder = (LinearLayout) findViewById(R.id.llEmptyOrder);
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
		if(cd.isConnectingToInternet()){
			fetchPreviousOrder();
		} else {
		    Toast.makeText(mContext,getString(R.string.lbl_network_connection_fail),Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void fetchPreviousOrder(){
		mServiceUrl=Constant.HOST+Constant.SERVICE_FETCH_PREVIOUS_ORDER;
		String parameter = Pref.getValue(Constant.PREF_USER_ID, "0");
		//parameter = "13";
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
						
						previousOrderItem.setOrderId(orderJSONObject.getString("od_id"));
						previousOrderItem.setOrderTotalPrice(orderJSONObject.getString("od_finalprice"));
						//orderJSONObject.getString("od_deliverytype");
						previousOrderItem.setOrderDeliveryDate(orderJSONObject.getString("od_deliverydate"));
						previousOrderItem.setOrderDeliveryTime(orderJSONObject.getString("od_deliverytime"));
						previousOrderItem.setOrderCreatedDate(orderJSONObject.getString("od_createddate"));
						previousOrderItem.setOrderCreatedTime(orderJSONObject.getString("od_createdtime"));
						previousOrderItem.setOrderStatus(orderJSONObject.getString("od_process"));
						previousOrderItem.setOrderPromoCode(orderJSONObject.getString("od_promocode"));
						previousOrderItem.setOrderCouponPrice(orderJSONObject.getString("od_coupon_price"));
						previousOrderItem.setOrderSubPrice(orderJSONObject.getString("od_subprice"));
						//orderJSONObject.getString("address_id");
						previousOrderItem.setUserName(orderJSONObject.getString("user_name"));
						//orderJSONObject.getString("phone_number");
						previousOrderItem.setAddressLine1(orderJSONObject.getString("address1"));
						previousOrderItem.setAddressLine2(orderJSONObject.getString("address2"));
						previousOrderItem.setAddressLandmark(orderJSONObject.getString("landmark"));
						previousOrderItem.setAddressCity(orderJSONObject.getString("city"));
						previousOrderItem.setAddressZipCode(orderJSONObject.getString("zipcode"));
						previousOrderItem.setTotalItem(""+orderJSONObject.getJSONArray("item").length());
						previousOrderItem.setJsonObject(orderJSONObject.toString());
						
						mPreviousOrderList.add(previousOrderItem);
					}
					if(mPreviousOrderList.size() > 0) {
						llEmptyOrder.setVisibility(View.GONE);
						previousOrderListView.setVisibility(View.VISIBLE);
						previousOrderListView.setAdapter(new PreviousOrderAdapter(MyPreviousOrderActivity.this, mPreviousOrderList));
					} else {
						llEmptyOrder.setVisibility(View.VISIBLE);
						previousOrderListView.setVisibility(View.GONE);
					}
				}
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