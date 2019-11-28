package com.b96software.schoolplannerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private int noteID, noteType;
    private String noteName, noteText, noteDate;

    public Note(){}


    public Note(String noteName, String noteText, String noteDate, int noteType)
    {
        this.noteName = noteName;
        this.noteDate = noteDate;
        this.noteText = noteText;
        this.noteType = noteType;
    }

    private Note(Parcel in)
    {
        noteID = in.readInt();
        noteType = in.readInt();
        noteName = in.readString();
        noteText = in.readString();
        noteDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(noteID);
        parcel.writeInt(noteType);
        parcel.writeString(noteName);
        parcel.writeString(noteText);
        parcel.writeString(noteDate);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }
}
