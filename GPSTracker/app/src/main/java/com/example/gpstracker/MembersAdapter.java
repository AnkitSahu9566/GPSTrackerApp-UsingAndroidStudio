package com.example.gpstracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {
    Context c;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    String current_user_name;
    ArrayList<CreateUser> namelist;
    MembersAdapter(ArrayList<CreateUser> namelist, Context c)
    {
        this.namelist = namelist;
        this.c = c;

    }
    MembersAdapter()
    {

    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         @SuppressLint("ResourceType") View v = LayoutInflater.from(parent.getContext()).inflate(R.id.card_layout,parent,false);

        MembersViewHolder membersViewHolder = new MembersViewHolder(v,c,namelist);
        return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        CreateUser currentUserObj = namelist.get(position);
        holder.name_txt.setText(currentUserObj.name);
        //for image
        Picasso.get().load(currentUserObj.imageUri).placeholder(R.drawable.defaultprofile).into(holder.circleImageView);

    }

    public  static  class MembersViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView name_txt;
        CircleImageView circleImageView;

        Context c;
        ArrayList<CreateUser> nameArrayList;
        FirebaseAuth auth;
        FirebaseUser user;


        public MembersViewHolder(@NonNull View itemView,  Context c, ArrayList<CreateUser> nameArrayList) {
            super(itemView);
            this.c = c;
            this.nameArrayList = nameArrayList;
            itemView.setOnClickListener(this);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            name_txt = itemView.findViewById(R.id.title_text);
            circleImageView = itemView.findViewById(R.id.circle_image_view);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(c,"You have clicked this user",Toast.LENGTH_LONG).show();
        }
    }

    }
