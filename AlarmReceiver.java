package com.example.springroll.database;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by SpringRoll on 4/30/2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        String evname = intent.getStringExtra("com.example.springroll.database.name");
        String hrsBefore = intent.getStringExtra("com.example.springroll.database.hrs");
        String minsBefore = intent.getStringExtra("com.example.springroll.database.mins");

        NotificationManager nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        PendingIntent p = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class),0);

        Notification n = new Notification.Builder(context)
                .setContentTitle(evname + " coming up!")
                .setContentText("Event in " + hrsBefore + " hours and " + minsBefore + " minutes!")
                .setSmallIcon(R.mipmap.splash_icon)
                .setContentIntent(p)
                .build();

        n.defaults |= Notification.DEFAULT_SOUND;

        int notId = (int)Calendar.getInstance().getTimeInMillis();

        nm.notify(notId,n);
    }
}
