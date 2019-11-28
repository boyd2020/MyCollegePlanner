package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.b96software.schoolplannerapp.util.Utils;

import java.text.SimpleDateFormat;

public class Grade implements Parcelable {

    private int id, assignID, courseColor;
    private double earned, worth;
    private String date, assignmentName, courseName;


    public Grade()
    {
        earned = 0.0;
        worth = 0.0;
        date = new SimpleDateFormat(Utils.SQL_DATE_FORMAT).format(System.currentTimeMillis());
    }

    public Grade(String date, double earned, double worth)
    {
        this.date = date;
        this.earned = earned;
        this.worth = worth;
    }

    public Grade(int id, int assignID, String date, double earned, double worth)
    {
        this(date, earned, worth);
        this.id = id;
        this.assignID = assignID;
    }

    protected Grade(Parcel in) {
        id = in.readInt();
        assignID = in.readInt();
        courseColor = in.readInt();
        earned = in.readDouble();
        worth = in.readDouble();
        date = in.readString();
        assignmentName = in.readString();
        courseName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(assignID);
        dest.writeInt(courseColor);
        dest.writeDouble(earned);
        dest.writeDouble(worth);
        dest.writeString(date);
        dest.writeString(assignmentName);
        dest.writeString(courseName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignID() {
        return assignID;
    }

    public void setAssignID(int assignID) {
        this.assignID = assignID;
    }

    public int getCourseColor() {
        return courseColor;
    }

    public void setCourseColor(int courseColor) {
        this.courseColor = courseColor;
    }

    public double getEarned() {
        return earned;
    }

    public void setEarned(double earned) {
        this.earned = earned;
    }

    public double getWorth() {
        return worth;
    }

    public void setWorth(double worth) {
        this.worth = worth;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
