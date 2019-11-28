package com.b96software.schoolplannerapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.b96software.schoolplannerapp.handlers.CourseHandler;
import com.b96software.schoolplannerapp.model.ClassDate;
import com.b96software.schoolplannerapp.model.Course;
import com.b96software.schoolplannerapp.util.BundleUtils;
import com.b96software.schoolplannerapp.util.ServiceUtils;
import com.b96software.schoolplannerapp.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClassDatesIntentService extends IntentService {

    public ClassDatesIntentService() {
        super("ClassDatesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<Integer> dates;

        switch (intent.getAction())
        {
            case ServiceUtils.SERVICE_ADD_CLASS_DATES:
                dates = intent.getIntegerArrayListExtra(BundleUtils.BUNDLE_CLASS_DATES);
                Course course = intent.getParcelableExtra(BundleUtils.BUNDLE_COURSE);
                addCourseDays(dates, course);
                break;
        }
    }

    private void addCourseDays(ArrayList<Integer> days, Course course)
    {
        CourseHandler handler = new CourseHandler(getApplicationContext());

        //Remove Previous Class Dates
        handler.removeClassDates(course.getCourseID());


        Calendar startDate = new GregorianCalendar(), endDate = new GregorianCalendar();
        Calendar startTime = new GregorianCalendar(), endTime = new GregorianCalendar();


        SimpleDateFormat format = new SimpleDateFormat(Utils.SQL_DATETIME_FORMAT);
        try {

           startDate.setTimeInMillis(format.parse(course.getCourseStartDate()).getTime());
           endDate.setTimeInMillis(format.parse(course.getCourseEndDate()).getTime());
           startTime.setTimeInMillis(format.parse(course.getCourseStart()).getTime());
           endTime.setTimeInMillis(format.parse(course.getCourseEnd()).getTime());

           while (startDate.getTimeInMillis() <= endDate.getTimeInMillis())
           {
               //Iterate through each day of the week
                if(days.contains(startDate.get(Calendar.DAY_OF_WEEK)))
                {
                    String classStart = format.format(startTime.getTimeInMillis());
                    String classEnd = format.format(endTime.getTimeInMillis());
                    String classDate = format.format(startDate.getTimeInMillis());

                    Log.e("Class Date", classDate);
                    ClassDate date = new ClassDate(course.getCourseID(), classDate, classStart, classEnd);
                    handler.addClassDate(date);
                }

                //Increment the Dates
               startDate.add(Calendar.DATE, 1);
               startTime.add(Calendar.DATE, 1);
               endTime.add(Calendar.DATE, 1);
           }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
