package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
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

import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    public static int timer = 2000;
    private ProgressDialog loadingbar;
    private TextView txt1, txt2, label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = (TextView)findViewById(R.id.head2);
        AssetManager as = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(as,"fonts/JosefinSans-Regular.ttf");
        label.setTypeface(typeface);
        /*txt1= (TextView)findViewById(R.id.center_text_id);
        txt2= (TextView)findViewById(R.id.head2);
        AssetManager as = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(as,"fonts/avenir_black.otf");
        txt1.setTypeface(typeface);
        txt2.setTypeface(typeface);*/

        Paper.init(this);

        loadingbar = new ProgressDialog(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                checkInternet();
            }
        }, timer);
    }

    public final boolean checkInternet() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            Intent in = new Intent(this, User_Lo_Sign_page.class);
            startActivity(in);
            finish();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, "Please Connect to Internet and Restart the app", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
