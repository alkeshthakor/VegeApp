package com.cb.vmss.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cb.vmss.R;
import com.cb.vmss.activity.ProductSelectionActivity;
import com.cb.vmss.model.Category;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.ServerConnector;

@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends Fragment {

	private Activity mActivity;
	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	private String mCatServiceUrl;
	private LinearLayout mCategoryLinearLayout;
	private String categoryStringList[];
	public static List<com.cb.vmss.model.Category> mCategoryList;
	
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
		mCategoryLinearLayout=(LinearLayout)view.findViewById(R.id.categoryLinearLayout);
	    mCatServiceUrl=Constant.HOST+Constant.SERVICE_GET_ALL_CATEGORY;
        new LoadCategoryTask().execute(mCatServiceUrl);
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
			mCategoryLinearLayout.removeAllViews();
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
					    mCategoryList.add(catItem);
					    categoryStringList[i]=catItem.getCategoryName();
					     View view = new View(mContext);
				         view = mActivity.getLayoutInflater().inflate(R.layout.layout_category_item, null);
						 TextView mNameTextView=(TextView)view.findViewById(R.id.categoryNameTextView);
						 mNameTextView.setText(catItem.getCategoryName());
						 ImageView catImageView=(ImageView)view.findViewById(R.id.categoryImageView);
						 catImageView.setTag(i);
						 catImageView.setId(i);
						 
						 new DownloadImageTask(catImageView).execute(catItem.getCategoryImage());
						 catImageView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent productIntent=new Intent(mContext,ProductSelectionActivity.class);
								productIntent.putExtra("cat_list",categoryStringList);	
								productIntent.putExtra("tabposition",mCategoryList.get(v.getId()).getCategoryName());
								startActivity(productIntent);
							}
						 });
						 mCategoryLinearLayout.addView(view);
				    }
				 }
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		public DownloadImageTask(ImageView bmImage) {
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
			Drawable imageDrawable = new BitmapDrawable(getResources(), result);
			bmImage.setBackground(imageDrawable);
		}
	}
}
