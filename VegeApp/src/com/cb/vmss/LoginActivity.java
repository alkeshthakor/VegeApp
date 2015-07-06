package com.cb.vmss;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

public class LoginActivity extends Activity implements OnClickListener {

	private Toolbar toolbar;
	private ImageView closeImageView;
	private EditText phoneNumberEditText;
	private ProgressBar progressIndicater;
	
	private ConnectionDetector cd;
	private ServerConnector connector;
	private Context mContext;
	private String mServiceUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_title_login));
			closeImageView=(ImageView)toolbar.findViewById(R.id.imgeCloseTopBar);
			closeImageView.setOnClickListener(this);
		}
		
		
		mContext = this;
		Constant.CONTEXT=mContext;
		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();

		progressIndicater=(ProgressBar)findViewById(R.id.progressIndicater);
		phoneNumberEditText=(EditText)findViewById(R.id.phoneNumberEditText);

		phoneNumberEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if(phoneNumberEditText.getText().length()==10){
					//Toast.makeText(getApplicationContext(),"Valid Phone number",Toast.LENGTH_SHORT).show();
					 mServiceUrl=Constant.HOST+Constant.SERVICE_USER_CREATION;
					// usr_phone= 9909983932
					 new createNewUserTask().execute(mServiceUrl,phoneNumberEditText.getText().toString());
				}
			}
		});
		
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.imgeCloseTopBar:
			finish();
			
			break;	
			
		}
		
	}
	
	
	private class createNewUserTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressIndicater.setVisibility(View.VISIBLE);;
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			return connector.getDataFromServer(params[0],"usr_phone="+params[1]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			progressIndicater.setVisibility(View.INVISIBLE);;
			try {
				if(result!=null&&result.getString("STATUS").equalsIgnoreCase("SUCCESS")){
					Toast.makeText(mContext,"User created successfully",Toast.LENGTH_SHORT).show();
					JSONObject returnObject=result.getJSONObject("DATA");
					Pref.setValue(Constant.PREF_USER_ID,returnObject.getString("usr_id"));
					Pref.setValue(Constant.PREF_PHONE_NUMBER,returnObject.getString("usr_phone"));
					
					Intent verifyPhoneIntent=new Intent(getApplicationContext(),VerifyPhoneActivity.class);
					startActivity(verifyPhoneIntent);
					
					
					finish();
				}else{
					Toast.makeText(mContext,"User creation fail or login fail",Toast.LENGTH_SHORT).show();;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		}
	}
	
/*	public void confirmCall() {

		AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		builder.setTitle("Confirm Phone number");
		builder.setMessage("");
		
		builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
			}
		});
		builder.setNegativeButton("No",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}
*/	
}