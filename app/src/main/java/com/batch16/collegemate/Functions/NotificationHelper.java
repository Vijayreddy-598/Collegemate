package com.batch16.collegemate.Functions;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.batch16.collegemate.MainActivity;
import com.batch16.collegemate.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String Title, String text) {
        Context ctx=getApplicationContext();
        return new NotificationCompat.Builder(ctx, channelID)
                .setContentTitle(Title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_perm_identity_black_24dp)
                .setContentIntent(new NavDeepLinkBuilder(ctx).setComponentName(MainActivity.class).setGraph(R.navigation.mobile_navigation).setDestination(R.id.navigation_map).createPendingIntent());
    }
}
