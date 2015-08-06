package com.sabziatdoor;

import java.util.List;

import com.sabziatdoor.R;
import com.sabziatdoor.adapter.MyCartAdapter;
import com.sabziatdoor.adapter.MyCartAdapter.IUpdateMyCart;
import com.sabziatdoor.database.VegAppDatabaseHelper;
import com.sabziatdoor.model.Product;
import com.sabziatdoor.util.Constant;
import com.sabziatdoor.util.Pref;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyCartActivity extends ActionBarActivity implements OnClickListener,IUpdateMyCart{
	private Toolbar toolbar;
	//private ImageView closeImageView;
	private TextView mTitle;
	private TextView priceTextViewMyCart;
	private LinearLayout llEmptyCart;
	private ListView myCartListView;
	private LinearLayout checkOutOutLinearLayout;
	
	private float totalAmount;
	private int totalQtyCount;
	private Context mContext;
	
	private VegAppDatabaseHelper mDatabaseHelper;
	List<Product> myCartList;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_cart);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		
				
		if (toolbar != null) {
			mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
		}

		mContext = this;
		Constant.CONTEXT = mContext;
		mDatabaseHelper=new VegAppDatabaseHelper(mContext);
		
		llEmptyCart = (LinearLayout) findViewById(R.id.llEmptyCart);
		myCartListView=(ListView)findViewById(R.id.myCartListView);
		checkOutOutLinearLayout=(LinearLayout)findViewById(R.id.bottomBarMyCart);
		checkOutOutLinearLayout.setOnClickListener(this);
		priceTextViewMyCart=(TextView)findViewById(R.id.priceTextViewMyCart);
		
			
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mDatabaseHelper.open();
		myCartList=mDatabaseHelper.getMyCartList();
		myCartListView.setAdapter(new MyCartAdapter(mContext, myCartList));
		mDatabaseHelper.close();
		
		totalAmount = Float.parseFloat(Pref.getValue(Constant.PREF_TOTAL_AMOUT, "0"));
		totalQtyCount = Integer.parseInt(Pref.getValue(Constant.PREF_QTY_COUNT, "0"));

		mTitle.setText("My Cart ("+totalQtyCount+")");
		
		if (totalQtyCount > 0) {
			llEmptyCart.setVisibility(View.GONE);
			myCartListView.setVisibility(View.VISIBLE);
			checkOutOutLinearLayout.setVisibility(View.VISIBLE);
			priceTextViewMyCart.setText("" + totalAmount);
		}else{
			llEmptyCart.setVisibility(View.VISIBLE);
			myCartListView.setVisibility(View.GONE);
			checkOutOutLinearLayout.setVisibility(View.GONE);
		}
		
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
		switch (view.getId()) {
		case R.id.bottomBarMyCart:
			if(!Pref.getValue(Constant.PREF_PHONE_NUMBER, "0").equals("0")) {
				     Intent chooseAddressIntent=new Intent(getApplicationContext(),ChooseAddressActivity.class);
				     chooseAddressIntent.putExtra("fromscreen",MyCartActivity.class.getCanonicalName());
				     startActivityForResult(chooseAddressIntent,Constant.CODE_MAIN_LOGIN);
					 
			} else {
				Intent loginIntent=new Intent(getApplicationContext(),LoginActivity.class);
				loginIntent.putExtra("fromscreen",MyCartActivity.class.getCanonicalName());
				startActivityForResult(loginIntent,Constant.CODE_MAIN_LOGIN);
			}
			break;
		}
	}

	@Override
	public void updateMyCart(int updateValue, float prize) {
		// TODO Auto-generated method stub
		totalAmount = Float.parseFloat(Pref.getValue(Constant.PREF_TOTAL_AMOUT, "0"));
		totalQtyCount = Integer.parseInt(Pref.getValue(Constant.PREF_QTY_COUNT, "0"));

		if (updateValue != -1) {
			totalAmount += prize;
			totalQtyCount += 1;
		} else {
			totalAmount -= prize;
			totalQtyCount -= 1;

			if (totalAmount < 0) {
				totalAmount = 0;
			}
			if (totalQtyCount < 0) {
				totalQtyCount = 0;
			}
		}

		if (totalQtyCount > 0) {
			checkOutOutLinearLayout.setVisibility(View.VISIBLE);
			llEmptyCart.setVisibility(View.GONE);
			myCartListView.setVisibility(View.VISIBLE);
		} else {
			checkOutOutLinearLayout.setVisibility(View.GONE);
			llEmptyCart.setVisibility(View.VISIBLE);
			myCartListView.setVisibility(View.GONE);
		}
		
		mTitle.setText("My Cart ("+totalQtyCount+")");
		priceTextViewMyCart.setText("" + totalAmount);
		Pref.setValue(Constant.PREF_TOTAL_AMOUT, totalAmount + "");
		Pref.setValue(Constant.PREF_QTY_COUNT, totalQtyCount + "");
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
