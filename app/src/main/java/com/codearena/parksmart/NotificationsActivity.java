package com.codearena.parksmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.Adapters.NotificationAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationsActivity extends AppCompatActivity {

    private ImageView back, delete;
    private RecyclerView recyclerview;
    private String[] description;
    private String[] time;
    private static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        back = (ImageView) findViewById(R.id.noti_back_btn);
        delete = (ImageView) findViewById(R.id.delete_noti_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerview = (RecyclerView)findViewById(R.id.notification_list_id);
        final SharedPreferences share= getSharedPreferences("key",MODE_PRIVATE);

        String mdesc = share.getString("description","");

        Set<String> hs = share.getStringSet("set",new HashSet<String>());
        if(!TextUtils.isEmpty(mdesc) && mdesc.length()>10)
        {
            hs.add(share.getString("description",""));
            SharedPreferences.Editor editor = share.edit();
            editor.clear();
            editor.putStringSet("set",hs);
            editor.commit();
        }
        //add(share.getString("description",""));

        //Toast.makeText(this, hs+"", Toast.LENGTH_SHORT).show();

        description = new String[hs.size()+1];
        time = new String[hs.size()+1];

        description[0] = "Welcome to ParkSmart!";
        time[0] = " ";

        if(hs.size()>0)
        {
            int k=1;
            for(String num: hs)
            {
                int index = num.contains("s") ? num.indexOf('s') : num.indexOf('f');
                description[k] = num.contains("s") ? "Your booking has been successful!" : "You attempted to book a parking slot!";
                time[k++] = num.substring(index+1,num.length());
            }
        }
        NotificationAdapter adapter = new NotificationAdapter(getApplicationContext(),description,time);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = share.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(NotificationsActivity.this,NotificationsActivity.class));
                finish();
            }
        });
    }
}
