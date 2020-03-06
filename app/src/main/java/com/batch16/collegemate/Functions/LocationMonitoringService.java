package com.batch16.collegemate.Functions;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.batch16.collegemate.ui.MapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationMonitoringService extends Service implements LocationListener {
    private LocationManager locationManager;
    DatabaseReference myRef;


    public LocationMonitoringService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5,  this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //myRef=FirebaseDatabase.getInstance().getReference();
        /*if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }*/
        getLocation();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onLocationChanged(Location location) {
        MapFragment mp=new MapFragment();
        mp.locationText.setText("\tLatitude:"+location.getLatitude()+"\n\tLongitude:"+location.getLongitude());
        mp.currentLocation=location;
        LatLongModel lat=new LatLongModel(location.getLatitude(),location.getLongitude(),"Vijay",MapFragment.count);
        MapFragment.count++;
        MapFragment.updateMap();
        //myRef.child("Users").child("ClassName").child("UserID").setValue(lat);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}