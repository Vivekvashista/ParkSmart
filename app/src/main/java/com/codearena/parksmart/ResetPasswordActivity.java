package com.codearena.parksmart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText pass, cpass;
    private Button updatebtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        pass = (EditText)findViewById(R.id.reset_pass_id);
        cpass = (EditText)findViewById(R.id.reset_c_pass_id);
        updatebtn = (Button)findViewById(R.id.reset_update_btn);
        loadingBar = new ProgressDialog(this);

        Intent in = getIntent();
        final String mobile_no = in.getStringExtra("userid");
        final String database = in.getStringExtra("type");
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = pass.getText().toString().trim();
                final String cpassword = cpass.getText().toString().trim();
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
                    updateUserInfo(mobile_no,password,database);
                }
                else
                {
                    pass.setError("password and confirm password do not match!");
                }
            }
        });
    }

    private void updateUserInfo(final String mobile_no, final String password, final String database)
    {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child(database);

        HashMap<String ,Object> hp = new HashMap<>();
        hp.put("password",password);

        dataRef.child(mobile_no).updateChildren(hp);

        loadingBar.dismiss();
        startActivity(new Intent(ResetPasswordActivity.this,login_form_for_both.class));
        //Toast.makeText(ResetPasswordActivity.this, password, Toast.LENGTH_SHORT).show();
        Toast.makeText(ResetPasswordActivity.this, "Password updated Successfully!!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
