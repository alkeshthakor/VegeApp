package com.cb.vmss.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cb.vmss.ProductSelectionActivity;
import com.cb.vmss.R;
import com.cb.vmss.adapter.ProductAdapter;
import com.cb.vmss.database.VegAppDatabaseHelper;
import com.cb.vmss.fadingactionbar.observablescrollview.ObservableListView;
import com.cb.vmss.fadingactionbar.observablescrollview.ScrollUtils;
import com.cb.vmss.model.Product;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.ServerConnector;
import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;


public class ProductSelectionFragment extends FlexibleSpaceWithImageBaseFragment<ObservableListView>{

	private Activity mActivity;

	ObservableListView mProductListView;

	Bundle argumentBundle;
	private String mServiceUrl;
	private String mCategoryId;
	
	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	public static List<Product> mProductList;

	private VegAppDatabaseHelper mDatabaseHelper;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);

	}

	public ProductSelectionFragment(String mCategoryId) {
		this.mCategoryId=mCategoryId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_layout_product_selection, container, false);
		mProductListView = (ObservableListView) view.findViewById(R.id.scroll);

		// Set padding view for ListView. This is the flexible space.
		View paddingView = new View(getActivity());
		final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);

		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
				flexibleSpaceImageHeight);
		paddingView.setLayoutParams(lp);

		// This is required to disable header's list selector effect
		paddingView.setClickable(true);

		mProductListView.addHeaderView(paddingView);

		// TouchInterceptionViewGroup should be a parent view other than
		// ViewPager.
		// This is a workaround for the issue #117:
		// https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
		mProductListView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

		// Scroll to the specified offset after layout
		Bundle args = getArguments();
		if (args != null && args.containsKey(ARG_SCROLL_Y)) {
			final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
			ScrollUtils.addOnGlobalLayoutListener(mProductListView, new Runnable() {
				@SuppressLint("NewApi")
				@Override
				public void run() {
					int offset = scrollY % flexibleSpaceImageHeight;
					mProductListView.setSelectionFromTop(0, -offset);
				}
			});
			updateFlexibleSpace(scrollY, view);
		} else {
			updateFlexibleSpace(0, view);
		}
		mProductListView.setScrollViewCallbacks(this);
		updateFlexibleSpace(0, view);
		//ProductSelectionActivity.currentFragment = ProductSelectionFragment.this;
		argumentBundle = this.getArguments();
		mContext = mActivity.getApplicationContext();
		mDatabaseHelper=new VegAppDatabaseHelper(mContext);

		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
		mServiceUrl = Constant.HOST + Constant.SERVICE_PRODUCT_BY_CAT_ID;
		
		
		 if(cd.isConnectingToInternet()){
			 new LoadProdcutByCategoryTask().execute(mServiceUrl, "cat_id="+mCategoryId);
		    }else{
		    	Toast.makeText(mContext,getString(R.string.lbl_network_connection_fail),Toast.LENGTH_SHORT).show();
		    }
		 
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private class LoadProdcutByCategoryTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			return connector.getDataFromServer(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			parseResponse(result);
		}
	}

	private void parseResponse(JSONObject responseData) {
		if (responseData != null && responseData.length() > 0) {
			try {
				JSONArray productArray = responseData.getJSONArray("DATA");
				mDatabaseHelper.open();
				if (productArray.length() > 0) {

					mProductList = new ArrayList<Product>();
					for (int i = 0; i < productArray.length(); i++) {
						Product productItem = new Product();
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
						
					
						int productQtyInCart=mDatabaseHelper.getCartProductQty(productItem.getProductId());
						if(productQtyInCart!=-1)
						productItem.setProductQty(productQtyInCart);
						else
						productItem.setProductQty(0);
						
						productItem.setProductBitmap(null);
						mProductList.add(productItem);
					}
					mProductListView.setAdapter(new ProductAdapter(mActivity, mProductList));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public void setScrollY(int scrollY, int threshold) {
		View view = getView();
		if (view == null) {
			return;
		}
		ObservableListView listView = (ObservableListView) view.findViewById(R.id.scroll);
		if (listView == null) {
			return;
		}
		View firstVisibleChild = listView.getChildAt(0);
		if (firstVisibleChild != null) {
			int offset = scrollY;
			int position = 0;
			if (threshold < scrollY) {
				int baseHeight = firstVisibleChild.getHeight();
				position = scrollY / baseHeight;
				offset = scrollY % baseHeight;
			}
			listView.setSelectionFromTop(position, -offset);
		}
	}

	@Override
	public void updateFlexibleSpace(int scrollY, View view) {
		int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);

		View listBackgroundView = view.findViewById(R.id.list_background);

		// Translate list background
		ViewHelper.setTranslationY(listBackgroundView, Math.max(0, -scrollY + flexibleSpaceImageHeight));

		// Also pass this event to parent Activity
		ProductSelectionActivity parentActivity = (ProductSelectionActivity) getActivity();
		if (parentActivity != null) {
			parentActivity.onScrollChanged(scrollY, (ObservableListView) view.findViewById(R.id.scroll));
		}
	}
}
