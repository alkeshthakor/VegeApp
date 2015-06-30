package com.cb.vmss.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cb.vmss.R;
import com.cb.vmss.model.Product;

public class ProductAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private ArrayList<Product> mProductRowItem = new ArrayList<Product>();
	
	public ProductAdapter(Context context, List<Product> mProductList) {
		this.context = context;
		this.mProductRowItem = (ArrayList<Product>) mProductList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mProductRowItem.size();
	}

	@Override
	public Object getItem(int position) {
		return mProductRowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Product rowItem = (Product) getItem(position);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_product_item, null);
            holder = new ViewHolder();
            holder.txtViewProductName = (TextView)convertView.findViewById(R.id.productItemNameTV);
            holder.txtViewProductWeight = (TextView)convertView.findViewById(R.id.productWeightTextView);
            holder.txtViewProductPrice = (TextView)convertView.findViewById(R.id.productPriceTextView);
            holder.txtViewQty= (TextView)convertView.findViewById(R.id.txtQty);
            holder.icoPlus= (ImageView)convertView.findViewById(R.id.iconPlus);
            holder.icoMinus= (ImageView)convertView.findViewById(R.id.iconMinus);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.icoMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("Qty", "icoMinus");
				
			}
		});
        
        holder.icoPlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("Qty", "icoPlus");
				
			}
		});
        holder.txtViewProductName.setText(rowItem.getProductName());
        //holder.txtViewProductWeight.setText(rowItem.getProductUnitId());
        holder.txtViewProductPrice.setText(rowItem.getProductMainPrice());
        return convertView;
	}

	static private class ViewHolder {
		TextView txtViewProductName;
		TextView txtViewProductWeight;
		TextView txtViewProductPrice;
		TextView txtViewQty;
		ImageView icoPlus;
		ImageView icoMinus;
    }
}