package com.sabziatdoor.util;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {

	
	private int hours,minute;
	private String title;
	
	private Context mContext;
	private OnTimeSetListener onTimeSet;
	private TimePickerDialog mTimePickerDialog;
	
	OnDateSetListener ondateSet;
	
	public TimePickerFragment(Context mContext) {
	  this.mContext=mContext; 
	}
	
	public void setCallBack(OnTimeSetListener ontime) {
		onTimeSet = ontime;
    }

	@Override
	 public void setArguments(Bundle args) {
	  super.setArguments(args);
	  hours = args.getInt("hours");
	  minute = args.getInt("minute");
	  title=args.getString("title");
	 }
	
	
	@Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
		// getDialog().setTitle(title);
		 
		mTimePickerDialog=new TimePickerDialog(getActivity(), onTimeSet, hours, minute,false);
				
		mTimePickerDialog.setTitle(title);
		
		return mTimePickerDialog;	
		
	}
}
