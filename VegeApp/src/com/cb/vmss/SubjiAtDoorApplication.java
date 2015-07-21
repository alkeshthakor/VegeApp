package com.cb.vmss;

import android.app.Application;
import android.graphics.Typeface;

public class SubjiAtDoorApplication extends Application {
	private static Typeface customTF;
	private static Typeface robotoLightTF;
	private static Typeface robotoRegular;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		customTF=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/custom.ttf");
		robotoLightTF=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Roboto-Light.ttf");
		robotoRegular=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/.ttf");
	}


	public static Typeface getCustomTF() {
		return customTF;
	}


	public static void setCustomTF(Typeface customTF) {
		SubjiAtDoorApplication.customTF = customTF;
	}


	public static Typeface getRobotoLightTF() {
		return robotoLightTF;
	}


	public static void setRobotoLightTF(Typeface robotoLightTF) {
		SubjiAtDoorApplication.robotoLightTF = robotoLightTF;
	}


	public static Typeface getRobotoRegular() {
		return robotoRegular;
	}


	public static void setRobotoRegular(Typeface robotoRegular) {
		SubjiAtDoorApplication.robotoRegular = robotoRegular;
	}
	
	
	
}
