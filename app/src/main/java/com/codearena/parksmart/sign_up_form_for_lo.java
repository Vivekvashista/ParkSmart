package com.codearena.parksmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up_form_for_lo extends AppCompatActivity {

    private ImageView backbtn, preview_field;
    private EditText lo_name, lo_mobile, lo_email, lo_address, lo_password, lo_cpassword, lo_adhar, lo_length, lo_breadth, previewerror, lo_registration;
    private Uri ImageUri;
    private Button lo_submitbtn, lo_cancelbtn, lo_loadimage;
    final static int GalleryCode=1;
    private ProgressDialog loadingbar;
    private String DownloadImageUrl;
    private String otp="";
    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_form_for_lo);

        lo_name = (EditText)findViewById(R.id.lo_name_id);
        lo_email= (EditText)findViewById(R.id.lo_email_id);
        lo_mobile= (EditText)findViewById(R.id.lo_mob_id);
        lo_address = (EditText)findViewById(R.id.lo_add_id);
        lo_password= (EditText)findViewById(R.id.lo_pass_id);
        lo_cpassword = (EditText)findViewById(R.id.lo_cpass_id);
        lo_adhar= (EditText)findViewById(R.id.lo_adhar_id);
        lo_registration = (EditText)findViewById(R.id.land_registration_id);
        lo_length = (EditText)findViewById(R.id.lo_land_length_id);
        lo_breadth= (EditText)findViewById(R.id.lo_land_breadth_id);
        previewerror= (EditText)findViewById(R.id.preview_error_id);
        preview_field = (ImageView)findViewById(R.id.land_Image_Preview);
        lo_submitbtn = (Button)findViewById(R.id.lo_submit_id);
        //lo_cancelbtn = (Button)findViewById(R.id.lo_cancel_id);
        lo_loadimage = (Button)findViewById(R.id.Land_Image);


        label = (TextView)findViewById(R.id.header);
        AssetManager as = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(as,"fonts/JosefinSans-Regular.ttf");
        label.setTypeface(typeface);

        loadingbar = new ProgressDialog(this);
        otp= new DecimalFormat("000000").format(new Random().nextInt(999999));

        backbtn = (ImageView)findViewById(R.id.back_btn_lo);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lo_loadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        lo_submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromForm();
            }
        });
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void getDataFromForm()
    {
        String name = lo_name.getText().toString().trim();
        String email = lo_email.getText().toString().trim();
        String mobile = lo_mobile.getText().toString();
        String address = lo_address.getText().toString().trim();
        String password = lo_password.getText().toString().trim();
        String cpassword = lo_cpassword.getText().toString().trim();
        String adhar = lo_adhar.getText().toString();
        String landRegistration = lo_registration.getText().toString().trim();
        String landlength = lo_length.getText().toString();
        String landbreadth = lo_breadth.getText().toString();

        if(TextUtils.isEmpty(name)) {
            lo_name.setError("Please enter your name!");
        }
        else
        if(TextUtils.isEmpty(email)) {
            lo_email.setError("Please enter your email-id!");
        }
        else
        if(!isValid(email)){
            lo_email.setError("Invalid email-id!");
        }
        else
        if(TextUtils.isEmpty(mobile) || mobile.length()!=10) {
            lo_mobile.setError("Please enter your Mobile Number!");
        }
        else
        if(TextUtils.isEmpty(address)) {
            lo_address.setError("Please enter your Address!");
        }
        else
        if(address.length()<15) {
            lo_address.setError("Please enter full Address!");
        }
        else
        if(TextUtils.isEmpty(password)){
            lo_password.setError("Please enter your password!");
        }
        else
        if(!isValidPassword(password)) {
            lo_password.setError("Password must be of atleast 8 characters and should contains uppercase and lowercase as well as special character");
        }
        else
        if(TextUtils.isEmpty(cpassword)) {
            lo_cpassword.setError("Please confirm password!");
        }
        else
        if(TextUtils.isEmpty(adhar) || adhar.length()!=12) {
            lo_adhar.setError("Please enter valid Adhar number!");
        }
        else
        if(TextUtils.isEmpty(landRegistration)){
            lo_registration.setError("Land Registration is mandatory!");
        }
        else
        if(TextUtils.isEmpty(landlength)) {
            lo_length.setError("Please enter land length!");
        }
        else
        if(TextUtils.isEmpty(landbreadth)) {
            lo_breadth.setError("Please enter land breadth!");
        }
        else
        if(!password.equals(cpassword)) {
            lo_name.setError("Password do not match!");
        }
        else
        if(ImageUri==null)
        {
            previewerror.setError("Land photo is mandatory!");
        }
        else
        {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait, while we are registering...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            getImageUrl(name,mobile,email,address,password,adhar,landRegistration,landlength,landbreadth);
        }

    }

    private void getImageUrl(final String name, final String mobile, final String email, final String address, final String password, final String adhar, final String landRegistration, final String landlength, final String landbreadth)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        String ImageRandomKey = saveCurrentDate+" "+saveCurrentTime;

        final StorageReference storeRef;
        storeRef = FirebaseStorage.getInstance().getReference().child("land-images").child(ImageUri.getLastPathSegment()+ImageRandomKey+".jpg");

        final UploadTask uploadTask = storeRef.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sign_up_form_for_lo.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw  task.getException();
                        }
                        DownloadImageUrl = storeRef.getDownloadUrl().toString();
                        return storeRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            DownloadImageUrl = task.getResult().toString();

                            saveLandOwnerData(mobile,email,name,password,address,adhar,landRegistration,landlength,landbreadth);
                        }
                    }
                });
            }
        });


    }

    private void saveLandOwnerData(final String mobile, final String email, final String name, final String password, final String address, final String adhar, final String landRegistration, final String landlength, final String landbreadth)
    {
        final DatabaseReference dref;
        dref = FirebaseDatabase.getInstance().getReference().child("landowner");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mobile).exists()) {
                    Toast.makeText(sign_up_form_for_lo.this, "Already exist an account with this mobile number!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent in = new Intent(sign_up_form_for_lo.this, OtpVerificationActivity.class);
                    in.putExtra("mode", "landowner");
                    in.putExtra("EmailIntent", email);
                    in.putExtra("name", name);
                    in.putExtra("mobile-no", mobile);
                    in.putExtra("password", password);
                    in.putExtra("otp", otp);
                    in.putExtra("DownloadIm ageUrl", DownloadImageUrl);
                    in.putExtra("adhar", adhar);
                    in.putExtra("landRegistrationNumber", landRegistration);
                    in.putExtra("address", address);
                    in.putExtra("landlength", landbreadth);
                    in.putExtra("landbreadth", landlength);
                    startActivity(in);
                    finish();
                }
                loadingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openGallery()
    {
        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in,GalleryCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryCode && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            preview_field.setImageURI(ImageUri);
        }
    }
}
