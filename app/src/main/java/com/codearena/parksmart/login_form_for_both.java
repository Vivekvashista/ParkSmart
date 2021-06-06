package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.w3c.dom.Text;

import io.paperdb.Paper;

public class login_form_for_both extends AppCompatActivity {

    private RadioGroup rg;
    private EditText email, password;
    private Button subbtn, canbtn, signup;
    private TextView errormsg, redirectLandOwner, label;
    private ProgressDialog loadingbar;
    private CheckBox rememberme;
    private RadioButton rdlobtn, rduserbtn;
    private TextView forgetpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form_for_both);

        rg = (RadioGroup)findViewById(R.id.radio_group_id);
        email = (EditText)findViewById(R.id.email_id_field);
        password = (EditText)findViewById(R.id.pass_id_field);
        errormsg = (TextView) findViewById(R.id.error_msg_id);
        redirectLandOwner = (TextView) findViewById(R.id.redirect_to_landowner_id);
        subbtn = (Button) findViewById(R.id.login_submit_button);
        //canbtn = (Button)findViewById(R.id.login_cancel_button);
        signup = (Button) findViewById(R.id.create_Account_id);
        rememberme = (CheckBox)findViewById(R.id.remember_id);
        rdlobtn = (RadioButton)findViewById(R.id.second);
        rduserbtn = (RadioButton)findViewById(R.id.first);
        label = (TextView)findViewById(R.id.login_head2);

        AssetManager as = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(as,"fonts/JosefinSans-Regular.ttf");
        label.setTypeface(typeface);

        forgetpass = (TextView)findViewById(R.id.forgot_password);

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(login_form_for_both.this,forgot_password.class);
                if(rdlobtn.isChecked())
                    in.putExtra("mode","landowner");
                else
                    in.putExtra("mode","users");
                startActivity(in);
            }
        });

        /*navigate to land owner*/
        redirectLandOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(login_form_for_both.this, sign_up_form_for_lo.class);
                startActivity(n);
            }
        });

        rduserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rememberme.setVisibility(View.VISIBLE);
            }
        });

        rdlobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rememberme.setVisibility(View.INVISIBLE);
            }
        });

        loadingbar = new ProgressDialog(this);

        Paper.init(this);

        /*navigate to users*/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(login_form_for_both.this,sign_up_form_for_user.class);
                startActivity(in);
            }
        });

        subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioid = rg.getCheckedRadioButtonId();
                if(radioid!=-1)
                {
                    errormsg.setVisibility(View.INVISIBLE);
                    RadioButton rgid = (RadioButton)findViewById(radioid);
                    String btntext = rgid.getText().toString();
                    if(btntext.equalsIgnoreCase("User"))
                        login("users");
                    else
                        login("landowner");
                }
                else
                {
                    errormsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void login(String databaseName)
    {
        String emailid = email.getText().toString().trim();
        String passid = password.getText().toString().trim();
        if(TextUtils.isEmpty(emailid) || emailid.length()!=10)
        {
            email.setError("Please enter your mobile-no");
        }
        else
        if(TextUtils.isEmpty(passid))
        {
            password.setError("Please enter your password");
        }
        else
        {
            loadingbar.setTitle("Logging In");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.setMessage("Please wait...");
            loadingbar.show();
            successfulLogin(emailid,passid,databaseName);
        }
    }

    private void successfulLogin(final String mobile_no, final String pass, final String databaseName)
    {
        if(rduserbtn.isChecked())
        {
            Paper.book().write(ReturningUser.LoginKey,mobile_no);
            Paper.book().write(ReturningUser.PasswordKey,pass);
        }
        final DatabaseReference dataref;
        dataref = FirebaseDatabase.getInstance().getReference().child(databaseName);

        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mobile_no).exists())
                {
                    errormsg.setVisibility(View.INVISIBLE);
                    if(databaseName.equalsIgnoreCase("users")) {
                        Users userdata = dataSnapshot.child(mobile_no).getValue(Users.class);
                        if(pass.equals(userdata.getPassword()))
                        {
                            errormsg.setVisibility(View.INVISIBLE);
                            Intent in = new Intent(login_form_for_both.this,UserHomeActivity.class);
                            ReturningUser.currentonlineuser=userdata;
                            startActivity(in);
                            finish();
                        }
                        else {
                            errormsg.setText("Password is incorrect!");
                            errormsg.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        LandOwner landownerdata = dataSnapshot.child(mobile_no).getValue(LandOwner.class);
                        if(pass.equals(landownerdata.getPassword()))
                        {
                            errormsg.setVisibility(View.INVISIBLE);
                            Intent in = new Intent(login_form_for_both.this,LandOwnerHomeActivity.class);
                            //ReturningUser.currentonlinelandowner = landownerdata;
                            startActivity(in);
                            finish();
                        }
                        else
                        {
                            errormsg.setText("Password is incorrect!");
                            errormsg.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else
                {
                    errormsg.setText("Invalid mobile number...please check the same!");
                    errormsg.setVisibility(View.VISIBLE);
                }
                loadingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
