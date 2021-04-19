package com.example.gpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends AppCompatActivity {
EditText e1_email;
FirebaseAuth auth;
ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e1_email=(EditText)findViewById(R.id.editText4);
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
    }
    public void goToPasswordActivity(View v)
    {
        dialog.setMessage("Checking email address");
        // check email is already registered or not
        auth.fetchSignInMethodsForEmail(e1_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.isSuccessful())
                        {
                            dialog.dismiss();
                            boolean check =!task.getResult().getSignInMethods().isEmpty();
                            if(!check)
                            {
                                //email does not exist,so create this email
                                Intent myIntent=new Intent(RegisterActivity.this,PasswordActivity.class);
                                myIntent.putExtra("email",e1_email.getText().toString());
                                startActivity(myIntent);
                                finish();

                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "this email already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}