package com.example.googlemapss;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    int session_id;
    List<DataModel> MeasurementList;
    DAO dao;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);

        // Get session_id

        Intent intent = getIntent();
        session_id = intent.getIntExtra("Session_id", 0);
        Log.d("Session", "Session id = " + session_id);

        // Make DAO object
        dao = new DAO(this);

        // Get list of all measurements connected to the session and pass it to the visualizer
        MeasurementList = dao.getMeasurements(session_id);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Visualize(MeasurementList);


        // Back Button

        MaterialButton btnBack = (MaterialButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Visualize(List<DataModel> DataList){

        // Loop through every DataModel in the list and Pin it to the map
        for (DataModel dataModel : DataList) {
            PinLocation(dataModel.getLatitude(), dataModel.getLongitude(), dataModel.getHeartrate(), dataModel.getPartmat1(), dataModel.getPartmat2(), dataModel.getPartmat3());
        }
    }

    private void PinLocation(final double latitude, final double longitude, final int heartrate, final double partmat1, final double partmat2, final double partmat3) {
        //initialize task location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Coords", "No Permission");
            return;
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //Initialize lat lng
                LatLng latLng = new LatLng(latitude, longitude);
                //Create marker options
                MarkerOptions options = new MarkerOptions().position(latLng)
                        .title("Heartrate: " + String.valueOf(heartrate) + "\t" +
                                "2.5 µm: " + String.valueOf(partmat1) + "\t" +
                                "7.5 µm: " + String.valueOf(partmat2) + "\t" +
                                "10 µm: " + String.valueOf(partmat3));
                //Zoom map
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                // Add marker on map
                googleMap.addMarker(options);
            }
        });
    }
}
