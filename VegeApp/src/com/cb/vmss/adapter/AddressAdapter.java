package com.cb.vmss.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cb.vmss.AddAddressActivity;
import com.cb.vmss.MainActivity;
import com.cb.vmss.R;
import com.cb.vmss.model.Address;
import com.cb.vmss.util.Constant;
import com.cb.vmss.util.Pref;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private ArrayList<Address> mAddressRowItem = new ArrayList<Address>();
	
	
	public interface ITotalCount {
		public void getTotal(int updateValue,int prize);
	}
	
	ITotalCount iTotalCount = null;
	
	public AddressAdapter(Context context, List<Address> mAddresstList) {
		this.context = context;
		this.mAddressRowItem = (ArrayList<Address>) mAddresstList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mAddressRowItem.size();
	}

	@Override
	public Object getItem(int position) {
		return mAddressRowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        final Address rowItem = (Address) getItem(position);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_address_item, null);
            holder = new ViewHolder();
            holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
            holder.address1TextView = (TextView)convertView.findViewById(R.id.address1TextView);
            holder.address2TextView = (TextView)convertView.findViewById(R.id.address2TextView);
            holder.address3TextView = (TextView)convertView.findViewById(R.id.address3TextView);
            holder.iconEditAddress = (ImageView) convertView.findViewById(R.id.icoEditAddress);
            holder.iconDeleteAddress = (ImageView) convertView.findViewById(R.id.icoDeleteAddress);
            
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtName.setText(rowItem.getAddFullName());
        holder.address1TextView.setText(rowItem.getAddAddress1() + ", "+ rowItem.getAddAddress2());
        holder.address2TextView.setText(rowItem.getAddLandmark()+ ", "+ rowItem.getAddCity());
        holder.address3TextView.setText(rowItem.getAddZipCode());
        
        holder.iconEditAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addressIntent=new Intent(context, AddAddressActivity.class);
				addressIntent.putExtra("edit", true);
				addressIntent.putExtra("address", (Serializable)rowItem);
				context.startActivity(addressIntent);
			}
		});
        
        holder.iconDeleteAddress.setOnClickListener(new OnClickListener() {
	
        	@Override
        	public void onClick(View v) {

        		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
        				context);
        			// set title
        			alertDialogBuilder.setTitle("Alert");
        			// set dialog message
        			alertDialogBuilder
        				.setMessage("Are you sure you want to delete?")
        				.setCancelable(false)
        				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog,int id) {
        						
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
        });
        
        return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		TextView address1TextView;
		TextView address2TextView;
		TextView address3TextView;
		ImageView iconEditAddress;
		ImageView iconDeleteAddress;
    }
}