package com.sabziatdoor;

import org.json.JSONException;
import org.json.JSONObject;

import com.sabziatdoor.R;
import com.sabziatdoor.util.ConnectionDetector;
import com.sabziatdoor.util.Constant;
import com.sabziatdoor.util.Pref;
import com.sabziatdoor.util.ServerConnector;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReferFriendActivity extends ActionBarActivity {

	private Toolbar toolbar;

	private ProgressDialog mProgressDialog;
	ServerConnector connector;

	private String mServiceUrl;

	private Context mContext;
	private ConnectionDetector cd;

	private EditText friendNameEditText;
	private EditText friendMobileEditText;
	private Button referButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refer_friend);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		mContext = this;
		Constant.CONTEXT = mContext;
		cd = new ConnectionDetector(mContext);

		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_refer_a_friend));
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}

		mProgressDialog = new ProgressDialog(ReferFriendActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		connector = new ServerConnector();

		friendNameEditText = (EditText) findViewById(R.id.etFriendNameRefer);
		friendMobileEditText = (EditText) findViewById(R.id.etFriendMobileRefer);
		referButton = (Button) findViewById(R.id.btnReferFriend);

		referButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (friendNameEditText.getText().toString().length() <= 0) {
					Toast.makeText(mContext, "Friend name should not be blank", Toast.LENGTH_SHORT).show();
					return;
				} else if (friendMobileEditText.getText().toString().length() <= 0) {
					Toast.makeText(mContext, "Mobile should not be blank", Toast.LENGTH_SHORT).show();
					return;
				} else {
					if (cd.isConnectingToInternet()) {
						mServiceUrl = Constant.HOST + Constant.SERVICE_REFER_FRIEND;
						String parameter = "rf_usr_id=" + Pref.getValue(Constant.PREF_USER_ID, "") + "&rf_name="
								+ friendNameEditText.getText().toString() + "&rf_phonenumber="
								+ friendMobileEditText.getText().toString();
						new ReferFriendTask().execute(mServiceUrl, parameter);
					} else {
						Toast.makeText(mContext, getString(R.string.lbl_network_connection_fail), Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
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

	private class ReferFriendTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			return connector.submitOrder(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			try {
				if (result != null) {
					
					if(result.getJSONArray("MESSAGES").length()>0){
						String message = result.getJSONArray("MESSAGES").get(0).toString();
						Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					/*
					 * if
					 * (result.getString("STATUS").equalsIgnoreCase("SUCCESS"))
					 * { Toast.makeText(mContext,
					 * "Refered to friend successfully"
					 * ,Toast.LENGTH_SHORT).show(); } else {
					 * Toast.makeText(mContext,"Refer to friend fail"
					 * ,Toast.LENGTH_SHORT).show(); }
					 */
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
