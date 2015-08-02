package com.sabziatdoor.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sabziatdoor.R;
import com.sabziatdoor.ProductSelectionActivity;
import com.sabziatdoor.adapter.CategoryAdapter;
import com.sabziatdoor.model.Category;
import com.sabziatdoor.util.ConnectionDetector;
import com.sabziatdoor.util.Constant;
import com.sabziatdoor.util.ServerConnector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends Fragment {

	private Activity mActivity;
	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	private String mCatServiceUrl;
	private LinearLayout mCategoryLinearLayout;
	private LinearLayout.LayoutParams categoryViewParams;
	private ListView mCategoryListView;
	private CategoryAdapter mCategoryAdapter;
	
	
	private String categoryStringList[];
	public static List<Category> mCategoryList;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@SuppressLint("JavascriptInterface")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_layout, container,
				false);
		mContext = mActivity.getApplicationContext();
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
		//mCategoryLinearLayout=(LinearLayout)view.findViewById(R.id.categoryLinearLayout);
	   
		mCategoryListView=(ListView)view.findViewById(R.id.categoryListView);
		
        		
		mCatServiceUrl=Constant.HOST+Constant.SERVICE_GET_ALL_CATEGORY;
	    
	    if(cd.isConnectingToInternet()){
	    	new LoadCategoryTask().execute(mCatServiceUrl);	
	    }else{
	    	Toast.makeText(mContext,getString(R.string.lbl_network_connection_fail),Toast.LENGTH_SHORT).show();
	    }
	    
	    mCategoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent productIntent=new Intent(mContext,ProductSelectionActivity.class);
				productIntent.putExtra("cat_list",categoryStringList);	
				productIntent.putExtra("tabposition",position+"");
				productIntent.putExtra("cat_name",mCategoryList.get(position).getCategoryName());
				startActivityForResult(productIntent,Constant.CODE_MAIN_LOGIN);
				//startActivity(productIntent);
			}
		});
	    
	    
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	private class LoadCategoryTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			return connector.getServerResponse(params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			parseResponse(result);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void parseResponse(JSONObject responseData) {
		if(responseData!=null&&responseData.length()>0){
			//mCategoryLinearLayout.removeAllViews();
			 try {
				 JSONArray categoryArray=responseData.getJSONArray("DATA");
				 if(categoryArray.length()>0){
				     mCategoryList=new ArrayList<Category>();
				     categoryStringList=new String[categoryArray.length()];
				     
				     for(int i=0;i<categoryArray.length();i++){
						Category catItem=new Category();
						catItem.setCategoryId(categoryArray.getJSONObject(i).getString("cat_id"));
						catItem.setCategoryName(categoryArray.getJSONObject(i).getString("cat_name"));
						catItem.setCategoryImage(categoryArray.getJSONObject(i).getString("cat_image"));
						catItem.setCategroyDiscription(categoryArray.getJSONObject(i).getString("cat_desc"));
						
					    mCategoryList.add(catItem);
					    categoryStringList[i]=catItem.getCategoryName().toUpperCase();
				    }
				 }
				 
				 mCategoryAdapter=new CategoryAdapter(mContext, mCategoryList);
				 mCategoryListView.setAdapter(mCategoryAdapter);
				 
				 
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}
	}
	
/*	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		RelativeLayout bmImage;
		public DownloadImageTask(RelativeLayout bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Bitmap result) {
			Drawable imageDrawable = new BitmapDrawable(mActivity.getResources(), result);
			if(imageDrawable!=null)
			bmImage.setBackground(imageDrawable);
		}
	}
*/}
