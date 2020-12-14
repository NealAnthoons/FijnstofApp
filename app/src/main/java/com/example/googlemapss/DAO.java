package com.example.googlemapss;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    // Session table strings

    public static final String SESSION_TABLE = "SESSION_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "DATE";

    // Measurement table strings

    public static final String MEASUREMENT_TABLE = "MEASUREMENT_TABLE";
    public static final String COLUMN_MEASUREMENT_ID = "MEASUREMENT_ID";
    public static final String COLUMN_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_LATITUDE = "LATITUDE";
    public static final String COLUMN_HEARTRATE = "HEARTRATE";
    public static final String COLUMN_PARTMAT1 = "PARTMAT1";
    public static final String COLUMN_PARTMAT2 = "PARTMAT2";
    public static final String COLUMN_PARTMAT3 = "PARTMAT3";
    public static final String COLUMN_TIMESTAMP = "TIMESTAMP";
    public static final String COLUMN_SESSION_ID = "SESSION_ID";
    public static final String COLUMN_BATVOLT = "BATVOLT";
    public static final String COLUMN_BATPERC = "BATPERC";


    // Session table definition

    private static final String SQL_CREATE_TABLE_SESSION = "CREATE TABLE " + SESSION_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT NOT NULL"
            + ");";

    // Measurement table definition

    private static final String SQL_CREATE_TABLE_MEASUREMENT = "CREATE TABLE " + MEASUREMENT_TABLE + " (" +
            COLUMN_MEASUREMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_LONGITUDE + " REAL NOT NULL," +
            COLUMN_LATITUDE + " REAL NOT NULL," +
            COLUMN_HEARTRATE + " INTEGER NOT NULL," +
            COLUMN_PARTMAT1 + " REAL NOT NULL," +
            COLUMN_PARTMAT2 + " REAL NOT NULL," +
            COLUMN_PARTMAT3 + " REAL NOT NULL," +
            COLUMN_BATVOLT + " REAL NOT NULL," +
            COLUMN_BATPERC + " REAL NOT NULL," +
            COLUMN_TIMESTAMP + " STRING NOT NULL," +
            COLUMN_SESSION_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY (SESSION_ID) REFERENCES SESSION_TABLE(ID));";


    // default constructor

    public DAO(@Nullable Context context) {
        super(context, "partmat.db", null, 12);
    }


    // Create or open database

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TABLE_SESSION);
        db.execSQL(SQL_CREATE_TABLE_MEASUREMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEASUREMENT_TABLE);
        onCreate(db);
    }

    // SESSION TABLE CALLS

    public boolean addSession(SessionModel sessionModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //cv.put(COLUMN_ID, sessionModel.getid());
        cv.put(COLUMN_DATE, sessionModel.getDate());

        // insert returns a long, IF -1 => Unsuccessful insert ELSE successful insert
        long insert = db.insert(SESSION_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<SessionModel> getAllSessions(){

        List<SessionModel> returnList = new ArrayList<>();

        // Get sessions from database
        String queryString = "SELECT * FROM " + SESSION_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        // rawQuery returns a cursor
        Cursor cursor = db.rawQuery(queryString, null);

        // See if something is returned
        if(cursor.moveToFirst()) {
            // loop through cursor and make an object for each row and put them in the returnList
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);

                SessionModel newSession = new SessionModel(id, date);
                returnList.add(newSession);

            } while (cursor.moveToNext());
        } else{
            // do not add anything
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public List<String> getAllSessionNames(){
        List<String> returnList = new ArrayList<>();

        // Get sessions from database
        String queryString = "SELECT " + COLUMN_ID + " FROM " + SESSION_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        // rawQuery returns a cursor
        Cursor cursor = db.rawQuery(queryString, null);

        // See if something is returned
        if(cursor.moveToFirst()) {
            // loop through cursor and make an object for each row and put them in the returnList
            do {
                int id = cursor.getInt(0);

                String string = "Session " + id;
                returnList.add(string);

            } while (cursor.moveToNext());
        } else{
            // do not add anything
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public int getLastId(){
        SQLiteDatabase db = this.getReadableDatabase();

        String QueryString = "SELECT MAX(" + COLUMN_ID + ") FROM " + SESSION_TABLE;

        Cursor cursor = db.rawQuery(QueryString, null);

        cursor.moveToFirst();
        int id = cursor.getInt(0);

        Log.d("Cursor", "Max id = " + id);

        return id;
    }

    public String getDate(int sess){

        String returnString = "";

        // Get timestamp from database
        String queryString = "SELECT " + COLUMN_DATE + " FROM " + SESSION_TABLE + " WHERE " + COLUMN_ID + "=" + sess;

        SQLiteDatabase db = this.getReadableDatabase();

        // rawQuery returns a cursor
        Cursor cursor = db.rawQuery(queryString, null);

        // See if something is returned
        if(cursor.moveToFirst()) {

            returnString = cursor.getString(0);


        } else{
            // do not add anything
        }

        return returnString;
    }

    // MEASUREMENTS TABLE CALLS

    public boolean addMeasurement(DataModel dataModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LONGITUDE, dataModel.getLongitude());
        cv.put(COLUMN_LATITUDE, dataModel.getLatitude());
        cv.put(COLUMN_HEARTRATE, dataModel.getHeartrate());
        cv.put(COLUMN_PARTMAT1, dataModel.getPartmat1());
        cv.put(COLUMN_PARTMAT2, dataModel.getPartmat2());
        cv.put(COLUMN_PARTMAT3, dataModel.getPartmat3());
        cv.put(COLUMN_TIMESTAMP, dataModel.getTimestamp());
        cv.put(COLUMN_BATPERC, dataModel.getBatperc());
        cv.put(COLUMN_BATVOLT, dataModel.getBatvolt());
        cv.put(COLUMN_SESSION_ID, dataModel.getSession_id());

        // insert returns a long, IF -1 => Unsuccessful insert ELSE successful insert
        long insert = db.insert(MEASUREMENT_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<DataModel> getMeasurements(int sess){

        List<DataModel> returnList = new ArrayList<>();

        // Get sessions from database
        String queryString = "SELECT * FROM " + MEASUREMENT_TABLE + " WHERE " + COLUMN_SESSION_ID + "=" + sess;

        SQLiteDatabase db = this.getReadableDatabase();

        // rawQuery returns a cursor
        Cursor cursor = db.rawQuery(queryString, null);

        // See if something is returned
        if(cursor.moveToFirst()) {
            // loop through cursor and make an object for each row and put them in the returnList
            do {
                int id = cursor.getInt(0);
                double longitude = cursor.getDouble(1);
                double latitude = cursor.getDouble(2);
                int heartrate = cursor.getInt(3);
                double partmat1 = cursor.getDouble(4);
                double partmat2 = cursor.getDouble(5);
                double partmat3 = cursor.getDouble(6);
                double batvolt = cursor.getDouble(7);
                double batperc = cursor.getDouble(8);
                String timestamp = cursor.getString(9);
                int session_id = cursor.getInt(10);

                DataModel dataModel = new DataModel(id,session_id,heartrate,longitude,latitude,partmat1, partmat2, partmat3, batvolt, batperc,timestamp);
                returnList.add(dataModel);

            } while (cursor.moveToNext());
        } else{
            // do not add anything
        }

        cursor.close();
        db.close();
        return returnList;
    }
}
