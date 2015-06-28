package com.cb.vmss.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.cb.vmss.R;
import com.cb.vmss.fragment.ProductSelectionFragment;



public class ProductSelectionActivity extends FragmentActivity{

	//private ViewPagerAdapter mViewPagerAdapter;
	private String catList[];
	public int defaultPosition;
	
	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_selection);
		
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.setBackgroundColor(Color.GREEN);
        Bundle veg = new Bundle();
        veg.putString("Category", "Vegetables");
        
        Bundle fruit = new Bundle();
        fruit.putString("Category", "Fruits");
        
        mTabHost.addTab(mTabHost.newTabSpec("fragment_product_vegetable").setIndicator("Vegetables"),ProductSelectionFragment.class, veg);
        mTabHost.addTab(mTabHost.newTabSpec("fragment_product_fruite").setIndicator("Fruits"),ProductSelectionFragment.class, fruit);
       
		catList=getIntent().getStringArrayExtra("cat_list");
		defaultPosition=Integer.parseInt(getIntent().getStringExtra("tabposition"));

		mTabHost.setCurrentTab(defaultPosition);
		
	}
}
