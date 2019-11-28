package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.interfaces.MenuItemClickedListener;
import com.b96software.schoolplannerapp.model.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MenuItem> items;
    private MenuItemClickedListener callback;


    public MenuItemAdapter(Context context, ArrayList<MenuItem> items, MenuItemClickedListener callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
    }

    @Override
    public MenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_menu_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuItemAdapter.ViewHolder holder, int position) {
        MenuItem item = items.get(position);

        holder.itemIcon.setImageResource(item.getImage());
        holder.itemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.menuItemIcon)
        ImageView itemIcon;

        @BindView(R.id.menuItemText)
        TextView itemName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onMenuItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
