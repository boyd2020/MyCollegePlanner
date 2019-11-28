package com.b96software.schoolplannerapp.managers;

public class Constants {

    //Database Info
    public static final String DATABASE_NAME ="PlannerDB";
    public static final int DATABASE_VERSION = 1;

    //Term Table
    public static final String TERM_TABLE = "terms";
    public static final String TERM_ID = "TermID";
    public static final String TERM_NAME = "TermName";
    public static final String TERM_START = "TermStart";
    public static final String TERM_END = "TermEnd";

    //Professor Table
    public static final String PROFESSOR_TABLE = "professors";
    public static final String PROF_ID = "profID";
    public static final String PROF_NAME = "profName";
    public static final String PROF_PHONE = "profPhone";
    public static final String PROF_EMAIL = "profEmail";
    public static final String PROF_OFFICE = "profOffice";
    public static final String PROF_IMAGE = "profImage";

    //Course Table
    public static final String COURSE_TABLE = "courses";
    public static final String COURSE_ID = "courseID";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_START = "courseStart";
    public static final String COURSE_END = "courseEnd";
    public static final String COURSE_COLOR = "courseColor";
    public static final String COURSE_LOCATION = "courseLocation";


    //ClassDates Table
    public static final String CLASS_DATES_TABLE = "classes";
    public static final String CLASS_DATE_ID = "classID";
    public static final String CLASS_DATE = "classDate";
    public static final String CLASS_START = "classStart";
    public static final String CLASS_END = "classEnd";

    //ClassDays Table
    public static final String CLASS_DAYS_TABLE = "classDays";
    public static final String CLASS_DAYS_ID = "daysID";
    public static final String CLASS_DAY_NAME = "dayName";
    public static final String CLASS_DAY_CHECKED = "dayChecked";
    public static final String CLASS_DAY_VALUE = "dayValue";

    //Assignment Table
    public static final String ASSIGNMENT_TABLE = "assignments";
    public static final String ASSIGNMENT_ID = "assignID";
    public static final String ASSIGNMENT_NAME = "assignName";
    public static final String ASSIGNMENT_TYPE = "assignType";
    public static final String ASSIGNMENT_DATE = "assignDate";
    public static final String ASSIGNMENT_PROGRESS = "assignProgress";

    //Notes Table
    public static final String NOTES_TABLE = "notes";
    public static final String NOTES_ID = "noteID";
    public static final String NOTES_NAME = "noteName";
    public static final String NOTE_TEXT = "noteText";
    public static final String NOTE_DATE = "noteDate";
    public static final String NOTE_TYPE = "noteType";
    public static final String NOTE_IMAGE = "noteImage";

    //Note Items Table
    public static final String NOTE_ITEMS_TABLE = "noteItems";
    public static final String NOTE_ITEM_ID = "noteItemID";
    public static final String NOTE_ITEM_CONTENT = "noteItemContent";
    public static final String NOTE_ITEM_CHECKED = "noteItemChecked";


    //Grades Table
    public static final String GRADES_TABLE = "grades";
    public static final String GRADE_ID = "gradeID";
    public static final String GRADE_WORTH = "pointsWorth";
    public static final String GRADE_EARNED = "paintsEarned";
    public static final String GRADE_DATE = "gradedDate";

    //Event Table
    public static final String EVENT_TABLE = "events";
    public static final String EVENT_ID = "eventID";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_DESC = "eventDesc";
    public static final String EVENT_COLOR = "eventColor";
    public static final String EVENT_TYPE_ID = "eventTypeID";
    public static final String EVENT_DATE = "eventDate";
    public static final String EVENT_TYPE = "eventType";
    public static final String EVENT_STATUS = "eventStatus";

    //Create Term Table
    public static final String CREATE_TERM_TABLE = "CREATE TABLE " + TERM_TABLE
    + "( " + TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TERM_NAME + " TEXT, "
    + TERM_START + " DATETIME, " + TERM_END + " DATETIME);";

    //Create Professor Table
    public static final String CREATE_PROFESSOR_TABLE = "CREATE TABLE " + PROFESSOR_TABLE
    + "( " + PROF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PROF_NAME + " TEXT, "
    + PROF_PHONE + " TEXT, " + PROF_EMAIL + " TEXT, " + PROF_IMAGE + " BLOB, "
    + PROF_OFFICE + " TEXT);";


    //Create Course Table
    public static final String CREATE_COURSE_TABLE = "CREATE TABLE " + COURSE_TABLE
    + "( " + COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PROF_ID + " INTEGER, "
    + TERM_ID + " INTEGER, " + COURSE_NAME + " TEXT, " + COURSE_START + " DATETIME, "
    + COURSE_END + " DATETIME, " + COURSE_LOCATION + " TEXT, " + COURSE_COLOR + " INTEGER, "
    + " FOREIGN KEY (" + PROF_ID + ") REFERENCES " + PROFESSOR_TABLE + "( " + PROF_ID + "));";

    //Create Class Table
    public static final String CREATE_CLASS_DATES_TABLE = "CREATE TABLE " + CLASS_DATES_TABLE
    + "( " + CLASS_DATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_ID + " INTEGER, "
    + CLASS_DATE + " DATETIME, " + CLASS_START + " DATETIME, " + CLASS_END + " DATETIME);";

    //Create Assignment Table
    public static final String CREATE_ASSIGNMENT_TABLE = "CREATE TABLE " + ASSIGNMENT_TABLE
    + "( " + ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_ID + " INTEGER, "
    + TERM_ID + " INTEGER, " + ASSIGNMENT_NAME + " TEXT, " + ASSIGNMENT_DATE + " DATETIME, "
    + ASSIGNMENT_PROGRESS + " INTEGER, " + ASSIGNMENT_TYPE + " INTEGER);";

    //Create Note Table
    public static final String CREATE_NOTE_TABLE = "CREATE TABLE " + NOTES_TABLE
    + "( " + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTES_NAME + " TEXT, "
    + NOTE_TEXT + " TEXT, " + NOTE_TYPE + " INTEGER, " + NOTE_DATE + " DATETIME);";

    //Create Note Item Table
    public static final String CREATE_NOTE_ITEM_TABLE = "CREATE TABLE " + NOTE_ITEMS_TABLE
    + "( " + NOTE_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTES_ID + " INTEGER, "
    + NOTE_ITEM_CONTENT + " TEXT, " + NOTE_ITEM_CHECKED + " INTEGER);";

    //Create Grades Table
    public static final String CREATE_GRADES_TABLE = " CREATE TABLE " + GRADES_TABLE
    + "( " + GRADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ASSIGNMENT_ID + " INTEGER, "
    + COURSE_ID + " INTEGER, " + GRADE_DATE + " DATETIME, " + GRADE_WORTH + " REAL, "
    + GRADE_EARNED + " REAL);";

    //Create Class Days Table
    public static final String CREATE_CLASS_DAYS_TABLE = "CREATE TABLE " + CLASS_DAYS_TABLE
    + "( " + CLASS_DAYS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_ID + " INTEGER, "
    + CLASS_DAY_VALUE + " INTEGER, " + CLASS_DAY_NAME + " TEXT, " + CLASS_DAY_CHECKED + " INTEGER);";

    //Create Event Table
    public static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE
    + "( " + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_TYPE_ID + " INTEGER, "
    + EVENT_DATE + " DATETIME, " + EVENT_TYPE + " INTEGER);";

}

