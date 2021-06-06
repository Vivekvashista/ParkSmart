package com.codearena.parksmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.Adapters.MyAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NearestLocationsActivity extends AppCompatActivity implements MyAdapter.OnItemListener {

    private String[] data;
    private RecyclerView recyclerView;
    private int[] images;
    private String[] name;
    private int Size, num;
    private MyAdapter adapter;
    private ArrayList<String> list_mobs;
    private TextView emptyMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_locations);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_container_id);
        emptyMsg = (TextView)findViewById(R.id.empty_parking_id);

        Intent in = getIntent();
        StringBuilder sb = new StringBuilder("");
        sb.append("Number of vehicle: "+in.getStringExtra("NumberOfVehicle")+"\n");
        num = Integer.parseInt(in.getStringExtra("NumberOfVehicle").trim());
        data = new String[num+1];
        for(int i=1 ; i<=num ; i++) {
            data[i] = in.getStringExtra("vehicle"+i);
            sb.append("vehicle" + i + ": " + data[i] + "\n");
        }

        list_mobs = in.getStringArrayListExtra("ListOfLandOwners");
        ArrayList<String> list_names = in.getStringArrayListExtra("ListOfNames");
        if(list_names.size()>0)
            emptyMsg.setVisibility(View.INVISIBLE);
        //System.out.println(list_mobs.size());
        Size=list_names.size();
        images = new int[list_mobs.size()];
        name = new String[list_mobs.size()];

        for(int i=0 ; i<list_mobs.size() ; i++)
        {
            name[i] = list_names.get(i);
            images[i] = R.drawable.recycler_icons;
        }

        adapter = new MyAdapter(getApplicationContext(),name,images,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClicked(int position) {
        Intent in = new Intent(this,DetailedLandOwnerInfoActivity.class);
        in.putExtra("mobile",list_mobs.get(position));
        in.putExtra("NumberOfVehicle",num+"");
        for(int i=1 ; i<=num ; i++)
            in.putExtra("vehicle"+i,data[i]);
        startActivity(in);
    }
}
