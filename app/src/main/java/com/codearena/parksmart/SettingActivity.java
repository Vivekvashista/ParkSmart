package com.codearena.parksmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.net.URI;

public class SettingActivity extends AppCompatActivity {

    private CardView cp, lang, feedback, share, help;
    private ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        back_btn = (ImageView)findViewById(R.id.back_btn_setting);
        cp = (CardView)findViewById(R.id.cp_id);
        lang = (CardView)findViewById(R.id.lang_id);
        feedback = (CardView)findViewById(R.id.feedback_id);
        share = (CardView)findViewById(R.id.share_id);
        help = (CardView)findViewById(R.id.help_id);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent direct = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                startActivity(direct);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackForm();
            }
        });
    }

    private void feedbackForm()
    {
        Intent in = new Intent(Intent.ACTION_VIEW , Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName()));
        startActivity(in);
    }

    private void shareApp()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        String shareMessage= "Hey there! checkout the below link so we can serve you.\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Choose one to share with"));
    }
}
