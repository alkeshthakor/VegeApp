package com.cb.vmss.util;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class DatePickerFragment extends DialogFragment{
	private int year, month, day,dialogType;
	private Context mContext;
	private DatePickerDialog mDatePickerDialog;
	OnDateSetListener ondateSet;
	
	public DatePickerFragment(Context mContext) {
	  this.mContext=mContext; 
	}
	
	public void setCallBack(OnDateSetListener ondate) {
		  ondateSet = ondate;
    }

	@Override
	 public void setArguments(Bundle args) {
	  super.setArguments(args);
	  year = args.getInt("year");
	  month = args.getInt("month");
	  day = args.getInt("day");
	  dialogType = args.getInt("type");
	  
	 }

	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 switch(dialogType){
		 case 0:	 
			 mDatePickerDialog=new DatePickerDialog(getActivity(), ondateSet, year, month, day);
			 return mDatePickerDialog;
		 case 1:
			 //return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
			 mDatePickerDialog=new DatePickerDialog(getActivity(), ondateSet, year, month, day){
				 @Override
				 protected void onCreate(Bundle savedInstanceState) {
					 
					  mDatePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
					 
					/* int day =mContext.getResources().getIdentifier("android:id/day", null, null);
				        if(day != 0){
				            View monthPicker =mDatePickerDialog.findViewById(day);
				            if(monthPicker != null){
				            	monthPicker.setVisibility(View.GONE);
				            }
				        }*/
				 };
			 };
			 return mDatePickerDialog;
		 }
	  return null;
	 }
	 
}
