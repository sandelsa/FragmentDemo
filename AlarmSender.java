package com.example.springroll.database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by SpringRoll on 4/30/2016.
 */
public class AlarmSender {
    private Context c;
    private PendingIntent pend;
    private AlarmManager am;


    public AlarmSender(Context con, String eventName, int hr, int min){
        c = con;
       am = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);


        Intent i = new Intent(c, AlarmReceiver.class);
        i.putExtra("com.example.springroll.database.name", eventName);
        i.putExtra("com.example.springroll.database.hrs", ""+hr);
        i.putExtra("com.example.springroll.database.mins", ""+min);

        pend = PendingIntent.getBroadcast(c, (int)System.currentTimeMillis(), i, PendingIntent.FLAG_ONE_SHOT);
    }

    public void setalarm(int hr, int min, Calendar ca){
        Calendar curr = Calendar.getInstance();
        Calendar diff = Calendar.getInstance();

        diff.add(Calendar.HOUR, ca.get(Calendar.HOUR)-curr.get(Calendar.HOUR));
        diff.add(Calendar.MINUTE, ca.get(Calendar.MINUTE)-curr.get(Calendar.MINUTE));


        diff.add(Calendar.MINUTE, -min);
        diff.add(Calendar.HOUR, -hr);
        long time = diff.getTimeInMillis();
        //long minusMin = min * 60 * 1000;
       // long minusHr = hr * 60 * 60 * 1000;
        //time = time - minusHr - minusMin;

        am.set(AlarmManager.RTC_WAKEUP, time, pend);
    }

    public void cancelAlarm(){
        Intent i = new Intent(c, AlarmReceiver.class);
        i.putExtra("com.example.springroll.database.name", "");
        i.putExtra("com.example.springroll.database.hrs", "");
        i.putExtra("com.example.springroll.database.mins", "");

        pend = PendingIntent.getBroadcast(c, 0, i, 0);

        am.cancel(pend);
        //pend.cancel();
    }
}
