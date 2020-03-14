package com.batch16.collegemate.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.batch16.collegemate.BuildConfig;
import com.batch16.collegemate.Functions.LatLongModel;
import com.batch16.collegemate.Functions.LocationMonitoringService;
import com.batch16.collegemate.MainActivity;
import com.batch16.collegemate.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MapFragment extends Fragment implements  OnMapReadyCallback {
    MapView mapView;
    public static GoogleMap map;
    View root;
    static Marker user;
    DatabaseReference myRef;
    Activity activity;
    String name;
    Double lat=0.0;
    Double lon=0.0;
    private TextView mMsgView;
    LatLongModel Umod,mod;
    SharedPreferences sp;
    Location A,B;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);
        activity = getActivity();
        myRef = FirebaseDatabase.getInstance().getReference();
        sp = this.getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        name=sp.getString("UserName",null);
        //Toast.makeText(getContext(), ""+name, Toast.LENGTH_SHORT).show();
        A=new Location("User");
        //mMsgView = root.findViewById(R.id.textview);
        //MapView Intialization returns Control to onMapReady

        mapView = root.findViewById(R.id.mapViewComponent);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                map.clear();
                List<Marker> mark= new ArrayList<>();
                Double d1=dataSnapshot.child(name).child("latitude").getValue(Double.class);
                Double d2=dataSnapshot.child(name).child("longitude").getValue(Double.class);
                String username=dataSnapshot.child(name).child("name").getValue(String.class);
                A.setLatitude(d1);
                A.setLongitude(d2);
                //Toast.makeText(activity, "Name"+name+"\nLati:"+d1+"\nLongi:"+d2, Toast.LENGTH_SHORT).show();
                updateMap(A.getLatitude(),A.getLongitude());
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String Uname=ds.child("name").getValue(String.class);
                    if(!username.equals(Uname)){
                        Double lat= ds.child("latitude").getValue(Double.class);
                        Double lon= ds.child("longitude").getValue(Double.class);
                        B=new Location("Friend");
                        B.setLongitude(lon);
                        B.setLatitude(lat);
                        float dis=A.distanceTo(B);
                        double kmdis=dis/1000;
                        if(kmdis<=1.00) {
                            mark.add(map.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(Uname)));
                            //Toast.makeText(getActivity(), ""+Uname, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    public static void updateMap(double lat, double lon) {

        user = map.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Your Location"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));

    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }
}