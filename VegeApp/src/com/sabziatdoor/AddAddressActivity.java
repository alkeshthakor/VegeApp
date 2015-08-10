package com.sabziatdoor;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sabziatdoor.R;
import com.sabziatdoor.model.Address;
import com.sabziatdoor.model.ZipCode;
import com.sabziatdoor.util.ConnectionDetector;
import com.sabziatdoor.util.Constant;
import com.sabziatdoor.util.Pref;
import com.sabziatdoor.util.ServerConnector;

public class AddAddressActivity extends ActionBarActivity implements OnClickListener {

	private Toolbar toolbar;
	//private ImageView closeImageView;

	private EditText nameEditText;
	private EditText houseEditText;
	private EditText streetEditText;
	private EditText areaEditText;
	private EditText cityEditText;
	//private EditText zipEditText;
    private Spinner zipSpinner;
    
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
	
	private List<ZipCode> zipCodeArrayList;
	private List<String> zipNameList;
	

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
		zipSpinner =(Spinner) findViewById(R.id.addZipSpinner);
		zipCodeArrayList=new ArrayList<ZipCode>();
		zipNameList=new ArrayList<String>();
		
		if(cd.isConnectingToInternet()){
			new GetZipCodeTask().execute(Constant.HOST+Constant.SERVICE_ZIP_CODE);
		}else{
		    Toast.makeText(mContext,getString(R.string.lbl_network_connection_fail),Toast.LENGTH_SHORT).show();
		}
	
		
		if (isEdit) {
			createButton.setText("Save");
			nameEditText.setText(item.getAddFullName());

			areaEditText.setText(item.getAddLandmark());
			cityEditText.setText(item.getAddCity());
			//zipEditText.setText(item.getAddZipCode());

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
							+ "&add_zipcode=" + zipCodeArrayList.get(zipSpinner.getSelectedItemPosition()).getZipId();
				} else {
					try {
						addressBody = "usr_id=" + userId + "&add_id=" + "&add_fullname=" + URLEncoder.encode(nameEditText.getText().toString(), "utf-8").toString()
								+ "&add_phone=" + Pref.getValue(Constant.PREF_PHONE_NUMBER, "0") 
								+ "&add_address1="+ URLEncoder.encode(houseEditText.getText().toString(), "utf-8").toString() 
								+ "&add_address2=" + URLEncoder.encode(streetEditText.getText().toString(), "utf-8").toString() 
								+ "&add_landmark=" + URLEncoder.encode(areaEditText.getText().toString(), "utf-8").toString() 
								+ "&add_zipcode="+zipCodeArrayList.get(zipSpinner.getSelectedItemPosition()).getZipId();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
				&& cityEditText.getText().toString().length() > 0 && zipCodeArrayList.size()>0) {
			return false;
		} else {
			return true;
		}
	}
	
	
	private class GetZipCodeTask extends AsyncTask<String, Void, JSONObject> {
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
				if (result != null && result.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
					
					zipCodeArrayList=new ArrayList<ZipCode>();
					zipNameList=new  ArrayList<String>();
					
					JSONArray zipDataArray=result.getJSONArray("DATA");
					for(int i=0;i<zipDataArray.length();i++){
						JSONObject obj=zipDataArray.getJSONObject(i);
						ZipCode rowitem=new ZipCode();
						rowitem.setZipId(obj.getString("zip_id"));
						rowitem.setZipCode(obj.getString("zip_code"));
						rowitem.setZipName(obj.getString("zip_name"));	
						zipNameList.add(obj.getString("zip_name"));
						zipCodeArrayList.add(rowitem);
					}
					// Create an ArrayAdapter using the string array and a default spinner layout
					ArrayAdapter<String> adapterZip = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, zipNameList);
					// Specify the layout to use when the list of choices appears
					adapterZip.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// Apply the adapter to the spinner
					zipSpinner.setAdapter(adapterZip);
					
					if (isEdit){
						if(item!=null){
							int zipPosition = 0;
							
							for(int i=0;i<zipCodeArrayList.size();i++){
								if(zipCodeArrayList.get(i).getZipId().equalsIgnoreCase(item.getAddZipCode())){
									zipPosition=i;
									break;
								}
							}
							zipSpinner.setSelection(zipPosition);
							
						}
					}					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
}