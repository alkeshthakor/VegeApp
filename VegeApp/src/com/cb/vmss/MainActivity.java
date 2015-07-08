package com.cb.vmss;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cb.vmss.fragment.FragmentDrawer;
import com.cb.vmss.fragment.HomeFragment;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;

public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener
{

	private static String TAG = MainActivity.class.getSimpleName();

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;

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
		}
		mToolbar.setTitleTextColor(Color.BLACK);
		
		//setSupportActionBar (mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);

		// display the first navigation drawer view on app launch
		displayView(0);
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
	public void onDrawerItemSelected(View view, int position)
	{
		displayView(position);
	}

	@SuppressLint("ShowToast")
	private void displayView(int position)
	{
		Fragment fragment = null;
		String title = getString(R.string.app_name);
		switch (position)
		{
			case 0 :
				fragment = new HomeFragment();
				title = getString(R.string.title_home);
				loadFragment(fragment,title);
				break;
			
			case 1 :
//				fragment = new CalenderFragment();
//				title = getString(R.string.title_calender);
				Intent chooseAddressIntent=new Intent(getApplicationContext(), ChooseAddressActivity.class);
				startActivity(chooseAddressIntent);
				break;
			
			case 2 :
//				fragment = new OverviewFragment();
//				title = getString(R.string.title_overview);
//				loadFragment(fragment,title);
				if(!Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").equals("0")){
					Intent checkoutIntent=new Intent(getApplicationContext(),CheckOutActivity.class);
					startActivity(checkoutIntent);
		        } else {
		        	Intent loginIntent=new Intent(getApplicationContext(),LoginActivity.class);
		        	startActivity(loginIntent);
		        }
				break;
			
			case 3 :
				/*fragment = new GroupsFragment();
				title = getString(R.string.title_groups);
				loadFragment(fragment,title);*/
				Intent cartIntent=new Intent(getApplicationContext(),MyCartActivity.class);
				startActivity(cartIntent);
				break;
			
			case 10 :
				if(!Pref.getValue(Constant.PREF_PHONE_NUMBER,"0").equals("0")){
					showConfirmLogout();
		        } else {
		        	Toast.makeText(MainActivity.this, "Already Logout", Toast.LENGTH_LONG);
		        }
				break;
			
			/*case 4 :
				fragment = new ListsFragment();
				title = getString(R.string.title_lists);
				loadFragment(fragment,title);

				break;
			case 5 :
				fragment = new ProfileFragment();
				title = getString(R.string.title_profile);
				loadFragment(fragment,title);

				break;
			case 6 :
				fragment = new ScheduleDeliveryFragment();
				title = getString(R.string.title_schedule_delivery);
				loadFragment(fragment,title);

				break;
			case 7 :
				fragment = new SettingsFragment();
				title = getString(R.string.title_settings);
				loadFragment(fragment,title);

				break;
			case 8 :
				
				 * fragment = new (); title = getString (R.string.title_settings);
				 
				// Operation for logout
				break;
			 */
				
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
	
	
	private void loadFragment(Fragment fragment,String title){
		if (fragment != null)
		{
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

			// set the toolbar title
			getSupportActionBar().setTitle(title);
		}
	}
	
	private void showConfirmLogout() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Logout");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Are you sure?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						//MainActivity.this.finish();
						Pref.setValue(Constant.PREF_PHONE_NUMBER, "0");
						
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
}