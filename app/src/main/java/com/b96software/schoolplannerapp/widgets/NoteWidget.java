package com.b96software.schoolplannerapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.managers.PreferenceManager;
import com.b96software.schoolplannerapp.notes.NoteOverviewActivity;
import com.b96software.schoolplannerapp.notes.NoteTabActivity;
import com.b96software.schoolplannerapp.services.NoteWidgetRemoteViewsService;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Implementation of App Widget functionality.
 */
public class NoteWidget extends AppWidgetProvider {

    static RemoteViews updateWidget(Context context)
    {

        String date = new PreferenceManager(context).getNoteDate();

        try {
            long time = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).parse(date).getTime();
            date = new SimpleDateFormat(Utils.DISPLAY_DATE).format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Intent for ListView service
        Intent intent = new Intent(context, NoteWidgetRemoteViewsService.class);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_note);
        remoteViews.setRemoteAdapter(R.id.listView, intent);
        remoteViews.setTextViewText(R.id.date, date);

        //Click Main Activity Pending Intent
        Intent mainIntent = new Intent(context, NoteTabActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context,
                1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Creates Assignment Activity Intent
        Intent clickIntent = new Intent(context, NoteOverviewActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        //Back Button Intent
        Intent backIntent = new Intent(ServiceUtils.WIDGET_ACTION_SUB_NOTE_DAY);
        PendingIntent backPendingIntent = PendingIntent.getBroadcast(context,
                0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Next Button Intent
        Intent nextIntent = new Intent(ServiceUtils.WIDGET_ACTION_ADD_NOTE_DAY);
        backIntent.setAction(ServiceUtils.WIDGET_ACTION_ADD_NOTE_DAY);
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

            case ServiceUtils.WIDGET_UPDATE_NOTES:
                update(context);
                break;

            case ServiceUtils.WIDGET_ACTION_ADD_NOTE_DAY:
                addDay(context);
                update(context);
                break;

            case ServiceUtils.WIDGET_ACTION_SUB_NOTE_DAY:
                subDay(context);
                update(context);
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
        ComponentName appWidgetId = new ComponentName(context, NoteWidget.class);

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
            long date = format.parse(manager.getNoteDate()).getTime();
            cal.setTimeInMillis(date);
            cal.add(Calendar.DATE, -1);

            manager.setNoteDate(format.format(cal.getTimeInMillis()));
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
            long date = format.parse(manager.getNoteDate()).getTime();
            cal.setTimeInMillis(date);
            cal.add(Calendar.DATE, 1);

            manager.setNoteDate(format.format(cal.getTimeInMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

