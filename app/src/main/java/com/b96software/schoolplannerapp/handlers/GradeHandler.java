package com.b96software.schoolplannerapp.handlers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.b96software.schoolplannerapp.managers.Constants;
import com.b96software.schoolplannerapp.managers.DatabaseHandler;
import com.b96software.schoolplannerapp.model.Grade;
import com.b96software.schoolplannerapp.providers.PlannerProvider;

import java.util.ArrayList;

public class GradeHandler {

    private Context context;
    private ContentResolver resolver;


    public GradeHandler(Context context)
    {
        this.context = context;
        this.resolver = context.getContentResolver();
    }

    /**Grade CRUD Operations */
    public Uri addGrade(Grade g)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.ASSIGNMENT_ID, g.getAssignID());
        values.put(Constants.GRADE_DATE, g.getDate());
        values.put(Constants.GRADE_WORTH, g.getWorth());
        values.put(Constants.GRADE_EARNED, g.getEarned());
        Uri uri = PlannerProvider.GRADES_URI;
        return resolver.insert(uri, values);
    }

    //Update Uri
    public int updateGrade(Grade g)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.ASSIGNMENT_ID, g.getAssignID());
        values.put(Constants.GRADE_DATE, g.getDate());
        values.put(Constants.GRADE_WORTH, g.getWorth());
        values.put(Constants.GRADE_EARNED, g.getEarned());

        String selection = Constants.GRADE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(g.getId())};

        Uri uri = PlannerProvider.GRADES_URI;
        return resolver.update(uri, values, selection, selectionArgs);
    }

    public int updateAssignmentGrade(Grade g)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.GRADE_DATE, g.getDate());
        values.put(Constants.GRADE_WORTH, g.getWorth());
        values.put(Constants.GRADE_EARNED, g.getEarned());

        String selection = Constants.ASSIGNMENT_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(g.getAssignID())};

        Uri uri = PlannerProvider.GRADES_URI;
        return resolver.update(uri, values, selection, selectionArgs);
    }

    public int deleteGrade(int gradeID)
    {
        Uri uri = PlannerProvider.GRADES_URI;

        String selection = Constants.GRADE_ID + " =?";
        String[] selectionArgs = new String[] {String.valueOf(gradeID)};

        return resolver.delete(uri, selection, selectionArgs);
    }

    //Get Grade From DB
    public Loader<Cursor> getGradeFromDB(int gradeID)
    {
        String[] projection = new String[]{ Constants.GRADE_ID, Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.GRADE_DATE,
                Constants.GRADE_EARNED, Constants.GRADE_WORTH, Constants.COURSE_NAME, Constants.ASSIGNMENT_NAME, Constants.COURSE_COLOR};

        String selection = Constants.GRADE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(gradeID)};

        Uri uri = PlannerProvider.GRADES_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }

    //Get All Grades from db
    public Loader<Cursor> getAllGradesFromDB()
    {
        String[] projection = new String[]{ Constants.GRADE_ID, Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.GRADE_DATE,
                Constants.GRADE_EARNED, Constants.GRADE_WORTH, Constants.COURSE_NAME, Constants.ASSIGNMENT_NAME, Constants.COURSE_COLOR};

        String sortOrder = Constants.GRADE_DATE + " DESC, " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID;

        Uri uri = PlannerProvider.GRADES_URI;
        return new CursorLoader(context, uri, projection, null, null, sortOrder);
    }



    public ArrayList<Grade> getCourseGrades(int courseID)
    {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        String[] projection = new String[]{ Constants.GRADE_ID, Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.GRADE_DATE,
                Constants.GRADE_EARNED, Constants.GRADE_WORTH, Constants.COURSE_NAME, Constants.ASSIGNMENT_NAME, Constants.COURSE_COLOR};


        String selection = Constants.COURSE_TABLE + "." + Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(courseID)};

        String sortOrder = Constants.GRADE_DATE + " DESC";

        builder.setTables(Constants.GRADES_TABLE + " JOIN " + Constants.ASSIGNMENT_TABLE + " ON "
                + Constants.GRADES_TABLE + "." + Constants.ASSIGNMENT_ID + " = " + Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID
                + " JOIN " + Constants.COURSE_TABLE + " ON " + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID + " = "
                + Constants.COURSE_TABLE + "." + Constants.COURSE_ID);

        //Query Tables
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Grade> grades = getGradesFromCursor(cursor);

        cursor.close();
        db.close();

        return grades;
    }


    public ArrayList<Grade> getGradesFromCursor(Cursor cursor)
    {
        ArrayList<Grade> grades = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do{
                Grade g = new Grade();
                g.setId(cursor.getInt(cursor.getColumnIndex(Constants.GRADE_ID)));
                g.setAssignID(cursor.getInt(cursor.getColumnIndex(Constants.ASSIGNMENT_ID)));
                g.setWorth(cursor.getDouble(cursor.getColumnIndex(Constants.GRADE_WORTH)));
                g.setEarned(cursor.getDouble(cursor.getColumnIndex(Constants.GRADE_EARNED)));
                g.setDate(cursor.getString(cursor.getColumnIndex(Constants.GRADE_DATE)));
                g.setAssignmentName(cursor.getString(cursor.getColumnIndex(Constants.ASSIGNMENT_NAME)));
                g.setCourseName(cursor.getString(cursor.getColumnIndex(Constants.COURSE_NAME)));
                g.setCourseColor(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_COLOR)));
                grades.add(g);
            } while (cursor.moveToNext());
        }

        return grades;
    }

    public Grade getGradeFromCursor(Cursor cursor)
    {
        if(cursor.moveToFirst())
        {
            Grade g = new Grade();
            g.setId(cursor.getInt(cursor.getColumnIndex(Constants.GRADE_ID)));
            g.setAssignID(cursor.getInt(cursor.getColumnIndex(Constants.ASSIGNMENT_ID)));
            g.setWorth(cursor.getDouble(cursor.getColumnIndex(Constants.GRADE_WORTH)));
            g.setEarned(cursor.getDouble(cursor.getColumnIndex(Constants.GRADE_EARNED)));
            g.setDate(cursor.getString(cursor.getColumnIndex(Constants.GRADE_DATE)));
            g.setAssignmentName(cursor.getString(cursor.getColumnIndex(Constants.ASSIGNMENT_NAME)));
            g.setCourseName(cursor.getString(cursor.getColumnIndex(Constants.COURSE_NAME)));
            g.setCourseColor(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_COLOR)));
            return g;
        }

        return null;
    }
}
