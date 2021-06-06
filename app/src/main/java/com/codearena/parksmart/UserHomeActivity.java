package com.codearena.parksmart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;

import com.codearena.parksmart.Model.Users;
import com.codearena.parksmart.TrackUser.ReturningUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class UserHomeActivity extends AppCompatActivity {
    private TextView user_name_textview, toolbar_title;
    private ImageView notifications_icon;
    private DrawerLayout dl;
    private BottomNavigationView navView;
    private androidx.appcompat.widget.Toolbar toolbar;
    private CircleImageView user_profile_image;
    //private boolean[] isOpen;
    final Fragment home = new HomeFragment();
    final Fragment offers = new OffersFragment();
    final Fragment inbox = new InboxFragment();
    private Fragment active= home;
    public LocationManager locationManager;
    public String provider="";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            /*for(int i=0 ; i<4 ; i++) {
                isOpen[i] = false;
            }*/
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar_title .setText("Home");
                    getSupportFragmentManager().beginTransaction().hide(active).show(home).commit();
                    active = home;
                    return true;
                case R.id.navigation_pendings:
                    toolbar_title .setText("Bookings");
                    startActivity(new Intent(UserHomeActivity.this, BookingsActivity.class));
                    navView.setSelectedItemId(R.id.navigation_home);
                    active = home;
                    return false;
                case R.id.navigation_offers:
                    toolbar_title .setText("Offers");
                    getSupportFragmentManager().beginTransaction().hide(active).show(offers).commit();
                    active = offers;
                    return true;
                case R.id.navigation_inbox:
                    toolbar_title .setText("Inbox");
                    getSupportFragmentManager().beginTransaction().hide(active).show(inbox).commit();
                    active = inbox;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //isOpen = new boolean[4];

        /*Bottom navigations section*/
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /* Side navigation drawer section*/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbarTextView);
        notifications_icon = (ImageView) findViewById(R.id.home_notification_id);
        setSupportActionBar(toolbar);

        notifications_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this,NotificationsActivity.class));
            }
        });

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.home_drawer_icon);
        ab.setDisplayShowTitleEnabled(false);

        dl = (DrawerLayout) findViewById(R.id.dl);

        /*Fragment section begin*/
        toolbar_title .setText("Home");
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,inbox,"4").hide(inbox).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,offers,"3").hide(offers).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,home,"1").commit();

        NavigationView nav_d_view = (NavigationView)findViewById(R.id.nav_drawer_view);
        View sideHeaderView = nav_d_view.getHeaderView(0);
        user_name_textview = sideHeaderView.findViewById(R.id.user_name_id);
        user_profile_image = sideHeaderView.findViewById(R.id.profile_icon_id);

        showInfoInDrawer();

        nav_d_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id)
                {
                    case R.id.nav_account:
                        //Toast.makeText(UserHomeActivity.this, "bookings clicked", Toast.LENGTH_SHORT).show();
                        Intent in  = new Intent(UserHomeActivity.this,AccountActivity.class);
                        startActivity(in);
                        break;
                    case R.id.nav_bookings:
                        startActivity(new Intent(UserHomeActivity.this,BookingsActivity.class));
                        break;
                    case R.id.nav_offers:
                        startActivity(new Intent(UserHomeActivity.this,OffersActivity.class));
                        break;
                    case R.id.nav_setting:
                        Intent direct = new Intent(UserHomeActivity.this,SettingActivity.class);
                        startActivity(direct);
                        break;
                    case R.id.nav_faq:
                        Toast.makeText(UserHomeActivity.this, "faq clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(UserHomeActivity.this,AboutUsActivity.class));
                        break;
                    case R.id.nav_logout:
                        Paper.book().destroy();
                        Intent intent = new Intent(UserHomeActivity.this,User_Lo_Sign_page.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });

        Paper.init(this);
    }

    private void showInfoInDrawer()
    {
        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users");
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userdata = dataSnapshot.child(ReturningUser.currentonlineuser.getMobile_no()).getValue(Users.class);
                ReturningUser.currentonlineuser = userdata;
                user_name_textview.setText(ReturningUser.currentonlineuser.getName());
                Picasso.get().load(ReturningUser.currentonlineuser.getImage()).placeholder(R.drawable.user_profile_icon).into(user_profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void loadFragment(Fragment fragment)
    {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frame_container,fragment);
        active = fragment;
        trans.addToBackStack(null);
        trans.commit();
    }*/

}
