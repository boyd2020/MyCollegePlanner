package com.b96software.schoolplannerapp.handlers;

import android.content.ContentResolver;
import android.content.ContentUris;
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
import com.b96software.schoolplannerapp.model.Assignment;
import com.b96software.schoolplannerapp.providers.PlannerProvider;

import java.util.ArrayList;

public class AssignmentHandler {

    private Context context;
    private ContentResolver resolver;

    public AssignmentHandler(Context context)
    {
        this.context = context;
        this.resolver = context.getContentResolver();
    }

    public int addAssignment(Assignment a) {
        ContentValues values = new ContentValues();
        values.put(Constants.COURSE_ID, a.getCourseID());
        values.put(Constants.ASSIGNMENT_NAME, a.getName());
        values.put(Constants.ASSIGNMENT_DATE, a.getDate());
        values.put(Constants.ASSIGNMENT_PROGRESS, a.getAssignProgress());
        values.put(Constants.ASSIGNMENT_TYPE, a.getAssignType());

        Uri uri = PlannerProvider.ASSIGN_URI;
        return (int) ContentUris.parseId(resolver.insert(uri, values));
    }

    public int updateAssignment(Assignment a) {
        ContentValues values = new ContentValues();
        values.put(Constants.COURSE_ID, a.getCourseID());
        values.put(Constants.ASSIGNMENT_NAME, a.getName());
        values.put(Constants.ASSIGNMENT_DATE, a.getDate());
        values.put(Constants.ASSIGNMENT_PROGRESS, a.getAssignProgress());
        values.put(Constants.ASSIGNMENT_TYPE, a.getAssignType());

        String selection = Constants.ASSIGNMENT_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(a.getId())};

