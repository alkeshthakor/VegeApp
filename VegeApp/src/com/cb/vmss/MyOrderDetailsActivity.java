package com.cb.vmss;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cb.vmss.adapter.ProductDetailsItemsAdapter;
import com.cb.vmss.model.OrderItems;
import com.cb.vmss.model.PreviousOrder;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.ServerConnector;
import com.cb.vmss.widget.ExpandedListView;

public class MyOrderDetailsActivity extends ActionBarActivity implements OnClickListener
{
	
	private Toolbar toolbar;
	TextView txtOrdDate;
	TextView txtOrdTime;
	TextView txtOrdNo;
	TextView txtStatus;
	TextView txtTotPrice;
	
	TextView txtUserName;
	TextView txtAddress1and2;
	TextView txtAddressLandmark;
	TextView txtAddresscity;
	TextView txtZipCode;
	
	public ExpandedListView orderItemListViewObj;
	TextView txtSubTotal;
	TextView txtDeliveryCharge;
	TextView txtPromoDiscount;
	TextView txtTotalAmt;
	
	View dividerPromoDiscount;
	LinearLayout llPromoDiscount;
	
	private ArrayList<OrderItems> mOrderItemsRowItem = new ArrayList<OrderItems>();
    
	ServerConnector connector;
	private PreviousOrder item;
	Context mContext;
	ConnectionDetector cd;
	
	private LinearLayout llCancelOrder;
	
