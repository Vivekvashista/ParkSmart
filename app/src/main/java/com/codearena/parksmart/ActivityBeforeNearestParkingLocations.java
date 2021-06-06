package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.Model.LandOwner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityBeforeNearestParkingLocations extends AppCompatActivity {

    private ArrayList<Pair> Addresses;
    private TextView txt;
    private int nov;
    private String longitude, latitude;
    private String[] data;
    private ArrayList<String> shortestDistance;
    private ArrayList<String> correspMob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_nearest_parking_locations);

        txt = (TextView)findViewById(R.id.text_id_2);
        shortestDistance = new ArrayList<>();
        correspMob = new ArrayList<>();

        Intent intent = getIntent();
        nov = Integer.parseInt(intent.getStringExtra("NumberOfVehicle").trim());
        data = new String[nov+1];
        for(int i=1 ; i<=nov ; i++)
            data[i] = intent.getStringExtra("vehicle"+i);
        longitude = intent.getStringExtra("Longitude");
        latitude = intent.getStringExtra("Latitude");

        Addresses = new ArrayList<>();
        getLocationOfLandOwners();
    }

    private void showData()
    {
        //StringBuilder sb = new StringBuilder("");
        double longi = Double.parseDouble(longitude);
        double lati = Double.parseDouble(latitude);

        for(int i=0 ; i<Addresses.size() ; i++)
        {
            double tmp = distance(Addresses.get(i).lat,Addresses.get(i).lon,27.2074d,77.9915d);
            shortestDistance.add(tmp+"");
            correspMob.add(Addresses.get(i).mob);
        }

        Intent in = new Intent(ActivityBeforeNearestParkingLocations.this,NearestLocationsActivity.class);
        in.putExtra("NumberOfVehicle",nov+"");
        for(int i=1 ; i<=nov ; i++)
            in.putExtra("vehicle"+i,data[i]);
        in.putStringArrayListExtra("shortestDistance", shortestDistance);
        in.putStringArrayListExtra("correspMob", correspMob);
        startActivity(in);
        finish();
    }

    private void getLocationOfLandOwners()
    {
        final DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("landowner");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    LandOwner user = data.getValue(LandOwner.class);
                    ArrayList<Double> ans = getLongitudeLatitude(getApplicationContext(),user.getAddress());
                    Pair p = new Pair(ans.get(0),ans.get(1),user.getMobile_no());
                    Addresses.add(p);

                }
                showData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Double> getLongitudeLatitude(Context applicationContext, String strAddress)
    {
        Geocoder coder = new Geocoder(applicationContext);
        List<Address> address;
        ArrayList<Double> p1 = new ArrayList<>();

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1.add(location.getLatitude());
            p1.add(location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    /*private float distance(double lat1, double lon1, double lat2, double lon2) {
        Location mylocation = new Location("");
        Location dest_location = new Location("");

        dest_location.setLatitude(lat1);
        dest_location.setLongitude(lon1);

        mylocation.setLatitude(lat2);
        mylocation.setLongitude(lon2);

        float distance = mylocation.distanceTo(dest_location);//in meters
        return distance;
    }*/
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist*1.609344);
    }

    private double deg2rad(double deg) {
        return ((deg * Math.PI) / 180.0);
    }

    private double rad2deg(double rad) {
        return ((rad * 180.0) / Math.PI);
    }
}

class Pair
{
    public double lat, lon;
    public String mob;
    public Pair(double a, double b, String c)
    {
        lat = a;
        lon = b;
        mob = c;
    }
}