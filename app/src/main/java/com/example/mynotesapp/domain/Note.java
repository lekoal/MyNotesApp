package com.example.mynotesapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Note implements Parcelable {
    private String title;
    private String content;
    private String date;
    private String time;
    private String id;

    public Note(String id, String title, String content) {
        this.title = title;
        this.content = content;
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        SimpleDateFormat formatForTimeNow = new SimpleDateFormat("hh:mm:ss", Locale.US);
        this.date = formatForDateNow.format(dateNow);
        this.time = formatForTimeNow.format(dateNow);
        this.id = id;
    }

    protected Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        date = in.readString();
        time = in.readString();
        id = in.readString();
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(title, note.title) && Objects.equals(content, note.content) && Objects.equals(date, note.date) && Objects.equals(time, note.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, date, time);
    }
}
