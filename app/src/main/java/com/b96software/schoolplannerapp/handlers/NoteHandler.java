package com.b96software.schoolplannerapp.handlers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.b96software.schoolplannerapp.managers.Constants;
import com.b96software.schoolplannerapp.managers.DatabaseHandler;
import com.b96software.schoolplannerapp.model.Note;
import com.b96software.schoolplannerapp.providers.PlannerProvider;

import java.util.ArrayList;

public class NoteHandler {

    private Context context;
    private ContentResolver resolver;


    public NoteHandler(Context context)
    {
        this.context = context;
        this.resolver = context.getContentResolver();
    }


    public int addNote(Note n)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.NOTES_NAME, n.getNoteName());
        values.put(Constants.NOTE_TEXT, n.getNoteText());
        values.put(Constants.NOTE_DATE, n.getNoteDate());
        values.put(Constants.NOTE_TYPE, n.getNoteType());

        Uri uri = PlannerProvider.NOTES_URI;
        return (int) ContentUris.parseId(resolver.insert(uri, values));
    }

    public Loader<Cursor> getNoteFromDB(int noteID)
    {
        String[] projection = new String[]{Constants.NOTES_ID, Constants.NOTES_NAME, Constants.NOTE_TEXT,
                Constants.NOTE_DATE, Constants.NOTE_TYPE};

        String selection = Constants.NOTES_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(noteID)};

        Uri uri = PlannerProvider.NOTES_URI;

        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }

    public Cursor getNotesBetweenDates(String startDate, String endDate)
    {
        String[] projection = new String[]{Constants.NOTES_ID, Constants.NOTES_NAME, Constants.NOTE_TEXT,
                Constants.NOTE_DATE, Constants.NOTE_TYPE};

        String selection = "date(" + Constants.NOTE_DATE + ") BETWEEN ? AND ?";
        String[] selectionArgs = new String[]{startDate, endDate};
        String sortOrder = Constants.NOTES_ID + " DESC";

        Uri uri = PlannerProvider.NOTES_URI;
        return resolver.query(uri, projection, selection, selectionArgs ,sortOrder);
    }


    public Loader<Cursor> getAllNotesFromDB()
    {
        String[] projection = new String[]{Constants.NOTES_ID, Constants.NOTES_NAME, Constants.NOTE_TEXT,
                Constants.NOTE_DATE, Constants.NOTE_TYPE};

        String sortOrder = Constants.NOTES_ID + " DESC";

        Uri uri = PlannerProvider.NOTES_URI;
        return new CursorLoader(context, uri, projection, null, null, sortOrder);
    }

    public int updateNote(Note n)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.NOTES_NAME, n.getNoteName());
        values.put(Constants.NOTE_TEXT, n.getNoteText());
        values.put(Constants.NOTE_DATE, n.getNoteDate());
        values.put(Constants.NOTE_TYPE, n.getNoteType());


        String selection = Constants.NOTES_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(n.getNoteID())};

        Uri uri = PlannerProvider.NOTES_URI;
        return resolver.update(uri, values, selection, selectionArgs);
    }

        //Delete Note
        public int deleteNote(int noteID)
        {
            String selection = Constants.NOTES_ID + " =?";
            String[] selectionArgs = new String[]{String.valueOf(noteID)};

        Uri uri = ContentUris.withAppendedId(PlannerProvider.NOTES_URI, noteID);
        return resolver.delete(uri, selection, selectionArgs);
    }


    //Note Operations
    public Loader<Cursor> getNotesByDateFromDB(String date)
    {
        String[] projection = new String[]{Constants.NOTES_ID, Constants.NOTES_NAME, Constants.NOTE_TEXT,
                Constants.NOTE_DATE, Constants.NOTE_TYPE};

        String selection = "date(" + Constants.NOTE_DATE + ") =?";
        String[] selectionArgs = new String[]{date};
        String sortOrder = Constants.NOTES_ID + " DESC";

        Uri uri = PlannerProvider.NOTES_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs ,sortOrder);
    }

    public Loader<Cursor> getNotesBetweenDatesFromDB(String startDate, String endDate)
    {
        String[] projection = new String[]{Constants.NOTES_ID, Constants.NOTES_NAME, Constants.NOTE_TEXT,
                Constants.NOTE_DATE, Constants.NOTE_TYPE};

        String selection = "date(" + Constants.NOTE_DATE + ") BETWEEN ? AND ?";
        String[] selectionArgs = new String[]{startDate, endDate};
        String sortOrder = Constants.NOTES_ID + " DESC";

        Uri uri = PlannerProvider.NOTES_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs ,sortOrder);
    }


    public ArrayList<Note> getNotesByDate(String date)
    {
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();


        String[] projection = new String[]{Constants.NOTES_ID, Constants.NOTES_NAME, Constants.NOTE_TEXT,
                Constants.NOTE_DATE, Constants.NOTE_TYPE};

        String selection = "date(" + Constants.NOTE_DATE + ") =?";
        String[] selectionArgs = new String[]{date};
        String sortOrder = Constants.NOTE_DATE + " DESC";

        Cursor cursor = db.query(Constants.NOTES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Note> notes = getNotesFromCursor(cursor);

        cursor.close();
        db.close();

        return notes;
    }


    public ArrayList<Note> getNotesFromCursor(Cursor cursor)
    {
        ArrayList<Note> notes = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {

                Note n = new Note();
                n.setNoteID(cursor.getInt(cursor.getColumnIndex(Constants.NOTES_ID)));
                n.setNoteName(cursor.getString(cursor.getColumnIndex(Constants.NOTES_NAME)));
                n.setNoteText(cursor.getString(cursor.getColumnIndex(Constants.NOTE_TEXT)));
                n.setNoteDate(cursor.getString(cursor.getColumnIndex(Constants.NOTE_DATE)));
                n.setNoteType(cursor.getInt(cursor.getColumnIndex(Constants.NOTE_TYPE)));
                notes.add(n);

            } while (cursor.moveToNext());
        }

        return notes;
    }

    public Note getNoteFromCursor(Cursor cursor)
    {
        if(cursor.moveToFirst())
        {
            Note n = new Note();
            n.setNoteID(cursor.getInt(cursor.getColumnIndex(Constants.NOTES_ID)));
            n.setNoteName(cursor.getString(cursor.getColumnIndex(Constants.NOTES_NAME)));
            n.setNoteText(cursor.getString(cursor.getColumnIndex(Constants.NOTE_TEXT)));
            n.setNoteDate(cursor.getString(cursor.getColumnIndex(Constants.NOTE_DATE)));
            n.setNoteType(cursor.getInt(cursor.getColumnIndex(Constants.NOTE_TYPE)));
            return n;
        }

        return null;
    }
}
