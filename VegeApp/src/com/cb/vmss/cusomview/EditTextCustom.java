package com.cb.vmss.cusomview;

import com.cb.vmss.SubjiAtDoorApplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextCustom extends EditText {

	public EditTextCustom(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public EditTextCustom(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public EditTextCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	
	private void init() {
       
        setTypeface(SubjiAtDoorApplication.getCustomTF());
    }
	
	/*public EditTextCustom(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public EditTextCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public EditTextCustom(Context context) {
		super(context);
		init();
	}

	private void init() {
       
        setTypeface(SubjiAtDoorApplication.getCustomTF());
    }*/
}
