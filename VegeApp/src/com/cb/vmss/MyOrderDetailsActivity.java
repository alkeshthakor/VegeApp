package com.cb.vmss;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cb.vmss.adapter.ProductDetailsItemsAdapter;
import com.cb.vmss.model.OrderItems;
import com.cb.vmss.model.PreviousOrder;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.ServerConnector;
import com.cb.vmss.widget.ExpandedListView;

public class MyOrderDetailsActivity extends ActionBarActivity
{
	
	private Toolbar toolbar;
	TextView txtOrdDate;
	TextView txtOrdNo;
	TextView txtTotItems;
	TextView txtTotPrice;
	
	public ExpandedListView orderItemListViewObj;
	TextView txtSubTotal;
	TextView txtDeliveryCharge;
	TextView txtTotalAmt;
	
	private ArrayList<OrderItems> mOrderItemsRowItem = new ArrayList<OrderItems>();
    
	ServerConnector connector;
	private PreviousOrder item;
	Context mContext;
	ConnectionDetector cd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previous_order_details);
		Constant.CONTEXT=this;
		
		mContext = this;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		
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
		
		txtOrdDate = (TextView)findViewById(R.id.txtOrdTime);
		txtOrdDate.setFocusable(true);
		txtOrdDate.setFocusableInTouchMode(true);	
		txtOrdDate.requestFocus();
        txtOrdNo = (TextView)findViewById(R.id.txtOrdNo);
        txtTotItems = (TextView)findViewById(R.id.totItemsTextView);
        txtTotPrice = (TextView)findViewById(R.id.totPriceTextView);
        
        orderItemListViewObj = (ExpandedListView) findViewById(R.id.orderItemListView);
        txtSubTotal = (TextView)findViewById(R.id.txtSubTotal);
        txtDeliveryCharge = (TextView)findViewById(R.id.txtDeliveryCharge);
        txtTotalAmt = (TextView)findViewById(R.id.txtTotalAmt);
        
        
        txtOrdDate.setText(item.getOrderDate());
        txtOrdNo.setText("Order Id : " + item.getOrderId());
        txtTotItems.setText("Order Items : " + item.getTotalItem());
        txtTotPrice.setText(item.getOrderTotalPrice());
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
        } catch (JSONException e) {
			e.printStackTrace();
		}
        
        orderItemListViewObj.setAdapter(new ProductDetailsItemsAdapter(MyOrderDetailsActivity.this, mOrderItemsRowItem));
        txtSubTotal.setText(""+itemTotalPrice);
        int totalPrize = itemTotalPrice + 49;
        txtTotalAmt.setText(""+totalPrize);
        
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
}