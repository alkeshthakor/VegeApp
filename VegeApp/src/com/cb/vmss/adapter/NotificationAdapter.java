package com.cb.vmss.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cb.vmss.R;
import com.cb.vmss.database.VegAppDatabaseHelper;
import com.cb.vmss.model.VNotification;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	private ArrayList<VNotification> mNotificationList= new ArrayList<VNotification>();
	private VegAppDatabaseHelper mAppDatabaseHelper;
	ClipboardManager myClipboard;
	ClipData myClip;
	
	public NotificationAdapter(Context context, List<VNotification> mNotificationList) {
		this.context = context;
		this.mNotificationList = (ArrayList<VNotification>) mNotificationList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAppDatabaseHelper=new VegAppDatabaseHelper(context);
		myClipboard = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);	
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNotificationList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mNotificationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		final ViewHolder holder;
        final VNotification rowItem = (VNotification) getItem(position);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_notification_item, null);
            holder = new ViewHolder();
              holder.titleTextView = (TextView)convertView.findViewById(R.id.tvNotificationTitle);
              holder.messageTextView = (TextView)convertView.findViewById(R.id.tvNotificationMessage);
              holder.promocodeTextView = (TextView)convertView.findViewById(R.id.tvNotificationPromocode);
              holder.tapToCopyTextView = (TextView)convertView.findViewById(R.id.tvNotificationTapToCopy);
              holder.codeLableTextView = (TextView)convertView.findViewById(R.id.tvNotificationLable);
              
              holder.dividerLineView = (View)convertView.findViewById(R.id.viewNotificationDevider);
              holder.iconDeleteAddress = (ImageView)convertView.findViewById(R.id.ivNotificationdelete);
       
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.titleTextView.setText(rowItem.getTitle());
        holder.messageTextView.setText(rowItem.getMessage());
        
        if(rowItem.getPromocode()!=null&&!"".equalsIgnoreCase(rowItem.getPromocode()))
        {
        	holder.promocodeTextView.setText(rowItem.getPromocode());
        }else{
        	holder.dividerLineView.setVisibility(View.GONE);
        	holder.tapToCopyTextView.setVisibility(View.INVISIBLE);
        	holder.promocodeTextView.setVisibility(View.INVISIBLE);
        	holder.codeLableTextView.setVisibility(View.INVISIBLE);
        }
        
        holder.iconDeleteAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				confirmDelete(rowItem.getNoti_Id().toString(),position);
			}
		});
        
        holder.tapToCopyTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myClip = ClipData.newPlainText("text", holder.promocodeTextView.getText().toString());
				myClipboard.setPrimaryClip(myClip);
				Toast.makeText(context,"Promo code copied",Toast.LENGTH_SHORT).show();
				
			}
		});
        
		return convertView;
	}

	
	public void confirmDelete(final String notificationId,final int position) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Delete Notification");
		builder.setMessage("Do you want to delete notification?");
		
		builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mAppDatabaseHelper.open();
				mAppDatabaseHelper.deleteNotification(notificationId);
				mAppDatabaseHelper.close();
				
				mNotificationList.remove(position);
				notifyDataSetChanged();
				
			}
		});
		builder.setNegativeButton("No",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}
	
	static class ViewHolder {
		TextView titleTextView;
		TextView messageTextView;
		TextView promocodeTextView;
		TextView tapToCopyTextView;
		TextView codeLableTextView;
		View dividerLineView;
		ImageView iconDeleteAddress;
    }
	
}
