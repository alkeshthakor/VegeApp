package com.cb.vmss.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cb.vmss.R;
import com.cb.vmss.activity.ProductSelectionActivity;
import com.cb.vmss.adapter.ProductAdapter;
import com.cb.vmss.model.Category;
import com.cb.vmss.model.Product;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.ServerConnector;

public class ProductSelectionFragment extends Fragment {

	private Activity mActivity;

	ListView mProductListView;
	Bundle argumentBundle;
	private String mServiceUrl;
	
	
	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	private ArrayList<Product> productArrayList = new ArrayList<Product>();
	
	public static List<Product> mProductList;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
	}
	
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		 View view=inflater.inflate(R.layout.fragment_layout_product_selection, container,false);
		 mProductListView=(ListView)view.findViewById(R.id.productList);
		 argumentBundle=this.getArguments();
		 mContext = mActivity.getApplicationContext();
	
				cd = new ConnectionDetector(mContext);
				connector = new ServerConnector();
				mProgressDialog = new ProgressDialog(mActivity);
				mProgressDialog.setMessage("Please wait...");
				mProgressDialog.setIndeterminate(false);
			
		 
		 mServiceUrl=Constant.HOST+Constant.SERVICE_PRODUCT_BY_CAT_ID;
		 if(argumentBundle.getString("Category").equalsIgnoreCase("Vegetables")) {
			 new LoadProdcutByCategoryTask().execute(mServiceUrl,"cat_id=10");
		 } else {
			 new LoadProdcutByCategoryTask().execute(mServiceUrl,"cat_id=11");
		 }
		
		 
		 return view;
		 
	}
	
	private class LoadProdcutByCategoryTask extends AsyncTask<String, Void, JSONObject> {

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
			
			parseResponse(result);
			
		}
	}
	
	private void parseResponse(JSONObject responseData) {
		if(responseData!=null&&responseData.length()>0) {
			try {
				 JSONArray productArray=responseData.getJSONArray("DATA");
				 if(productArray.length()>0){
					
				     mProductList=new ArrayList<Product>();
				     
				     for(int i=0;i<productArray.length();i++){
				    	 Product productItem=new Product();
				    	 productItem.setProductId(productArray.getJSONObject(i).getString("prd_id"));
				    	 productItem.setProductName(productArray.getJSONObject(i).getString("prd_name"));
				    	 productItem.setProductImage(productArray.getJSONObject(i).getString("prd_mainimage"));
				    	 
				    	 productItem.setProductMainPrice(productArray.getJSONObject(i).getString("prd_mainprice"));
				    	 productItem.setProductDisplayPrice(productArray.getJSONObject(i).getString("prd_displayprice"));
				    	 productItem.setProductUnitId(productArray.getJSONObject(i).getString("prd_unit_id"));
				    	 
				    	 productItem.setCategoryId(productArray.getJSONObject(i).getString("cat_id"));
				    	 productItem.setCategoryName(productArray.getJSONObject(i).getString("cat_name"));
				    	 productItem.setUnit_key(productArray.getJSONObject(i).getString("unit_key"));
				    	 productItem.setUnit_value(productArray.getJSONObject(i).getString("unit_value"));
				    	 
				    	 mProductList.add(productItem);
					    
				   }
				     mProductListView.setAdapter(new ProductAdapter(mActivity,mProductList));
				 }else{
					 
				 }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		}
}
