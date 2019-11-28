package com.b96software.schoolplannerapp.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class PreferenceManager {

    //TODO:Change ads back to 4
    public static final int SHOW_AD = 4;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    int CONTEXT_MODE = Context.MODE_PRIVATE;

    //Shared Preference file name
    private static final String PREF_NAME = "planner_app_prefs";
    private static final String FIRST_TIME_LAUNCH = "first_time_launched";
    private static final String AD_COUNT = "ad_count";
    private static final String PREF_NOTE_DATE = "noteDate";
    private static final String PREF_EVENT_DATE = "eventDate";

    public PreferenceManager(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, CONTEXT_MODE);
        editor = preferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime)
    {
        editor.putBoolean(FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch()
    {
        return preferences.getBoolean(FIRST_TIME_LAUNCH, true);
    }

    public void setAdCount(int adCount)
    {
        editor.putInt(AD_COUNT, adCount);
        editor.commit();
    }

    public int getAdCount()
    {
        return preferences.getInt(AD_COUNT, 1);
    }

    public void addCount()
    {
        int adCount = getAdCount() + 1;
        setAdCount(adCount);
    }

    public String getNoteDate()
    {
        return preferences.getString(PREF_NOTE_DATE, new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(new GregorianCalendar().getTimeInMillis()));
    }

    public void setNoteDate(String date)
    {
        editor.putString(PREF_NOTE_DATE, date);
        editor.commit();
    }

    public String getEventDate()
    {
        return preferences.getString(PREF_EVENT_DATE, new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(new GregorianCalendar().getTimeInMillis()));
    }

    public void setEventDate(String date)
    {
        editor.putString(PREF_EVENT_DATE, date);
        editor.commit();
    }
}
