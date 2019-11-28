package com.b96software.schoolplannerapp.managers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.providers.PlannerProvider;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;
    private ContentResolver resolver;

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
        this.resolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TERM_TABLE);
        db.execSQL(Constants.CREATE_PROFESSOR_TABLE);
        db.execSQL(Constants.CREATE_COURSE_TABLE);
        db.execSQL(Constants.CREATE_ASSIGNMENT_TABLE);
        db.execSQL(Constants.CREATE_CLASS_DATES_TABLE);
        db.execSQL(Constants.CREATE_NOTE_TABLE);
        db.execSQL(Constants.CREATE_NOTE_ITEM_TABLE);
        db.execSQL(Constants.CREATE_GRADES_TABLE);
        db.execSQL(Constants.CREATE_CLASS_DAYS_TABLE);
        db.execSQL(Constants.CREATE_EVENT_TABLE);

        addDefaultTerm(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    private void addDefaultTerm(SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.TERM_NAME, context.getString(R.string.default_term));
        db.insert(Constants.TERM_TABLE, null, values);
    }

    //Event Operations
    public Loader<Cursor> getEventsByDate(String date)
    {
        //Dates for assignments and classes
        String selection = "date(" + Constants.EVENT_DATE + ") BETWEEN ? AND ?";
        String[] selectionArgs = new String[]{date, date};

        Uri uri = PlannerProvider.EVENT_URI;
        return new CursorLoader(context, uri, null, selection, selectionArgs, null);
    }


}
