package com.b96software.schoolplannerapp.util;

public class Utils {

    //String formats for planner dates
    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd", SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME = "hh:mm a", DISPLAY_DATE = "MMM dd, yyyy", DATE_MONTH_DAY = "MMMM dd";
    public static final String DATE_DAY_NUM = "dd", DATE_WEEKDAY ="EEEE", DATE_MONTH_YEAR = "MMM yyyy", DATE_DAY = "MMM dd";
    public static final String DATE_MONTH = "MMMM yyyy";
    public static final String COURSE_TIME = "hh:mm a";


    //Bundle Keys
    public static final String KEY_LOADER_ID = "loaderID";
    public static final String KEY_START_DATE = "startDate", KEY_END_DATE = "endDate";
    public static final String KEY_DATES = "dates", KEY_DATE = "date";
    public static final String KEY_CALENDAR = "calendar";

    //Used for assignment radio groups
    public static final int ASSIGN_TYPE_HOMEWORK = 0, ASSIGN_TYPE_EXAM = 1;
    public static final int ASSIGN_TYPE_NOT_DONE = 0, ASSIGN_TYPE_COMPLETED = 1;

    //Time Types
    public static final int COURSE_TIME_FROM = 0, COURSE_TIME_TO = 1;

    //Time Types
    public static final int COURSE_DATE_FROM = 0, COURSE_DATE_TO = 1;

    //Checked
    public static final int NOT_CHECKED = 0, CHECKED = 1;

    //Note Types
    public static final int NOTE_TEXT_TYPE = 0, NOTE_ITEM_TYPE = 1;

    //Event Types
    public static final int EVENT_TYPE_ASSIGNMENT = 0, EVENT_TYPE_DATE = 1;


    //Numbers
    public static final int WEEK = 7;

}
