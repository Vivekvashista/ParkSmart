package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.JavaMailAPI.GMailSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class OtpVerificationActivity extends AppCompatActivity {

    private TextView emailTextview;
    private EditText edtext;
    private Button verifybtn;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        emailTextview = (TextView)findViewById(R.id.sample_otp_id);
        verifybtn = (Button)findViewById(R.id.otp_verify_id);
        edtext = (EditText)findViewById(R.id.verify_text_id);
        loadingBar = new ProgressDialog(this);

        final Intent in = getIntent();
        final String transferedEmail = in.getStringExtra("EmailIntent");
        final String otptext = in.getStringExtra("otp");

        emailTextview.setText("We have sent an OTP to \n" + transferedEmail.substring(0, 2) + "****" + transferedEmail.substring(7, transferedEmail.length()));

        //sendEmail(transferedEmail,otptext);
        //Toast.makeText(OtpVerificationActivity.this, "Hello world! "+transferedEmail+" "+otptext, Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("goyalvivek81@gmail.com",
                            "Tcsase@2020");
                    sender.sendMail("ParkSmart - OTP Verification", "Your One Time Password(OTP) for sign up is "+otptext,
                            "goyalvivek81@gmail.com", transferedEmail);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str=edtext.getText().toString().trim();
                if(TextUtils.isEmpty(str))
                {
                    edtext.setError("please enter the valid OTP");
                    return;
                }
                if(in.getStringExtra("mode").equals("users")) {
                    final String namefield = in.getStringExtra("name");
                    final String mobfield = in.getStringExtra("mobile-no");
                    final String passwordfield = in.getStringExtra("password");
                    if(str.equals(otptext))
                    {
                        loadingBar.setTitle("Creating Account");
                        loadingBar.setMessage("Please wait, you are about to become the member of ParkSmart...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        saveUserDatabase(namefield,mobfield,transferedEmail,passwordfield);
                    }
                    else
                        Toast.makeText(OtpVerificationActivity.this, "Invalid OTP...", Toast.LENGTH_SHORT).show();
                }
                else
                if(in.getStringExtra("mode").equals("landowner"))
                {
                    if(str.equals(otptext))
                    {
                        loadingBar.setTitle("Creating Account");
                        loadingBar.setMessage("Please wait, while we are registering...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        final String namefield = in.getStringExtra("name");
                        final String mobfield = in.getStringExtra("mobile-no");
                        final String passwordfield = in.getStringExtra("password");
                        final String address = in.getStringExtra("address").toLowerCase();
                        final String adhar = in.getStringExtra("adhar");
                        final String landRegistration = in.getStringExtra("landRegistrationNumber");
                        final String landlength = in.getStringExtra("landlength");
                        final String landbreadth = in.getStringExtra("landbreadth");
                        final String Imageurl = in.getStringExtra("DownloadImageUrl");
                        saveLandOwnerData(namefield,mobfield,transferedEmail,address,passwordfield,adhar,landRegistration,landlength,landbreadth,Imageurl);
                    }
                    else
                        Toast.makeText(OtpVerificationActivity.this, "Invalid OTP...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(str.equals(otptext))
                    {
                        final String mobfield = in.getStringExtra("userid");
                        final String database = in.getStringExtra("type");
                        Intent in = new Intent(OtpVerificationActivity.this,ResetPasswordActivity.class);
                        in.putExtra("type",database);
                        in.putExtra("userid",mobfield);
                        startActivity(in);
                        finish();
                    }
                    else
                        Toast.makeText(OtpVerificationActivity.this, "Invalid OTP...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void saveUserDatabase(final String name, final String mobile_no, final String email_id, final String pass)
    {
        final DatabaseReference dataref;
        dataref = FirebaseDatabase.getInstance().getReference();

        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("users").child(mobile_no).exists()))
                {
                    HashMap<String,Object> hp = new HashMap<>();
                    hp.put("name",name);
                    hp.put("mobile_no",mobile_no);
                    hp.put("email_id",email_id);
                    hp.put("password",pass);
                    hp.put("image","none");
                    dataref.child("users").child(mobile_no).updateChildren(hp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(OtpVerificationActivity.this, "You are registered successfully!", Toast.LENGTH_SHORT).show();
                                        Intent in = new Intent(OtpVerificationActivity.this,login_form_for_both.class);
                                        startActivity(in);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(OtpVerificationActivity.this, "Sorry, something went wrong....please try again", Toast.LENGTH_SHORT).show();
                                        Intent in = new Intent(OtpVerificationActivity.this,sign_up_form_for_user.class);
                                        startActivity(in);
                                        finish();
                                    }
                                    loadingBar.dismiss();
                                }
                            });
                }
                else
                {
                    Toast.makeText(OtpVerificationActivity.this, "Already exist an account with this mobile number!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveLandOwnerData(final String name, final String mobile, final String email, final String address, final String password, final String adhar, final String landRegistration, final String landlength, final String landbreadth, final String imageurl)
    {
        final DatabaseReference dref;
        dref = FirebaseDatabase.getInstance().getReference().child("landowner");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child(mobile).exists()))
                {
                    HashMap<String,Object> hp = new HashMap<>();
                    hp.put("name",name);
                    hp.put("email_id",email);
                    hp.put("mobile_no",mobile);
                    hp.put("address",address);
                    hp.put("password",password);
                    hp.put("Adhar_no",adhar);
                    hp.put("land_registration_number",landRegistration);
                    hp.put("land_length",landlength);
                    hp.put("land_breadth",landbreadth);
                    hp.put("image_url",imageurl);

                    dref.child(mobile).updateChildren(hp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(OtpVerificationActivity.this, "You are registered successfully!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(OtpVerificationActivity.this, login_form_for_both.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        String message = task.getException().toString();
                                        Toast.makeText(OtpVerificationActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingBar.dismiss();
                                }
                            });
                }
                else
                {
                    Toast.makeText(OtpVerificationActivity.this, "Already exist an account with this mobile number!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
