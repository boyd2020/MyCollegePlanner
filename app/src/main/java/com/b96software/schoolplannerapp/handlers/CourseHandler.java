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

import com.b96software.schoolplannerapp.R;
import com.b96software.schoolplannerapp.managers.Constants;
import com.b96software.schoolplannerapp.managers.DatabaseHandler;
import com.b96software.schoolplannerapp.model.ClassDate;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.model.Day;
import com.b96software.schoolplannerapp.providers.PlannerProvider;
import com.b96software.schoolplannerapp.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;

public class CourseHandler {

    private Context context;
    private ContentResolver resolver;

    public CourseHandler(Context context) {
        this.context = context;
        this.resolver = context.getContentResolver();
    }

    //Course CRUD Operations
    public Uri addCourse(Course c){

        //Create Content Values
        ContentValues values = new ContentValues();
        values.put(Constants.PROF_ID, c.getProfID());
        values.put(Constants.TERM_ID, c.getTermID());
        values.put(Constants.COURSE_COLOR, c.getCourseColor());
        values.put(Constants.COURSE_NAME, c.getCourseName());
        values.put(Constants.COURSE_START, c.getCourseStart());
        values.put(Constants.COURSE_END, c.getCourseEnd());
        values.put(Constants.COURSE_LOCATION, c.getLocation());

        Uri courseUri = PlannerProvider.COURSE_URI;
        return resolver.insert(courseUri, values);
    }

