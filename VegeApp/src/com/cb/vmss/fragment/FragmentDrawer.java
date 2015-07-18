package com.cb.vmss.fragment;


import com.cb.vmss.R;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private TextView mPhoneNumberTextView;
    private TextView qtyCountTextView;
    private TextView locationTextView;
    
   
    private LinearLayout loginObj;
    private LinearLayout locationObj;
    private LinearLayout addressObj;
    private LinearLayout orderObj;
    private LinearLayout cartObj;
    private LinearLayout helpObj;
    private LinearLayout calusObj;
    private LinearLayout rateusObj;
    private LinearLayout shareObj;
    private LinearLayout aboutObj;
    private LinearLayout logoutObj;
    private ImageView mPhoneIcon;
    
    private View locationDivider;
    
    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        
        mPhoneIcon=(ImageView)layout.findViewById(R.id.imgPhone);
        
        mPhoneNumberTextView=(TextView)layout.findViewById(R.id.userName);
        qtyCountTextView=(TextView)layout.findViewById(R.id.txtQtyCount);
        locationTextView=(TextView)layout.findViewById(R.id.txtLocation);
        
        
        loginObj = (LinearLayout) layout.findViewById(R.id.nav_login);
        locationObj = (LinearLayout) layout.findViewById(R.id.nav_location);
        addressObj = (LinearLayout) layout.findViewById(R.id.nav_address);
        orderObj = (LinearLayout) layout.findViewById(R.id.nav_order);
        cartObj = (LinearLayout) layout.findViewById(R.id.nav_cart);
        helpObj = (LinearLayout) layout.findViewById(R.id.nav_help);
        calusObj = (LinearLayout) layout.findViewById(R.id.nav_callus);
        rateusObj = (LinearLayout) layout.findViewById(R.id.nav_rateus);
        shareObj = (LinearLayout) layout.findViewById(R.id.nav_share);
        aboutObj = (LinearLayout) layout.findViewById(R.id.nav_about);
        logoutObj = (LinearLayout) layout.findViewById(R.id.nav_logout);
        
        locationDivider=(View)layout.findViewById(R.id.locationDevider);
        
        
        loginObj.setOnClickListener(mClickListener);
        locationObj.setOnClickListener(mClickListener);
        addressObj.setOnClickListener(mClickListener);
        orderObj.setOnClickListener(mClickListener);
        cartObj.setOnClickListener(mClickListener);
        helpObj.setOnClickListener(mClickListener);
        calusObj.setOnClickListener(mClickListener);
        rateusObj.setOnClickListener(mClickListener);
        shareObj.setOnClickListener(mClickListener);
        aboutObj.setOnClickListener(mClickListener);
        logoutObj.setOnClickListener(mClickListener);
        
        return layout;
    }
    
    private OnClickListener mClickListener  = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
     		if (isDrawerOpen()) {
    			closeDrawer();
    		}
    		drawerListener.onDrawerItemSelected(v);
		}
	};
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	Constant.CONTEXT = activity;
    }

    @Override
    public void onResume() {
    	super.onResume();
    	qtyCountTextView.setText(Pref.getValue(Constant.PREF_QTY_COUNT, "0"));	
    	
    	if(!Pref.getValue(Constant.PREF_ADDRESS,"").equalsIgnoreCase("")){
    		locationObj.setVisibility(View.VISIBLE);
    		locationDivider.setVisibility(View.VISIBLE);
    		locationTextView.setText(Pref.getValue(Constant.PREF_ADDRESS,""));
    	}else{
    		locationObj.setVisibility(View.GONE);
    		locationDivider.setVisibility(View.GONE);
    		locationTextView.setText(Pref.getValue(Constant.PREF_ADDRESS,""));
    	}
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").equals("0")){
                	mPhoneNumberTextView.setText(Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").toString());
                	mPhoneIcon.setVisibility(View.VISIBLE);
                	loginObj.setVisibility(View.GONE);
                	logoutObj.setVisibility(View.VISIBLE);
                } else {
                	mPhoneNumberTextView.setText("Welcome");
                	mPhoneIcon.setVisibility(View.GONE);
                	loginObj.setVisibility(View.VISIBLE);
                	logoutObj.setVisibility(View.GONE);
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setToolbarNavigationClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isDrawerOpen()) {
					 closeDrawer();
				  } else openDrawer();
			}
		});
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

    public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(containerView);
	}
    
    public void openDrawer() {
		//if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(containerView);
			mDrawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mDrawerLayout.setFocusableInTouchMode(false);
		//}
	}
	
	public void closeDrawer() {
		if (mDrawerLayout != null) {
			mDrawerLayout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			mDrawerLayout.setFocusableInTouchMode(false);
			mDrawerLayout.closeDrawer(containerView);
		}
	}
	
    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view);
    }
}
