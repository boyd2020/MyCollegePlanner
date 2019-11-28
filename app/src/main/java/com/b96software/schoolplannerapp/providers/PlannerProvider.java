package com.b96software.schoolplannerapp.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.b96software.schoolplannerapp.managers.Constants;
import com.b96software.schoolplannerapp.managers.DatabaseHandler;
import com.b96software.schoolplannerapp.util.ServiceUtils;

public class PlannerProvider extends ContentProvider {

    //Base content Uri
    private static final String AUTHORITY = "com.b96software.schoolplannerapp";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Uri Matcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Paths
    private static final String COURSE_PATH = Constants.COURSE_TABLE;
    private static final String PROF_PATH = Constants.PROFESSOR_TABLE;
    private static final String ASSIGN_PATH = Constants.ASSIGNMENT_TABLE;
    private static final String TERM_PATH = Constants.TERM_TABLE;
    private static final String CLASS_PATH = Constants.CLASS_DATES_TABLE;
    private static final String NOTES_PATH = Constants.NOTES_TABLE;
    private static final String GRADES_PATH = Constants.GRADES_TABLE;
    private static final String CLASS_DATE_PATH = Constants.CLASS_DAYS_TABLE;
    private static final String EVENT_PATH = Constants.EVENT_TABLE;
    private static final String ID_PATH = "/#";

