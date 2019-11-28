package com.b96software.schoolplannerapp.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.receivers.NotificationReceiver;
import com.b96software.schoolplannerapp.widgets.EventWidget;
import com.b96software.schoolplannerapp.widgets.NoteWidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class ServiceUtils {


    public static final String SERVICE_ADD_CLASS_DATES = "addDates";

    public static final String WIDGET_UPDATE_NOTES ="com.b96software.schoolplannerapp.action.UPDATE_NOTES";
    public static final String WIDGET_UPDATE_EVENTS = "com.b96software.schoolplannerapp.action.UPDATE_EVENTS";

    public static final String WIDGET_ACTION_ADD_EVENT_DAY = "com.b96software.schoolplannerapp.action.ADD_EVENT_DAY";
    public static final String WIDGET_ACTION_SUB_EVENT_DAY = "com.b96software.schoolplannerapp.action.SUB_EVENT_DAY";
    public static final String WIDGET_ACTION_SELECT_EVENT = "com.b96software.schoolplannerapp.action.SELECT_EVENT";

    public static final String WIDGET_ACTION_ADD_NOTE_DAY = "com.b96software.schoolplannerapp.action.ADD_NOTE_DAY";
    public static final String WIDGET_ACTION_SUB_NOTE_DAY = "com.b96software.schoolplannerapp.action.SUB_NOTE_DAY";

    //Constants
    public static final int DAILY_NOTIFICATION_ID = 0;

    //Notification Channel ID
    private static final String REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    //Notification actions
    public static final String DAILY_NOTIF_ACTION = "com.b96software.schoolplannerapp.action.DAILY_NOTIF";


    public static void sendNoteUpdateBroadcast(Context context)
    {
        Intent intent = new Intent(WIDGET_UPDATE_NOTES);
        intent.setComponent(new ComponentName(context, NoteWidget.class));
        context.sendBroadcast(intent);
    }


    public static void sendEventUpdateBroadcast(Context context)
    {
        Intent intent = new Intent(WIDGET_UPDATE_EVENTS);
        intent.setComponent(new ComponentName(context, EventWidget.class));
        context.sendBroadcast(intent);
    }

    //Create Broadcast for notification
    public static void registerReceiver(Context context)
    {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(DAILY_NOTIF_ACTION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, DAILY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        boolean alarmSet = (PendingIntent.getBroadcast(context, DAILY_NOTIFICATION_ID, intent,
                PendingIntent.FLAG_NO_CREATE) != null);

        if(alarmSet) {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, 7);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);

            //Increment date if notification time has already passed
            if(System.currentTimeMillis() > c.getTimeInMillis())
                c.add(Calendar.DATE, 1);


            Log.e("Alarm Date and Time", new SimpleDateFormat("MMM dd, yy hh:mm a").format(c.getTimeInMillis()));
            //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            //If the Toggle is turned on, set the repeating alarm with a  interval
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        else {
            alarmManager.cancel(pendingIntent);
            notificationManager.cancelAll();
        }
    }

    /** Send Email */
    public static void sendEmail(Context context)
    {
        String[] to = new String[]{"bboyd.apps@gmail.com"};
        String[] cc = new String[]{};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //Add Data to email intent
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_question));

        try{
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (ActivityNotFoundException ex)
        {
            Toast.makeText(context, context.getString(R.string.email_error), Toast.LENGTH_LONG).show();
        }
    }

    public static void rateApp(Context context)
    {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
    }
}
