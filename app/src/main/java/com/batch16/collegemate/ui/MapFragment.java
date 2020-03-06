package com.batch16.collegemate.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.batch16.collegemate.Functions.LocationMonitoringService;
import com.batch16.collegemate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements  OnMapReadyCallback{
    public static int count=0;
    MapView mapView;
    public static GoogleMap map;
    public static Location  currentLocation;
    public static TextView locationText;
    View root;
    static Marker user;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);
        getActivity().startService(new Intent(getActivity(), LocationMonitoringService.class));
        locationText=root.findViewById(R.id.textview);
        mapView=root.findViewById(R.id.mapViewComponent);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;
    }
        @Override
        public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
    }
        public static void updateMap(){
        map.clear();
        LatLng latLng=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        user=map.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
    }

   @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
}
