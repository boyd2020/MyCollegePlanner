package com.b96software.schoolplannerapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.assignments.AssignmentOverviewActivity;
import com.b96software.schoolplannerapp.classdate.EditClassDateActivity;
import com.b96software.schoolplannerapp.managers.PreferenceManager;
import com.b96software.schoolplannerapp.services.EventWidgetRemoteViewsService;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Implementation of App Widget functionality.
 */
public class EventWidget extends AppWidgetProvider {

    static RemoteViews updateWidget(Context context)
    {

        String date = new PreferenceManager(context).getEventDate();

        try {
            long time = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(date).getTime();
            date = new SimpleDateFormat(Utils.DISPLAY_DATE).format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Intent for ListView service
        Intent intent = new Intent(context, EventWidgetRemoteViewsService.class);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_event);
        remoteViews.setRemoteAdapter(R.id.listView, intent);
        remoteViews.setTextViewText(R.id.date, date);


        //Creates Assignment Activity Intent
        Intent clickIntent = new Intent(ServiceUtils.WIDGET_ACTION_SELECT_EVENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Back Button Intent
        Intent backIntent = new Intent(ServiceUtils.WIDGET_ACTION_SUB_EVENT_DAY);
        PendingIntent backPendingIntent = PendingIntent.getBroadcast(context,
                0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Next Button Intent
        Intent nextIntent = new Intent(ServiceUtils.WIDGET_ACTION_ADD_EVENT_DAY);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context,
                0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        //Update Widget
        remoteViews.setPendingIntentTemplate(R.id.listView, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.back, backPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.next, nextPendingIntent);

        return  remoteViews;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = updateWidget(context);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action)
        {
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                update(context);
                break;

            case ServiceUtils.WIDGET_UPDATE_EVENTS:
                update(context);
                break;

            case ServiceUtils.WIDGET_ACTION_ADD_EVENT_DAY:
                addDay(context);
                update(context);
                break;

            case ServiceUtils.WIDGET_ACTION_SUB_EVENT_DAY:
                subDay(context);
                update(context);
                break;

            case ServiceUtils.WIDGET_ACTION_SELECT_EVENT:
                Intent i;

                if(intent.hasExtra(BundleUtils.BUNDLE_ASSIGNMENT)) {
                    i = new Intent(context, AssignmentOverviewActivity.class);
                    i.putExtra(BundleUtils.BUNDLE_ASSIGNMENT,intent.getParcelableExtra(BundleUtils.BUNDLE_ASSIGNMENT));
                }
                else if (intent.hasExtra(BundleUtils.BUNDLE_CLASS_DATE)) {
                    i = new Intent(context, EditClassDateActivity.class);
                    i.putExtra(BundleUtils.BUNDLE_CLASS_DATE,intent.getParcelableExtra(BundleUtils.BUNDLE_CLASS_DATE));
                }
                else {
                    i = new Intent(context, AssignmentOverviewActivity.class);
                    i.putExtra(BundleUtils.BUNDLE_ASSIGNMENT,intent.getParcelableExtra(BundleUtils.BUNDLE_ASSIGNMENT));
                }

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void update(Context context)
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName appWidgetId = new ComponentName(context, EventWidget.class);

        RemoteViews views = updateWidget(context);
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(appWidgetId), R.id.listView);
        manager.updateAppWidget(appWidgetId, views);
    }

    private void subDay(Context context)
    {
        Calendar cal = new GregorianCalendar();
        PreferenceManager manager = new PreferenceManager(context);
        SimpleDateFormat format = new SimpleDateFormat(Utils.SQL_DATE_FORMAT);

        try {
            long date = format.parse(manager.getEventDate()).getTime();
            cal.setTimeInMillis(date);
            cal.add(Calendar.DATE, -1);

            manager.setEventDate(format.format(cal.getTimeInMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addDay(Context context)
    {
        Calendar cal = new GregorianCalendar();
        PreferenceManager manager = new PreferenceManager(context);
        SimpleDateFormat format = new SimpleDateFormat(Utils.SQL_DATE_FORMAT);

        try {
            long date = format.parse(manager.getEventDate()).getTime();
            cal.setTimeInMillis(date);
            cal.add(Calendar.DATE, 1);

            manager.setEventDate(format.format(cal.getTimeInMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

