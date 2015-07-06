package com.cb.vmss;

import com.cb.vmss.adapter.ProductAdapter2.ITotalCount;
import com.cb.vmss.fadingactionbar.observablescrollview.CacheFragmentStatePagerAdapter;
import com.cb.vmss.fadingactionbar.observablescrollview.ScrollUtils;
import com.cb.vmss.fadingactionbar.observablescrollview.Scrollable;
import com.cb.vmss.fadingactionbar.widget.SlidingTabLayout;
import com.cb.vmss.fragment.FlexibleSpaceWithImageBaseFragment;
import com.cb.vmss.fragment.HomeFragment;
import com.cb.vmss.fragment.ProductSelectionFragment2;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductSelectionActivity2 extends BaseActivity implements ITotalCount,OnClickListener{

	public int defaultPosition;

	protected static final float MAX_TEXT_SCALE_DELTA = 0.3f;

	RelativeLayout relLayout, qtyCountRelLayoutObj;
	TextView txtQtyCountObj, productPriceTextViewObj;
	public static Fragment currentFragment;
	
	// Fading action bar
	private ViewPager mPager;
	private NavigationAdapter mPagerAdapter;
	private SlidingTabLayout mSlidingTabLayout;
	private int mFlexibleSpaceHeight;
	private int mTabHeight;

	private int totalAmount;
	private int totalQtyCount;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_selection2);
		mContext = this;
		Constant.CONTEXT = mContext;
		currentFragment = new ProductSelectionFragment2(HomeFragment.mCategoryList.get(0).getCategoryId());
		mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(), getIntent().getStringArrayExtra("cat_list"));
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mPagerAdapter);

		defaultPosition = Integer.parseInt(getIntent().getStringExtra("tabposition"));

		mPager.setCurrentItem(defaultPosition);

		mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
		mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);

		TextView titleView = (TextView) findViewById(R.id.title);
		titleView.setText("Sabji At Door");

		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
		// mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
		mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);

		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mPager);

		relLayout = (RelativeLayout) findViewById(R.id.relLayout);
		qtyCountRelLayoutObj = (RelativeLayout) findViewById(R.id.qtyCountRelLayout);
		txtQtyCountObj = (TextView) findViewById(R.id.txtQtyCount);
		productPriceTextViewObj = (TextView) findViewById(R.id.productPriceTextView);
		relLayout.setOnClickListener(this);
		
		relLayout.setVisibility(View.GONE);

		// Initialize the first Fragment's state when layout is completed.
		ScrollUtils.addOnGlobalLayoutListener(mSlidingTabLayout, new Runnable() {
			@Override
			public void run() {
				translateTab(0, false);
			}
		});

		totalAmount = Integer.parseInt(Pref.getValue(Constant.PREF_TOTAL_AMOUT, "0"));
		totalQtyCount = Integer.parseInt(Pref.getValue(Constant.PREF_QTY_COUNT, "0"));
		if (totalQtyCount > 0) {
			relLayout.setVisibility(View.VISIBLE);
			txtQtyCountObj.setText("" + totalQtyCount);
			productPriceTextViewObj.setText("" + totalAmount);
			
		}

	}

	/*
	 * public void getCount() { if ( mCurrentFragment instanceof
	 * ITotalCountActivity) { iTotalCountActivity = (ITotalCountActivity)
	 * mCurrentFragment; } }
	 */
	@Override
	public void getTotal(int updateValue, int prize) {

		totalAmount = Integer.parseInt(Pref.getValue(Constant.PREF_TOTAL_AMOUT, "0"));
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
			relLayout.setVisibility(View.VISIBLE);
		} else {
			relLayout.setVisibility(View.GONE);
		}

		
		txtQtyCountObj.setText("" + totalQtyCount);
		productPriceTextViewObj.setText("" + totalAmount);
		
		Pref.setValue(Constant.PREF_TOTAL_AMOUT, totalAmount + "");
		Pref.setValue(Constant.PREF_QTY_COUNT, totalQtyCount + "");

	}


	private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

		// private static final String[] TITLES = new String[]{"Applepie",
		// "Butter Cookie", "Cupcake", "Donut", "Eclair", "Froyo",
		// "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean",
		// "KitKat", "Lollipop"};
		private String[] TITLES;

		private int mScrollY;

		public NavigationAdapter(FragmentManager fm, String[] mTitles) {
			super(fm);
			TITLES = mTitles;
		}

		public void setScrollY(int scrollY) {
			mScrollY = scrollY;
		}

		@Override
		protected Fragment createItem(int position) {
			FlexibleSpaceWithImageBaseFragment mFragment;
			// final int pattern = position % 4;
			/*
			 * switch (pattern) { case 0: { f = new
			 * ProductSelectionFragment2(HomeFragment.mCategoryList.get(position
			 * ).getCategoryId()); break; } case 1: { f = new
			 * ProductSelectionFragment2(); break; } case 2: { f = new
			 * ProductSelectionFragment2(); break; } case 3: default: { f = new
			 * ProductSelectionFragment2(); break; } }
			 */

			mFragment = new ProductSelectionFragment2(HomeFragment.mCategoryList.get(position).getCategoryId());
			mFragment.setArguments(mScrollY);
			return mFragment;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}
	}

	/**
	 * Called by children Fragments when their scrollY are changed. They all
	 * call this method even when they are inactive but this Activity should
	 * listen only the active child, so each Fragments will pass themselves for
	 * Activity to check if they are active.
	 *
	 * @param scrollY
	 *            scroll position of Scrollable
	 * @param s
	 *            caller Scrollable view
	 */
	public void onScrollChanged(int scrollY, Scrollable s) {
		@SuppressWarnings("rawtypes")
		FlexibleSpaceWithImageBaseFragment fragment = (FlexibleSpaceWithImageBaseFragment) mPagerAdapter
				.getItemAt(mPager.getCurrentItem());
		if (fragment == null) {
			return;
		}
		View view = fragment.getView();
		if (view == null) {
			return;
		}
		Scrollable scrollable = (Scrollable) view.findViewById(R.id.scroll);
		if (scrollable == null) {
			return;
		}
		if (scrollable == s) {
			// This method is called by not only the current fragment but also
			// other fragments
			// when their scrollY is changed.
			// So we need to check the caller(S) is the current fragment.
			int adjustedScrollY = Math.min(scrollY, mFlexibleSpaceHeight - mTabHeight);
			translateTab(adjustedScrollY, false);
			propagateScroll(adjustedScrollY);
		}
	}

	private void translateTab(int scrollY, boolean animated) {
		int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
		int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
		View imageView = findViewById(R.id.image);
		View overlayView = findViewById(R.id.overlay);
		TextView titleView = (TextView) findViewById(R.id.title);

		// Translate overlay and image
		float flexibleRange = flexibleSpaceImageHeight - getActionBarSize();
		int minOverlayTransitionY = tabHeight - overlayView.getHeight();
		ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
		ViewHelper.setTranslationY(imageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

		// Change alpha of overlay
		ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

		// Scale title text
		float scale = 1
				+ ScrollUtils.getFloat((flexibleRange - scrollY - tabHeight) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
		setPivotXToTitle(titleView);
		ViewHelper.setPivotY(titleView, 0);
		ViewHelper.setScaleX(titleView, scale);
		ViewHelper.setScaleY(titleView, scale);

		// Translate title text
		int maxTitleTranslationY = flexibleSpaceImageHeight - tabHeight - getActionBarSize();
		int titleTranslationY = maxTitleTranslationY - scrollY;
		ViewHelper.setTranslationY(titleView, titleTranslationY);

		// If tabs are moving, cancel it to start a new animation.
		ViewPropertyAnimator.animate(mSlidingTabLayout).cancel();
		// Tabs will move between the top of the screen to the bottom of the
		// image.
		float translationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceHeight - mTabHeight, 0,
				mFlexibleSpaceHeight - mTabHeight);
		if (animated) {
			// Animation will be invoked only when the current tab is changed.
			ViewPropertyAnimator.animate(mSlidingTabLayout).translationY(translationY).setDuration(200).start();
		} else {
			// When Fragments' scroll, translate tabs immediately (without
			// animation).
			ViewHelper.setTranslationY(mSlidingTabLayout, translationY);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void setPivotXToTitle(View view) {
		final TextView mTitleView = (TextView) view.findViewById(R.id.title);
		Configuration config = getResources().getConfiguration();
		if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
				&& config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
			ViewHelper.setPivotX(mTitleView, view.findViewById(android.R.id.content).getWidth());
		} else {
			ViewHelper.setPivotX(mTitleView, 0);
		}
	}

	private void propagateScroll(int scrollY) {
		// Set scrollY for the fragments that are not created yet
		mPagerAdapter.setScrollY(scrollY);

		// Set scrollY for the active fragments
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			// Skip current item
			if (i == mPager.getCurrentItem()) {
				continue;
			}

			// Skip destroyed or not created item
			FlexibleSpaceWithImageBaseFragment f = (FlexibleSpaceWithImageBaseFragment) mPagerAdapter.getItemAt(i);
			if (f == null) {
				continue;
			}

			View view = f.getView();
			if (view == null) {
				continue;
			}
			f.setScrollY(scrollY, mFlexibleSpaceHeight);
			f.updateFlexibleSpace(scrollY);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.relLayout:
			 Intent myCartIntent=new Intent(getApplicationContext(),MyCartActivity.class);
			 startActivity(myCartIntent);
			break;
		}
	}

}
