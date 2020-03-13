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

    private TextView mMsgView;
    LatLongModel model;
    SharedPreferences sp;
    Location A;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);
        activity = getActivity();
        myRef = FirebaseDatabase.getInstance().getReference();
        sp = this.getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);

       // mMsgView = root.findViewById(R.id.textview);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        //String name=sp.getString("UserDetails",null);
                        if (latitude != null && longitude != null) {
                            Double lat=Double.parseDouble(latitude);
                            Double lon=Double.parseDouble(longitude);
                            updateMap(lat,lon);
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        //MapView Intialization returns Control to onMapReady

        mapView = root.findViewById(R.id.mapViewComponent);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Double lat=Double.parseDouble(sp.getString("UserLat",""));
        Double lon=Double.parseDouble(sp.getString("UserLon",""));
        LatLng latLng = new LatLng(lat, lon);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        user = map.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
    }


    public static void updateMap(double lat, double lon) {
        user.setPosition(new LatLng(lat, lon));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
        user.setVisible(true);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }
}