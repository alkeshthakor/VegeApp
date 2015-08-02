package com.sabziatdoor.util;

import android.content.Context;

public class Constant {
	
	public static Context CONTEXT;

	// APPLICATION PREFERENCE FILE NAME
	public static final String PREF_FILE = "PREF_VMSS";

	//public static final String HOST="http://45.55.152.215/dev/sabjiatdoor/services/";
	
	public static final String HOST="https://www.sabziatdoor.com/services/";
	
	
		
	//http://45.55.152.215/dev/sabjiatdoor/services/share
	public static final String PREF_USER_ID = "PREF_USER_ID";
	public static final String PREF_PHONE_NUMBER= "PREF_PHONE_NUMBER";
	public static final String PREF_ADD_ID = "PREF_ADD_ID";
	public static final String PREF_ADDRESS = "PREF_ADDRESS";
	
	public static final String PREF_IS_GCM_REGISTER = "PREF_IS_GCM_REGISTER";
	public static final String PREF_GCM_REGISTRATION_ID = "PREF_GCM_REGISTRATION_ID";
	public static final String PREF_SHARE_URL = "PREF_SHARE_URL";
	
	public static final String SERVICE_GET_ALL_CATEGORY = "category/getallcategory";
	public static final String SERVICE_GET_ALL_PRODUCT = "PREF_VMSS";
	public static final String SERVICE_PRODUCT_BY_CAT_ID = "product/getproductbycatid";
	public static final String SERVICE_ADD_ADDRESS = "user/addaddress";
	public static final String SERVICE_FETCH_ADDRESS = "user/getaddressbyusrid";
	public static final String SERVICE_DELETE_ADDRESS = "user/deleteaddress";
	public static final String SERVICE_ZIP_CODE = "zipcode/getallzipcode";
			
	public static final String SERVICE_FETCH_PREVIOUS_ORDER = "order/orderhistory";
	public static final String SERVICE_USER_CREATION = "user/adduser";
	public static final String SERVICE_USER_VERIFY = "user/verifyuser";
	public static final String SERVICE_ADD_ORDER = "order/addorder";
	public static final String SERVICE_CANCEL_ORDER = "order/cancelorder";
	public static final String SERVICE_PROMO_CODE = "order/promocode";
	public static final String SERVICE_REFER_FRIEND = "refertofriend/addrefertofriend";
	
	public static final String SERVICE_SHARE = "share";
	
	
	public static final String PREF_QTY_COUNT = "PREF_QTY_COUNT";
	public static final String PREF_TOTAL_AMOUT = "PREF_TOTAL_AMOUNT";
	
	public static final int CODE_MAIN_LOGIN = 1001;
	public static final int CODE_BACK = 1002;
	
	public static final int CODE_BACK_WITH_CHECK_ORDER = 1003;
	public static final int CODE_BACK_WITH_COUTINUE_SHOPPING = 1004;
	

	
	/*	
	public static int DEVICE_SCREEN_WIDTH =0;
	
	public static int MOBILE_DEVICE_SCREEN = 480;
	public static int SAVEN_INCH_DEVICE_SCREEN =600;
	public static int TEN_INCH_DEVICE_SCREEN =800;

	public static String[] MONTHS_INDEXED = new String[] {"JAN", "FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
*/}
