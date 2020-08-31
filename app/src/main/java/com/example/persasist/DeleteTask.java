package com.example.persasist;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//class extending the Broadcast Receiver
public class DeleteTask extends BroadcastReceiver {
    private InputDbHelper mHelper;

    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);

        int minutes = calendar.get(Calendar.MINUTE);


        if (intent.getStringExtra("admin")!=null&&intent.getStringExtra("admin").equals("admin")){
            Log.i("message", "delete msg");
            String message = "Deleting all reminders for the day..!";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            mHelper = new InputDbHelper(context);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.execSQL("delete from remindersAutoSend");
    }
        else {
            Log.i("message", "alarm");

            Intent intent2 = new Intent(context, MainActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
//        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification not = new Notification();
//        nm.notify();
            String channelId = "channel-id";
            String channelName = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                mNotificationManager.createNotificationChannel(mChannel);
            }
            String message = "Reminder for you...";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context,channelId)
                            .setSmallIcon(R.mipmap.nudger_round)
                            .setContentTitle("Your Reminder !")
                            .setContentText(intent.getStringExtra("message"));
            mBuilder.setContentIntent(pi);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);

            mNotificationManager.notify(createID(), mBuilder.build());

//            try {
//                String mobile = "+919489739539";
//               String msg = "Its Working";
////                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg)));
//                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg);
//                Intent sendIntent = new Intent(Intent.ACTION_SEND, uri);
////                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
////                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.whatsapp");
//                context.startActivity(sendIntent);
//                Toast.makeText(context, "whatsapp message sent", Toast.LENGTH_SHORT).show();




            try {
//                Uri uri = Uri.parse("smsto:" + "+919865617256");
//                Intent i = new Intent(Intent.ACTION_SENDTO);
//               // String url = "https://api.whatsapp.com/send?phone="+ "+919865617256" +"&text=" + URLEncoder.encode(message, "UTF-8");
//                i.putExtra(Intent.EXTRA_TEXT, "This is my text to send."); i.setType("text/plain");
//                i.setPackage("com.whatsapp");
//               // i.setData(Uri.parse(url));
//
//                    context.startActivity(i);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(intent.getStringExtra("mobile"), null, intent.getStringExtra("message"), null, null);

                Toast.makeText(context.getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();

            }catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }



        }

        //you can check the log that it is fired
        //Here we are actually not doing anything Log.d("deleteTask", "Deleting reminders just fired");
        //but you can do any task here that you want to be done at a specific time everyday
//        MediaPlayer mediaPlayer= MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
    }
    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(now));
        return id;
    }
}