package com.example.googlemapss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String COLUMN_PACE = "PACE";
    public static final String COLUMN_HEARTRATE = "HEARTRATE";
    public static final String COLUMN_PARTMAT = "PARTMAT";
    public static final String COLUMN_TIMESTAMP = "TIMESTAMP";
    public static final String COLUMN_SESSION_ID = "SESSION_ID";


    // Session table definition

    private static final String SQL_CREATE_TABLE_SESSION = "CREATE TABLE " + SESSION_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT NOT NULL"
            + ");";

    // Measurement table definition

    private static final String SQL_CREATE_TABLE_MEASUREMENT = "CREATE TABLE " + MEASUREMENT_TABLE + " (" +
            COLUMN_MEASUREMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_LONGITUDE + " INTEGER NOT NULL," +
            COLUMN_LATITUDE + " INTEGER NOT NULL," +
            COLUMN_PACE + " REAL NOT NULL," +
            COLUMN_HEARTRATE + " INTEGER NOT NULL," +
            COLUMN_PARTMAT + " REAL NOT NULL," +
            COLUMN_TIMESTAMP + " STRING NOT NULL," +
            COLUMN_SESSION_ID + " INTEGER NOT NULL);";


    // default constructor

    public DAO(@Nullable Context context) {
        super(context, "partmat.db", null, 5);
    }


    // Create or open database

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
        }else{
            // do not add anything
        }

        cursor.close();
        db.close();
        return returnList;
    }
}
