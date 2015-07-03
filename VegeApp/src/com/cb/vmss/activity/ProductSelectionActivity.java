package com.cb.vmss.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.cb.vmss.R;
import com.cb.vmss.adapter.ProductAdapter.ITotalCount;
import com.cb.vmss.fragment.ProductSelectionFragment;



public class ProductSelectionActivity extends FragmentActivity  implements ITotalCount{

	public int defaultPosition;
	
	private FragmentTabHost mTabHost;
	public static Fragment currentFragment;
	public interface ITotalCountActivity {
		public void getTotalActivity(int count, int prize);
	}
	ITotalCountActivity iTotalCountActivity = null;
	
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
        currentFragment = new ProductSelectionFragment();
        mTabHost.addTab(mTabHost.newTabSpec("fragment_product_vegetable").setIndicator("Vegetables"),ProductSelectionFragment.class, veg);
        mTabHost.addTab(mTabHost.newTabSpec("fragment_product_fruite").setIndicator("Fruits"),ProductSelectionFragment.class, fruit);
       
		getIntent().getStringArrayExtra("cat_list");
		mTabHost.setCurrentTabByTag(getIntent().getStringExtra("tabposition"));
	}
	
	public void getCount() {
		if ( currentFragment instanceof ITotalCountActivity) {
			iTotalCountActivity = (ITotalCountActivity) currentFragment;
		}
	}

	@Override
	public void getTotal(int count, int prize) {
		iTotalCountActivity.getTotalActivity(count, prize);
	}
}
