package com.b96software.schoolplannerapp.handlers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.b96software.schoolplannerapp.managers.Constants;
import com.b96software.schoolplannerapp.managers.DatabaseHandler;
import com.b96software.schoolplannerapp.model.Event;
import com.b96software.schoolplannerapp.providers.PlannerProvider;
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;

public class EventHandler {

    private Context context;
    private ContentResolver resolver;

    public EventHandler(Context context)
    {
        this.context = context;
        this.resolver = context.getContentResolver();
    }

    public Loader<Cursor> getEventsByDate(String date)
    {
        String[] selectionArgs = new String[] {date, date};
        Uri uri = PlannerProvider.EVENT_URI;

        return new CursorLoader(context, uri, null, null, selectionArgs, null);
    }


    public Loader<Cursor> getEventsByDate(String startDate, String endDate)
    {
        String[] selectionArgs = new String[] {startDate, endDate};
        Uri uri = PlannerProvider.EVENT_URI;

        return new CursorLoader(context, uri, null, null, selectionArgs, null);
    }



    public ArrayList<Event> getEvents(String date)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();
        String[] selectionArgs = new String[] {date, date};

        String query = "SELECT " + Constants.CLASS_DATE_ID + " AS " + Constants.EVENT_ID + ", " + Constants.CLASS_DATE + " AS "
                +Constants.EVENT_DATE + ", " + Constants.COURSE_COLOR + " AS " + Constants.EVENT_COLOR + ", " + Constants.COURSE_NAME + " AS "
                + Constants.EVENT_NAME + ", CASE WHEN StrFTime('%H'," + Constants.CLASS_START + ") % 12 = 0 THEN 12 ELSE StrFTime('%H',"
                + Constants.CLASS_START + ") % 12 " + " END || ':' || StrFTime('%M'," + Constants.CLASS_START + ") || ' ' || CASE WHEN "
                + " StrFTime('%H'," +  Constants.CLASS_START + ") > 12 THEN 'PM' ELSE 'AM' END || ' - ' || CASE WHEN StrFTime('%H'," + Constants.CLASS_END
                + ") % 12 = 0 THEN 12 ELSE StrFTime('%H'," + Constants.CLASS_END + ") % 12 " + " END || ':' || StrFTime('%M'," + Constants.CLASS_END
                + ") || ' ' || CASE WHEN StrFTime('%H'," +  Constants.CLASS_END + ") > 12 THEN 'PM' ELSE 'AM' END "
                + Constants.EVENT_DESC + ", ( '" + Constants.CLASS_DATES_TABLE + "' || ' ' ||" + Constants.CLASS_DATE_ID  + ") AS " + Constants.EVENT_TYPE
                + ", " + Constants.CLASS_DATE_ID + " AS " + Constants.EVENT_STATUS +
                " FROM " + Constants.CLASS_DATES_TABLE + " JOIN " + Constants.COURSE_TABLE + " ON "
                + Constants.CLASS_DATES_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID
                + " WHERE date(" + Constants.CLASS_DATE + ") =? " +

                " UNION SELECT " + Constants.ASSIGNMENT_ID + " AS " + Constants.EVENT_ID + ", " + Constants.ASSIGNMENT_DATE + " AS " + Constants.EVENT_DATE + ","
                + Constants.COURSE_COLOR + " AS " + Constants.EVENT_COLOR + ", " + Constants.ASSIGNMENT_NAME + " AS " + Constants.EVENT_NAME + ", " + Constants.COURSE_NAME + " AS "
                + Constants.EVENT_DESC + ", ( '" + Constants.ASSIGNMENT_TABLE + "' || ' ' ||" + Constants.ASSIGNMENT_ID  + ") AS " + Constants.EVENT_TYPE
                + ", " + Constants.ASSIGNMENT_PROGRESS + " AS " + Constants.EVENT_STATUS +  " FROM " + Constants.ASSIGNMENT_TABLE + " JOIN " + Constants.COURSE_TABLE + " ON "
                + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID
                + " WHERE date(" + Constants.ASSIGNMENT_DATE + ") =? ";

        Cursor cursor = db.rawQuery(query, selectionArgs);


        ArrayList<Event> events = getEventsFromCursor(cursor);

        cursor.close();
        db.close();

        return events;
    }


    public int getEventCount(String date)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();
        String[] selectionArgs = new String[] {date, date};

        String query = "SELECT " + Constants.CLASS_DATE_ID + " AS " + Constants.EVENT_ID
                + " FROM " + Constants.CLASS_DATES_TABLE + " WHERE date(" + Constants.CLASS_DATE + ") =? " +

                " UNION SELECT " + Constants.ASSIGNMENT_ID + " AS " + Constants.EVENT_ID
                + " FROM " + Constants.ASSIGNMENT_TABLE + " WHERE date(" + Constants.ASSIGNMENT_DATE + ") =? ";

        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }


    public ArrayList<Event> getEventsFromCursor(Cursor cursor)
    {
        ArrayList<Event> events = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do{
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex(Constants.EVENT_ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(Constants.EVENT_NAME)));
                event.setDesc(cursor.getString(cursor.getColumnIndex(Constants.EVENT_DESC)));
                event.setDate(cursor.getString(cursor.getColumnIndex(Constants.EVENT_DATE)));
                event.setColor(cursor.getInt(cursor.getColumnIndex(Constants.EVENT_COLOR)));
                event.setStatus(cursor.getInt(cursor.getColumnIndex(Constants.EVENT_STATUS)));

                //Set Event Type
                String type = cursor.getString(cursor.getColumnIndex(Constants.EVENT_TYPE));
                event.setType(type.contains(Constants.ASSIGNMENT_TABLE) ? Utils.EVENT_TYPE_ASSIGNMENT : Utils.EVENT_TYPE_DATE);
                events.add(event);
            } while (cursor.moveToNext());
        }

        return events;
    }
}
