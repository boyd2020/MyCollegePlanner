package com.b96software.schoolplannerapp.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.SplashActivity;
import com.b96software.schoolplannerapp.handlers.EventHandler;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;

public class NotificationReceiver extends BroadcastReceiver {

    //Constants
    public static final int DAILY_NOTIFICATION_ID = 0;

    //Notification Channel ID
    private static final String REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    //Notification actions
    public static final String DAILY_NOTIF_ACTION = "com.b96software.schoolplannerapp.action.DAILY_NOTIF";

    public NotificationReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction())
        {
            case DAILY_NOTIF_ACTION:
                NotificationManager manager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

                //Create Notification Channel for Android O
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    NotificationChannel channel = new NotificationChannel(REMINDER_NOTIFICATION_CHANNEL_ID,
                            "Name", NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                }

                Notification notification = dailyNotification(context);
                manager.notify(DAILY_NOTIFICATION_ID, notification);
                break;

            case Intent.ACTION_TIME_CHANGED:
                ServiceUtils.registerReceiver(context);
                break;

        }
    }



    /** Notifications */
    public static Notification dailyNotification(Context context)
    {
        //Create the intent which opens the MainActivity
        Intent contentIntent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                ServiceUtils.DAILY_NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Get the number of events from the db
        EventHandler handler = new EventHandler(context);
        String date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(System.currentTimeMillis());
        int events = handler.getEventCount(date);


        //Create Notification
        String eventString = "";

        if(events == 0)
            eventString = context.getString(R.string.event_empty);
        else
            eventString = events + " " + context.getString(R.string.notification_events_today);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(icon)
                .setContentTitle(eventString)
                .setContentText(context.getString(R.string.notification_more_details))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        return builder.build();
    }
}
