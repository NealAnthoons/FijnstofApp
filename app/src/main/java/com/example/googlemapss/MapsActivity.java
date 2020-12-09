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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity {

    // references
    FloatingActionButton btn_StartSession;
    MaterialButton btn_GetSession;
    ListView lv_SessionList;

    // Initialize ArrayAdapter needed for the ListView
    ArrayAdapter SessionArrayAdapter;

    // DAO initialization
    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //txtLat = (TextView) findViewById(R.id.textview1);
        btn_StartSession = (FloatingActionButton) findViewById(R.id.btnStartSession);
        btn_GetSession = findViewById(R.id.btnGetSession);
        lv_SessionList = findViewById(R.id.lv_SessionList);

        dao = new DAO(MapsActivity.this);

        ShowSessions();

        // Button Start onclicklistener

        btn_StartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                SessionModel session = new SessionModel(0 ,currentTime.toString());

                boolean succes = dao.addSession(session);
                Log.d("Debug", "SUCCES = " + succes);

                // Switch to session activity

                Intent intent = new Intent(MapsActivity.this, SessionActivity.class);
                // pass through session_id
                int session_id = dao.getLastId();
                intent.putExtra("Session_id", session_id);

                startActivity(intent);
            }
        });

        // GET BUTTON LISTENER

        btn_GetSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("Debug", dao.getAllSessions().toString());
                //Log.d("Debug", dao.getMeasurements(2).toString());
                //Toast.makeText(MapsActivity.this, dao.getAllSessions().toString(), Toast.LENGTH_LONG).show();

                SessionArrayAdapter = new ArrayAdapter<SessionModel>(MapsActivity.this, android.R.layout.simple_list_item_1, dao.getAllSessions());
                lv_SessionList.setAdapter(SessionArrayAdapter);
            }
        });
    }

    private void ShowSessions() {
        SessionArrayAdapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_list_item_1, dao.getAllSessionNames());
        lv_SessionList.setAdapter(SessionArrayAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Get newly added session
        ShowSessions();
    }
}