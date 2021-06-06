package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codearena.parksmart.Model.Users;
import com.codearena.parksmart.TrackUser.ReturningUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldpass, pass, cpass;
    private Button updatebtn;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Paper.init(this);

        oldpass = (EditText)findViewById(R.id.old_pass_id);
        pass = (EditText)findViewById(R.id.change_pass_id);
        cpass = (EditText)findViewById(R.id.change_c_pass_id);
        updatebtn = (Button)findViewById(R.id.change_update_btn);
        loadingBar = new ProgressDialog(this);

        Intent in = getIntent();
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldpassword = oldpass.getText().toString().trim();
                final String password = pass.getText().toString().trim();
                final String cpassword = cpass.getText().toString().trim();

                if(TextUtils.isEmpty(oldpassword))
                {
                    oldpass.setError("Old password cannot be empty!");
                }
                else
                if(TextUtils.isEmpty(password))
                {
                    pass.setError("Password cannot be empty!");
                }
                else
                if(TextUtils.isEmpty(cpassword))
                {
                    cpass.setError("Confirm Password cannot be empty!");
                }
                else
                if(password.equals(cpassword))
                {
                    loadingBar.setTitle("Reset Password");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    updateUserInfo(oldpassword,password);
                }
                else
                {
                    pass.setError("password and confirm password do not match!");
                }
            }
        });
    }

    private void updateUserInfo(final String oldpassword, final String password)
    {
        final String mobile_no = ReturningUser.currentonlineuser.getMobile_no();
        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users");
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.child(mobile_no).getValue(Users.class);
                //Toast.makeText(ChangePasswordActivity.this, user.getPassword()+"-"+oldpassword, Toast.LENGTH_SHORT).show();
                if(user.getPassword().equals(oldpassword))
                {
                    HashMap<String ,Object> hp = new HashMap<>();
                    hp.put("password",password);
                    dataref.child(mobile_no).updateChildren(hp);

                    Paper.book().destroy();
                    Paper.book().write(ReturningUser.LoginKey,mobile_no);
                    Paper.book().write(ReturningUser.PasswordKey,password);

                    Toast.makeText(ChangePasswordActivity.this, "Password updated Successfully!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect!", Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
