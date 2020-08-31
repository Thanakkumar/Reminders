package com.example.persasist.ui.dashboard;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    ArrayList<Reminders> remindersList = new ArrayList<Reminders>();
    private InputDbHelper mHelper;
    HomeFragment home = new HomeFragment();
    private static int  checkFlag = 0;
    private DashboardViewModel dashboardViewModel;
    Button bt;
    EditText eText;
    EditText eTextSearch;
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


        if (minutes > 45) {
            tp.setCurrentHour(hour + 1);
            tp.setCurrentMinute(minutes - 45);
        } else {
            tp.setCurrentMinute(minutes + 15);
        }

        bt = root.findViewById(R.id.button1);
        eText = root.findViewById(R.id.editText);
        eTextSearch = root.findViewById(R.id.editTextSearch);
//        eTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Log.i("phone number", "test phone ");
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    performSearch(eTextSearch.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
        bt.setOnClickListener(this);

//                   dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//
//            }
//        });
//

        return root;
    }

    @Override
    public void onClick(View v) {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Nudger's Message");
        alertDialog.setMessage("Are you sure that the details are correct? Click YES to proceed and NO to stop");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mHelper = new InputDbHelper(getActivity().getBaseContext());
                        String hour = String.valueOf(tp.getCurrentHour());
                        String mins = String.valueOf(tp.getCurrentMinute());
                        String timeRemind = hour + ":" + mins;
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        Calendar calendar = Calendar.getInstance();

                        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);

                        int minutes = calendar.get(Calendar.MINUTE);

                        Log.i("text", "minutes" + hourNow);
                        if ((tp.getCurrentHour() < hourNow || tp.getCurrentHour() == hourNow && tp.getCurrentMinute() <= (minutes + 14))) {
                            Toast.makeText(getActivity(), "Please set reminder for future time at least after 15 min of current time",
                                    Toast.LENGTH_SHORT).show();

                        }


                        else {
                            if(minutes==0){
                                minutes=60;
                            }
                            if (minutes > 45) {
                                minutes = minutes - 45;
                            }

                            if (eText.getText().toString().length() >= 3&&eTextSearch.getText().toString().length()>10) {

                                if (tp.getCurrentHour()+1 ==hourNow && tp.getCurrentMinute() < minutes) {

                                    Toast.makeText(getActivity(), "Please set reminder for future time at least after 15 min of current time",
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    String backStateName = home.getClass().getName();
//                    String input =  eText.getText().toString()+ " at " + timeRemind;
                                    String input =  eText.getText().toString();

                                    ContentValues values = new ContentValues();
                                    values.put(InputContract.TaskEntry.REM_VALUE, eText.getText().toString());
                                    values.put(InputContract.TaskEntry.REM_TIME,timeRemind);
                                    values.put(InputContract.TaskEntry.MOBILE_NO,eTextSearch.getText().toString());
                                    Cursor cursorInit =  db.rawQuery("select * from remindersAutoSend where time like ?",new String[]{"%"+tp.getCurrentHour()+":"+tp.getCurrentMinute()+"%"});
                                    int initCount = cursorInit.getCount();
                                    if(initCount>0){
                                        Toast.makeText(getActivity(), "Cant set,Already a reminder present at this time",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Long insVal = db.insertWithOnConflict(InputContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                                        if (insVal != 0L) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                                            builder.setMessage("Reminder added!")
                                                    .setTitle("Done");
                                            setAlarm(hour, mins, input,eTextSearch.getText().toString());

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            checkFlag = checkFlag + 1;

                                        }
                                        Cursor cursor = db.rawQuery("SELECT  * FROM " + InputContract.TaskEntry.TABLE, null);
                                        int count = cursor.getCount();
                                        if (count > 0 && checkFlag == 1) {
                                            setAlarm("00", "00", "All Reminders are deleted.You can set new Reminders!!",null);
                                        }
                                        db.close();
                                        Bundle bundle = new Bundle();


                                        remindersList = home.updateUI(getActivity());

                                        //remindersList.add(new Reminders(eText.getText().toString()+" "+timeRemind));
                                        bundle.putSerializable("reminders", remindersList);

//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
                                        home.setArguments(bundle);
                                        getFragmentManager().popBackStackImmediate();
                                    }
                                }

                            } else {
                                if(eText.getText().toString().length() >= 3)
                                Toast.makeText(getActivity(), "Please type atleast 3 letters what to remind",
                                        Toast.LENGTH_SHORT).show();
                                if(eTextSearch.getText().toString().length()<10)
                                    Toast.makeText(getActivity(), "Please check the Mobile number entered",
                                            Toast.LENGTH_SHORT).show();

                            }


                        }
                    }

                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
        alertDialog.show();



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


    public void setAlarm(String hour, String mins, String message,String mobile) {

        //System request code
        Log.i("text2", "hour::" + hour + " mins:: " + mins);
        int DATA_FETCHER_RC = 123;
        //Create an alarm manager
        AlarmManager mAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        int hourNum = Integer.parseInt(hour);
        int minNum = Integer.parseInt(mins);
        Intent intent = new Intent(getContext(), DeleteTask.class);
        //Create the time of day you would like it to go off. Use a calendar
        Calendar calendar = Calendar.getInstance();
        if (hourNum == 00 && minNum == 00) {

            intent.putExtra("admin", "admin");
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        else {

            calendar.set(Calendar.HOUR_OF_DAY, hourNum);
            calendar.set(Calendar.MINUTE, Integer.parseInt(mins) - 14);
            calendar.set(Calendar.MILLISECOND, 0);
        }


        //Create an intent that points to the receiver. The system will notify the app about the current time, and send a broadcast to the app

        intent.putExtra("message", message);
        intent.putExtra("mobile", mobile);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), new Random().nextInt(1000), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //initialize the alarm by using inexactrepeating. This allows the system to scheduler your alarm at the most efficient time around your
        //set time, it is usually a few seconds off your requested time.
        // you can also use setExact however this is not recommended. Use this only if it must be done then.
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            Log.i("test","version 19");
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            Log.i("test","version other");
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        //Also set the interval using the AlarmManager constants



    }

//    public void performSearch(String contactNumber){
//        Log.i("inside", "perform search");
//        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactNumber));
//        Cursor cursor = getActivity().getContentResolver().query(
//                uri,
//                new String[] { ContactsContract.Contacts.PHOTO_ID,
//                        ContactsContract.Contacts.DISPLAY_NAME,
//                        ContactsContract.Contacts._ID },
//                ContactsContract.Contacts.HAS_PHONE_NUMBER, null,
//                ContactsContract.Contacts.DISPLAY_NAME);
//        List<String> contactNumbers = new ArrayList<String>();
//        while (cursor.moveToNext()) {
//            contactNumbers.add(cursor.getString(0));
//            Log.i("phone number", "text phone : "+cursor.getString(0));
//        }
//    }
}