package com.cb.vmss.cusomview;

import com.cb.vmss.SubjiAtDoorApplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewCustom extends TextView {

	
	public TextViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public TextViewCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public TextViewCustom(Context context) {
		super(context);
		init();
	}

	private void init() {
       
        setTypeface(SubjiAtDoorApplication.getCustomTF());
    }
	
}
