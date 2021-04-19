package com.example.gpstracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {
FirebaseAuth auth;
EditText e1,e2;
ProgressDialog dialog;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e1=(EditText)findViewById(R.id.editText);
        e2=(EditText)findViewById(R.id.editText2);

        auth=FirebaseAuth.getInstance();

    }


    public void login(View v)
    {
        auth.signInWithEmailAndPassword(e1.getText().toString(),e2.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //Toast.makeText(getApplicationContext(),"User Logged in Succesfully",Toast.LENGTH_LONG).show();
                            FirebaseUser user = auth.getCurrentUser();
                            if(user.isEmailVerified()) {
                               Intent myIntent = new Intent(LoginActivity.this, MyNavigationActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Email is not verified yet",Toast.LENGTH_LONG).show();

                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Wrong email or Password",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void forgotPassword(View v)
    {
        Intent myIntent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(myIntent);
        finish();
    }
}