package com.cb.vmss;

import com.cb.vmss.fragment.FragmentDrawer;
import com.cb.vmss.fragment.HomeFragment;
import com.cb.vmss.util.ConnectionDetector;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;
import com.cb.vmss.util.ServerConnector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener
{

	private static String TAG = MainActivity.class.getSimpleName();

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;
	private TextView mTitleTextView;
	
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Constant.CONTEXT = this;
		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		if (mToolbar != null)
		{
			setSupportActionBar(mToolbar);
			mTitleTextView= (TextView) mToolbar
					.findViewById(R.id.toolbar_title);
			mTitleTextView.setText(getString(R.string.app_name));
			
		
		}
		mToolbar.setTitleTextColor(Color.BLACK);
		
		//setSupportActionBar (mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);
		
		drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);

		// display the first navigation drawer view on app launch
		displayView(R.id.nav_location);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu; this adds items to the action bar
	 * if it is present. getMenuInflater ().inflate (R.menu.menu_main, menu); return true; }
	 */

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle action bar item clicks here. The action
	 * bar will // automatically handle clicks on the Home/Up button, so long // as you specify a parent activity in
	 * AndroidManifest.xml. int id = item.getItemId ();
	 * 
	 * //noinspection SimplifiableIfStatement if (id == R.id.action_settings) { return true; }
	 * 
	 * if (id == R.id.action_search) { Toast.makeText (getApplicationContext (), "Search action is selected!",
	 * Toast.LENGTH_SHORT).show (); return true; }
	 * 
	 * return super.onOptionsItemSelected (item); }
	 */

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Constant.CONTEXT = this;
	}
	@Override
	public void onDrawerItemSelected(View view)
	{
		displayView(view.getId());
	}

	@SuppressLint("ShowToast")
	private void displayView(int position)
	{
		Fragment fragment = null;
		//String title = getString(R.string.app_name);
		switch (position)
		{
			case R.id.nav_location :
				fragment = new HomeFragment();
				//title = getString(R.string.title_home);
				loadFragment(fragment);
				break;
			
			case R.id.nav_login :

				if(!Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").equals("0")){
					Toast.makeText(MainActivity.this, "Already Login", Toast.LENGTH_LONG);
		        } else {
		        	Intent loginIntent=new Intent(getApplicationContext(),LoginActivity.class);
		        	loginIntent.putExtra("fromscreen",MainActivity.class.getCanonicalName());
		        	startActivityForResult(loginIntent,Constant.CODE_MAIN_LOGIN);
		        }
				break;
			case R.id.nav_address :
				if(!Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").equals("0")){					
					Intent chooseAddressIntent=new Intent(getApplicationContext(), ChooseAddressActivity.class);
					chooseAddressIntent.putExtra("fromscreen",MainActivity.class.getCanonicalName());
					startActivity(chooseAddressIntent);
		        } else {
		        	Intent loginIntent=new Intent(getApplicationContext(),LoginActivity.class);
		        	loginIntent.putExtra("fromscreen",MainActivity.class.getCanonicalName());
		        	startActivityForResult(loginIntent,Constant.CODE_MAIN_LOGIN);
		        }
				break;
			case R.id.nav_order :
				if(!Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").equals("0")){					
					Intent checkoutIntent=new Intent(getApplicationContext(),MyPreviousOrderActivity.class);
					startActivity(checkoutIntent);
		        } else {
		        	Intent loginIntent=new Intent(getApplicationContext(),LoginActivity.class);
		        	loginIntent.putExtra("fromscreen",MainActivity.class.getCanonicalName());
		        	startActivityForResult(loginIntent,Constant.CODE_MAIN_LOGIN);
		        }
				break;
			case R.id.nav_cart :	
				Intent cartIntent=new Intent(getApplicationContext(),MyCartActivity.class);
				startActivity(cartIntent);
				break;
			
			case R.id.nav_share :	
				 Intent intent = new Intent(Intent.ACTION_SEND);
				 intent.setType("text/plain");
				 intent.putExtra(Intent.EXTRA_TEXT, "My App\nhttps://play.google.com/store/apps/details?id=com.google.android.apps.messaging&hl=en");
				 startActivity(Intent.createChooser(intent, "Share this app"));
				 break;
				
			case R.id.nav_logout :
				if(!Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").equals("0")){
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
				Intent helpIntent=new Intent(getApplicationContext(),HelpActivity.class);
				startActivity(helpIntent);
				break;
			case R.id.nav_about:
				Intent aboutIntent=new Intent(getApplicationContext(),AboutUsActivity.class);
				startActivity(aboutIntent);
				
				break;
				
				default :
				break;
		}

	/*	if (fragment != null)
		{
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

			// set the toolbar title
			getSupportActionBar().setTitle(title);
		}*/
	}
	
	
	private void loadFragment(Fragment fragment){
		if (fragment != null)
		{
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

			// set the toolbar title
			//getSupportActionBar().setTitle(title);
		}
	}
	
	private void showConfirmLogout() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
			// set title
			alertDialogBuilder.setTitle("Logout");
			// set dialog message
			alertDialogBuilder
				.setMessage("Are you sure you want to logout?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						//MainActivity.this.finish();
						Pref.setValue(Constant.PREF_PHONE_NUMBER, "0");
						
						Pref.setValue(Constant.PREF_ADD_ID,"0");
						Pref.setValue(Constant.PREF_ADDRESS,"");
						
						
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
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
	    }else if (resultCode == Constant.CODE_BACK_WITH_CHECK_ORDER) {
	    	   //setResult(Constant.CODE_BACK_WITH_CHECK_ORDER);
     	   //finish(); 
     	displayView(R.id.nav_order);
     	
	    }else if (resultCode == Constant.CODE_BACK_WITH_COUTINUE_SHOPPING) {
	    	displayView(0);  
	    }
	}
	
	
	public void rateUs(){
		try {
			Uri marketUri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
			startActivity(marketIntent);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	public void confirmCall() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Contact Subji At Door");
		builder.setMessage("Do you want to call the Subji At Door help line?");
		
		builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:9925833511"));
				startActivity(intent);
				
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
	
}