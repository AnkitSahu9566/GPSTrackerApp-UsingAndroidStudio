package com.example.gpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ForgotPasswordActivity extends AppCompatActivity {
EditText e1,e2,e3;
FirebaseAuth auth;
FirebaseUser user;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        e1=(EditText)findViewById(R.id.editText);
        e2=(EditText)findViewById(R.id.editText2);
        e3=(EditText)findViewById(R.id.editText3);

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    public void onSubmit(View v)
    {
            auth.sendPasswordResetEmail(e1.getText().toString()).addOnCompleteListener(new
        OnCompleteListener<Void>()
         {
             @Override
             public void onComplete(@NonNull Task<Void> task)
             {
                 Toast.makeText(getApplicationContext(),"Successuflly Mail Sent.Check it!",Toast.LENGTH_LONG).show();

             }


         }

         );


    }
}
    //

      //
