package com.cb.vmss;

import com.cb.vmss.util.Constant;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutUsActivity extends ActionBarActivity {

	private Toolbar toolbar;
	private TextView appVersionTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_about_us_title));

			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}

		appVersionTextView = (TextView) findViewById(R.id.versionTextView);

		try {
			String versionName = getApplicationContext().getPackageManager()
					.getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;

			appVersionTextView.setText("V" + versionName);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