	private ProgressDialog mProgressDialog;
	private String mServiceUrl;
	private String orderId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previous_order_details);
		Constant.CONTEXT=this;
		
		mContext = this;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		
		Constant.CONTEXT=mContext;

		mProgressDialog = new ProgressDialog(MyOrderDetailsActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_order_details));
			
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
		}
		
		if (getIntent() != null) {
			if (getIntent().getSerializableExtra("order") != null) {
				Serializable b = getIntent().getSerializableExtra("order");
				item = (PreviousOrder) b;
			}
		}
		
		txtOrdDate = (TextView)findViewById(R.id.txtOrdDate);
        txtOrdTime = (TextView)findViewById(R.id.txtOrdTime);
        txtOrdNo = (TextView)findViewById(R.id.txtOrdNo);
        txtStatus = (TextView)findViewById(R.id.statusTextView);
        txtTotPrice = (TextView)findViewById(R.id.totPriceTextView);
        
        txtOrdDate.setFocusable(true);
		txtOrdDate.setFocusableInTouchMode(true);	
		txtOrdDate.requestFocus();
        txtOrdNo = (TextView)findViewById(R.id.txtOrdNo);
        txtTotPrice = (TextView)findViewById(R.id.totPriceTextView);
        
        orderItemListViewObj = (ExpandedListView) findViewById(R.id.orderItemListView);
        txtSubTotal = (TextView)findViewById(R.id.txtSubTotal);
        txtDeliveryCharge = (TextView)findViewById(R.id.txtDeliveryCharge);
        txtPromoDiscount = (TextView)findViewById(R.id.txtPromoDiscount);
        txtTotalAmt = (TextView)findViewById(R.id.txtTotalAmt);
        
        txtUserName = (TextView)findViewById(R.id.txtNameAddress);
    	txtAddress1and2  = (TextView)findViewById(R.id.address1TextView);
    	txtAddressLandmark = (TextView)findViewById(R.id.address2TextView);
        txtAddresscity = (TextView)findViewById(R.id.address3TextView);
    	
    	llCancelOrder=(LinearLayout) findViewById(R.id.llCancelOrder);
        llCancelOrder.setOnClickListener(this);
        
        dividerPromoDiscount = (View) findViewById(R.id.dividerPromoDiscount);
        llPromoDiscount = (LinearLayout) findViewById(R.id.llPromoDiscount);    	
    	
        if(item.getOrderStatus().equalsIgnoreCase("Cancelled")) {
        	llCancelOrder.setVisibility(View.GONE);
        }
        orderId=item.getOrderId();
        
        txtOrdDate.setText(item.getOrderDate());
        txtOrdTime.setText(item.getOrderTime());
        txtOrdNo.setText("Order ID : " + item.getOrderId());
        txtStatus.setText("Status : " + item.getOrderStatus());
        txtTotPrice.setText(item.getOrderTotalPrice());
        
        txtUserName.setText(item.getUserName());
        txtAddress1and2.setText(item.getAddressLine1()+", "+item.getAddressLine2());
        txtAddressLandmark.setText(item.getAddressLandmark());
        txtAddresscity.setText(item.getAddressCity()+"-"+item.getAddressZipCode());
        
        int itemTotalPrice = 0;
        try {
    		
        	JSONObject orderJSONObject = new JSONObject(item.getJsonObject());
        	JSONArray orderItemJSONArray;
			orderItemJSONArray = orderJSONObject.getJSONArray("item");
			for(int i=0; i<orderItemJSONArray.length();i++) {
				OrderItems orderItem = new OrderItems();
				orderItem.setOrdItemName(orderItemJSONArray.getJSONObject(i).getString("prd_name"));
				orderItem.setOrdItemQty(orderItemJSONArray.getJSONObject(i).getString("item_quantity"));
				orderItem.setOrdItemPrice(orderItemJSONArray.getJSONObject(i).getString("item_price"));
				itemTotalPrice = itemTotalPrice + Integer.parseInt(orderItemJSONArray.getJSONObject(i).getString("item_totalprice"));
				mOrderItemsRowItem.add(orderItem);
			}
			
			if(orderJSONObject.getString("od_promocode").length() > 0) {
				if(orderJSONObject.getString("od_coupon_price").contains("%")) {
					int subTotal = Integer.parseInt(orderJSONObject.getString("od_subprice"));
					int couponPrice = Integer.parseInt(orderJSONObject.getString("od_coupon_price").replace("%", ""));
					int promoDiscount = couponPrice * subTotal / 100;
					txtPromoDiscount.setText(String.valueOf(promoDiscount));
				} else {
					int promoDiscount = Integer.parseInt(orderJSONObject.getString("od_coupon_price").replace("Rs.", ""));
					txtPromoDiscount.setText(String.valueOf(promoDiscount));
				}
			} else {
				dividerPromoDiscount.setVisibility(View.GONE);
				llPromoDiscount.setVisibility(View.GONE);
			}
        } catch (JSONException e) {
			e.printStackTrace();
		}
        
        orderItemListViewObj.setAdapter(new ProductDetailsItemsAdapter(MyOrderDetailsActivity.this, mOrderItemsRowItem));
        txtSubTotal.setText(""+itemTotalPrice);
        int totalPrize = itemTotalPrice;
        txtTotalAmt.setText(item.getOrderTotalPrice());
        
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.llCancelOrder:
			 if(cd.isConnectingToInternet()){
				 showConfirmCancel();
			    }else{
			    	Toast.makeText(mContext,getString(R.string.lbl_network_connection_fail),Toast.LENGTH_SHORT).show();
			    }
			break;
		}
	}
	
	private void showConfirmCancel() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MyOrderDetailsActivity.this);
			// set title
			alertDialogBuilder.setTitle("Cancel Alert");
			// set dialog message
			alertDialogBuilder
				.setMessage("Are you sure you want to cancel?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {

						mServiceUrl=Constant.HOST+Constant.SERVICE_CANCEL_ORDER;
						 new CancelOrderTask().execute(mServiceUrl,"order_id="+orderId);
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
    private class CancelOrderTask extends AsyncTask<String, Void, JSONObject> {
 	 
 		@Override
 		protected void onPreExecute() {
 			// TODO Auto-generated method stub
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
 			Toast.makeText(mContext,"Your order successfully canceled ",Toast.LENGTH_SHORT).show();
 			finish();
 		}
 	}    
}