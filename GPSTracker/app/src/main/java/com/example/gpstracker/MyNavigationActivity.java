package com.example.gpstracker;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MyNavigationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    //MyNavigationActivity

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth auth;
    GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLng;
    LatLng latLng_circle;
    DatabaseReference databaseReference,databaseReference2;
    StorageReference storageReference;
    FirebaseUser user;
    String current_user_name;
    String current_user_email;
    String current_user_image;
    String current_user_invite_code;
    String current_circle_member_id;
    double current_user_lat;
    double current_user_lang;
    TextView t1_currentName , t2_current_email, t3_current_user_invite_code;
    String circle_member_lat,circle_member_lang,circle_member_name;
   boolean b_value=false;


    ImageView i1;

//for lat lang


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_signOut)
                .setDrawerLayout(drawer)
                .build();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);
        t1_currentName = header.findViewById(R.id.title_text);
        t2_current_email = header.findViewById(R.id.email_text);
        t3_current_user_invite_code =header.findViewById(R.id.invite_code3);
        i1 = header.findViewById(R.id.imageViewProfile);


        //get data from users firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_user_name = snapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = snapshot.child(user.getUid()).child("email").getValue(String.class);

                current_user_image = snapshot.child(user.getUid()).child("imageUri").getValue(String.class);
                current_user_invite_code = snapshot.child(user.getUid()).child("code").getValue(String.class);

                t1_currentName.setText(current_user_name);
                t2_current_email.setText(current_user_email);
                t3_current_user_invite_code.setText(current_user_invite_code);
                //Remaining work for upload pic on nav_header
                //Toast.makeText(getApplicationContext(),current_user_image,Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),"lat="+current_user_lat+"lang="+current_user_lang,Toast.LENGTH_LONG).show();
               // Toast.makeText(getApplicationContext(), "location updated",Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(),current_circle_member_id,Toast.LENGTH_LONG).show();
                Picasso.get().load(current_user_image).placeholder(R.drawable.defaultprofile).error(R.drawable.defaultprofile).into(i1);
//for circle member

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_signOut) {
                    auth.signOut();
                    Intent intent1 = new Intent(MyNavigationActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else if (id == R.id.nav_joinCircle) {
                    Intent intent1 = new Intent(MyNavigationActivity.this,JoinCircleActivity.class);
                    startActivity(intent1);


                } else if (id == R.id.nav_joinedCircle) {

                    //joined means sent a invite code to friends

                    Intent i1 = new Intent(Intent.ACTION_SEND);
                    i1.setType("text/plain");
                    i1.putExtra(Intent.EXTRA_TEXT,"My Invite Code is: "+current_user_invite_code+"   Go To the the GPS TrackerApp->SignIn->Join Circle (Option). Enter the Invite(Circle) Code and Click on Submit");
                    startActivity(i1.createChooser(i1,"Share using:"));

                } else if (id == R.id.nav_myCircle) {
                    Intent intent1 = new Intent(MyNavigationActivity.this,MyCircleAcyivity.class);
                    startActivity(intent1);
                   // finish();

                } else if (id == R.id.nav_shareLocation) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT,"My Loaction is: "+"https://www.google.com/maps/@ "+latLng.latitude+","+latLng.longitude+",17z");
                    startActivity(i.createChooser(i,"Share using:"));

                    //finish();


                } else if (id == R.id.nav_contactUs) {


                  //  get_circle_member();
                } else if (id == R.id.nav_friend_view) {
                    //track root
                    Intent intent = new Intent(MyNavigationActivity.this,SourceDestinationActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_inviteMember) {

                    //sent a invitation link to other to install the app

                   // Intent i1 = new Intent(MyNavigationActivity.this,MyInviteActivity.class);
                    //startActivity(i1);


                }
                return false;
            }
        });

       }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_navigation, menu);
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }
    //share icon in page

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share)
        {
            ApplicationInfo  api = getApplicationContext().getApplicationInfo();
            String apkpath = api.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/vnd.android.package-archive");
            intent.putExtra(intent.EXTRA_STREAM,Uri.fromFile(new File(apkpath)));
            startActivity(Intent.createChooser(intent,"ShareVia"));
        }
        if(id == R.id.search)
        {
            Intent intent = new Intent(MyNavigationActivity.this,SourceDestinationActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in India and move the camera
        //20.5937° N, 78.9629° E
        /*LatLng india = new LatLng(20.5937, 78.9629);
        mMap.addMarker(new MarkerOptions().position(india).title("Marker in India"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));*/

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //1 call..
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        request.setInterval(3000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client,request, this);

    }


    //end of on Connected
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //here the onconnected method
    @Override
    public void onLocationChanged(Location location) {
        //2 call...
        if(location == null)
        {
            Toast.makeText(getApplicationContext(),"Could not get Loaction",Toast.LENGTH_SHORT).show();
        }
        else
        {
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            current_user_lat = location.getLatitude();
            current_user_lang = location.getLongitude();

//update value of lat lang in fire base
            String s=""+current_user_lat;
            String s2=""+current_user_lang;
            databaseReference.child(user.getUid()).child("lat").setValue(s);
            databaseReference.child(user.getUid()).child("lng").setValue(s2);


            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.title("Current location");
            mMap.clear();
            mMap.addMarker(options);


        }


//end of onlocationchange

        }

    }



