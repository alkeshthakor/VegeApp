package com.cb.vmss.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cb.vmss.R;
import com.cb.vmss.model.NavDrawerItem;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder>
{
    List<NavDrawerItem> data = Collections.emptyList ();
    //String[] nav_icon;
    TypedArray nav_icon;
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data)
    {
        this.context = context;
        inflater = LayoutInflater.from (context);
        this.data = data;
        //nav_icon = context.getResources ().getStringArray (R.array.nav_drawer_icon);
        nav_icon = context.getResources ().obtainTypedArray (R.array.nav_drawer_icon);
    }

    public void delete(int position)
    {
        data.remove (position);
        notifyItemRemoved (position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate (R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        NavDrawerItem current = data.get (position);
        holder.title.setText (current.getTitle ());
        holder.icon.setImageResource (nav_icon.getResourceId (position, -1));
    }

    @Override
    public int getItemCount()
    {
        return data.size ();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView)
        {
            super (itemView);
            title = (TextView) itemView.findViewById (R.id.title);
            icon = (ImageView) itemView.findViewById (R.id.nav_item_icon);
        }
    }
}
