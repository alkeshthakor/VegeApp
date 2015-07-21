package com.cb.vmss;

import java.util.List;

import com.cb.vmss.adapter.NotificationAdapter;
import com.cb.vmss.database.VegAppDatabaseHelper;
import com.cb.vmss.model.VNotification;
import com.cb.vmss.util.Constant;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class NotifictaionListActivity extends ActionBarActivity {
	private Toolbar toolbar;
	private VegAppDatabaseHelper mDatabaseHelper;
	private Context mContext;
	private List<VNotification> notificationList;
	private NotificationAdapter mNotificationAdapter;
	private ListView mNotificationListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifictaion_list);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.title_notification));

			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}
		
		mContext = this;
		Constant.CONTEXT = mContext;
		mDatabaseHelper=new VegAppDatabaseHelper(mContext);
		
		mNotificationListView=(ListView)findViewById(R.id.notificationListView);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mDatabaseHelper.open();
		notificationList=mDatabaseHelper.getNotificationList();
		mDatabaseHelper.close();
		
		if(notificationList!=null){
			mNotificationAdapter=new NotificationAdapter(mContext, notificationList);
			mNotificationListView.setAdapter(mNotificationAdapter);
			
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
}
