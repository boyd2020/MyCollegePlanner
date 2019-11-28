package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SpinnerItem implements Parcelable {

    private String itemName, itemDate;

    public SpinnerItem(){}

    public SpinnerItem(String itemName, String itemDate)
    {
        this.itemName = itemName;
        this.itemDate = itemDate;
    }

    private SpinnerItem(Parcel in)
    {
        itemName = in.readString();
        itemDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(itemName);
        parcel.writeString(itemDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<SpinnerItem> CREATOR = new Creator<SpinnerItem>() {
        @Override
        public SpinnerItem createFromParcel(Parcel in) {
            return new SpinnerItem(in);
        }

        @Override
        public SpinnerItem[] newArray(int size) {
            return new SpinnerItem[size];
        }
    };

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }
}
