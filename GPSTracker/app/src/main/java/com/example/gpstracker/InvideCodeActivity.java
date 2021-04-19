package com.example.gpstracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InvideCodeActivity extends AppCompatActivity {

    String name ,email,password,date,isSharing,code,userid;


    ProgressDialog progressDialog;
    Uri imageUri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;
    DatabaseReference reference;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invide_code);

        t1= (TextView)findViewById(R.id.textView);
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        Intent myIntent = getIntent();


        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");


        if(myIntent!=null)
        {
            name = myIntent.getStringExtra("name");
            email=myIntent.getStringExtra("email");
            password=myIntent.getStringExtra("password");
            code=myIntent.getStringExtra("code");
            isSharing=myIntent.getStringExtra("isSharing");
            imageUri=myIntent.getParcelableExtra("imageUri");
        }
        t1.setText(code);
    }



    public void registerUser(View v)
    {
        progressDialog.setMessage("Please wait while we are creating an account for you");
        progressDialog.show();




        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //insert values in Realtime Database

                            user = auth.getCurrentUser();
                            CreateUser createUser = new CreateUser(name,email,password,code,"false","na","na","na",user.getUid());

                            userId = user.getUid();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                               //save images to firebase storage
                                               StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                                               sr.putFile(imageUri)
                                                       .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                                                           {
                                                               if(task.isSuccessful())
                                                               {
                                                                   String download_image_path = task.getResult().getUploadSessionUri().toString();

                                                                   reference.child(user.getUid()).child("imageUri").setValue(download_image_path)
                                                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull Task<Void> task)
                                                                               {
                                                                               if(task.isSuccessful())
                                                                               { progressDialog.dismiss();
                                                                                  // Toast.makeText(getApplicationContext(),"Email sent for verification. Check mail",Toast.LENGTH_SHORT).show();
                                                                                   //for verification
                                                                                   sendVerificationEmail();
                                                                                   Intent myIntent = new Intent(InvideCodeActivity.this,MainActivity.class);
                                                                                   startActivity(myIntent);
                                                                                   finish();
                                                                               }
                                                                               else {}
                                                                               }
                                                                           });
                                                                   
                                                               }
                                                               
                                                           }
                                                       });

                                                //auth.signOut();
                                              //  finish();


                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext()," could not register users ",Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                    }
                });
    }
    public void sendVerificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                       if(task.isSuccessful()) {
                           Toast.makeText(getApplicationContext(), "Email sent for verification", Toast.LENGTH_SHORT).show();

                           auth.signOut();
                           finish();
                       }else{
                           Toast.makeText(getApplicationContext(),"Could not send email",Toast.LENGTH_SHORT).show();
                       }

                    }
                });
    }


}
