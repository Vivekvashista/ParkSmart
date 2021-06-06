package com.codearena.parksmart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codearena.parksmart.Model.Users;
import com.codearena.parksmart.TrackUser.ReturningUser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private TextView sampleName, editImage;
    private EditText username, useremail, usermob;
    private ImageView backbtn;
    private CircleImageView prev_image;
    private Uri imageuri;
    private Button savebtn;
    private StorageReference stref;
    private String myurl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sampleName = (TextView)findViewById(R.id.sample_name_id);
        username = (EditText)findViewById(R.id.user_name_id);
        useremail = (EditText)findViewById(R.id.user_email_id);
        usermob = (EditText)findViewById(R.id.user_mob_id);
        backbtn = (ImageView)findViewById(R.id.close_btn);
        editImage = (TextView)findViewById(R.id.edit_profile_image_id);
        prev_image = (CircleImageView)findViewById(R.id.profile_image_id);
        savebtn = (Button)findViewById(R.id.save_btn_id);

        stref = FirebaseStorage.getInstance().getReference().child("Profile-Picture");

        usermob.setEnabled(false);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(AccountActivity.this,UserHomeActivity.class);
                startActivity(in);
                finish();
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(imageuri)
                        .setAspectRatio(1,1)
                        .start(AccountActivity.this);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
            }
        });

        showUserInfo();
    }

    private void updateUserData()
    {
        if(TextUtils.isEmpty(username.getText().toString()))
        {
            Toast.makeText(this, "Name is empty!", Toast.LENGTH_SHORT).show();
        }
        else
        if(TextUtils.isEmpty(useremail.getText().toString()))
        {
            Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
        }
        else
        if(imageuri!=null)
        {
            updateFullData();
        }
        else
        {
            updateOnlyInfo();
        }
    }

    private void updateFullData()
    {
        final ProgressDialog loadingbar = new ProgressDialog(this);
        loadingbar.setTitle("Updating");
        loadingbar.setMessage("Please wait...");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        final StorageReference storage = stref.child(ReturningUser.currentonlineuser.getMobile_no()+".jpg");

        StorageTask upload = storage.putFile(imageuri);

        upload.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful())
                    throw task.getException();

                return storage.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if(task.isSuccessful())
                {
                    Uri downloadable = task.getResult();
                    myurl = downloadable.toString();

                    DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users");

                    HashMap<String,Object> hp = new HashMap<>();
                    hp.put("name",username.getText().toString().trim());
                    hp.put("image",myurl);
                    hp.put("email_id",useremail.getText().toString().trim());

                    dataref.child(ReturningUser.currentonlineuser.getMobile_no()).updateChildren(hp);
                    loadingbar.dismiss();
                    startActivity(new Intent(AccountActivity.this,AccountActivity.class));
                    Toast.makeText(AccountActivity.this, "Profile Updated Successfully!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    loadingbar.dismiss();
                    Toast.makeText(AccountActivity.this, "Something went wrong! try again...", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingbar.dismiss();
                Toast.makeText(AccountActivity.this, "Something went wrong! try again...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateOnlyInfo()
    {
        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users");
        HashMap<String,Object> hp = new HashMap<>();
        hp.put("name",username.getText().toString().trim());
        hp.put("email_id",useremail.getText().toString().trim());

        dataref.child(ReturningUser.currentonlineuser.getMobile_no()).updateChildren(hp);
        startActivity(new Intent(AccountActivity.this,AccountActivity.class));
        Toast.makeText(AccountActivity.this, "Profile Updated Successfully!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showUserInfo()
    {
        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("users");
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userdata = dataSnapshot.child(ReturningUser.currentonlineuser.getMobile_no()).getValue(Users.class);
                ReturningUser.currentonlineuser = userdata;
                sampleName.setText(userdata.getName());
                username.setText(userdata.getName());
                useremail.setText(userdata.getEmail_id());
                usermob.setText(userdata.getMobile_no());
                Picasso.get().load(userdata.getImage()).placeholder(R.drawable.user_profile_icon).into(prev_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();
            prev_image.setImageURI(imageuri);
        }
        else
        {
            Toast.makeText(this, "Something went wrong! Please try again...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(this,UserHomeActivity.class);
        startActivity(in);
        finish();
        super.onBackPressed();
    }
}
