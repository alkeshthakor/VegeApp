package com.cb.vmss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cb.vmss.R;

public class ProductAdapter extends BaseAdapter {

	Context context;
	private String productNameList[];
	LayoutInflater inflater;
	
	public ProductAdapter(Context context, String items[]) {
		this.context=context;
		this.productNameList=new String[items.length];
		for(int i=0;i<items.length;i++) {
			this.productNameList[i] = items[i];
		}
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return productNameList.length;
	}

	@Override
	public Object getItem(int position) {
		return productNameList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        String rowItem = (String) getItem(position);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_product_item, null);
            holder = new ViewHolder();
            holder.txtViewProductName = (TextView)convertView.findViewById(R.id.productItemNameTV);
           
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        	holder.txtViewProductName.setText(rowItem);
 
       
        return convertView;
	}

	static private class ViewHolder {
		TextView txtViewProductName;
    }
}