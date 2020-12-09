package com.example.googlemapss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

public class SessionActivity extends AppCompatActivity implements LocationListener{

    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;

    protected LocationManager locationManager;

    TextView txtLat;

    int session_id;

    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);


        // Keep alive when user locks screen
        Context mContext = getApplicationContext();
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock =  powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"motionDetection:keepAwake");
        wakeLock.acquire();

        // Get session_id

        Intent intent = getIntent();
        session_id = intent.getIntExtra("Session_id", 0);
        Log.d("Session", "Session id = " + session_id);


        MaterialButton btnBack = (MaterialButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop location updates
                // SessionActivity.this because the LocationListener interface is implemented in this class
                locationManager.removeUpdates(SessionActivity.this);
                wakeLock.release();
                finish();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Get location

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show();

            // Ask permission from user
            ActivityCompat.requestPermissions(SessionActivity.this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

            Locate();
        } else{
            // Just locate if permission has been given
            Locate();
        }
    }

    // Interface implementation

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        txtLat = (TextView) findViewById(R.id.textview1);
        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        PinLocation(location.getLatitude(), location.getLongitude());

        // Create the measurement
        Date currentTime = Calendar.getInstance().getTime();
        //DataModel dataModel;

        int heartrate = (int)(Math.random()*((200 - 50) + 1 )) + 50;
        double pace = (Math.random()*((40 - 10) + 1 )) + 10;
        double partmat1 = 2.5, partmat2 = 7.5, partmat3 = 10.0;
        double batvolt = 12.2, batperc = 100.0;

        // Make DAO object
        dao = new DAO(SessionActivity.this);

        // Make dataModel and add it to the database
        DataModel dataModel = new DataModel(0, session_id, heartrate, location.getLongitude(), location.getLatitude(), partmat1, partmat2, partmat3, batvolt, batperc, currentTime.toString());
        Log.d("Succes", dataModel.toString());

        boolean succes = dao.addMeasurement(dataModel);

        Log.d("Succes", "Succes = " + succes);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }



    // Functions

    // SuppressLint because Permission is already checked before call of this function

    @SuppressLint("MissingPermission")
    private void Locate(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    private void PinLocation(final double latitude, final double longitude) {
        //initialize task location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //When succes
                if (location != null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialize lat lng
                            LatLng latLng = new LatLng(latitude, longitude);
                            //Create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title(String.valueOf(latLng));
                            //Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                            // Add marker on map
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }
    
    //TODO: ADD FUNCTION THAT GETS ALL THE MEASUREMENTS AND THEN PLOTS THEM ALL ON THE MAP (FUNCTION IN DAO TO GET AND FUNCTION IN HERE TO PLOT)
    //TODO: Pas locatie nemen als er iets wordt doorgestuurd (random tijd wachten om door te sturen en daarop wachten)
    //TODO: Kijk cards op Materialview => optie om hiermee laatste data dat werd doorgestuurd te laten zien tijdens de sessie

}
