package com.cb.vmss;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.cb.vmss.model.Address;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddAddressActivity extends ActionBarActivity implements OnClickListener {

	private Toolbar toolbar;
	//private ImageView closeImageView;

	private EditText nameEditText;
	private EditText houseEditText;
	private EditText streetEditText;
	private EditText areaEditText;
	private EditText cityEditText;
	private EditText zipEditText;

	private TextView createButton;
	private String mServiceUrl;
	private String addressBody;

	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	private Address item;
	private boolean isEdit = false;
	private String[] addressLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			createButton = (TextView) findViewById(R.id.btnCreateAdd);
			createButton.setOnClickListener(this);
			
			mTitle.setText(getResources().getString(R.string.lbl_add_address));

			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}

		if (getIntent() != null) {
			if (getIntent().hasExtra("edit")) {
				isEdit = getIntent().getBooleanExtra("edit", false);
			}
			if (getIntent().getSerializableExtra("address") != null) {
				Serializable b = getIntent().getSerializableExtra("address");
				item = (Address) b;
			}
		}

		mContext = this;

		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		mProgressDialog = new ProgressDialog(AddAddressActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);

		nameEditText = (EditText) findViewById(R.id.addNameEditText);
		houseEditText = (EditText) findViewById(R.id.addHouseEditText);
		streetEditText = (EditText) findViewById(R.id.addStreetEditText);
		areaEditText = (EditText) findViewById(R.id.addAreaEditText);
		cityEditText = (EditText) findViewById(R.id.addCityEditText);
		zipEditText = (EditText) findViewById(R.id.addZipEditText);

		

		if (isEdit) {
			createButton.setText("Save");
			nameEditText.setText(item.getAddFullName());

			areaEditText.setText(item.getAddLandmark());
			cityEditText.setText(item.getAddCity());
			zipEditText.setText(item.getAddZipCode());

			houseEditText.setText(item.getAddAddress1());
			streetEditText.setText(item.getAddAddress2());
			
			/*addressLine = item.getAddAddress1().split(",");
			if (addressLine.length > 1)
				houseEditText.setText(addressLine[0]);

			if (addressLine.length == 2) {
				streetEditText.setText(addressLine[1]);
			} else if (addressLine.length > 2) {
				String addStreet = "";
				for (int i = 1; i < addressLine.length; i++) {
					addStreet += addressLine[i];
				}
				streetEditText.setText(addStreet);
			}
*/
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnCreateAdd:
			if (cd.isConnectingToInternet()) {
				createAddress();
			} else {
				Toast.makeText(mContext, "Internet connection not available", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void createAddress() {
		if (!isEmptyField()) {
			String userId = Pref.getValue(Constant.PREF_USER_ID, "0");
			if (!userId.equals("0")) {
				if (isEdit) {
					addressBody = "usr_id=" + userId + "&add_id=" + item.getAddId() + "&add_fullname="
							+ nameEditText.getText().toString() + "&add_phone="
							+ Pref.getValue(Constant.PREF_PHONE_NUMBER, "0") 
							+ "&add_address1="+ houseEditText.getText().toString() 
							+ "&add_address2=" + streetEditText.getText().toString() 
							+ "&add_landmark=" + areaEditText.getText().toString() 
							+ "&add_zipcode=" + zipEditText.getText().toString();
				} else {
					addressBody = "usr_id=" + userId + "&add_id=" + "&add_fullname=" + nameEditText.getText().toString()
							+ "&add_phone=" + Pref.getValue(Constant.PREF_PHONE_NUMBER, "0") 
							+ "&add_address1="+ houseEditText.getText().toString() 
							+ "&add_address2=" + streetEditText.getText().toString() 
							+ "&add_landmark=" + areaEditText.getText().toString() 
							+ "&add_zipcode=" + zipEditText.getText().toString();

				}
				mServiceUrl = Constant.HOST + Constant.SERVICE_ADD_ADDRESS;

				if (cd.isConnectingToInternet()) {
					new addAddressOnServerTask().execute(mServiceUrl, addressBody);
				} else {
					Toast.makeText(mContext, getString(R.string.lbl_network_connection_fail), Toast.LENGTH_SHORT)
							.show();
				}

			}
		} else {
			Toast.makeText(mContext, "Field should not be blank", Toast.LENGTH_SHORT).show();
		}
	}

	private class addAddressOnServerTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			return connector.getDataFromServer(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			try {
				if (result != null && result.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
					if (isEdit)
						Toast.makeText(mContext, "Address updated successfully", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(mContext, "Address added successfully", Toast.LENGTH_SHORT).show();
					finish();
					
					
					String defaultAddresss= streetEditText.getText().toString()+", "+areaEditText.getText().toString();
					Pref.setValue(Constant.PREF_ADDRESS, defaultAddresss);
					
					
				} else {
					if (isEdit)
						Toast.makeText(mContext, "Add address fail", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(mContext, "Update address fail", Toast.LENGTH_SHORT).show();
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isEmptyField() {
		if (nameEditText.getText().toString().length() > 0 && houseEditText.getText().toString().length() > 0
				&& streetEditText.getText().toString().length() > 0 && areaEditText.getText().toString().length() > 0
				&& cityEditText.getText().toString().length() > 0 && zipEditText.getText().toString().length() > 0) {
			return false;
		} else {
			return true;
		}
	}
}