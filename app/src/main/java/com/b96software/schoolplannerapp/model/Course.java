package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {

    private int courseID, profID, termID, courseColor;
    private String courseName, courseStart, courseEnd, location, profName, courseDays,
    courseStartDate, courseEndDate;

    public Course(){ termID = 1;}


    protected Course(Parcel in) {
        courseID = in.readInt();
        profID = in.readInt();
        termID = in.readInt();
        courseColor = in.readInt();
        courseName = in.readString();
        courseStart = in.readString();
        courseEnd = in.readString();
        location = in.readString();
        profName = in.readString();
        courseDays = in.readString();
        courseStartDate = in.readString();
        courseEndDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courseID);
        dest.writeInt(profID);
        dest.writeInt(termID);
        dest.writeInt(courseColor);
        dest.writeString(courseName);
        dest.writeString(courseStart);
        dest.writeString(courseEnd);
        dest.writeString(location);
        dest.writeString(profName);
        dest.writeString(courseDays);
        dest.writeString(courseStartDate);
        dest.writeString(courseEndDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getProfID() {
        return profID;
    }

    public void setProfID(int profID) {
        this.profID = profID;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public int getCourseColor() {
        return courseColor;
    }

    public void setCourseColor(int courseColor) {
        this.courseColor = courseColor;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public String getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    public String getCourseDays() {
        return courseDays;
    }

    public void setCourseDays(String courseDays) {
        this.courseDays = courseDays;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }
}
