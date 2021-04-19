package com.example.gpstracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Intent.ACTION_GET_CONTENT;

public class NameActivity extends AppCompatActivity {
    String email,password;
    EditText e5_name;
    ImageView imageView;

    Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Intent myIntent=getIntent();
        e5_name=(EditText)findViewById(R.id.editText5);
        imageView=(ImageView)findViewById(R.id.imageViewProfile);
        if(myIntent!=null)
        {
            email=myIntent.getStringExtra("email");
            password=myIntent.getStringExtra("password");
        }
    }

    public void genrateCode(View v)
    {
        Date myDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm::ss a", Locale.getDefault());
        String date =format1.format(myDate);
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        String code = String.valueOf(n);

        if(resultUri!=null)
        {
            Intent myIntent = new Intent(NameActivity.this,InvideCodeActivity.class);
            myIntent.putExtra("name",e5_name.getText().toString());
            myIntent.putExtra("email",email);
            myIntent.putExtra("password",password);
            myIntent.putExtra("date",date);
            myIntent.putExtra("isSharing","false");
            myIntent.putExtra("code",code);
            myIntent.putExtra("imageUri", resultUri);

            startActivity(myIntent);
            //finish();


        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please choose an Image",Toast.LENGTH_SHORT).show();
        }
    }

    public void selectImage(View v)
    {
        Intent i = new Intent();
        i.setAction(ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12 && resultCode == RESULT_OK && data!=null)
        {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 resultUri = result.getUri();
                 imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
/*
CropImage.activity()
        .setGuidelines(CropImageView.Guidelines.ON)
        .start(this);

// start cropping activity for pre-acquired image saved on the device
        CropImage.activity(imageUri)
        .start(this);

// for fragment (DO NOT use `getActivity()`)
        CropImage.activity()
        .start(getContext(), this);*/
