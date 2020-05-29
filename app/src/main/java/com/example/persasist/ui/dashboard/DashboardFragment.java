package com.example.persasist.ui.dashboard;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.persasist.DeleteTask;
import com.example.persasist.InputContract;
import com.example.persasist.InputDbHelper;
import com.example.persasist.R;
import com.example.persasist.Reminders;
import com.example.persasist.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    ArrayList<Reminders> remindersList = new ArrayList<Reminders>();
    private InputDbHelper mHelper;
    HomeFragment home = new HomeFragment();
boolean checkFlag = false;
    private DashboardViewModel dashboardViewModel;
    Button bt;
    EditText eText;
    TimePicker tp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        tp = (TimePicker) root.findViewById(R.id.timePicker1);
        tp.setIs24HourView(true);
        Calendar calendar = Calendar.getInstance();

        int minutes = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //if()
        //db.execSQL("delete from tasks");

        if (minutes > 45) {
            tp.setCurrentHour(hour + 1);
            tp.setCurrentMinute(minutes - 45);
        } else {
            tp.setCurrentMinute(minutes + 15);
        }

        bt = root.findViewById(R.id.button1);
        eText = root.findViewById(R.id.editText);
        bt.setOnClickListener(this);
//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//
//            }
//        });
        return root;
    }

    @Override
    public void onClick(View v) {
        mHelper = new InputDbHelper(getActivity().getBaseContext());
        String hour = String.valueOf(tp.getCurrentHour());
        String mins = String.valueOf(tp.getCurrentMinute());
        String timeRemind = hour + ":" + mins;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);

        int minutes = calendar.get(Calendar.MINUTE);
        //db.execSQL("delete from tasks");
        Log.i("text", "minutes" + hourNow);
        if ((tp.getCurrentHour() < hourNow || tp.getCurrentHour() == hourNow && tp.getCurrentMinute() <= (minutes + 14))) {
            Toast.makeText(getActivity(), "Please set reminder for future time at least after 15 min of current time",
                    Toast.LENGTH_SHORT).show();

        } else {
            if (minutes > 45) {
                minutes = minutes - 45;
            }
            if (eText.getText().toString().length() > 0) {

                if (tp.getCurrentHour() > hourNow && tp.getCurrentMinute() < minutes) {

                    Toast.makeText(getActivity(), "Please set reminder for future time at least after 15 min of current time",
                            Toast.LENGTH_SHORT).show();
                } else {

                    String backStateName = home.getClass().getName();
                    String input = eText.getText().toString() + " at " + timeRemind;

                    ContentValues values = new ContentValues();
                    values.put(InputContract.TaskEntry.REM_VALUE, input);

                    //
                    Long insVal = db.insertWithOnConflict(InputContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    if (insVal != 0L) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("Reminder added!")
                                .setTitle("Done");
                        setAlarm(hour, mins, input);

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        checkFlag = true;
                    }
                    Cursor cursor = db.rawQuery("SELECT  * FROM "+InputContract.TaskEntry.TABLE, null);
                    int count = cursor.getCount();
                    if(count>0&&checkFlag){
                        setAlarm("00", "00", input);
                    }
                    cursor.close();
                    Bundle bundle = new Bundle();


                    remindersList = home.updateUI(getActivity());
                    //remindersList.add(new Reminders(eText.getText().toString()+" "+timeRemind));
                    bundle.putSerializable("reminders", remindersList);

//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
                    home.setArguments(bundle);
                    getFragmentManager().popBackStackImmediate();

                }

            } else {
                Toast.makeText(getActivity(), "Please type what to remind",
                        Toast.LENGTH_SHORT).show();

            }


        }


    }
//    public  ArrayList<Reminders> updateUI() {
//        ArrayAdapter adapter=null;
//        ArrayList<Reminders> taskList = new ArrayList<>();
//        mHelper=  new InputDbHelper(getActivity().getBaseContext());
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(InputContract.TaskEntry.TABLE,
//                new String[]{InputContract.TaskEntry._ID, InputContract.TaskEntry.REM_VALUE},
//                null, null, null, null, null);
//        while (cursor.moveToNext()) {
//            int idx = cursor.getColumnIndex(InputContract.TaskEntry.REM_VALUE);
//            taskList.add(new Reminders(cursor.getString(idx)));
//        }
//
////        if (adapter== null) {
////            adapter= new ArrayAdapter<>(this,  android.R.layout.simple_expandable_list_item_1,
////                    taskList);
////            lv.setAdapter(adapter);
////        } else {
////            adapter.clear();
////            adapter.addAll(taskList);
////            adapter.notifyDataSetChanged();
////        }
//
//        cursor.close();
//        db.close();
//        return taskList;
//    }


    public void setAlarm(String hour, String mins, String message) {

        //System request code
        Log.i("text2", "hour::" + hour + " mins:: " + mins);
        int DATA_FETCHER_RC = 123;
        //Create an alarm manager
        AlarmManager mAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        int hourNum = Integer.parseInt(hour);
        int minNum = Integer.parseInt(mins);

        //Create the time of day you would like it to go off. Use a calendar
        Calendar calendar = Calendar.getInstance();
        if(hourNum==0 && minNum==0){
            calendar.set(Calendar.HOUR_OF_DAY, hourNum);
            calendar.set(Calendar.MINUTE, Integer.parseInt(mins));
            calendar.set(Calendar.MILLISECOND, 0);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourNum);
        calendar.set(Calendar.MINUTE, Integer.parseInt(mins) - 14);
        calendar.set(Calendar.MILLISECOND, 0);


        //Create an intent that points to the receiver. The system will notify the app about the current time, and send a broadcast to the app
        Intent intent = new Intent(getContext(), DeleteTask.class);
        intent.putExtra("message",  message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), new Random().nextInt(1000), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //initialize the alarm by using inexactrepeating. This allows the system to scheduler your alarm at the most efficient time around your
        //set time, it is usually a few seconds off your requested time.
        // you can also use setExact however this is not recommended. Use this only if it must be done then.

        //Also set the interval using the AlarmManager constants
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


    }
}