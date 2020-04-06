package com.batch16.collegemate;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.batch16.collegemate.Functions.LatLongModel;
import com.batch16.collegemate.Functions.MyDB;
import com.batch16.collegemate.Functions.NotificationHelper;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    public static  boolean checkNear = false;
    DatabaseReference myRef;
    long min,max;
    Location A,B;
    public static final String ACTION_LOCATION_BROADCAST = AlarmReceiver.class.getName() + "LocationBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        //For Checking
        NotificationHelper notificationHelper = new NotificationHelper(context);
        Calendar cal=Calendar.getInstance();
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Alarm","Alarm Working Fine at "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND));
        notificationHelper.getManager().notify(1, nb.build());
        Log.i("Collegemate", "on Alarm Receive: ");
        //Check time if it is between 9.30-10.30
        cal.set(Calendar.HOUR,9);
        cal.set(Calendar.MINUTE,30);
        min=cal.getTimeInMillis();

        cal.set(Calendar.HOUR,10);
        cal.set(Calendar.MINUTE,30);
        max=cal.getTimeInMillis();
        if(min <= System.currentTimeMillis() && System.currentTimeMillis() <= max){
            if(A != null){
                B.setLatitude(0.0);
                B.setLongitude(0.0);
                float dis=A.distanceTo(B);
                double kmdis=dis/1000;
                MyDB my=new MyDB(context);
                ContentValues cv=new ContentValues();
                cv.put(my.A_COL_2,cal.get(Calendar.DATE));
                cv.put(my.A_COL_3,cal.get(Calendar.MONTH));
                if(kmdis<=1.00) {
                    cv.put(my.A_COL_4,"$Present");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !MainActivity.notificationManager.isNotificationPolicyAccessGranted()) {
                        MainActivity.myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                }else{
                    cv.put(my.A_COL_4,"$Absent");
                    checkNear=true;
                }
                my.insertAttendance(cv);
            }
        }else {
            checkNear=true;
        }

        /*Why written??
        Intent inte = new Intent(ACTION_LOCATION_BROADCAST);
        //intent.putExtra(EXTRA, 1);
        LocalBroadcastManager.getInstance(context).sendBroadcast(inte);*/

        //Get Location of User set by LocationMonitoringService
        Double lat=Double.parseDouble(MainActivity.sp.getString("UserLat","0.00"));
        Double lon=Double.parseDouble(MainActivity.sp.getString("UserLon","0.00"));
        String Name=MainActivity.sp.getString("UserName",null);
        //Update userlocation to Firebase
        Toast.makeText(context, "Location Updating", Toast.LENGTH_SHORT).show();
        LatLongModel latLongModel=new LatLongModel(lat,lon,Name);
        myRef= FirebaseDatabase.getInstance().getReference();
        myRef.child("Users").child(Name).setValue(latLongModel);
        if (checkNear){
            notifynearby(Name,lat,lon,context);
        }

    }

    //Show Notification if anyone is nearby
    public void notifynearby(final String userName, Double lat, Double lon, final Context context){
        A=new Location("User");
        A.setLatitude(lat);
        A.setLongitude(lon);
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Marker> mark= new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String Uname=ds.child("name").getValue(String.class);
                    if(!userName.equals(Uname)){
                        Double lat= ds.child("latitude").getValue(Double.class);
                        Double lon= ds.child("longitude").getValue(Double.class);
                        B=new Location("Friend");
                        B.setLongitude(lon);
                        B.setLatitude(lat);
                        float dis=A.distanceTo(B);
                        double kmdis=dis/1000;
                        if(kmdis<=1.00) {

                            NotificationHelper notificationHelper=new NotificationHelper(context);
                            NotificationCompat.Builder nb=notificationHelper.getChannelNotification("Friend Popup","Your buddy "+Uname+" is around you");
                            notificationHelper.getManager().notify(1,nb.build());

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
