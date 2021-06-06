package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up_form_for_user extends AppCompatActivity {

    private EditText namefield, mobfield, emailfield, passwordfield, confirmpasswordfield;
    private ImageView back_arrow;
    private Button subbtn, canbtn;
    private String otp;
    private ProgressDialog loadingbar;
    private TextView label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_form_for_user);

        namefield = (EditText)findViewById(R.id.user_name_id);
        mobfield = (EditText)findViewById(R.id.user_mob_id);
        emailfield = (EditText)findViewById(R.id.user_email_id);
        passwordfield = (EditText)findViewById(R.id.user_password_id);
        confirmpasswordfield = (EditText)findViewById(R.id.user_confirm_pass_id);
        subbtn = (Button)findViewById(R.id.user_submit);
        label = (TextView)findViewById(R.id.header);

        AssetManager as = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(as,"fonts/JosefinSans-Regular.ttf");
        label.setTypeface(typeface);

        loadingbar = new ProgressDialog(this);

        back_arrow = (ImageView)findViewById(R.id.back_btn);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    private void createAccount()
    {
        String name = namefield.getText().toString().trim();
        String mobile_no = mobfield.getText().toString().trim();
        String email_id = emailfield.getText().toString().trim();
        String pass = passwordfield.getText().toString().trim();
        String cpass = confirmpasswordfield.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            //Drawable customDrawable = getResources().getDrawable(R.drawable.error_icon);
            //customDrawable.setBounds(280,45,customDrawable.getIntrinsicWidth(),customDrawable.getIntrinsicHeight());
            namefield.setError("Please enter your name");
        }
        else
        if(TextUtils.isEmpty(mobile_no) || mobile_no.length()!=10)
        {
            mobfield.setError("Please enter your valid number");
        }
        else
        if(TextUtils.isEmpty(email_id))
        {
            emailfield.setError("Please enter your email-id");
        }
        else
        if(!isValid(email_id)){
            emailfield.setError("Invalid email-id!");
        }
        else
        if(TextUtils.isEmpty(pass))
        {
            passwordfield.setError("Please enter password");
        }
        else
        if(!isValidPassword(pass))
        {
            passwordfield.setError("Password must be of atleast 8 characters and should contains uppercase and lowercase as well as special character");
        }
        else
        if(TextUtils.isEmpty(cpass))
        {
            confirmpasswordfield.setError("Please confirm password");
        }
        else
        if(!pass.equals(cpass))
        {
            confirmpasswordfield.setError("Password do not match");
        }
        else
        {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait, while we are registering...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            saveUserDatabase(mobile_no,email_id,name,pass);
        }
    }
    private void saveUserDatabase(final String mobile_no, final String email_id, final String name, final String pass)
    {
        final DatabaseReference dataref;
        dataref = FirebaseDatabase.getInstance().getReference();

        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("users").child(mobile_no).exists())
                {
                    Toast.makeText(sign_up_form_for_user.this, "Already exist an account with this mobile number!", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
                else
                {
                    Intent in = new Intent(sign_up_form_for_user.this, OtpVerificationActivity.class);
                    in.putExtra("EmailIntent", email_id);
                    in.putExtra("name", name);
                    in.putExtra("mobile-no", mobile_no);
                    in.putExtra("password", pass);
                    in.putExtra("otp", otp);
                    in.putExtra("mode", "users");
                    loadingbar.dismiss();
                    startActivity(in);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

