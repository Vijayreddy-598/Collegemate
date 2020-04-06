package com.batch16.collegemate.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.batch16.collegemate.Functions.HomeNotificationAdapter;
import com.batch16.collegemate.MainActivity;
import com.batch16.collegemate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {
    RecyclerView rv;
    Context ctx;
    List<String> title,subti;
    DatabaseReference myRef;
    Button dndaccess;
    CardView cardView;
    FirebaseDatabase fb;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ctx=getContext();
        rv=root.findViewById(R.id.recycler);
        dndaccess=root.findViewById(R.id.dndaccess);
        cardView=root.findViewById(R.id.dndrequest);
        RecyclerView.LayoutManager lay= new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,true);
        rv.setLayoutManager(lay);

        title=new ArrayList<>();
        subti=new ArrayList<>();
        title.add("No Notifications available");
        subti.add("Check your internet connection , we are trying to get you latest content");
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> title1= new ArrayList<>();
                List<String> subti1=new ArrayList<>();
                title1.add("End");
                subti1.add("No more Notifications");
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String t=ds.child("Head").getValue(String.class);
                    String s=ds.child("Body").getValue(String.class);
                    title1.add(t);
                    subti1.add(s);
                }
                rv.setAdapter(new HomeNotificationAdapter(getContext(),title1,subti1));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dndaccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !MainActivity.notificationManager.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    Toast.makeText(MainActivity.ctx, "Find Collegemate and Grant permission", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }
        });


        HomeNotificationAdapter adapter=new HomeNotificationAdapter(getContext(),title,subti);
        rv.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        //DND Settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !MainActivity.notificationManager.isNotificationPolicyAccessGranted()) {
            cardView.setVisibility(View.VISIBLE);
        }else
        {
            cardView.setVisibility(View.GONE);
        }
        super.onResume();
    }
}
