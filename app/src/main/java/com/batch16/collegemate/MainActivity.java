package com.batch16.collegemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

import static com.batch16.collegemate.ui.MapFragment.map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SharedPreferences sp;
    DatabaseReference myRef;
    String name;
    Location A,B;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 /*       A=
        myRef= FirebaseDatabase.getInstance().getReference();*/
       //SetUserName
        sp=getSharedPreferences("UserDetails",MODE_PRIVATE);
        if(!sp.contains("UserName")){
            mAuth= FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
            String mail=user.getEmail();
            mail=mail.substring(0,mail.length()-10);
            Toast.makeText(this, "\n"+mail+" \n"+!sp.contains("UserName"), Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor se=sp.edit();
            se.putString("UserName",mail);
            se.apply();
        }else{
            name=sp.getString("UserName","");
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        //Actionbar Code
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_map, R.id.navigation_calender,R.id.navigation_settings).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Marker> mark= new ArrayList<>();
                map.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    int i=0;
                    if(!ds.child("name").equals(name)){
                        Double lat= ds.child("latitude").getValue(Double.class);
                        Double lon= ds.child("longitude").getValue(Double.class);
                        String Uname=ds.child("name").getValue(String.class);
                        B=new Location("Friend");
                        B.setLongitude(lat);
                        B.setLatitude(lon);
                        float dis=A.distanceTo(B);
                        float kmdis=dis/1000;
                        if(kmdis<=1.00) {
                            mark.add(map.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(Uname)));
                        }

                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
