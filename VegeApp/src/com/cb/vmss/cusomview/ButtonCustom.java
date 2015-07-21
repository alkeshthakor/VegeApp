package com.cb.vmss.cusomview;

import com.cb.vmss.SubjiAtDoorApplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonCustom extends Button {

	
	public ButtonCustom(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public ButtonCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public ButtonCustom(Context context) {
		super(context);
		init();
	}

	private void init() {
       
        setTypeface(SubjiAtDoorApplication.getCustomTF());
    }
	
}
