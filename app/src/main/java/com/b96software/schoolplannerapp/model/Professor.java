package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Professor implements Parcelable {

    private int id;
    private byte[] image;
    private String name, phone, email, office;

    public Professor(){}

    public Professor(String name, String phone, String email, String office, byte[] image)
    {
        this.name = name;
        this.email = email;
        this.office = office;
        this.phone = phone;
        this.image = image;
    }


    private Professor(Parcel in) {
        id = in.readInt();
        image = in.createByteArray();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        office = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(image);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(office);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Professor> CREATOR = new Creator<Professor>() {
        @Override
        public Professor createFromParcel(Parcel in) {
            return new Professor(in);
        }

        @Override
        public Professor[] newArray(int size) {
            return new Professor[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
