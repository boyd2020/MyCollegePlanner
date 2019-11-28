package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Assignment implements Parcelable {

    private String name, date, gradeDate, courseName, grade, gradeWorth;
    private int id, gradeID, courseID, assignType, assignProgress, courseColor;


    public Assignment(){
        this.gradeID = 0;
        this.gradeWorth = "0.0";
        this.grade = "0.0";
    }

    public Assignment(int id)
    {
        this.id = id;
        this.gradeID = 0;
        this.gradeWorth = "0.0";
        this.grade = "0.0";
    }

    protected Assignment(Parcel in) {
        name = in.readString();
        date = in.readString();
        gradeDate = in.readString();
        courseName = in.readString();
        grade = in.readString();
        gradeWorth = in.readString();
        id = in.readInt();
        gradeID = in.readInt();
        courseID = in.readInt();
        assignType = in.readInt();
        assignProgress = in.readInt();
        courseColor = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(gradeDate);
        dest.writeString(courseName);
        dest.writeString(grade);
        dest.writeString(gradeWorth);
        dest.writeInt(id);
        dest.writeInt(gradeID);
        dest.writeInt(courseID);
        dest.writeInt(assignType);
        dest.writeInt(assignProgress);
        dest.writeInt(courseColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(String gradeDate) {
        this.gradeDate = gradeDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradeWorth() {
        return gradeWorth;
    }

    public void setGradeWorth(String gradeWorth) {
        this.gradeWorth = gradeWorth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGradeID() {
        return gradeID;
    }

    public void setGradeID(int gradeID) {
        this.gradeID = gradeID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getAssignType() {
        return assignType;
    }

    public void setAssignType(int assignType) {
        this.assignType = assignType;
    }

    public int getAssignProgress() {
        return assignProgress;
    }

    public void setAssignProgress(int assignProgress) {
        this.assignProgress = assignProgress;
    }

    public int getCourseColor() {
        return courseColor;
    }

    public void setCourseColor(int courseColor) {
        this.courseColor = courseColor;
    }
}