        Uri uri = PlannerProvider.ASSIGN_URI;
        return resolver.update(uri, values, selection, selectionArgs);
    }

    public void deleteAssignment(int assignID) {

        //Delete Assignment
        String selection = Constants.ASSIGNMENT_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(assignID)};

        Uri uri = PlannerProvider.ASSIGN_URI;
        resolver.delete(uri, selection, selectionArgs);

        //Delete Grade (if applicable)
        uri = PlannerProvider.GRADES_URI;
        resolver.delete(uri, selection, selectionArgs);
    }

    public Loader<Cursor> getAssignment(int assignID) {

        String[] projection = new String[]{Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.ASSIGNMENT_NAME,
                Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID, Constants.COURSE_NAME, Constants.ASSIGNMENT_DATE, Constants.ASSIGNMENT_TYPE, Constants.ASSIGNMENT_PROGRESS,
                Constants.GRADE_ID, Constants.GRADE_EARNED, Constants.GRADE_WORTH, Constants.COURSE_COLOR};

        String selection = Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(assignID)};

        String sortOrder = Constants.ASSIGNMENT_DATE + " DESC, " + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID;

        Uri uri = PlannerProvider.ASSIGN_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
    }



    public Loader<Cursor> getAssignmentsBetweenDates(String fromDate, String toDate) {

        String[] projection = new String[]{Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.ASSIGNMENT_NAME, Constants.COURSE_TABLE + "." + Constants.COURSE_ID,
                Constants.COURSE_NAME, Constants.ASSIGNMENT_DATE, Constants.ASSIGNMENT_TYPE, Constants.ASSIGNMENT_PROGRESS,
                Constants.GRADE_ID, Constants.GRADE_EARNED, Constants.COURSE_COLOR};

        String selection = Constants.ASSIGNMENT_DATE + " BETWEEN ? AND ?";
        String[] selectionArgs = new String[]{fromDate, toDate};
        String sortOrder = Constants.ASSIGNMENT_DATE + " ASC";

        Uri uri = PlannerProvider.ASSIGN_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public Loader<Cursor> getAssignmentsByCourseAndType(int courseID, int assignType) {

        String[] projection = new String[]{Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.ASSIGNMENT_NAME, Constants.COURSE_TABLE + "." + Constants.COURSE_ID,
                Constants.COURSE_NAME, Constants.ASSIGNMENT_DATE, Constants.ASSIGNMENT_TYPE, Constants.ASSIGNMENT_PROGRESS,
                Constants.GRADE_ID, Constants.GRADE_EARNED, Constants.COURSE_COLOR};

        String selection = Constants.ASSIGNMENT_TYPE + " =? AND " + Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(assignType), String.valueOf(courseID)};
        String sortOrder = Constants.COURSE_ID + " ," + Constants.ASSIGNMENT_DATE + " DESC";

        Uri uri = PlannerProvider.ASSIGN_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public Loader<Cursor> getAssignmentsByType(int assignType) {
        String[] projection = new String[]{Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.ASSIGNMENT_NAME, Constants.ASSIGNMENT_PROGRESS,
                Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID, Constants.ASSIGNMENT_DATE, Constants.COURSE_NAME, Constants.COURSE_COLOR};

        String selection = Constants.ASSIGNMENT_TYPE + " =?";
        String[] selectionArgs = new String[]{String.valueOf(assignType)};
        String sortOrder = Constants.ASSIGNMENT_DATE + " DESC, " + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID;

        Uri uri = PlannerProvider.ASSIGN_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public Loader<Cursor> getAllAssignments() {

        String[] projection = new String[]{Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.ASSIGNMENT_NAME,
                Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID, Constants.COURSE_NAME, Constants.ASSIGNMENT_DATE,
                Constants.ASSIGNMENT_TYPE, Constants.ASSIGNMENT_PROGRESS, Constants.GRADE_ID, Constants.GRADE_EARNED,
                Constants.GRADE_WORTH, Constants.COURSE_COLOR};

        String sortOrder = Constants.ASSIGNMENT_DATE + " DESC, " + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID;

        Uri uri = PlannerProvider.ASSIGN_URI;
        return new CursorLoader(context, uri, projection, null, null, sortOrder);
    }



    public ArrayList<Assignment> getCourseAssignmentByType(int type, int courseID)
    {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        String[] projection = new String[]{Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.ASSIGNMENT_NAME, Constants.ASSIGNMENT_PROGRESS,
                Constants.COURSE_TABLE + "." + Constants.COURSE_ID, Constants.ASSIGNMENT_DATE, Constants.COURSE_NAME, Constants.COURSE_COLOR};

        String selection = Constants.ASSIGNMENT_TYPE + " =? AND " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID + " =?";

        String[] selectionArgs = new String[]{String.valueOf(type), String.valueOf(courseID)};
        String sortOrder = Constants.ASSIGNMENT_DATE + " DESC, " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID;


        //Join Course and Grade Tables
        builder.setTables(Constants.ASSIGNMENT_TABLE + " LEFT JOIN " + Constants.COURSE_TABLE + " ON "
                + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID
                + " LEFT JOIN " + Constants.GRADES_TABLE + " ON " + Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID + " = "
                + Constants.GRADES_TABLE + "." + Constants.ASSIGNMENT_ID);

        //Query Tables
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Assignment> assignments = getAssignmentsFromCursor(cursor);


        cursor.close();
        db.close();

        return assignments;
    }

    public Assignment getAssignmentById(int assignID)
    {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        String[] projection = new String[]{Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID, Constants.ASSIGNMENT_NAME,
                Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID, Constants.COURSE_NAME, Constants.ASSIGNMENT_DATE, Constants.ASSIGNMENT_TYPE, Constants.ASSIGNMENT_PROGRESS,
                Constants.GRADE_ID, Constants.GRADE_EARNED, Constants.GRADE_WORTH, Constants.COURSE_COLOR};

        String selection = Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(assignID)};

        String sortOrder = Constants.ASSIGNMENT_DATE + " DESC, " + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID;

        //Join Course and Grade Tables
        builder.setTables(Constants.ASSIGNMENT_TABLE + " LEFT JOIN " + Constants.COURSE_TABLE + " ON "
                + Constants.ASSIGNMENT_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID
                + " LEFT JOIN " + Constants.GRADES_TABLE + " ON " + Constants.ASSIGNMENT_TABLE + "." + Constants.ASSIGNMENT_ID + " = "
                + Constants.GRADES_TABLE + "." + Constants.ASSIGNMENT_ID);

        //Query Tables
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Assignment assignment = getAssignmentFromCursor(cursor);
        cursor.close();
        db.close();

        return assignment;
    }



    public ArrayList<Assignment> getAssignmentsFromCursor(Cursor cursor)
    {
        ArrayList<Assignment> assignments = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
               Assignment a = new Assignment();
               a.setId(cursor.getInt(cursor.getColumnIndex(Constants.ASSIGNMENT_ID)));
               a.setName(cursor.getString(cursor.getColumnIndex(Constants.ASSIGNMENT_NAME)));
               a.setDate(cursor.getString(cursor.getColumnIndex(Constants.ASSIGNMENT_DATE)));
               a.setCourseName(cursor.getString(cursor.getColumnIndex(Constants.COURSE_NAME)));
               a.setCourseColor(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_COLOR)));
               a.setAssignProgress(cursor.getInt(cursor.getColumnIndex(Constants.ASSIGNMENT_PROGRESS)));
               assignments.add(a);
            } while (cursor.moveToNext());
        }

        return assignments;
    }


    public Assignment getAssignmentFromCursor(Cursor cursor)
    {
        if(cursor.moveToFirst())
        {
            Assignment a = new Assignment();
            a.setId(cursor.getInt(cursor.getColumnIndex(Constants.ASSIGNMENT_ID)));
            a.setCourseID(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_ID)));
            a.setName(cursor.getString(cursor.getColumnIndex(Constants.ASSIGNMENT_NAME)));
            a.setDate(cursor.getString(cursor.getColumnIndex(Constants.ASSIGNMENT_DATE)));
            a.setCourseName(cursor.getString(cursor.getColumnIndex(Constants.COURSE_NAME)));
            a.setCourseColor(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_COLOR)));
            a.setGradeID(cursor.getInt(cursor.getColumnIndex(Constants.GRADE_ID)));
            a.setGrade(cursor.getString(cursor.getColumnIndex(Constants.GRADE_EARNED)));
            a.setGradeWorth(cursor.getString(cursor.getColumnIndex(Constants.GRADE_WORTH)));
            a.setAssignType(cursor.getInt(cursor.getColumnIndex(Constants.ASSIGNMENT_TYPE)));
            a.setAssignProgress(cursor.getInt(cursor.getColumnIndex(Constants.ASSIGNMENT_PROGRESS)));
            return a;
        }

        return null;
    }
}
