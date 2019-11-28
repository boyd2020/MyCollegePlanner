package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.Day;
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Day> days;

    public DayAdapter(Context context, ArrayList<Day> days)
    {
        this.context = context;
        this.days = days;
    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_class_day, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DayAdapter.ViewHolder holder, int position) {
        Day day = days.get(position);
        holder.dayName.setText(day.getName());

        if(day.getDayChecked() ==  Utils.NOT_CHECKED)
            holder.checkBox.setChecked(false);
        else
            holder.checkBox.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public ArrayList<Day> getDays()
    {
        return days;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.dayCheckbox)
        CheckBox checkBox;

        @BindView(R.id.dayName)
        TextView dayName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                int checked = days.get(getAdapterPosition()).getDayChecked();
                days.get(getAdapterPosition()).setDayChecked(checked == Utils.CHECKED ? Utils.NOT_CHECKED : Utils.CHECKED);

                notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
