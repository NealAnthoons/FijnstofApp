package com.example.googlemapss;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class MapsActivity extends FragmentActivity {

    // reference to Button
    Button btn_StartSession;
    Button btn_GetSession;

    // DAO initialization
    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //txtLat = (TextView) findViewById(R.id.textview1);
        btn_StartSession = findViewById(R.id.btn_StartSession);
        btn_GetSession = findViewById(R.id.btn_GetSession);

        dao = new DAO(MapsActivity.this);

        // Button onclicklistener
        btn_StartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                SessionModel session = new SessionModel(0 ,currentTime.toString());

                boolean succes = dao.addSession(session);
                Log.d("Debug", "SUCCES = " + succes);

                // Switch to session activity

                startActivity(new Intent(MapsActivity.this, SessionActivity.class));
            }
        });

        btn_GetSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", dao.getAllSessions().toString());
                Toast.makeText(MapsActivity.this, dao.getAllSessions().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}