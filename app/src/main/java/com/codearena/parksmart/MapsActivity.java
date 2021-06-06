package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.codearena.parksmart.Model.LandOwner;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SuggestionsAdapter.OnItemViewClickListener {

    private GoogleMap mMap;
    private int nov;
    private String longitude, latitude;
    private String[] data;
    private MaterialSearchBar searchBar;
    private ProgressDialog loadingbar;
    private ArrayList<String> ListOfLandOwners, ListOfNames, lastSearchedList;
    //private ListView listview;
    private Set<String> hs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ListOfLandOwners = new ArrayList<>();
        ListOfNames = new ArrayList<>();
        loadingbar = new ProgressDialog(this);

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        //listview = (ListView)findViewById(R.id.suggestions_list_id);
        searchBar.setSearchIcon(R.drawable.searchicon);
        searchBar.setSpeechMode(true);
        searchBar.showSuggestionsList();
        lastSearchedList = loadSuggestionsFromSet();
        searchBar.setLastSuggestions(lastSearchedList);
        searchBar.setSuggestionsClickListener(this);
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                if(text.length()<1)
                    return;

                loadingbar.setTitle("Please wait");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                matchLandOwners(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        Intent intent = getIntent();
        nov = Integer.parseInt(intent.getStringExtra("NumberOfVehicle").trim());
        data = new String[nov+1];
        for(int i=1 ; i<=nov ; i++)
            data[i] = intent.getStringExtra("vehicle"+i);
        longitude = intent.getStringExtra("Longitude");
        latitude = intent.getStringExtra("Latitude");
    }

    private void matchLandOwners(final CharSequence text)
    {
        final DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("landowner");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListOfLandOwners.clear();
                ListOfNames.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    LandOwner user = data.getValue(LandOwner.class);
                    String address = user.getAddress().toLowerCase();
                    String mob_no = user.getMobile_no();
                    String names = user.getName();
                    //System.out.println(address);
                    String pattern = String.valueOf(text).toLowerCase();
                    int[] patternLPS = computeLPS(pattern);
                    if(KMP(address,pattern,patternLPS)){
                        System.out.println("Yes");
                        ListOfLandOwners.add(mob_no);
                        ListOfNames.add(names);
                    }

                }

                Intent in = new Intent(MapsActivity.this,NearestLocationsActivity.class);
                in.putExtra("NumberOfVehicle",nov+"");
                for(int i=1 ; i<=nov ; i++)
                    in.putExtra("vehicle"+i,data[i]);
                in.putStringArrayListExtra("ListOfLandOwners", ListOfLandOwners);
                in.putStringArrayListExtra("ListOfNames", ListOfNames);
                loadingbar.dismiss();
                startActivity(in);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
        int[] computeLPS(String str)
        {
            int[] ar = new int[str.length()];
            int i=1, j=0;
            ar[0]=0;
            while(i<str.length())
            {
                if(str.charAt(i)==str.charAt(j))
                {
                    j++;
                    ar[i]=j;
                    i++;
                }
                else
                {
                    if(j!=0)
                        j=ar[j-1];
                    else
                    {
                        ar[i]=0;
                        i++;
                    }
                }
            }
            return ar;
        }
        boolean KMP(String str, String pat, int[] ar)
        {
            int k=0, j=0;
            while(j<str.length())
            {
                if(str.charAt(j)==pat.charAt(k))
                {
                    k++;
                    j++;
                }
                else
                {
                    if(k!=0)
                        k=ar[k-1];
                    else
                        j++;
                }
                if(k==pat.length())
                {
                    return true;
                }
            }
            return false;
        }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng locate = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        mMap.addMarker(new MarkerOptions().position(locate));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locate, 14), 200, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveSearchSuggestion(searchBar.getLastSuggestions());
    }

    private void saveSearchSuggestion(List lastSuggestions)
    {
        SharedPreferences sharedP = getSharedPreferences("searched",MODE_PRIVATE);
        hs = sharedP.getStringSet("suggestionsSet",new HashSet<String>());
        hs.addAll(lastSuggestions);
        SharedPreferences.Editor editor = sharedP.edit();
        editor.clear();
        editor.putStringSet("suggestionsSet",hs);
        editor.commit();
    }

    private ArrayList<String> loadSuggestionsFromSet()
    {
        SharedPreferences sharedP = getSharedPreferences("searched",MODE_PRIVATE);
        Set<String> listSet = sharedP.getStringSet("suggestionsSet",new HashSet<String>());
        ArrayList<String> ans = new ArrayList<>();
        for(String s: listSet)
            ans.add(s);
        return ans;
    }

    @Override
    public void OnItemClickListener(int position, View v) {
        searchBar.setText(lastSearchedList.get(position));
    }

    @Override
    public void OnItemDeleteListener(int position, View v) {

    }
    /*@Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.suggestion_one: searchBar.setText(item.getTitle().toString());
                        break;
            case R.id.suggestion_two: searchBar.setText(item.getTitle().toString());
                break;
            case R.id.suggestion_three: searchBar.setText(item.getTitle().toString());
                break;
        }
        return true;
    }*/
}
