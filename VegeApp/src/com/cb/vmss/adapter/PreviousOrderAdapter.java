package com.cb.vmss.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
            holder.address1TextView = (TextView)convertView.findViewById(R.id.address1TextView);
            holder.address2TextView = (TextView)convertView.findViewById(R.id.address2TextView);
            holder.address3TextView = (TextView)convertView.findViewById(R.id.address3TextView);
            holder.iconEditAddress = (ImageView) convertView.findViewById(R.id.icoEditAddress);
            
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtName.setText("Order No : " + rowItem.getOrderId());
        holder.address1TextView.setText("Order Date : " + rowItem.getOrderDate());
        holder.address2TextView.setText("Order Items : " + rowItem.getTotalItem());
        holder.address3TextView.setText("Total Prize : " + rowItem.getOrderTotalPrice());
        return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		TextView address1TextView;
		TextView address2TextView;
		TextView address3TextView;
		ImageView iconEditAddress;
    }
}