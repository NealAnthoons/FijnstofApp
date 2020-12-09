package com.example.googlemapss;

import android.os.Parcel;
import android.os.Parcelable;

public class DataModel{

    // partmat => particulate matter

    private int id, session_id, heartrate;
    private double longitude, latitude, partmat1, partmat2, partmat3,  batvolt, batperc;
    private String timestamp;

    // Constructor

    public DataModel(int id, int session_id, int heartrate, double longitude, double latitude, double partmat1, double partmat2, double partmat3, double batvolt, double batperc, String timestamp) {
        this.id = id;
        this.session_id = session_id;
        this.heartrate = heartrate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.partmat1 = partmat1;
        this.partmat2 = partmat2;
        this.partmat3 = partmat3;
        this.batvolt = batvolt;
        this.batperc = batperc;
        this.timestamp = timestamp;
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
                ", partmat1=" + partmat1 +
                ", partmat2=" + partmat2 +
                ", partmat3=" + partmat3 +
                ", batvolt=" + batvolt +
                ", batperc=" + batperc +
                ", timestamp='" + timestamp + '\'' +
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

    public double getPartmat1() {
        return partmat1;
    }

    public void setPartmat1(double partmat1) {
        this.partmat1 = partmat1;
    }

    public double getPartmat2() {
        return partmat2;
    }

    public void setPartmat2(double partmat2) {
        this.partmat2 = partmat2;
    }

    public double getPartmat3() {
        return partmat3;
    }

    public void setPartmat3(double partmat3) {
        this.partmat3 = partmat3;
    }

    public double getBatvolt() {
        return batvolt;
    }

    public void setBatvolt(double batvolt) {
        this.batvolt = batvolt;
    }

    public double getBatperc() {
        return batperc;
    }

    public void setBatperc(double batperc) {
        this.batperc = batperc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
