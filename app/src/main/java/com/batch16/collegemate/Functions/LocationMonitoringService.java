package com.batch16.collegemate.Functions;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.batch16.collegemate.MainActivity;
import com.batch16.collegemate.R;
import com.google.android.gms.location.LocationListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.batch16.collegemate.ui.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LocationMonitoringService extends Service  implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        {
    private static final String TAG = "Collegemate";
    public static  String EXTRA_LATITUDE = "";
    public static  String EXTRA_LONGITUDE="";
    String lat,lon;
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    DatabaseReference myRef;
    String Name;
    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(5*60*1000);
        mLocationRequest.setFastestInterval(1*60*1000);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default

        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes

        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        myRef = FirebaseDatabase.getInstance().getReference();

        Name=MainActivity.sp.getString("UserName",null);
        Log.i(TAG, "onLocationStartCommand: "+Name);

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }
    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");

        if (location != null) {

            Log.d(TAG, "== location != null");
            lat=String.valueOf(location.getLatitude());
            lon= String.valueOf(location.getLongitude());

            //Update to local Shared Prefs
            SharedPreferences sp=getSharedPreferences("UserDetails",MODE_PRIVATE);
            SharedPreferences.Editor se=sp.edit();
            se.putString("UserLat",lat);
            se.putString("UserLon",lon);
            se.apply();

            //Update to Firebase
            LatLongModel latLongModel=new LatLongModel(location.getLatitude(),location.getLatitude(),Name);
            myRef= FirebaseDatabase.getInstance().getReference();
            myRef.child("Users").child(Name).setValue(latLongModel);

            //Local Broadcast Intent
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_LATITUDE, lat);
            intent.putExtra(EXTRA_LONGITUDE, lon);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");
    }
}