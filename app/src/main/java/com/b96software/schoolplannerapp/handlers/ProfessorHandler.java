package com.b96software.schoolplannerapp.handlers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.b96software.schoolplannerapp.managers.Constants;
import com.b96software.schoolplannerapp.managers.DatabaseHandler;
import com.b96software.schoolplannerapp.model.Professor;
import com.b96software.schoolplannerapp.providers.PlannerProvider;

import java.util.ArrayList;

public class ProfessorHandler {

    private Context context;
    private ContentResolver resolver;

    public ProfessorHandler(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
    }

    public Uri addProfessor(Professor p)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.PROF_NAME, p.getName());
        values.put(Constants.PROF_EMAIL, p.getEmail());
        values.put(Constants.PROF_PHONE, p.getPhone());
        values.put(Constants.PROF_OFFICE, p.getOffice());
        values.put(Constants.PROF_IMAGE, p.getImage());

        Log.e("Phone", p.getPhone());
        Log.e("Email", p.getEmail());

        Uri uri = PlannerProvider.PROF_URI;
        return resolver.insert(uri, values);
    }

    //Get professor from database
    public Loader<Cursor> getProfessorFromDB(int profID)
    {
        String[] projection = new String[] {Constants.PROF_ID, Constants.PROF_NAME, Constants.PROF_PHONE, Constants.PROF_EMAIL,
                Constants.PROF_OFFICE, Constants.PROF_IMAGE};
        String selection = Constants.PROF_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(profID)};

        Uri uri = PlannerProvider.PROF_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }


    //Get Professors from database
    public Loader<Cursor> getAllProfessorsFromDB()
    {
        String[] projection = new String[]{Constants.PROF_ID, Constants.PROF_NAME, Constants.PROF_EMAIL};

        Uri uri = PlannerProvider.PROF_URI;
        return new CursorLoader(context, uri, projection, null, null, null);
    }

    //Update Professor
    public int updateProfessor(Professor p)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.PROF_NAME, p.getName());
        values.put(Constants.PROF_PHONE, p.getPhone());
        values.put(Constants.PROF_EMAIL, p.getEmail());
        values.put(Constants.PROF_OFFICE, p.getOffice());
        values.put(Constants.PROF_IMAGE, p.getImage());

        String selection = Constants.PROF_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(p.getId())};
        Uri uri = PlannerProvider.PROF_URI;

        return resolver.update(uri, values, selection, selectionArgs);
    }

    public ArrayList<Professor> getProfessorsFromCursor(Cursor cursor)
    {
        ArrayList<Professor> professors = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do{
                Professor p = new Professor();
                p.setName(cursor.getString(cursor.getColumnIndex(Constants.PROF_NAME)));
                p.setId(cursor.getInt(cursor.getColumnIndex(Constants.PROF_ID)));
                p.setEmail(cursor.getString(cursor.getColumnIndex(Constants.PROF_EMAIL)));

                professors.add(p);
            } while(cursor.moveToNext());
        }
        return professors;
    }

    public Professor getProfessorFromCursor(Cursor cursor)
    {
        if(cursor.moveToFirst())
        {
            Professor p = new Professor();
            p.setName(cursor.getString(cursor.getColumnIndex(Constants.PROF_NAME)));
            p.setId(cursor.getInt(cursor.getColumnIndex(Constants.PROF_ID)));
            p.setImage(cursor.getBlob(cursor.getColumnIndex(Constants.PROF_IMAGE)));
            p.setEmail(cursor.getString(cursor.getColumnIndex(Constants.PROF_EMAIL)));
            p.setPhone(cursor.getString(cursor.getColumnIndex(Constants.PROF_PHONE)));
            p.setOffice(cursor.getString(cursor.getColumnIndex(Constants.PROF_OFFICE)));
            return p;
        }


        return null;
    }

    public byte[] getProfessorImage(int profID)
    {
        byte[] image = null;

        String[] projection = new String[]{Constants.PROF_IMAGE};
        String selection = Constants.PROF_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(profID)};

        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        Cursor cursor = db.query(Constants.PROFESSOR_TABLE, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst())
            image =  cursor.getBlob(cursor.getColumnIndex(Constants.PROF_IMAGE));

        db.close();
        cursor.close();

        return image;
    }

}
