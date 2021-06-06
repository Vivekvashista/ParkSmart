package com.codearena.parksmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandOwnerHomeActivity extends AppCompatActivity {

    private Button logoutbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_owner_home);

        logoutbtn = (Button)findViewById(R.id.logout_btn_id);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LandOwnerHomeActivity.this,login_form_for_both.class);
                startActivity(in);
                finish();
            }
        });
    }
}
