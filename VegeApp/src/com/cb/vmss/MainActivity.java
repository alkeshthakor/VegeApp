package com.cb.vmss;

import static com.cb.vmss.gcm.notification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.cb.vmss.gcm.notification.CommonUtilities.EXTRA_MESSAGE;
import static com.cb.vmss.gcm.notification.CommonUtilities.SENDER_ID;

import com.cb.vmss.fragment.FragmentDrawer;
import com.cb.vmss.fragment.HomeFragment;
import com.cb.vmss.gcm.notification.AlertDialogManager;
import com.cb.vmss.gcm.notification.ServerUtilities;
import com.cb.vmss.gcm.notification.WakeLocker;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;
import com.google.android.gcm.GCMRegistrar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

	private static String TAG = MainActivity.class.getSimpleName();

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;
	private TextView mTitleTextView;

	/*
	 * ConnectionDetector cd; ServerConnector connector; Context mContext;
	 * 
	 * public static String name; public static String email;
	 */

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	private ConnectionDetector cd;
	private ServerConnector connector;
	private Context mContext;
	private String mServiceUrl;
	public static String name;
	public static String email;
	private String registrationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Constant.CONTEXT = this;
		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		Pref.setValue(Constant.PREF_USER_ID, "108");
		Pref.setValue(Constant.PREF_PHONE_NUMBER, "9879890783");

		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			//mTitleTextView = (TextView) mToolbar.findViewById(R.id.toolbar_title);
			//mTitleTextView.setText(getString(R.string.app_name));

		}
		mToolbar.setTitleTextColor(Color.BLACK);

		/*getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);*/
		
		// setSupportActionBar (mToolbar);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		/*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);