    public int updateCourse(Course c)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.PROF_ID, c.getProfID());
        values.put(Constants.COURSE_NAME, c.getCourseName());
        values.put(Constants.TERM_ID, c.getTermID());
        values.put(Constants.COURSE_COLOR, c.getCourseColor());
        values.put(Constants.COURSE_START, c.getCourseStart());
        values.put(Constants.COURSE_END, c.getCourseEnd());
        values.put(Constants.COURSE_LOCATION, c.getLocation());

        String selection = Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(c.getCourseID())};

        Uri courseUri = PlannerProvider.COURSE_URI;
        return resolver.update(courseUri, values, selection, selectionArgs);
    }

    public void deleteCourse(int courseID)
    {
        String selection = Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(courseID)};
        Uri uri = PlannerProvider.COURSE_URI;

        //Delete Course
        resolver.delete(uri, selection, selectionArgs);
        //deleteCourseGrades(courseID);
        //deleteCourseAssignments(courseID);
    }


    //Get All Courses
    public Loader<Cursor> getAllCourses()
    {
        String[] projection = new String[]{Constants.COURSE_ID, Constants.PROFESSOR_TABLE + "." + Constants.PROF_NAME, Constants.COURSE_NAME,
                Constants.COURSE_COLOR, Constants.COURSE_START, Constants.COURSE_END, Constants.PROFESSOR_TABLE + "." + Constants.PROF_ID,
                Constants.TERM_TABLE + "." + Constants.TERM_ID};

        Uri uri = PlannerProvider.COURSE_URI;
        return new CursorLoader(context, uri, projection, null, null, null);
    }


    //Get a course from database
    public Loader<Cursor> getCourseFromDB(int courseID)
    {
        String[] projection = new String[] {Constants.COURSE_ID, Constants.PROFESSOR_TABLE + "." + Constants.PROF_ID,
                Constants.PROFESSOR_TABLE + "." + Constants.PROF_NAME, Constants.COURSE_NAME, Constants.COURSE_START, Constants.COURSE_END,
                Constants.COURSE_LOCATION, Constants.COURSE_COLOR, Constants.TERM_TABLE + "." + Constants.TERM_ID};

        String selection = Constants.COURSE_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(courseID)};

        Uri uri = PlannerProvider.COURSE_URI;

        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }

    public ArrayList<Course> getCoursesFromCursor(Cursor cursor)
    {
        ArrayList<Course> courses = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                Course c = new Course();
                c.setCourseID(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_ID)));
                c.setCourseName(cursor.getString(cursor.getColumnIndex(Constants.COURSE_NAME)));
                c.setCourseColor(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_COLOR)));
                c.setProfID(cursor.getInt(cursor.getColumnIndex(Constants.PROF_ID)));
                c.setProfName(cursor.getString(cursor.getColumnIndex(Constants.PROF_NAME)));
                c.setCourseStart(cursor.getString(cursor.getColumnIndex(Constants.COURSE_START)));
                c.setCourseEnd(cursor.getString(cursor.getColumnIndex(Constants.COURSE_END)));
                c.setTermID(cursor.getInt(cursor.getColumnIndex(Constants.TERM_ID)));
                c.setCourseDays(getCourseDays(c.getCourseID()));
                courses.add(c); 
            } while (cursor.moveToNext());
        }

        return courses;
    }

    public Course getCourseFromCursor(Cursor cursor)
    {
        if(cursor.moveToFirst())
        {
            Course c = new Course();
            c.setCourseID(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_ID)));
            c.setCourseName(cursor.getString(cursor.getColumnIndex(Constants.COURSE_NAME)));
            c.setCourseColor(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_COLOR)));
            c.setProfName(cursor.getString(cursor.getColumnIndex(Constants.PROF_NAME)));
            c.setProfID(cursor.getInt(cursor.getColumnIndex(Constants.PROF_ID)));
            c.setCourseStart(cursor.getString(cursor.getColumnIndex(Constants.COURSE_START)));
            c.setCourseEnd(cursor.getString(cursor.getColumnIndex(Constants.COURSE_END)));
            c.setLocation(cursor.getString(cursor.getColumnIndex(Constants.COURSE_LOCATION)));
            c.setTermID(cursor.getInt(cursor.getColumnIndex(Constants.TERM_ID)));
            c.setCourseDays(getCourseDays(c.getCourseID()));
            return c;
        }

        return null;
    }



    //Class Days
    public void addCourseDays(int courseID, ArrayList<Day> days)
    {
        for(int i = 0; i < days.size(); i++)
        {
            ContentValues values = new ContentValues();
            values.put(Constants.CLASS_DAY_NAME, days.get(i).getName());
            values.put(Constants.COURSE_ID, courseID);
            values.put(Constants.CLASS_DAY_CHECKED, days.get(i).getDayChecked());
            values.put(Constants.CLASS_DAY_VALUE, days.get(i).getDayValue());

            Uri uri = PlannerProvider.CLASS_URI;
            resolver.insert(uri, values);
        }
    }

    public ArrayList<Day> addCourseDays()
    {
        ArrayList<Day> days = new ArrayList<>();
        days.add(new Day(context.getString(R.string.mon), Calendar.MONDAY, Utils.NOT_CHECKED));
        days.add(new Day(context.getString(R.string.tues), Calendar.TUESDAY, Utils.NOT_CHECKED));
        days.add(new Day(context.getString(R.string.wens), Calendar.WEDNESDAY, Utils.NOT_CHECKED));
        days.add(new Day(context.getString(R.string.thurs), Calendar.THURSDAY, Utils.NOT_CHECKED));
        days.add(new Day(context.getString(R.string.fri), Calendar.FRIDAY, Utils.NOT_CHECKED));
        days.add(new Day(context.getString(R.string.sat), Calendar.SATURDAY, Utils.NOT_CHECKED));
        days.add(new Day(context.getString(R.string.sun), Calendar.SUNDAY, Utils.NOT_CHECKED));

        return days;
    }


    public String getCourseDays(int courseID)
    {
        StringBuilder builder = new StringBuilder();

        String[] projection = new String[]{Constants.CLASS_DAY_NAME};
        String selection = Constants.COURSE_ID + " =? AND " + Constants.CLASS_DAY_CHECKED + " =?";
        String[] selectionArgs = new String[]{String.valueOf(courseID), String.valueOf(Utils.CHECKED)};

        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();
        Cursor cursor = db.query(Constants.CLASS_DAYS_TABLE, projection, selection, selectionArgs, null, null, null);


        if(cursor.moveToFirst())
        {
            do{
                builder.append(cursor.getString(cursor.getColumnIndex(Constants.CLASS_DAY_NAME)) + " ");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return builder.toString();
    }

    public String getCourseDays(ArrayList<Day> days)
    {
        StringBuilder builder = new StringBuilder();


        for(Day d: days) {

            if(d.getDayChecked() == Utils.CHECKED)
                builder.append(d.getName() + " ");
        }

        return builder.toString();
    }

    /** Class Date Operations */
    public Uri addClassDate(ClassDate c)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.COURSE_ID, c.getCourseID());
        values.put(Constants.CLASS_DATE, c.getClassDate());
        values.put(Constants.CLASS_START, c.getClassStart());
        values.put(Constants.CLASS_END, c.getClassEnd());

        Uri uri = PlannerProvider.CLASS_DATE_URI;
        return resolver.insert(uri, values);
    }

    public int updateClassDate(ClassDate c)
    {
        ContentValues values = new ContentValues();
        values.put(Constants.CLASS_DATE, c.getClassDate());
        values.put(Constants.CLASS_START, c.getClassStart());
        values.put(Constants.CLASS_END, c.getClassEnd());


        String selection = Constants.CLASS_DATE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(c.getClassID())};

        Uri uri = PlannerProvider.CLASS_DATE_URI;

        return resolver.update(uri, values, selection, selectionArgs);
    }

    public void updateCourseDays(ArrayList<Day> days)
    {
        for(Day day: days)
        {
            ContentValues values = new ContentValues();
            values.put(Constants.CLASS_DAY_NAME, day.getName());
            values.put(Constants.CLASS_DAY_CHECKED, day.getDayChecked());
            values.put(Constants.COURSE_ID, day.getCourseID());
            values.put(Constants.CLASS_DAY_VALUE, day.getDayValue());

            String selection = Constants.CLASS_DAYS_ID + " =?";
            String[] selectionArgs = new String[]{String.valueOf(day.getId())};


            Uri uri = PlannerProvider.CLASS_URI;
            resolver.update(uri, values, selection, selectionArgs);
        }
    }


    public int removeClassDates(int courseID)
    {
        String selection = Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(courseID)};

        Uri uri = PlannerProvider.CLASS_DATE_URI;

        return resolver.delete(uri, selection, selectionArgs);
    }


    public ArrayList<Day> getClassDaysByCourseID(int courseID)
    {
        ArrayList<Day> days = new ArrayList<>();

        String[] projection = new String[]{Constants.CLASS_DAYS_ID, Constants.CLASS_DAY_NAME, Constants.CLASS_DAY_CHECKED,
        Constants.COURSE_ID, Constants.CLASS_DAY_VALUE};


        String selection = Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(courseID)};

        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();
        Cursor cursor = db.query(Constants.CLASS_DAYS_TABLE, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst())
        {
            do{
                Day d = new Day();
                d.setId(cursor.getInt(cursor.getColumnIndex(Constants.CLASS_DAYS_ID)));
                d.setCourseID(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_ID)));
                d.setName(cursor.getString(cursor.getColumnIndex(Constants.CLASS_DAY_NAME)));
                d.setDayValue(cursor.getInt(cursor.getColumnIndex(Constants.CLASS_DAY_VALUE)));
                d.setDayChecked(cursor.getInt(cursor.getColumnIndex(Constants.CLASS_DAY_CHECKED)));
                days.add(d);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return days;
    }


    public String getFirstClassDateByCourse(int courseID)
    {
        String[] projection = new String[]{Constants.CLASS_DATE};

        String selection = Constants.COURSE_TABLE + "." + Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(courseID)};
        String sortOrder = Constants.CLASS_DATE + " ASC LIMIT 1";

        Uri uri = PlannerProvider.CLASS_DATE_URI;
        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);

        if(cursor.moveToFirst())
            return cursor.getString(0);

        return "";
    }

    public String getLastClassDateByCourse(int courseID)
    {
        String[] projection = new String[]{Constants.CLASS_DATE};

        String selection = Constants.COURSE_TABLE + "." + Constants.COURSE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(courseID)};
        String sortOrder = Constants.CLASS_DATE + " DESC LIMIT 1";

        Uri uri = PlannerProvider.CLASS_DATE_URI;
        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);

        if(cursor.moveToFirst())
            return cursor.getString(0);

        return "";
    }


    //Get a course from database
    public Loader<Cursor> getCourseDateFromDB(int classDateID)
    {
        String[] projection = new String[]{Constants.CLASS_DATE_ID, Constants.CLASS_DATE, Constants.COURSE_TABLE + "." + Constants.COURSE_ID,
                Constants.COURSE_NAME, Constants.CLASS_START, Constants.CLASS_END, Constants.COURSE_COLOR};

        String selection = Constants.CLASS_DATE_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(classDateID)};

        Uri uri = PlannerProvider.CLASS_DATE_URI;
        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }



    public ClassDate getClassDateById(int id)
    {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase db = new DatabaseHandler(context).getReadableDatabase();

        String[] projection = new String[]{Constants.CLASS_DATE_ID, Constants.CLASS_DATE, Constants.COURSE_TABLE + "." + Constants.COURSE_ID,
            Constants.COURSE_NAME, Constants.CLASS_START, Constants.CLASS_END, Constants.COURSE_COLOR};


        String selection = Constants.CLASS_DATE_ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        builder.setTables(Constants.CLASS_DATES_TABLE + " JOIN " + Constants.COURSE_TABLE + " ON "
        + Constants.CLASS_DATES_TABLE + "." + Constants.COURSE_ID + " = " + Constants.COURSE_TABLE + "." + Constants.COURSE_ID);

        //Query Tables
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, null);
        ClassDate classDate = getClassDateFromCursor(cursor);
        cursor.close();
        db.close();

        return classDate;
    }

    public ClassDate getClassDateFromCursor(Cursor cursor)
    {
        if(cursor.moveToFirst())
        {
            ClassDate d = new ClassDate();
            d.setClassID(cursor.getInt(cursor.getColumnIndex(Constants.CLASS_DATE_ID)));
            d.setClassDate(cursor.getString(cursor.getColumnIndex(Constants.CLASS_DATE)));
            d.setClassStart(cursor.getString(cursor.getColumnIndex(Constants.CLASS_START)));
            d.setClassEnd(cursor.getString(cursor.getColumnIndex(Constants.CLASS_END)));
            d.setCourseID(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_ID)));
            d.setCourseName(cursor.getString(cursor.getColumnIndex(Constants.COURSE_NAME)));
            d.setCourseColor(cursor.getInt(cursor.getColumnIndex(Constants.COURSE_COLOR)));
            return d;
        }

        return null;
    }

}
