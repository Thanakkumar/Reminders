package com.example.persasist;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

//class extending the Broadcast Receiver
public class DeleteTask extends BroadcastReceiver {
    private InputDbHelper mHelper;

    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);

        int minutes = calendar.get(Calendar.MINUTE);
        if (hourNow==0&&minutes==0){
            mHelper = new InputDbHelper(context);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from tasks");
    }
        Log.i("message", "alarm");
        String message = "Alarm firing...";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
//        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification not = new Notification();
//        nm.notify();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.persist_time)
                        .setContentTitle("Your Reminder !")
                        .setContentText(intent.getStringExtra("message"));
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
        //you can check the log that it is fired
        //Here we are actually not doing anything Log.d("deleteTask", "Deleting reminders just fired");
        //but you can do any task here that you want to be done at a specific time everyday
//        MediaPlayer mediaPlayer= MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
    }

}