package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassDate implements Parcelable {

    private int classID, courseID, courseColor;
    private String classDate, classStart, classEnd, courseName;


    public ClassDate(){}

    public ClassDate(int classID)
    {
        this.classID = classID;
    }

    public ClassDate(int courseID, String classDate, String classStart, String classEnd)
    {
        this.courseID = courseID;
        this.classDate = classDate;
        this.classStart = classStart;
        this.classEnd = classEnd;
    }


    protected ClassDate(Parcel in) {
        classID = in.readInt();
        courseID = in.readInt();
        courseColor = in.readInt();
        classDate = in.readString();
        classStart = in.readString();
        classEnd = in.readString();
        courseName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(classID);
        dest.writeInt(courseID);
        dest.writeInt(courseColor);
        dest.writeString(classDate);
        dest.writeString(classStart);
        dest.writeString(classEnd);
        dest.writeString(courseName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClassDate> CREATOR = new Creator<ClassDate>() {
        @Override
        public ClassDate createFromParcel(Parcel in) {
            return new ClassDate(in);
        }

        @Override
        public ClassDate[] newArray(int size) {
            return new ClassDate[size];
        }
    };

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getCourseColor() {
        return courseColor;
    }

    public void setCourseColor(int courseColor) {
        this.courseColor = courseColor;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getClassStart() {
        return classStart;
    }

    public void setClassStart(String classStart) {
        this.classStart = classStart;
    }

    public String getClassEnd() {
        return classEnd;
    }

    public void setClassEnd(String classEnd) {
        this.classEnd = classEnd;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
