package com.codearena.parksmart;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import static androidx.core.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    private CheckBox cb1, cb2, cb3;
    private EditText ed1, ed2, ed3;
    private Button dbtn1, dbtn2, dbtn3, proceed, reset;
    private RelativeLayout relout, relout1, relout2, relout3, relout4, relout5;
    private TextView info_label_textview, error_field;
    private RelativeLayout[] info;
    private EditText[] vehicleInfo;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String provider;
    private LocationManager locationManager;
    private int numberOfVehicle=0;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        /*Spinner dropdownitems = (Spinner)container.findViewById(R.id.dropdown1_items_id);
        String[] items = new String[]{"1","2","3","4","5"};
        *//*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.items_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*//*

        ArrayAdapter<String> demo = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,items);
        demo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownitems.setAdapter(demo);*/
        cb1 = (CheckBox)v.findViewById(R.id.two_wheeler_chbox);
        cb2 = (CheckBox)v.findViewById(R.id.four_wheeler_chbox);
        cb3 = (CheckBox)v.findViewById(R.id.comm_wheeler_chbox);

        ed1 = (EditText)v.findViewById(R.id.nov_id1);
        ed2 = (EditText)v.findViewById(R.id.nov_id2);
        ed3 = (EditText)v.findViewById(R.id.nov_id3);

        dbtn1 = (Button)v.findViewById(R.id.two_btn_id);
        dbtn2 = (Button)v.findViewById(R.id.four_btn_id);
        dbtn3 = (Button)v.findViewById(R.id.comm_btn_id);

        vehicleInfo = new EditText[6];
        vehicleInfo[1] = (EditText)v.findViewById(R.id.right_value_id1);
        vehicleInfo[2] = (EditText)v.findViewById(R.id.right_value_id2);
        vehicleInfo[3] = (EditText)v.findViewById(R.id.right_value_id3);
        vehicleInfo[4] = (EditText)v.findViewById(R.id.right_value_id4);
        vehicleInfo[5] = (EditText)v.findViewById(R.id.right_value_id5);

        info_label_textview = (TextView)v.findViewById(R.id.extra_text_info_id);

        proceed = (Button)v.findViewById(R.id.proceed_button_id);
        reset = (Button)v.findViewById(R.id.reset_button_id);
        error_field = (TextView)v.findViewById(R.id.error_textview_id);

        /*Reset button*/
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAll(view);
            }
        });

        relout = (RelativeLayout)v.findViewById(R.id.additional_info_id);

        info = new RelativeLayout[6];
        info[1] = (RelativeLayout)v.findViewById(R.id.info_layout_id_1);
        info[2] = (RelativeLayout)v.findViewById(R.id.info_layout_id_2);
        info[3] = (RelativeLayout)v.findViewById(R.id.info_layout_id_3);
        info[4] = (RelativeLayout)v.findViewById(R.id.info_layout_id_4);
        info[5] = (RelativeLayout)v.findViewById(R.id.info_layout_id_5);

        hideAll(v);

        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAll(view);
                dbtn1.setVisibility(View.VISIBLE);
                ed1.setEnabled(true);
                cb1.setChecked(true);
            }
        });
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAll(view);
                dbtn2.setVisibility(View.VISIBLE);
                ed2.setEnabled(true);
                cb2.setChecked(true);
            }
        });
        cb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAll(view);
                dbtn3.setVisibility(View.VISIBLE);
                ed3.setEnabled(true);
                cb3.setChecked(true);
            }
        });
        dbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ed1.getText().toString())) {
                    ed1.setError("Enter the number of vehicle!");
                    return;
                }

                int tmp = Integer.parseInt(ed1.getText().toString().trim());
                if(tmp>5 || tmp<0)
                    ed1.setError("Number of vehicle must be between 1 to 5!");
                else {
                    showOnButtonsClick(view, "two wheeler", tmp);
                    numberOfVehicle = tmp;
                }
            }
        });
        dbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(ed2.getText().toString())) {
                    ed2.setError("Enter the number of vehicle!");
                    return;
                }

                int tmp = Integer.parseInt(ed2.getText().toString().trim());
                if(tmp>5 || tmp<0)
                    ed2.setError("Number of vehicle must be between 1 to 5!");
                else {
                    showOnButtonsClick(view, "four wheeler", tmp);
                    numberOfVehicle = tmp;
                }
            }
        });
        dbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ed3.getText().toString())) {
                    ed3.setError("Enter the number of vehicle!");
                    return;
                }

                int tmp = Integer.parseInt(ed3.getText().toString().trim());
                if(tmp>5 || tmp<0)
                    ed3.setError("Number of vehicle must be between 1 to 5!");
                else {
                    showOnButtonsClick(view, "commercial vehicle", tmp);
                    numberOfVehicle = tmp;
                }
            }
        });

        /*Proceed button section*/

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_field.setVisibility(View.INVISIBLE);
                if(numberOfVehicle>0)
                {
                    boolean flag=false;
                    for(int i=1 ; i<=numberOfVehicle ; i++)
                    {
                        if(TextUtils.isEmpty(vehicleInfo[i].getText().toString()))
                        {
                            //vehicleInfo[i].setError("Mandatory");
                            error_field.setVisibility(View.VISIBLE);
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        return;
                    Intent in = new Intent(v.getContext(),LocationPermissionActivity.class);
                    in.putExtra("NumberOfVehicle",numberOfVehicle+"");
                    for(int i=1 ; i<=numberOfVehicle ; i++)
                        in.putExtra("vehicle"+i,vehicleInfo[i].getText().toString());
                    startActivity(in);
                }

            }
        });
        return v;
    }

    private void showOnButtonsClick(View v, String s, int tmp)
    {
        relout.setVisibility(View.VISIBLE);
        for(int i=1 ; i<=5 ; i++)
            info[i].setVisibility(View.VISIBLE);

        info_label_textview.setText("You have selected "+s+" mode:");
        for(int i=tmp+1 ; i<=5 ; i++)
        {
            info[i].setVisibility(View.INVISIBLE);
        }
    }
    private void hideAll(View view)
    {
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);

        ed1.setEnabled(false);
        ed2.setEnabled(false);
        ed3.setEnabled(false);

        ed1.setHint("0");
        ed2.setHint("0");
        ed3.setHint("0");

        ed1.setText("");
        ed2.setText("");
        ed3.setText("");

        dbtn1.setVisibility(View.INVISIBLE);
        dbtn2.setVisibility(View.INVISIBLE);
        dbtn3.setVisibility(View.INVISIBLE);

        relout.setVisibility(View.INVISIBLE);
        error_field.setVisibility(View.INVISIBLE);
    }

}
