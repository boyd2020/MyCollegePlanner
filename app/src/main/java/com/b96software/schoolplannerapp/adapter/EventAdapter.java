package com.b96software.schoolplannerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.assignments.AssignmentOverviewActivity;
import com.b96software.schoolplannerapp.classdate.EditClassDateActivity;
import com.b96software.schoolplannerapp.handlers.AssignmentHandler;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.model.Assignment;
import com.b96software.schoolplannerapp.model.ClassDate;
import com.b96software.schoolplannerapp.model.Event;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> events;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Event e = events.get(position);
        Log.e(e.getTitle(), "ID: " + String.valueOf(e.getId()));

        holder.eventName.setText(e.getTitle());
        holder.eventInfo.setText(e.getDesc());

        //Set Colors
        if(e.getColor() != 0) {

            holder.eventColor.setBackgroundColor(e.getColor());
            holder.eventInfo.setTextColor(e.getColor());
        }

        if(e.getType() == Utils.EVENT_TYPE_ASSIGNMENT)
            addAssignmentColor(holder, e);

    }

    private void addAssignmentColor(EventAdapter.ViewHolder holder, Event e)
    {
        int assignProgress = e.getStatus();
        long date = new GregorianCalendar().getTimeInMillis();

        try {
            date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(e.getDate()).getTime();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        holder.eventCard.setStrokeWidth((int)context.getResources().getDimension(R.dimen.default_stroke_width));

        //Get Assignment progress
        if(assignProgress == Utils.ASSIGN_TYPE_NOT_DONE && System.currentTimeMillis() < date)
            holder.eventCard.setStrokeColor(context.getResources().getColor(R.color.yellow));

        else if(System.currentTimeMillis() > date && assignProgress != Utils.ASSIGN_TYPE_COMPLETED)
            holder.eventCard.setStrokeColor(context.getResources().getColor(R.color.red));

        else
            holder.eventCard.setStrokeColor(context.getResources().getColor(R.color.colorPrimary));

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setCursor(ArrayList<Event> e)
    {
        events.clear();
        events.addAll(e);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.eventName)
        TextView eventName;

        @BindView(R.id.eventInfo)
        TextView eventInfo;

        @BindView(R.id.eventColor)
        ImageView eventColor;

        @BindView(R.id.eventCard)
        MaterialCardView eventCard;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;

                    Event e = events.get(getAdapterPosition());
                    Log.e("CLicked Event", "ID: " + e.getId());

                    switch (e.getType())
                    {
                        case Utils.EVENT_TYPE_ASSIGNMENT:
                            intent = new Intent(context, AssignmentOverviewActivity.class);
                            intent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT, new Assignment(e.getId()));
                            context.startActivity(intent);
                            break;

                        case Utils.EVENT_TYPE_DATE:
                            intent = new Intent(context, EditClassDateActivity.class);
                            intent.putExtra(BundleUtils.BUNDLE_CLASS_DATE, new CourseHandler(context).getClassDateById(e.getId()));
                            context.startActivity(intent);
                            break;
                    }
                }
            });
        }
    }
}
