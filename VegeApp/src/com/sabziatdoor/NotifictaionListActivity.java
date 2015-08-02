package com.sabziatdoor;

import java.util.List;

import com.sabziatdoor.R;
import com.sabziatdoor.adapter.NotificationAdapter;
import com.sabziatdoor.database.VegAppDatabaseHelper;
import com.sabziatdoor.database.VegAppDatabase.VegAppColumn;
import com.sabziatdoor.model.VNotification;
import com.sabziatdoor.util.Constant;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NotifictaionListActivity extends ActionBarActivity {
	private Toolbar toolbar;
	private VegAppDatabaseHelper mDatabaseHelper;
	private Context mContext;
	private List<VNotification> notificationList;
	private NotificationAdapter mNotificationAdapter;
	private ListView mNotificationListView;
	private LinearLayout llEmptyNotifications;
	
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
		
		llEmptyNotifications = (LinearLayout) findViewById(R.id.llEmptyNotifications);
		mNotificationListView=(ListView)findViewById(R.id.notificationListView);
		
		
		mNotificationListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mDatabaseHelper.open();
				ContentValues values=new ContentValues();
				values.put(VegAppColumn.NOTI_IS_READ,"true");
				mDatabaseHelper.updateNotificationAsRead(notificationList.get(position).getNoti_Id(),values);
				mDatabaseHelper.close();
			}
		});
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		mDatabaseHelper.open();
		notificationList=mDatabaseHelper.getNotificationList();
		mDatabaseHelper.close();
		
		if(notificationList!=null && !notificationList.isEmpty()){
			llEmptyNotifications.setVisibility(View.GONE);
			mNotificationListView.setVisibility(View.VISIBLE);
			mNotificationAdapter=new NotificationAdapter(mContext, notificationList);
			mNotificationListView.setAdapter(mNotificationAdapter);
		} else {
			llEmptyNotifications.setVisibility(View.VISIBLE);
			mNotificationListView.setVisibility(View.GONE);
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
