package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.Model.LandOwner;
import com.codearena.parksmart.TrackUser.ReturningUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DetailedLandOwnerInfoActivity extends AppCompatActivity {


    private TextView addtxt, nametxt, mobtxt, availtxt;
    private Button payOnline, payOffline, cancel;
    private int nov;
    private String[] data;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_land_owner_info);

        addtxt = (TextView)findViewById(R.id.fetch_address_id);
        nametxt = (TextView)findViewById(R.id.fetch_name_id);
        mobtxt = (TextView)findViewById(R.id.fetch_mob_id);
        availtxt = (TextView)findViewById(R.id.availability_id);

        loadingBar = new ProgressDialog(this);

        Intent in = getIntent();
        String mobile = in.getStringExtra("mobile");
        nov = Integer.parseInt(in.getStringExtra("NumberOfVehicle").trim());
        data = new String[nov+1];
        for(int i=1 ; i<=nov ; i++)
            data[i] = in.getStringExtra("vehicle"+i);

        payOffline = (Button)findViewById(R.id.bottom_btn_id);
        cancel = (Button)findViewById(R.id.payment_cancel_btn);

        payOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMap();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fetchLandOwnerData(mobile);
    }

    private void openGoogleMap()
    {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage("Do you want to book slot?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadingBar.setTitle("Confirm Booking");
                        loadingBar.setMessage("Please wait, while we process your booking...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        saveDatabaseOfBooking("Success");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveDatabaseOfBooking("Pending");
                        dialogInterface.dismiss();
                    }
                })
                .show();


    }

    private void saveDatabaseOfBooking(final String pending)
    {
        final String mobile = ReturningUser.currentonlineuser.getMobile_no();
        final String Name = ReturningUser.currentonlineuser.getName();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatdate = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = formatdate.format(calendar.getTime());
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss a");
        String currentTime = formattime.format(calendar.getTime());
        final String key = currentDate+" "+currentTime;

        formatdate = new SimpleDateFormat("ddMMyyyy");
        currentDate = formatdate.format(calendar.getTime());
        formattime = new SimpleDateFormat("HHmmss");
        currentTime = formattime.format(calendar.getTime());
        final String BookingId = currentDate+currentTime;

        //notification section start
        SharedPreferences shared = getSharedPreferences("key",MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        String time = new SimpleDateFormat("HH:mm a").format(new Date().getTime()).toString();
        editor.putString("description",BookingId+(pending.equalsIgnoreCase("success") ? "s":"f")+time);
        editor.apply();
        //end

        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Bookings").child(mobile);
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> hp = new HashMap<>();
                hp.put("booking_id", BookingId);
                hp.put("date_time", key);
                hp.put("name",Name);
                hp.put("mobile_no",mobile);
                hp.put("number_of_vehicles",nov+"");
                for(int i=1 ; i<=nov ; i++)
                    hp.put("vehicle_no_"+i ,data[i]);
                for(int i=nov+1 ; i<=5 ; i++)
                    hp.put("vehicle_no_"+i ,"None");
                hp.put("parking_address",addtxt.getText().toString());
                hp.put("amount","20");
                hp.put("mode_of_payment","Offline");
                hp.put("status",pending);

                dataref.child(BookingId).updateChildren(hp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            if(pending.equalsIgnoreCase("success"))
                            {
                                Toast.makeText(DetailedLandOwnerInfoActivity.this, "Your booking has been confirmed!", Toast.LENGTH_SHORT).show();
                                Uri gmmIntentUri = Uri.parse("google.navigation:q="+addtxt.getText().toString());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        }
                        else
                            Toast.makeText(DetailedLandOwnerInfoActivity.this, "Something went wrong...please try again!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailedLandOwnerInfoActivity.this, "Something went wrong...please try again!", Toast.LENGTH_SHORT).show();
                    }
                });

                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchLandOwnerData(final String mobile)
    {
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("landowner");
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LandOwner owner = dataSnapshot.child(mobile).getValue(LandOwner.class);
                addtxt.setText(owner.getAddress());
                nametxt.setText(owner.getName());
                mobtxt.setText("+91-"+owner.getMobile_no());
                double length = Double.parseDouble(owner.getLand_length().trim());
                double breadth = Double.parseDouble(owner.getLand_breadth().trim());
                long capacity = (long) Math.floor((length*breadth)/16.7d);
                availtxt.setText("Available: "+capacity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
