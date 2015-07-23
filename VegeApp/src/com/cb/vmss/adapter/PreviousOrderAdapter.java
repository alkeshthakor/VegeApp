package com.cb.vmss.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cb.vmss.AddAddressActivity;
import com.cb.vmss.MyOrderDetailsActivity;
import com.cb.vmss.R;
import com.cb.vmss.model.PreviousOrder;

public class PreviousOrderAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;		
	private ArrayList<PreviousOrder> mPreviousOrderRowItem = new ArrayList<PreviousOrder>();
	
	
	public interface ITotalCount {
		public void getTotal(int updateValue,int prize);
	}
	
	ITotalCount iTotalCount = null;
	
	public PreviousOrderAdapter(Context context, List<PreviousOrder> mPreviousOrderList) {
		this.context = context;
		this.mPreviousOrderRowItem = (ArrayList<PreviousOrder>) mPreviousOrderList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mPreviousOrderRowItem.size();
	}

	@Override
	public Object getItem(int position) {
		return mPreviousOrderRowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        final PreviousOrder rowItem = (PreviousOrder) getItem(position);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_previous_order_item, null);
            holder = new ViewHolder();
            holder.txtOrdDate = (TextView)convertView.findViewById(R.id.txtOrdDate);
            holder.txtOrdTime = (TextView)convertView.findViewById(R.id.txtOrdTime);
            holder.txtOrdNo = (TextView)convertView.findViewById(R.id.txtOrdNo);
            holder.txtStatus = (TextView)convertView.findViewById(R.id.statusTextView);
            holder.txtTotPrice = (TextView)convertView.findViewById(R.id.totPriceTextView);
            holder.iconEditAddress = (ImageView) convertView.findViewById(R.id.icoEditAddress);
            holder.viewDeatilsRelLayoutObj = (RelativeLayout) convertView.findViewById(R.id.viewDeatilsRelLayout);
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtOrdDate.setText(rowItem.getOrderDate());
        holder.txtOrdTime.setText(rowItem.getOrderTime());
        holder.txtOrdNo.setText("Order ID : " + rowItem.getOrderId());
        holder.txtStatus.setText("Status : " + rowItem.getOrderStatus());
        holder.txtTotPrice.setText(rowItem.getOrderTotalPrice());
        holder.viewDeatilsRelLayoutObj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myOrderDetailsIntent=new Intent(context, MyOrderDetailsActivity.class);
				myOrderDetailsIntent.putExtra("order", (Serializable)rowItem);
				context.startActivity(myOrderDetailsIntent);
			}
		});
        return convertView;
	}

	static class ViewHolder {
		TextView txtOrdDate;
		TextView txtOrdTime;
		TextView txtOrdNo;
		TextView txtStatus;
		TextView txtTotPrice;
		ImageView iconEditAddress;
		RelativeLayout viewDeatilsRelLayoutObj;
    }
}