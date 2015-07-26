package com.cb.vmss.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cb.vmss.R;
import com.cb.vmss.model.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater inflater;
	private ArrayList<Category> mCategoryList = new ArrayList<Category>();

	public interface ITotalCount {
		public void getTotal(int updateValue, int prize);
	}

	ITotalCount iTotalCount = null;

	public CategoryAdapter(Context context, List<Category> mCategoryAdapter) {
		this.context = context;
		this.mCategoryList = (ArrayList<Category>) mCategoryAdapter;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mCategoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCategoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		final Category rowItem = (Category) getItem(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_category_item, null);
			holder = new ViewHolder();
			holder.categoryNameTextView = (TextView) convertView.findViewById(R.id.tvCategoryNameItem);
			holder.discriptionTextView = (TextView) convertView.findViewById(R.id.tvCategoryDiscriptionItem);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.categoryNameTextView.setText(rowItem.getCategoryName());
		holder.discriptionTextView.setText(rowItem.getCategroyDiscription());

		return convertView;
	}

	static class ViewHolder {
		TextView categoryNameTextView;
		TextView discriptionTextView;
		
	}
}