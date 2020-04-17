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
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.batch16.collegemate.AlarmReceiver;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.batch16.collegemate.MainActivity.REQUEST_PERMISSIONS_REQUEST_CODE;;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    public static GoogleMap map;
    View root;
    static Marker user;
    DatabaseReference myRef;
    Activity activity;
    Location A, B;
    //User Details
    String name;
    Double d1, d2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);
        activity = getActivity();
        myRef = FirebaseDatabase.getInstance().getReference();

        //Get latitude and longitude set by LocationMonitoringService
        d1 = Double.parseDouble(Objects.requireNonNull(MainActivity.sp.getString("UserLat", "0.00")));
        d2 = Double.parseDouble(Objects.requireNonNull(MainActivity.sp.getString("UserLon", "0.00")));
        name = MainActivity.sp.getString("UserName", null);

        A = new Location("User");

        //MapView Intialization returns Control to onMapReady
        mapView = root.findViewById(R.id.mapViewComponent);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        //Add markers on Nearby Friends
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Marker> mark = new ArrayList<>();
                A.setLatitude(d1);
                A.setLongitude(d2);
                //Toast.makeText(activity, "Name"+name+"\nLati:"+d1+"\nLongi:"+d2, Toast.LENGTH_SHORT).show();
                updateMap(A.getLatitude(), A.getLongitude());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String Uname = ds.child("name").getValue(String.class);
                    if (!name.equals(Uname)) {
                        Double lat = ds.child("latitude").getValue(Double.class);
                        Double lon = ds.child("longitude").getValue(Double.class);
                        B = new Location("Friend");
                        B.setLongitude(lon);
                        B.setLatitude(lat);
                        float dis = A.distanceTo(B);
                        double kmdis = dis / 1000;
                        if (kmdis <= 1.00) {
                            mark.add(map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(Uname)));
                            //Toast.makeText(getActivity(), ""+Uname, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        if(latitude != null && longitude != null){
                            updateMap(Double.parseDouble(latitude),Double.parseDouble(longitude));
                        }
                    }
                }, new IntentFilter(AlarmReceiver.ACTION_LOCATION_BROADCAST)
        );




        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

    }


    public void updateMap(double lat, double lon) {
        LatLng newLoc=new LatLng(lat,lon);
        Marker user=map.addMarker(new MarkerOptions().position(newLoc).title("Your Location"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 15));

    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                root.findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }
}