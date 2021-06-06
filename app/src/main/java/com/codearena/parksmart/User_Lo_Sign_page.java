package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.Model.LandOwner;
import com.codearena.parksmart.Model.Users;
import com.codearena.parksmart.TrackUser.ReturningUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class User_Lo_Sign_page extends AppCompatActivity {

    private Button User_id, LO_id;
    private TextView loginForm;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__lo__sign_page);

        /*User_id = (Button)findViewById(R.id.sign_up_user_id);
        LO_id = (Button)findViewById(R.id.sign_up_LO_id);
        loginForm = (TextView)findViewById(R.id.login_form_directive);*/
        loadingbar = new ProgressDialog(this);

        Paper.init(this);

        /*User_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(User_Lo_Sign_page.this, sign_up_form_for_user.class);
                startActivity(n);
            }
        });
        LO_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(User_Lo_Sign_page.this, sign_up_form_for_lo.class);
                startActivity(n);
            }
        });
        loginForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(User_Lo_Sign_page.this, login_form_for_both.class);
                startActivity(n);
            }
        });*/

        String userid = Paper.book().read(ReturningUser.LoginKey);
        String userpass = Paper.book().read(ReturningUser.PasswordKey);

        if(!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(userpass)){
            AllowAccess(userid,userpass);
            loadingbar.setTitle("Welcome Back!");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
        }
        else
        {
            Intent in = new Intent(User_Lo_Sign_page.this,login_form_for_both.class);
            startActivity(in);
            finish();
        }
}

    private void AllowAccess(final String userid, final String userpass)
    {
        final DatabaseReference dataref;
        dataref = FirebaseDatabase.getInstance().getReference().child("users");

        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userid).exists())
                {
                        Users userdata = dataSnapshot.child(userid).getValue(Users.class);
                        if(userpass.equals(userdata.getPassword()))
                        {
                            Intent in = new Intent(User_Lo_Sign_page.this,UserHomeActivity.class);
                            ReturningUser.currentonlineuser = userdata;
                            startActivity(in);
                            finish();
                            loadingbar.dismiss();
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
