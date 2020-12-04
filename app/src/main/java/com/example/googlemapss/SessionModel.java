package com.example.googlemapss;

public class SessionModel {

    private int id;
    private String date;

    // constructor


    public SessionModel(int id, String date) {
        this.id = id;
        this.date = date;
    }

    // toString method


    @Override
    public String toString() {
        return "SessionModel{" +
                "session_id=" + id +
                ", date='" + date + '\'' +
                '}';
    }

    // getters and setters

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
