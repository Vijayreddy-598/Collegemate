package com.batch16.collegemate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.batch16.collegemate.Functions.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA ="Value";
    MediaPlayer mediaPlayer;
    public static final String ACTION_LOCATION_BROADCAST = AlarmReceiver.class.getName() + "LocationBroadcast";
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Alarm","Alarm Working Fine buddy");
        notificationHelper.getManager().notify(1, nb.build());

        //mediaPlayer=MediaPlayer.create(context,R.raw.song);
        //mediaPlayer.start();

        Log.i("Jay", "onReceive: ");
        Intent inte = new Intent(ACTION_LOCATION_BROADCAST);
        //intent.putExtra(EXTRA, 1);
        LocalBroadcastManager.getInstance(context).sendBroadcast(inte);


    }

}
