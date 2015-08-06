package com.sabziatdoor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sabziatdoor.R;
import com.sabziatdoor.database.VegAppDatabase.VegAppColumn;
import com.sabziatdoor.database.VegAppDatabaseHelper;
import com.sabziatdoor.imageutils.ImageLoader;
import com.sabziatdoor.model.Product;
import com.sabziatdoor.util.Constant;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private ArrayList<Product> mProductRowItem = new ArrayList<Product>();
	private VegAppDatabaseHelper mDatabaseHelper;
	private ImageLoader imgLoader;
	
	
	public interface ITotalCount {
		public void getTotal(int updateValue,float prize);
	}
	
	ITotalCount iTotalCount = null;
	
	public ProductAdapter(Context context, List<Product> mProductList) {
		this.context = context;
		this.mProductRowItem = (ArrayList<Product>) mProductList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		iTotalCount = (ITotalCount) context;
		mDatabaseHelper=new VegAppDatabaseHelper(context);
		imgLoader = new ImageLoader(this.context);
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
            holder.productDisplayPriceTextView = (TextView)convertView.findViewById(R.id.productDisplayPriceTextView);
            holder.productMainPriceTextView = (TextView)convertView.findViewById(R.id.productMainPriceTextView);
            holder.txtViewQty= (TextView)convertView.findViewById(R.id.txtQty);
            holder.icoPlus= (LinearLayout)convertView.findViewById(R.id.iconPlus);
            holder.icoMinus= (LinearLayout)convertView.findViewById(R.id.iconMinus);
            holder.productImageObj= (ImageView)convertView.findViewById(R.id.productImage);
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.icoMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				mDatabaseHelper.open();
				int productQtyInCart=mDatabaseHelper.getCartProductQty(rowItem.getProductId());
				
				if(productQtyInCart!=-1){
			     	productQtyInCart-=1;
			     	
			     	if(productQtyInCart<=0){
			     		productQtyInCart=0;
			     		mDatabaseHelper.deleteMyCartItem(rowItem.getProductId());
			     	} else {
			     		Float productsubTotal=Constant.round(productQtyInCart*Float.parseFloat(rowItem.getProductMainPrice()),2);
				     	ContentValues cartValue=new ContentValues();
				     	cartValue.put(VegAppColumn.CART_PRODUCT_QTY,productQtyInCart);
				     	cartValue.put(VegAppColumn.CART_PRODUCT_SUB_TOTAL,productsubTotal);
				     	mDatabaseHelper.updateMyCart(rowItem.getProductId(),cartValue);
			     	}
			     	mDatabaseHelper.close();
			     	holder.txtViewQty.setText(""+productQtyInCart);
					rowItem.setProductQty(productQtyInCart);
					iTotalCount.getTotal(-1,Float.parseFloat(rowItem.getProductMainPrice()));
				}
			}
		});
        
        holder.icoPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				mDatabaseHelper.open();
				
				int productQtyInCart=mDatabaseHelper.getCartProductQty(rowItem.getProductId());
				if(productQtyInCart!=-1){
					productQtyInCart+=1;
					Float productsubTotal=Constant.round(productQtyInCart*Float.parseFloat(rowItem.getProductMainPrice()),2);
			     	ContentValues cartValue=new ContentValues();
			     	cartValue.put(VegAppColumn.CART_PRODUCT_QTY,productQtyInCart);
			     	cartValue.put(VegAppColumn.CART_PRODUCT_SUB_TOTAL,productsubTotal);
			     	mDatabaseHelper.updateMyCart(rowItem.getProductId(),cartValue);
				} else {
			     	productQtyInCart=1;
			     	Float productsubTotal=productQtyInCart*Float.parseFloat(rowItem.getProductMainPrice());

			     	ContentValues cartValue=new ContentValues();
			     	
			     	cartValue.put(VegAppColumn.CART_PRODUCT_ID,rowItem.getProductId());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_NAME,rowItem.getProductName());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_IMAGE_URL,rowItem.getProductImage());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_MAIN_PRICE,rowItem.getProductMainPrice());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_DISPLAY_PRICE,rowItem.getProductDisplayPrice());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_UNIT_ID,rowItem.getProductUnitId());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_UNIT_KEY,rowItem.getUnit_key());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_UNIT_VALUE,rowItem.getUnit_value());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_CAT_ID,rowItem.getCategoryId());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_CAT_NAME,rowItem.getCategoryName());
			     	cartValue.put(VegAppColumn.CART_PRODUCT_QTY,productQtyInCart);
			     	cartValue.put(VegAppColumn.CART_PRODUCT_SUB_TOTAL,productsubTotal);
			     	mDatabaseHelper.addInToMyCart(cartValue);
			    }
				
				if(productQtyInCart>rowItem.getStockQty()){
					Toast.makeText(context,"Out of stock. Will be delivered tomorrow",Toast.LENGTH_SHORT).show();
				}
				holder.txtViewQty.setText(""+productQtyInCart);
				rowItem.setProductQty(productQtyInCart);				
				mDatabaseHelper.close();
				iTotalCount.getTotal(1,Float.parseFloat(rowItem.getProductMainPrice()));
			}
		});
        holder.txtViewProductName.setText(rowItem.getProductName());
        holder.txtViewProductWeight.setText(rowItem.getUnit_value().trim()+ " " + rowItem.getUnit_key().trim());
        holder.productDisplayPriceTextView.setText(rowItem.getProductDisplayPrice());
        holder.productMainPriceTextView.setText(rowItem.getProductMainPrice());
        holder.productDisplayPriceTextView.setPaintFlags(holder.productDisplayPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtViewQty.setText(rowItem.getProductQty()+"");
        if(rowItem.getProductBitmap() != null) {
        	Drawable imageDrawable = new BitmapDrawable(context.getResources(), rowItem.getProductBitmap());
            holder.productImageObj.setImageDrawable(imageDrawable);
        } else {
        	if(rowItem.getProductImage().length()>0) {
        		imgLoader.DisplayImage(rowItem.getProductImage(), holder.productImageObj);
        		//new DownloadImageTask(position, holder.productImageObj).execute(rowItem.getProductImage());
        	} else {
        		holder.productImageObj.setImageResource(R.drawable.no_image);
        	}
        }
        return convertView;
	}

	static class ViewHolder {
		TextView txtViewProductName;
		TextView txtViewProductWeight;
		TextView productDisplayPriceTextView;
		TextView productMainPriceTextView;
		TextView txtViewQty;
		LinearLayout icoPlus;
		LinearLayout icoMinus;
		ImageView productImageObj;
    }
	
	/*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
	}*/
}