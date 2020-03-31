package com.example.persasist;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private ArrayList<Reminders> listdata;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mText;

        public ViewHolder(LinearLayout v) {
            super(v);

            mText = (TextView) v.findViewById(R.id.text_home);
        }
    }
    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<Reminders> listdata) {
        this.listdata = listdata;
    }

    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminders, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder((LinearLayout)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {
        Reminders reminders = listdata.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.mText;
        textView.setText(reminders.getDescription());
    }


    @Override
    public int getItemCount() {
        if(listdata!=null)
        return listdata.size();
        return 0;
    }

    public void add(Reminders reminders) {
        listdata.add(reminders);

        notifyDataSetChanged();
    }

}