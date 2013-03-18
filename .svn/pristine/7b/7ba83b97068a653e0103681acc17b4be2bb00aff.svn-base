package fr.eurecom.mobserv.arianna.drawerutil;
//package com.example.testslidingmenu;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.LinearLayout;
//
//public class MenuListAdapter extends ArrayAdapter<MenuItem>
//{
//
//    private int resource;
//
//    public MenuListAdapter(Context context, int resource, List<MenuItem> items)
//    {
//        super(context, resource, items);
//
//        this.resource = resource;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//        ViewHolder holder = null;
//        LinearLayout contactListView;
//
//        if (convertView == null)
//        {
//            contactListView = new LinearLayout(getContext());
//            String inflater = Context.LAYOUT_INFLATER_SERVICE;
//            LayoutInflater layoutInflater;
//            layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
//            layoutInflater.inflate(resource, contactListView, true);
//
//            holder = new ViewHolder();
//
//            holder.textViewName = (TextView) contactListView.findViewById(R.id.name);
//            holder.textViewAddress = (TextView) contactListView.findViewById(R.id.address);
//
//            contactListView.setTag(holder);
//        }
//        else
//        {
//            contactListView= (LinearLayout) convertView;
//
//            holder = (ViewHolder) contactListView.getTag();
//        }
//
//        MenuItem item = getItem(position);
//
//        holder.textViewName.setText(item.getName());
//        holder.textViewAddress.setText(item.getAddress());
//
//        return contactListView;
//    }
//
//    protected static class ViewHolder
//    {
//        TextView textViewName;
//        TextView textViewAddress;
//    }
//}