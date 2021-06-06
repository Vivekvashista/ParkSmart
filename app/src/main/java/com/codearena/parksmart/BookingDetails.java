package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codearena.parksmart.Model.Bookings;
import com.codearena.parksmart.TrackUser.ReturningUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingDetails extends AppCompatActivity {

    private ImageView backbtn;
    private TextView bid, add, dt, mop, nov, vn, amt, stat;
    private Button download_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        backbtn = (ImageView)findViewById(R.id.book_back_btn);
        bid = (TextView) findViewById(R.id.bookings_id_id);
        add = (TextView) findViewById(R.id.bookings_add_id);
        dt = (TextView) findViewById(R.id.bookings_dt_id);
        mop= (TextView) findViewById(R.id.bookings_MOP_id);
        nov = (TextView) findViewById(R.id.bookings_NOV_id);
        vn = (TextView) findViewById(R.id.bookings_VN_id);
        amt= (TextView) findViewById(R.id.bookings_amt_id);
        stat = (TextView) findViewById(R.id.bookings_status_id);

        download_btn = (Button)findViewById(R.id.download_pdf_id);
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        fillData(intent.getStringExtra("BookingId"));
    }

    private void fillData(final String bookingId)
    {
        String mobile = ReturningUser.currentonlineuser.getMobile_no();
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Bookings").child(mobile).child(bookingId);
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bookings bookings = dataSnapshot.getValue(Bookings.class);
                bid.setText(bookings.getBooking_id());
                add.setText(bookings.getParking_address());
                dt.setText(bookings.getDate_time());
                amt.setText(bookings.getAmount());
                mop.setText(bookings.getMode_of_payment());
                nov.setText(bookings.getNumber_of_vehicles());
                vn.setText(vn.getText().toString()+ (!bookings.getVehicle_no_1().equals("None") ? bookings.getVehicle_no_1()+", ":""));
                vn.setText(vn.getText().toString()+ (!bookings.getVehicle_no_2().equals("None") ? bookings.getVehicle_no_2()+", ":""));
                vn.setText(vn.getText().toString()+ (!bookings.getVehicle_no_3().equals("None") ? bookings.getVehicle_no_3()+", ":""));
                vn.setText(vn.getText().toString()+ (!bookings.getVehicle_no_4().equals("None") ? bookings.getVehicle_no_4()+", ":""));
                vn.setText(vn.getText().toString()+ (!bookings.getVehicle_no_5().equals("None") ? bookings.getVehicle_no_5()+", ":""));
                stat.setText(bookings.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
