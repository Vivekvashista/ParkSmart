package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codearena.parksmart.Model.LandOwner;
import com.codearena.parksmart.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Random;

public class forgot_password extends AppCompatActivity {

    private EditText mobile_no;
    private Button forgetbtn;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        Intent prev = getIntent();
        final String mode = prev.getStringExtra("mode");

        mobile_no = (EditText)findViewById(R.id.forget_mobile_id);
        forgetbtn = (Button)findViewById(R.id.forget_submit_id);
        loadingBar= new ProgressDialog(this);

        final String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));

        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mb=mobile_no.getText().toString().trim();
                if(TextUtils.isEmpty(mb))
                {
                    mobile_no.setError("Please enter your mobile number!");
                }
                else {
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    retrieveUser(mobile_no.getText().toString().trim(), mode, otp);
                }
            }
        });
    }

    private void retrieveUser(final String mobile_no, final String database, final String otp)
    {
        final DatabaseReference dref;
        dref = FirebaseDatabase.getInstance().getReference().child(database);

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mobile_no).exists())
                {
                    if(database.equals("users"))
                    {
                        Users userdata = dataSnapshot.child(mobile_no).getValue(Users.class);

                        Intent in = new Intent(forgot_password.this,OtpVerificationActivity.class);
                        in.putExtra("EmailIntent",userdata.getEmail_id());
                        in.putExtra("mode","forget");
                        in.putExtra("otp",otp);
                        in.putExtra("type",database);
                        in.putExtra("userid",mobile_no);

                        loadingBar.dismiss();
                        startActivity(in);
                        finish();
                    }
                    else
                    if(database.equals("landowner"))
                    {
                        LandOwner userdata = dataSnapshot.child(mobile_no).getValue(LandOwner.class);

                        Intent in = new Intent(forgot_password.this,OtpVerificationActivity.class);
                        in.putExtra("EmailIntent",userdata.getEmail_id());
                        in.putExtra("mode","forget");
                        in.putExtra("otp",otp);
                        in.putExtra("type",database);
                        in.putExtra("userid",mobile_no);
                        loadingBar.dismiss();
                        startActivity(in);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(forgot_password.this, "No account exist on this mobile number!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
