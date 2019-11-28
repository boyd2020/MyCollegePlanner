package com.b96software.schoolplannerapp.widgetadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.handlers.AssignmentHandler;
import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.handlers.EventHandler;
import com.b96software.schoolplannerapp.managers.PreferenceManager;
import com.b96software.schoolplannerapp.model.Event;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;

public class EventRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private EventHandler handler;
    private ArrayList<Event> events;
    private PreferenceManager preferenceManager;

    public EventRemoteViewsFactory(Context context) {
        this.context = context;
        handler = new EventHandler(context);
        events = new ArrayList<>();
        preferenceManager = new PreferenceManager(context);
    }



    @Override
    public void onCreate() {
        getEvents();
    }

    @Override
    public void onDataSetChanged() {
        long identityToken = Binder.clearCallingIdentity();
        getEvents();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if(events.isEmpty())
            return null;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_event);
        Event e = events.get(position);

        views.setTextViewText(R.id.eventName, e.getTitle());
        views.setTextViewText(R.id.eventInfo, e.getDesc());
        views.setTextColor(R.id.eventInfo, e.getColor());
        views.setInt(R.id.eventColor, "setBackgroundColor", e.getColor());

        Intent fillInIntent = new Intent();



        switch (e.getType())
        {
            case Utils.EVENT_TYPE_ASSIGNMENT:
                fillInIntent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT, new AssignmentHandler(context).getAssignmentById(e.getId()));
                break;

            case Utils.EVENT_TYPE_DATE:
                fillInIntent.putExtra(BundleUtils.BUNDLE_CLASS_DATE, new CourseHandler(context).getClassDateById(e.getId()));
                break;

            default:
                fillInIntent.putExtra(BundleUtils.BUNDLE_ASSIGNMENT, new AssignmentHandler(context).getAssignmentById(e.getId()));
                break;
        }


        views.setOnClickFillInIntent(R.id.eventCardContainer, fillInIntent);
        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return  events.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getEvents()
    {
        String date = preferenceManager.getEventDate();

        Log.e("Event Date", date);
        events.clear();
        events.addAll(handler.getEvents(date));
    }
}
