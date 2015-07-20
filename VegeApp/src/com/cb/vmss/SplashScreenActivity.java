package com.cb.vmss;

import static com.cb.vmss.gcm.notification.CommonUtilities.EXTRA_MESSAGE;
import static com.cb.vmss.gcm.notification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.cb.vmss.gcm.notification.CommonUtilities.SENDER_ID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.cb.vmss.gcm.notification.AlertDialogManager;
import com.cb.vmss.gcm.notification.ServerUtilities;
import com.cb.vmss.gcm.notification.WakeLocker;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;
import com.google.android.gcm.GCMRegistrar;

public class SplashScreenActivity extends Activity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash_screen);

		mContext = this;
		Constant.CONTEXT = mContext;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();

		cd = new ConnectionDetector(getApplicationContext());
		name="Alkesh Thakor";
		email="";
		
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(SplashScreenActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		if (Pref.getValue(Constant.PREF_GCM_REGISTRATION_ID, "")
				.equalsIgnoreCase("")) {
			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(this);
			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.
			GCMRegistrar.checkManifest(this);

			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					DISPLAY_MESSAGE_ACTION));

			// Get GCM registration id
			final String regId = GCMRegistrar.getRegistrationId(this);

			// Check if regid already presents
			if (regId.equals("")) {
				// Registration is not present, register now with GCM
				GCMRegistrar.register(this, SENDER_ID);

			} else {
				// Device is already registered on GCM
				if (GCMRegistrar.isRegisteredOnServer(this)) {
					// Skips registration.
					Toast.makeText(getApplicationContext(),
							"Already registered with GCM", Toast.LENGTH_LONG)
							.show();
				} else {
					// Try to register again, but not in the UI thread.
					// It's also necessary to cancel the thread onDestroy(),
					// hence the use of AsyncTask instead of a raw thread.
					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							// Register on our server
							// On server creates a new user
							ServerUtilities.register(context, name, email,
									regId);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							Pref.setValue(Constant.PREF_GCM_REGISTRATION_ID,
									regId);
							mRegisterTask = null;
						}
					};
					mRegisterTask.execute(null, null, null);
				}
			}
		}

		if (Pref.getValue(Constant.PREF_SHARE_URL, "").equalsIgnoreCase("")) {
			mServiceUrl = Constant.HOST + Constant.SERVICE_SHARE;
			new GetShareUrlTask().execute(mServiceUrl);
		}else{
			sleepThread();
		}

		
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

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

	private class GetShareUrlTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			return connector.getServerResponse(params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			try {
				if (result != null
						&& result.getString("STATUS").equalsIgnoreCase(
								"SUCCESS")) {
					JSONObject obj=result.getJSONObject("DATA");
					Pref.setValue(Constant.PREF_SHARE_URL, obj.getString("SHARE_URL"));
				} else {
					Pref.setValue(Constant.PREF_SHARE_URL,"");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			sleepThread();
			
		}
	}

	
	public void sleepThread(){
		/****** Create Thread that will sleep for 5 seconds *************/
		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 5 seconds
					sleep(3 * 1000);

					// After 5 seconds redirect to another intent
					Intent i = new Intent(getBaseContext(), MainActivity.class);
					startActivity(i);
					// Remove activity
					finish();

				} catch (Exception e) {

				}
			}
		};
		// start thread
		background.start();
	}
}
