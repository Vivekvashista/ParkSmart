package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

import static com.codearena.parksmart.GpsUtils.LOCATION_REQUEST;

public class LocationPermissionActivity extends AppCompatActivity {

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private int MY_PERMISSIONS_REQUEST_LOCATION_CODE = 99;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView txt;
    private LocationRequest locationRequest;
    private boolean isContinue = false;
    private boolean isGPS = false;
    private LocationCallback locationCallback;
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    private Button btnLocation;
    private int nov=0;
    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);

        txt = (TextView)findViewById(R.id.cross_btn_id);
        btnLocation = (Button)findViewById(R.id.refresh_btn_id);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        nov = Integer.parseInt(intent.getStringExtra("NumberOfVehicle").trim());
        data = new String[nov+1];
        for(int i=1 ; i<=nov ; i++)
            data[i] = intent.getStringExtra("vehicle"+i);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    //Toast.makeText(LocationPermissionActivity.this, "callback called", Toast.LENGTH_SHORT).show();
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        isContinue=false;
                        if (!isContinue) {

                            //txt.setText(wayLatitude+" "+wayLongitude);
                            Intent in = new Intent(LocationPermissionActivity.this,MapsActivity.class);
                            in.putExtra("NumberOfVehicle",nov+"");
                            in.putExtra("Latitude",wayLatitude+"");
                            in.putExtra("Longitude",wayLongitude+"");
                            for(int i=1 ; i<=nov ; i++)
                                in.putExtra("vehicle"+i,data[i]);
                            startActivity(in);
                            finish();
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                    else
                        isContinue=true;
                }
            }
        };
        //checkLocationPermission();

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGPS)
                {
                    Toast.makeText(LocationPermissionActivity.this, "Please turn on GPS...", Toast.LENGTH_SHORT).show();
                    return;
                }
                isContinue=true;
                getLocation();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(LocationPermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LocationPermissionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationPermissionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                //Toast.makeText(this, "iscontinue = false", Toast.LENGTH_SHORT).show();
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    wayLatitude = location.getLatitude();
                                    wayLongitude = location.getLongitude();

                                    //txt.setText(wayLatitude+" "+wayLongitude);
                                    Intent in = new Intent(LocationPermissionActivity.this,MapsActivity.class);
                                    in.putExtra("NumberOfVehicle",nov+"");
                                    in.putExtra("Latitude",wayLatitude+"");
                                    in.putExtra("Longitude",wayLongitude+"");
                                    for(int i=1 ; i<=nov ; i++)
                                        in.putExtra("vehicle"+i,data[i]);
                                    startActivity(in);
                                    finish();

                                } else {
                                    Toast.makeText(LocationPermissionActivity.this, "no location", Toast.LENGTH_LONG).show();
                                    isContinue=true;
                                }
                            }
                        });
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        Toast.makeText(this, "permission granted and callback", Toast.LENGTH_SHORT).show();
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if (location != null) {
                                            wayLatitude = location.getLatitude();
                                            wayLongitude = location.getLongitude();

                                            //txt.setText(wayLatitude+" "+wayLongitude);
                                            Intent in = new Intent(LocationPermissionActivity.this,MapsActivity.class);
                                            in.putExtra("NumberOfVehicle",nov+"");
                                            in.putExtra("Latitude",wayLatitude+"");
                                            in.putExtra("Longitude",wayLongitude+"");
                                            for(int i=1 ; i<=nov ; i++)
                                                in.putExtra("vehicle"+i,data[i]);
                                            startActivity(in);
                                            finish();

                                        } else {
                                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

}
