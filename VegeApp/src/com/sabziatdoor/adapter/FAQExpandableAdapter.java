package com.sabziatdoor.adapter;

import java.util.List;

import com.sabziatdoor.R;
import com.sabziatdoor.model.FAQBean;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class FAQExpandableAdapter extends BaseExpandableListAdapter {

	List<FAQBean> mFAQList;
	private Context mContext;

	public FAQExpandableAdapter(Context context, List<FAQBean> mFAQList) {
		this.mFAQList = mFAQList;
		mContext = context;
	}

	@Override
	public int getGroupCount() {
		return mFAQList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mFAQList.get(groupPosition);

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mFAQList.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		FAQBean mFaqBean = (FAQBean) getGroup(groupPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item_faq_help, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.faqTitleTextView);
		//item.setTypeface(null, Typeface.BOLD);
		item.setText(mFaqBean.getHeading().toString());

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		FAQBean mFaqBean = (FAQBean) getGroup(groupPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_faq_childe_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.faqsubcontentTextView);
		item.setText(mFaqBean.getContent().toString());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
