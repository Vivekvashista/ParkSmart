package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.Adapters.BookingAdapter;
import com.codearena.parksmart.Model.Bookings;
import com.codearena.parksmart.TrackUser.ReturningUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingItemListener {

    private ImageView img;
    private String[] bookingids;
    private String[] Address;
    private String[] dateTime;
    private String[] bookingStatus;
    private RecyclerView recycler;
    private TextView emptyMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        img = (ImageView)findViewById(R.id.back_btn);
        recycler = (RecyclerView)findViewById(R.id.bookings_recycler_view_id);
        emptyMsg = (TextView)findViewById(R.id.empty_booking_id);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        countBooking();

        findBookingDatabase();
    }

    private void findBookingDatabase()
    {
        String mobile = ReturningUser.currentonlineuser.getMobile_no();
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Bookings").child(mobile);
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot book: dataSnapshot.getChildren())
                {
                    Bookings booking = book.getValue(Bookings.class);
                    bookingids[i] = booking.getBooking_id();
                    Address[i] = booking.getParking_address();
                    if(booking.getStatus().equalsIgnoreCase("success"))
                        bookingStatus[i] = "Success";
                    else
                        bookingStatus[i] = "failed";
                    dateTime[i] = booking.getDate_time();
                    //System.out.println(bookingids[i]+" "+Address[i]+" "+dateTime[i]+" "+bookingStatus[i]);
                    i++;
                }
                BookingAdapter adapter = new BookingAdapter(getApplicationContext(),bookingids,Address,dateTime,bookingStatus,BookingsActivity.this);
                recycler.setAdapter(adapter);
                recycler.setLayoutManager(new LinearLayoutManager(BookingsActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void countBooking()
    {
        String mobile = ReturningUser.currentonlineuser.getMobile_no();
        //System.out.print("count: "+mobile+" "+count);
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("Bookings").child(mobile);
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int c=0;
                for(DataSnapshot book: dataSnapshot.getChildren())
                    c++;

                bookingids = new String[c];
                Address = new String[c];
                dateTime = new String[c];
                bookingStatus = new String[c];
                if(c>0)
                    emptyMsg.setVisibility(View.INVISIBLE);
                //Toast.makeText(BookingsActivity.this, bookingids.length+" ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent in = new Intent(this,BookingDetails.class);
        in.putExtra("BookingId",bookingids[position]);
        startActivity(in);
    }
}
