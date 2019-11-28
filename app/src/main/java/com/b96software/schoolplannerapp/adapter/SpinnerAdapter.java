package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.SpinnerItem;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    private Context context;
    private ArrayList<SpinnerItem> items;

    public SpinnerAdapter(Context context, int resource, ArrayList<SpinnerItem> items) {
        super(context, resource);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return  createDropDownItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return  createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        View v = LayoutInflater.from(context).inflate(R.layout.spinner_card_selected, parent, false);

        TextView itemDate = v.findViewById(R.id.itemDateTextView);
        itemDate.setTextColor(context.getResources().getColor(android.R.color.white));

        SpinnerItem item = items.get(position);
        itemDate.setText(item.getItemDate());

        return v;
    }


    private View createDropDownItemView(int position, View convertView, ViewGroup parent){
        View v = LayoutInflater.from(context).inflate(R.layout.card_spinner, parent, false);

        TextView itemType = v.findViewById(R.id.itemTypeTextView);
        TextView itemDate = v.findViewById(R.id.itemDateTextView);

        SpinnerItem item = items.get(position);

        itemType.setText(item.getItemName());
        itemDate.setText(item.getItemDate());

        return v;
    }


    public void addItems(ArrayList<SpinnerItem> spinnerItems)
    {
        items.addAll(spinnerItems);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<SpinnerItem> spinnerItems)
    {
        for(int i = 0; i < spinnerItems.size(); i++)
        {
            items.set(i, spinnerItems.get(i));
        }
        notifyDataSetChanged();
    }




    @Override
    public int getCount() {
        return items.size();
    }

}
