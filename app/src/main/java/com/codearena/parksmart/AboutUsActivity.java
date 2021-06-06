package com.codearena.parksmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        img = (ImageView)findViewById(R.id.back_btn);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*SharedPreferences shared = getSharedPreferences("key",MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("time", new SimpleDateFormat("HH:mm a").format(new Date().getTime()).toString());
        editor.apply();*/
    }
}
