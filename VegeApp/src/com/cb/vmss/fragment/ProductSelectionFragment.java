package com.cb.vmss.fragment;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cb.vmss.R;
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
		 
		 new LoadProdcutByCategoryTask().execute(mServiceUrl,"cat_id=10");
		 
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
		}
	}
	
}
