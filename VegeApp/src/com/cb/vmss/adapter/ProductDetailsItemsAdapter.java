package com.cb.vmss.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cb.vmss.R;
import com.cb.vmss.model.OrderItems;

public class ProductDetailsItemsAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private ArrayList<OrderItems> mOrderItemsRowItem = new ArrayList<OrderItems>();
	
	
	public interface ITotalCount {
		public void getTotal(int updateValue,int prize);
	}
	
	ITotalCount iTotalCount = null;
	
	public ProductDetailsItemsAdapter(Context context, List<OrderItems> mOrderItemsList) {
		this.context = context;
		this.mOrderItemsRowItem = (ArrayList<OrderItems>) mOrderItemsList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mOrderItemsRowItem.size();
	}

	@Override
	public Object getItem(int position) {
		return mOrderItemsRowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        final OrderItems rowItem = (OrderItems) getItem(position);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_product_order, null);
            holder = new ViewHolder();
            holder.productItemNameTV = (TextView)convertView.findViewById(R.id.productItemNameTV);
            holder.productQtyTextView = (TextView)convertView.findViewById(R.id.productQtyTextView);
            holder.productPriceTextView = (TextView)convertView.findViewById(R.id.productPriceTextView);
            holder.productTotalPriceTextView = (TextView)convertView.findViewById(R.id.productTotalPriceTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.productItemNameTV.setText(rowItem.getOrdItemName());
        holder.productQtyTextView.setText(rowItem.getOrdItemQty());
        holder.productPriceTextView.setText(rowItem.getOrdItemPrice());
        holder.productTotalPriceTextView.setText(String.valueOf(Integer.parseInt(rowItem.getOrdItemQty()) * Integer.parseInt(rowItem.getOrdItemPrice())));
        return convertView;
	}

	static class ViewHolder {
		TextView productItemNameTV;
		TextView productQtyTextView;
		TextView productPriceTextView;
		TextView productTotalPriceTextView;
    }
}