package com.cb.vmss;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

public class VerifyPhoneActivity extends ActionBarActivity implements OnClickListener{

	private Toolbar toolbar;

	private TextView phoneNumberTextView;
	private EditText manualCodeEditText;
	private Button resendCodeButton;
	private Button nextButton;
	
	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	private String mServiceUrl;
	private String mVerificationBody;
	private String mPhoneNumber;
	private String mUserId;
	private String mFromScreen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_phone);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_verifying_phone));
			
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
			
		}
		mFromScreen=getIntent().getStringExtra("fromscreen");
		mContext = this;
		Constant.CONTEXT=mContext;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		mProgressDialog = new ProgressDialog(VerifyPhoneActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        
        
		phoneNumberTextView=(TextView)findViewById(R.id.phoneNumberTextView);
		manualCodeEditText=(EditText)findViewById(R.id.manualCodeVerifyEdittext);
		resendCodeButton=(Button)findViewById(R.id.btnResendCodePhoneVerify);
		nextButton=(Button)findViewById(R.id.btnNextPhoneVerify);
		
		resendCodeButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		

		mPhoneNumber=getIntent().getStringExtra(Constant.PREF_PHONE_NUMBER);
		mUserId=getIntent().getStringExtra(Constant.PREF_USER_ID);
		
		phoneNumberTextView.setText("+91 "+mPhoneNumber);
		
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
		switch(view.getId()){
		case R.id.btnResendCodePhoneVerify:
			 mServiceUrl=Constant.HOST+Constant.SERVICE_USER_CREATION;
			 mVerificationBody="usr_phone="+mPhoneNumber;
			new CodeVerificationTaskTask().execute(mServiceUrl,mVerificationBody,"CODE_RESEND");
			break;
		case R.id.btnNextPhoneVerify:
			if(manualCodeEditText.getText().toString().length()>0){
				 mServiceUrl=Constant.HOST+Constant.SERVICE_USER_VERIFY;
				 mVerificationBody="usr_id="+mUserId+"&sms_code="+manualCodeEditText.getText().toString();
				 new CodeVerificationTaskTask().execute(mServiceUrl,mVerificationBody,"PHONE_VERIFICATION");
			}else{
				Toast.makeText(getApplicationContext(),"Code should not be blank",Toast.LENGTH_SHORT).show();
			}
			break;	
		}
	}
	
	private class CodeVerificationTaskTask extends AsyncTask<String, Void, JSONObject> {
	   private String requestType;
	   
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog.show();
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			requestType=params[2];
			return connector.getDataFromServer(params[0],params[1]);
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if(requestType.equals("CODE_RESEND")){
				codeResendResponse(result);
			}else if(requestType.equals("PHONE_VERIFICATION")){
				phoneVerificationResponse(result);
			}
			
						
		}
	}

	private void phoneVerificationResponse(JSONObject result){
		try {
			if(result!=null&&result.getString("STATUS").equalsIgnoreCase("SUCCESS")){
				    Toast.makeText(mContext,"Phone number verification success",Toast.LENGTH_SHORT).show();
				    JSONArray dataArray=result.getJSONArray("DATA");    
				    Pref.setValue(Constant.PREF_USER_ID,dataArray.getJSONObject(0).getString("usr_id"));
					Pref.setValue(Constant.PREF_PHONE_NUMBER,dataArray.getJSONObject(0).getString("usr_phone"));	
					forwardToAnotherActivity();
			}else{
				Toast.makeText(mContext,"Phone number verification fail",Toast.LENGTH_SHORT).show();;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void codeResendResponse(JSONObject result){
		try {
			if(result!=null&&result.getString("STATUS").equalsIgnoreCase("SUCCESS")){
				//JSONObject returnObject=result.getJSONObject("DATA");
				//Pref.setValue(Constant.PREF_USER_ID,returnObject.getString("usr_id"));
				//Pref.setValue(Constant.PREF_PHONE_NUMBER,returnObject.getString("usr_phone"));	
				
			}else{
				Toast.makeText(mContext,"Resend verification code request fail",Toast.LENGTH_SHORT).show();;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	public void forwardToAnotherActivity(){
		if(mFromScreen.equalsIgnoreCase(MainActivity.class.getCanonicalName())){
			setResult(Constant.CODE_MAIN_LOGIN);
			finish();
		}else{
			 Intent checkOutIntent=new Intent(getApplicationContext(),CheckOutActivity.class);
			 startActivity(checkOutIntent);				 
		}
	}
}
