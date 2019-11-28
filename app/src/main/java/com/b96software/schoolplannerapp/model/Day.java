package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable {

    private int id, courseID, dayChecked, dayValue;
    private String name;

    public Day(){}

    public Day(String name, int dayValue, int dayChecked)
    {
        this.name = name;
        this.dayValue = dayValue;
        this.dayChecked = dayChecked;
    }


    protected Day(Parcel in) {
        id = in.readInt();
        courseID = in.readInt();
        dayChecked = in.readInt();
        dayValue = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(courseID);
        dest.writeInt(dayChecked);
        dest.writeInt(dayValue);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getDayChecked() {
        return dayChecked;
    }

    public void setDayChecked(int dayChecked) {
        this.dayChecked = dayChecked;
    }

    public int getDayValue() {
        return dayValue;
    }

    public void setDayValue(int dayValue) {
        this.dayValue = dayValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
