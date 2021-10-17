package com.example.mynotesapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note {
    private String title;
    private String content;
    private String date;
    private String time;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        SimpleDateFormat formatForTimeNow = new SimpleDateFormat("hh:mm:ss", Locale.US);
        this.date = formatForDateNow.format(dateNow);
        this.time = formatForTimeNow.format(dateNow);
    }

    public void setDate(String date) {
        this.date = date;
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
}
