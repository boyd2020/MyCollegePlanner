package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.model.Event;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Long> dates;
    private ArrayList<EventAdapter> adapters;
    private SimpleDateFormat format;

    public WeeklyAdapter(Context context, ArrayList<Long> dates, ArrayList<EventAdapter> adapters)
    {
        this.context = context;
        this.dates = dates;
        this.adapters = adapters;
    }

    @Override
    public WeeklyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_agenda_day, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WeeklyAdapter.ViewHolder holder, int position) {
        long startTime = dates.get(position);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

        holder.eventRecyclerView.setLayoutManager(layoutManager);
        holder.eventRecyclerView.setAdapter(adapters.get(position));

        format = new SimpleDateFormat(Utils.DATE_DAY_NUM);
        holder.dayNum.setText(format.format(startTime));

        format = new SimpleDateFormat(Utils.DATE_WEEKDAY);
        holder.dayTextView.setText(format.format(startTime));

        format = new SimpleDateFormat(Utils.DATE_MONTH_YEAR);
        holder.monthTextView.setText(format.format(startTime));

        format = new SimpleDateFormat(Utils.SQL_DATE_FORMAT);

        if(format.format(startTime).equals(format.format(System.currentTimeMillis())))
        {
            holder.dayNum.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.dayTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.monthTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            holder.dayNum.setTextColor(context.getResources().getColor(R.color.subTextColor));
            holder.dayTextView.setTextColor(context.getResources().getColor(R.color.subTextColor));
            holder.monthTextView.setTextColor(context.getResources().getColor(R.color.subTextColor));
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void setEventCursor(ArrayList<Event> events, int pos)
    {
        adapters.get(pos).setCursor(events);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.dayNumTextView)
        TextView dayNum;

        @BindView(R.id.dayTextView)
        TextView dayTextView;

        @BindView(R.id.monthTextView)
        TextView monthTextView;

        @BindView(R.id.eventRecyclerView)
        RecyclerView eventRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
