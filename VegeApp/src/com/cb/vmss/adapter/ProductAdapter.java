package com.cb.vmss.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
	private int totalCount = 0;
	private int totalPrize = 0;
	//private HashMap<Integer,Bitmap> productImageMap = new HashMap<Integer, Bitmap>();
	
	public interface ITotalCount {
		public void getTotal(int count,int prize);
	}
	
	ITotalCount iTotalCount = null;
	
	public ProductAdapter(Context context, List<Product> mProductList) {
		this.context = context;
		this.mProductRowItem = (ArrayList<Product>) mProductList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		iTotalCount = (ITotalCount) context;
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

        final ViewHolder holder;
        final Product rowItem = (Product) getItem(position);
         
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_product_item, null);
            holder = new ViewHolder();
            holder.txtViewProductName = (TextView)convertView.findViewById(R.id.productItemNameTV);
            holder.txtViewProductWeight = (TextView)convertView.findViewById(R.id.productWeightTextView);
            holder.txtViewProductPrice = (TextView)convertView.findViewById(R.id.productPriceTextView);
            holder.txtViewQty= (TextView)convertView.findViewById(R.id.txtQty);
            holder.icoPlus= (ImageView)convertView.findViewById(R.id.iconPlus);
            holder.icoMinus= (ImageView)convertView.findViewById(R.id.iconMinus);
            holder.productImageObj= (ImageView)convertView.findViewById(R.id.productImage);
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.icoMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Qty", "icoMinus");
				int qty=Integer.parseInt(holder.txtViewQty.getText().toString())-1;
				if(qty>=0) {
					totalCount = totalCount - 1;
					totalPrize = totalPrize - Integer.parseInt(holder.txtViewProductPrice.getText().toString());
					if(totalPrize<=0) {
						totalPrize = 0;
					}
					if(totalCount<=0) {
						totalCount = 0;
					}
				}
				if(qty<=0){
					holder.txtViewQty.setText("0");
					rowItem.setProductQty(0);
				}else{
					holder.txtViewQty.setText(""+qty);
					rowItem.setProductQty(qty);
				}
				iTotalCount.getTotal(totalCount,totalPrize);
			}
		});
        
        holder.icoPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Qty", "icoPlus");
				int qty=Integer.parseInt(holder.txtViewQty.getText().toString())+1;
				totalPrize = totalPrize + Integer.parseInt(holder.txtViewProductPrice.getText().toString());
				holder.txtViewQty.setText(""+qty);
				rowItem.setProductQty(qty);
				totalCount = totalCount + 1;
			    iTotalCount.getTotal(totalCount,totalPrize);
			}
		});
        holder.txtViewProductName.setText(rowItem.getProductName());
        holder.txtViewProductPrice.setText(rowItem.getProductMainPrice());
        holder.txtViewQty.setText(rowItem.getProductQty()+"");
        if(rowItem.getProductBitmap() != null) {
        	Drawable imageDrawable = new BitmapDrawable(context.getResources(), rowItem.getProductBitmap());
            holder.productImageObj.setImageDrawable(imageDrawable);
        } else 
        	new DownloadImageTask(position, holder.productImageObj).execute(rowItem.getProductImage());
        
        return convertView;
	}

	static class ViewHolder {
		TextView txtViewProductName;
		TextView txtViewProductWeight;
		TextView txtViewProductPrice;
		TextView txtViewQty;
		ImageView icoPlus;
		ImageView icoMinus;
		ImageView productImageObj;
    }
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		int position;
		public DownloadImageTask(int position,ImageView bmImage) {
			this.position = position;
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(Bitmap result) {
			//productImageMap.put(this.position, result);
			mProductRowItem.get(this.position).setProductBitmap(result);
			Drawable imageDrawable = new BitmapDrawable(context.getResources(), result);
			bmImage.setImageDrawable(imageDrawable);
		}
	}
}