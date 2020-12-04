package com.example.googlemapss;

public class DataModel {

    // partmat => particulate matter

    private int id, session_id, heartrate;
    private double longitude, latitude, pace, partmat;
    private String date;

    // Constructor

    public DataModel(int id, int session_id, int heartrate, double longitude, double latitude, double pace, double partmat, String date) {
        this.id = id;
        this.session_id = session_id;
        this.heartrate = heartrate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.pace = pace;
        this.partmat = partmat;
        this.date = date;
    }

    // toString

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", session_id=" + session_id +
                ", heartrate=" + heartrate +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", pace=" + pace +
                ", partmat=" + partmat +
                ", date='" + date + '\'' +
                '}';
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(int heartrate) {
        this.heartrate = heartrate;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public double getPartmat() {
        return partmat;
    }

    public void setPartmat(double partmat) {
        this.partmat = partmat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
