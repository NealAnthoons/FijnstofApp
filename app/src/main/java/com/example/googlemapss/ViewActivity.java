package com.example.googlemapss;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    int session_id;
    List<DataModel> MeasurementList;
    DAO dao;
    SupportMapFragment mapFragment;
    Toolbar main_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);

        TextView txtDate = findViewById(R.id.txtDate);
        main_toolbar = findViewById(R.id.main_toolbar);

        // Get session_id and set title

        Intent intent = getIntent();
        session_id = intent.getIntExtra("Session_id", 0);
        main_toolbar.setTitle("Session " + session_id + " Overview");

        // Make DAO object
        dao = new DAO(this);

        // Get list of all measurements connected to the session and pass it to the visualizer
        MeasurementList = dao.getMeasurements(session_id);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        // Visualize measurements on map; give means, peaks and date of session
        Visualize(MeasurementList);
        Means(MeasurementList);
        Peaks(MeasurementList);
        txtDate.setText(dao.getDate(session_id));

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
                        .title("Heartrate: " + heartrate + " |" + "\t" +
                                "2.5 µm: " + (int) partmat1 + " |" + "\t" +
                                "7.5 µm: " + (int) partmat2 + " |" + "\t" +
                                "10 µm: " + (int) partmat3);
                //Zoom map
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                // Add marker on map
                googleMap.addMarker(options);
            }
        });
    }


    // Find mean of measurements
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void Means(List<DataModel> DataList){

        // Find TextViews
        TextView txtMeanHeart = findViewById(R.id.txtMeanHeart);
        TextView txtMeanPart1 = findViewById(R.id.txtMeanPart1);
        TextView txtMeanPart2 = findViewById(R.id.txtMeanPart2);
        TextView txtMeanPart3 = findViewById(R.id.txtMeanPart3);


        // Variables
        int i = 0;
        int mean_heartrate = 0;
        int mean_partmat1 = 0;
        int mean_partmat2 = 0;
        int mean_partmat3 = 0;

        for (DataModel dataModel : DataList) {
            mean_heartrate += dataModel.getHeartrate();
            mean_partmat1 += dataModel.getPartmat1();
            mean_partmat2 += dataModel.getPartmat2();
            mean_partmat3 += dataModel.getPartmat3();
            i++;
        }

        txtMeanHeart.setText(Integer.toString(mean_heartrate/i));
        txtMeanPart1.setText(Integer.toString(mean_partmat1/i));
        txtMeanPart2.setText(Integer.toString(mean_partmat2/i));
        txtMeanPart3.setText(Integer.toString(mean_partmat3/i));

    }

    // Find peak of measurements
    @SuppressLint("SetTextI18n")
    private void Peaks(List<DataModel> DataList){

        // Find TextViews
        TextView txtPeakHeart = findViewById(R.id.txtPeakHeart);
        TextView txtPeakPart1 = findViewById(R.id.txtPeakPart1);
        TextView txtPeakPart2 = findViewById(R.id.txtPeakPart2);
        TextView txtPeakPart3 = findViewById(R.id.txtPeakPart3);

        // Variables

        int MaxHeart = 0;
        int MaxPart1 = 0;
        int MaxPart2 = 0;
        int MaxPart3 = 0;

        for (DataModel dataModel : DataList) {
            if(dataModel.getHeartrate() > MaxHeart){
                MaxHeart = dataModel.getHeartrate();
            }

            if(dataModel.getPartmat1() > MaxPart1){
                MaxPart1 = (int) dataModel.getPartmat1();
            }

            if(dataModel.getPartmat2() > MaxPart2){
                MaxPart2 = (int) dataModel.getPartmat2();
            }

            if(dataModel.getPartmat1() > MaxPart3){
                MaxPart3 = (int) dataModel.getPartmat3();
            }
        }

        txtPeakHeart.setText(Integer.toString(MaxHeart));
        txtPeakPart1.setText(Integer.toString(MaxPart1));
        txtPeakPart2.setText(Integer.toString(MaxPart2));
        txtPeakPart3.setText(Integer.toString(MaxPart3));
    }
}