    //Uris
    public static final Uri COURSE_URI = Uri.withAppendedPath(BASE_CONTENT_URI, COURSE_PATH);
    public static final Uri PROF_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PROF_PATH);
    public static final Uri ASSIGN_URI = Uri.withAppendedPath(BASE_CONTENT_URI, ASSIGN_PATH);
    public static final Uri TERM_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TERM_PATH);
    public static final Uri CLASS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CLASS_PATH);
    public static final Uri NOTES_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NOTES_PATH);
    public static final Uri GRADES_URI = Uri.withAppendedPath(BASE_CONTENT_URI, GRADES_PATH);
    public static final Uri CLASS_DATE_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CLASS_DATE_PATH);
    public static final Uri EVENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, EVENT_PATH);

    //Codes
    private static final int COURSE = 100, PROF = 102, ASSIGN = 104, TERM = 106, CLASS = 108,
    NOTE = 110, GRADE = 112, CLASS_DATE = 114, EVENT = 116;

    private DatabaseHandler handler;

    static
    {
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, COURSE);
        uriMatcher.addURI(AUTHORITY, PROF_PATH, PROF);
        uriMatcher.addURI(AUTHORITY, ASSIGN_PATH, ASSIGN);
        uriMatcher.addURI(AUTHORITY, TERM_PATH, TERM);
        uriMatcher.addURI(AUTHORITY, CLASS_PATH, CLASS);
        uriMatcher.addURI(AUTHORITY, NOTES_PATH, NOTE);
        uriMatcher.addURI(AUTHORITY, GRADES_PATH, GRADE);
        uriMatcher.addURI(AUTHORITY, CLASS_DATE_PATH, CLASS_DATE);
        uriMatcher.addURI(AUTHORITY, EVENT_PATH, EVENT);
    }

    //Database Handler
    //private DatabaseHandler handler;

    @Override
    public boolean onCreate() {
        handler = new DatabaseHandler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = handler.getReadableDatabase();
        Cursor cursor;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        int match = uriMatcher.match(uri);

        switch (match)
        {
            case COURSE:
                //Join Term and Professor Tables
                builder.setTables(Constants.COURSE_TABLE + " LEFT JOIN " + Constants.TERM_TABLE + " ON "
                + Constants.COURSE_TABLE + "." + Constants.TERM_ID + " = " + Constants.TERM_TABLE + "." + Constants.TERM_ID
                + " LEFT JOIN " + Constants.PROFESSOR_TABLE + " ON " + Constants.COURSE_TABLE + "." + Constants.PROF_ID + " = "
                + Constants.PROFESSOR_TABLE + "." + Constants.PROF_ID);

                //Query Tables
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PROF:
                cursor = db.query(Constants.PROFESSOR_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case ASSIGN:
                //Join Course and Grade Tables
                builder.setTables(Constants.ASSIGNMENT_TABLE + " LEFT JOIN " + Constants.COURSE_TABLE + " ON "
                + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID
                + " LEFT JOIN " + Constants.GRADES_TABLE + " ON " + Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID + " = "
                + Constants.GRADES_TABLE + "." + Constants.ASSIGNMENT_ID);

                //Query Tables
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case TERM:
                cursor = db.query(Constants.TERM_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case CLASS:
                cursor = db.query(Constants.CLASS_DAYS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTE:
                cursor = db.query(Constants.NOTES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case GRADE:
                builder.setTables(Constants.GRADES_TABLE + " LEFT JOIN " + Constants.ASSIGNMENT_TABLE + " ON "
                + Constants.GRADES_TABLE + "." + Constants.ASSIGNMENT_ID + " = " + Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID
                + " LEFT JOIN " + Constants.COURSE_TABLE + " ON " + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID + " = "
                + Constants.COURSE_TABLE + "." + Constants.COURSE_ID);

                //Query Tables
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case CLASS_DATE:
                builder.setTables(Constants.CLASS_DATES_TABLE + " JOIN " + Constants.COURSE_TABLE + " ON "
                        + Constants.CLASS_DATES_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID);

                cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case EVENT:
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
                + " WHERE date(" + Constants.CLASS_DATE + ") =?" +

                " UNION SELECT " + Constants.ASSIGNMENT_ID + " AS " + Constants.EVENT_ID + ", " + Constants.ASSIGNMENT_DATE + " AS " + Constants.EVENT_DATE + ","
                + Constants.COURSE_COLOR + " AS " + Constants.EVENT_COLOR + ", " + Constants.ASSIGNMENT_NAME + " AS " + Constants.EVENT_NAME + ", " + Constants.COURSE_NAME + " AS "
                + Constants.EVENT_DESC + ", ( '" + Constants.ASSIGNMENT_TABLE + "' || ' ' ||" + Constants.ASSIGNMENT_ID  + ") AS " + Constants.EVENT_TYPE
                + ", " + Constants.ASSIGNMENT_PROGRESS + " AS " + Constants.EVENT_STATUS +  " FROM " + Constants.ASSIGNMENT_TABLE + " JOIN " + Constants.COURSE_TABLE + " ON "
                + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID
                + " WHERE date(" + Constants.ASSIGNMENT_DATE + ") =?";

                cursor = db.rawQuery(query, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        //Set notification URI on the Cursor and update if nay changes have been made
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = handler.getWritableDatabase();
        long id;

        int match = uriMatcher.match(uri);

        switch (match)
        {
            case COURSE:
                id = db.insert(Constants.COURSE_TABLE, null, contentValues);
                break;

            case PROF:
                id = db.insert(Constants.PROFESSOR_TABLE, null, contentValues);
                break;

            case ASSIGN:
                id = db.insert(Constants.ASSIGNMENT_TABLE, null, contentValues);
                break;

            case TERM:
                id = db.insert(Constants.TERM_TABLE, null, contentValues);
                break;

            case CLASS:
                id = db.insert(Constants.CLASS_DAYS_TABLE, null, contentValues);
                break;

            case NOTE:
                id = db.insert(Constants.NOTES_TABLE, null, contentValues);
                ServiceUtils.sendNoteUpdateBroadcast(getContext());
                break;

            case GRADE:
                id = db.insert(Constants.GRADES_TABLE, null, contentValues);
                break;

            case CLASS_DATE:
                id = db.insert(Constants.CLASS_DATES_TABLE, null, contentValues);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        ServiceUtils.sendEventUpdateBroadcast(getContext());

        //Update the event uri
        getContext().getContentResolver().notifyChange(EVENT_URI, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = handler.getWritableDatabase();
        int rowsUpdated = 0;
        int match = uriMatcher.match(uri);

        switch (match)
        {
            case COURSE:
                rowsUpdated = db.update(Constants.COURSE_TABLE, contentValues, selection, selectionArgs);
                break;

            case PROF:
                rowsUpdated = db.update(Constants.PROFESSOR_TABLE, contentValues, selection, selectionArgs);
                break;

            case ASSIGN:
                rowsUpdated = db.update(Constants.ASSIGNMENT_TABLE, contentValues, selection, selectionArgs);
                break;

            case TERM:
                rowsUpdated = db.update(Constants.TERM_TABLE, contentValues, selection, selectionArgs);
                break;

            case CLASS:
                rowsUpdated = db.update(Constants.CLASS_DAYS_TABLE, contentValues, selection, selectionArgs);
                break;

            case NOTE:
                rowsUpdated = db.update(Constants.NOTES_TABLE, contentValues, selection, selectionArgs);
                ServiceUtils.sendNoteUpdateBroadcast(getContext());
                break;

            case GRADE:
                rowsUpdated = db.update(Constants.GRADES_TABLE, contentValues, selection, selectionArgs);
                break;

            case CLASS_DATE:
                rowsUpdated = db.update(Constants.CLASS_DATES_TABLE, contentValues, selection, selectionArgs);
                break;

            case EVENT:
                rowsUpdated = db.update(Constants.EVENT_TABLE, contentValues, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        ServiceUtils.sendEventUpdateBroadcast(getContext());

        //Update the event uri
        getContext().getContentResolver().notifyChange(EVENT_URI, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = handler.getWritableDatabase();
        int rowsDeleted = 0;
        int match = uriMatcher.match(uri);

        switch (match)
        {
            case COURSE:
                rowsDeleted = db.delete(Constants.COURSE_TABLE, selection, selectionArgs);
                break;

            case PROF:
                rowsDeleted = db.delete(Constants.COURSE_TABLE, selection, selectionArgs);
                break;

            case ASSIGN:
                rowsDeleted = db.delete(Constants.ASSIGNMENT_TABLE, selection, selectionArgs);
                break;

            case TERM:
                rowsDeleted = db.delete(Constants.TERM_TABLE, selection, selectionArgs);
                break;

            case CLASS:
                rowsDeleted = db.delete(Constants.CLASS_DAYS_TABLE, selection, selectionArgs);
                break;

            case NOTE:
                rowsDeleted = db.delete(Constants.NOTES_TABLE, selection, selectionArgs);
                ServiceUtils.sendNoteUpdateBroadcast(getContext());
                break;

            case GRADE:
                rowsDeleted = db.delete(Constants.GRADES_TABLE, selection, selectionArgs);
                break;

            case CLASS_DATE:
                rowsDeleted = db.delete(Constants.CLASS_DATES_TABLE, selection, selectionArgs);
                break;

            case EVENT:
                rowsDeleted = db.delete(Constants.EVENT_TABLE, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        ServiceUtils.sendEventUpdateBroadcast(getContext());

        //Update the event uri
        getContext().getContentResolver().notifyChange(EVENT_URI, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
