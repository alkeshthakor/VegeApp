package com.cb.vmss;

import org.json.JSONException;
import org.json.JSONObject;

import com.cb.vmss.gcm.notification.AlertDialogManager;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

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
	private ProgressBar progressBarSplash;
	

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
		
		progressBarSplash=(ProgressBar)findViewById(R.id.progressBarSplash);
		//progressBarSplash.setVisibility(View.VISIBLE);
		
		
		/*// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(SplashScreenActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			finish();
			//return;
		}*/

	    /*if (cd.isConnectingToInternet()) {
				mServiceUrl = Constant.HOST + Constant.SERVICE_SHARE;
				new GetShareUrlTask().execute(mServiceUrl);
		}else{	
		}*/	
	    
	    sleepThread();
	    
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

	/*private class GetShareUrlTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBarSplash.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			return connector.getServerResponse(params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			progressBarSplash.setVisibility(View.INVISIBLE);
			try {
				if (result != null
						&& result.getString("STATUS").equalsIgnoreCase(
								"SUCCESS")) {
					JSONObject obj=result.getJSONObject("DATA");
					if(obj.getString("SHARE_URL") != null && !obj.getString("SHARE_URL").equalsIgnoreCase("null")) {
						Pref.setValue(Constant.PREF_SHARE_URL, obj.getString("SHARE_URL"));					
					} else {
						Pref.setValue(Constant.PREF_SHARE_URL, "https://play.google.com/store/apps/details?id=com.cb.vmss&hl=en");
					}
				} else {
					Pref.setValue(Constant.PREF_SHARE_URL, "https://play.google.com/store/apps/details?id=com.cb.vmss&hl=en");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Intent i = new Intent(getBaseContext(), MainActivity.class);
			startActivity(i);
			// Remove activity
			finish();
			
			//sleepThread();
		}
	}
*/
	
	public void sleepThread(){
		/****** Create Thread that will sleep for 5 seconds *************/
		Thread background = new Thread() {
			public void run() {
				try {
					// Thread will sleep for 5 seconds
					
					
					sleep(2 * 1000);
					//progressBarSplash.setVisibility(View.INVISIBLE);
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