*/
		//getResources().getDrawable(R.drawable.ic_navigation_drawer)
		
		mContext = this;
		Constant.CONTEXT = mContext;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();

		cd = new ConnectionDetector(getApplicationContext());
		name = "Alkesh Thakor";
		email = "";

		drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),
				mToolbar);
		drawerFragment.setDrawerListener(this);

		// display the first navigation drawer view on app launch
		//displayView(R.id.nav_location);
		
		Fragment fragment = new HomeFragment();
		// title = getString(R.string.title_home);
		loadFragment(fragment);
		

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Constant.CONTEXT = this;

		if (Pref.getValue(Constant.PREF_GCM_REGISTRATION_ID, "").equalsIgnoreCase("")) {
			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(MainActivity.this);
			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.
			GCMRegistrar.checkManifest(MainActivity.this);

			registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

			// Get GCM registration id
			registrationId = GCMRegistrar.getRegistrationId(MainActivity.this);

			// Check if regid already presents
			if (registrationId.equals("")) {
				// Registration is not present, register now with GCM
				GCMRegistrar.register(MainActivity.this, SENDER_ID);
				registerOnOurServer();
			} else {
				registerOnOurServer();
			}
		}

	}

	@Override
	public void onDrawerItemSelected(View view) {
		displayView(view.getId());
	}

	@SuppressLint("ShowToast")
	private void displayView(int position) {
		// String title = getString(R.string.app_name);
		switch (position) {
		case R.id.nav_address:
			if (!Pref.getValue(Constant.PREF_PHONE_NUMBER, "0").equals("0")) {
				Intent chooseAddressIntent = new Intent(getApplicationContext(), ChooseAddressActivity.class);
				chooseAddressIntent.putExtra("fromscreen", MainActivity.class.getCanonicalName());
				startActivity(chooseAddressIntent);
			} else {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				loginIntent.putExtra("fromscreen", MainActivity.class.getCanonicalName());
				startActivityForResult(loginIntent, Constant.CODE_MAIN_LOGIN);
			}
			break;
			
		case R.id.nav_login:

			if (!Pref.getValue(Constant.PREF_PHONE_NUMBER, "0").equals("0")) {
				Toast.makeText(MainActivity.this, "Already Login", Toast.LENGTH_LONG);
			} else {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				loginIntent.putExtra("fromscreen", MainActivity.class.getCanonicalName());
				startActivityForResult(loginIntent, Constant.CODE_MAIN_LOGIN);
			}
			break;
		case R.id.nav_order:
			if (!Pref.getValue(Constant.PREF_PHONE_NUMBER, "0").equals("0")) {
				Intent checkoutIntent = new Intent(getApplicationContext(), MyPreviousOrderActivity.class);
				startActivity(checkoutIntent);
			} else {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				loginIntent.putExtra("fromscreen", MainActivity.class.getCanonicalName());
				startActivityForResult(loginIntent, Constant.CODE_MAIN_LOGIN);
			}
			break;
		case R.id.nav_cart:
			Intent cartIntent = new Intent(getApplicationContext(), MyCartActivity.class);
			startActivity(cartIntent);
			break;
		case R.id.nav_notification_center:
			Intent notificationIntent = new Intent(getApplicationContext(), NotifictaionListActivity.class);
			startActivity(notificationIntent);
			break;

		case R.id.nav_share:
			String shareMessage = Pref.getValue(Constant.PREF_SHARE_URL, "");
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
			startActivity(Intent.createChooser(intent, "Share this app"));
			break;

		case R.id.nav_logout:
			if (!Pref.getValue(Constant.PREF_PHONE_NUMBER, "0").equals("0")) {
				showConfirmLogout();
			} else {
				Toast.makeText(MainActivity.this, "Already Logout", Toast.LENGTH_LONG);
			}
			break;
		case R.id.nav_callus:
			confirmCall();
			break;
		case R.id.nav_rateus:
			rateUs();
			break;
		case R.id.nav_help:
			Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
			startActivity(helpIntent);
			break;
		case R.id.nav_about:
			Intent aboutIntent = new Intent(getApplicationContext(), AboutUsActivity2.class);
			startActivity(aboutIntent);
			break;
		case R.id.nav_refer_a_friend:
			if (!Pref.getValue(Constant.PREF_PHONE_NUMBER, "0").equals("0")) {
				Intent referFriendIntent = new Intent(getApplicationContext(), ReferFriendActivity.class);
				startActivity(referFriendIntent);
			} else {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				loginIntent.putExtra("fromscreen", MainActivity.class.getCanonicalName());
				startActivityForResult(loginIntent, Constant.CODE_MAIN_LOGIN);
			}
			break;
		default:
			
			
			break;
		}
		/*
		 * if (fragment != null) { FragmentManager fragmentManager =
		 * getSupportFragmentManager(); FragmentTransaction fragmentTransaction
		 * = fragmentManager.beginTransaction();
		 * fragmentTransaction.replace(R.id.container_body, fragment);
		 * fragmentTransaction.commit();
		 * 
		 * // set the toolbar title getSupportActionBar().setTitle(title); }
		 */
	}

	private void loadFragment(Fragment fragment) {
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

			// set the toolbar title
			// getSupportActionBar().setTitle(title);
		}
	}

	private void showConfirmLogout() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		// set title
		alertDialogBuilder.setTitle("Logout");
		// set dialog message
		alertDialogBuilder.setMessage("Are you sure you want to logout?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						// MainActivity.this.finish();
						Pref.setValue(Constant.PREF_PHONE_NUMBER, "0");

						Pref.setValue(Constant.PREF_ADD_ID, "0");
						Pref.setValue(Constant.PREF_ADDRESS, "");

					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Check which request we're responding to
		if (resultCode == Constant.CODE_MAIN_LOGIN) {
			displayView(0);
		} else if (resultCode == Constant.CODE_BACK_WITH_CHECK_ORDER) {
			// setResult(Constant.CODE_BACK_WITH_CHECK_ORDER);
			// finish();
			displayView(R.id.nav_order);

		} else if (resultCode == Constant.CODE_BACK_WITH_COUTINUE_SHOPPING) {
			displayView(0);
		}
	}

	public void rateUs() {
		try {
			Uri marketUri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
			startActivity(marketIntent);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	public void confirmCall() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Contact Subji At Door");
		builder.setMessage("Do you want to call the Subji At Door help line?");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:7228033322"));
				startActivity(intent);

			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Receiving push messages
	 */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 */

			// Showing received message
			// Toast.makeText(getApplicationContext(), "New Message: " +
			// newMessage, Toast.LENGTH_LONG).show();

			Log.d("GCM Message: ", "New Message: " + newMessage);
			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {

			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	public void registerOnOurServer() {
		// Try to register again, but not in the UI thread.
		// It's also necessary to cancel the thread onDestroy(),
		// hence the use of AsyncTask instead of a raw thread.
		final Context context = this;
		mRegisterTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// Register on our server
				// On server creates a new user
				if (registrationId == null || "".equals(registrationId)) {
					registrationId = GCMRegistrar.getRegistrationId(MainActivity.this);
				}

				if (registrationId != null && !"".equals(registrationId)) {
					ServerUtilities.register(context, name, email, registrationId);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				if (registrationId != null && !"".equals(registrationId)) {
					Pref.setValue(Constant.PREF_GCM_REGISTRATION_ID, registrationId);
				}
				mRegisterTask = null;
			}
		};
		mRegisterTask.execute(null, null, null);
	}

}