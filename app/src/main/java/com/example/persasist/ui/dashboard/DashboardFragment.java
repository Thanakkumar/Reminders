package com.example.persasist.ui.dashboard;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.persasist.InputContract;
import com.example.persasist.InputDbHelper;
import com.example.persasist.R;
import com.example.persasist.Reminders;
import com.example.persasist.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    ArrayList<Reminders> remindersList = new ArrayList<Reminders>();
    private InputDbHelper mHelper;
    HomeFragment home = new HomeFragment();

    private DashboardViewModel dashboardViewModel;
    Button bt ;
    EditText eText;
    TimePicker tp;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        tp = (TimePicker) root.findViewById(R.id.timePicker1);
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
    public void onClick(View v)
    {mHelper=  new InputDbHelper(getActivity().getBaseContext());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        if((hour24hrs+":"+minutes).equals("00:00")){
            db.execSQL("delete from tasks");
        }
        if(tp!=null && eText!=null) {
        String hour = String.valueOf(tp.getCurrentHour());
        String mins = String.valueOf(tp.getCurrentMinute());
        String timeRemind = hour + ":" + mins;

        String backStateName = home.getClass().getName();
        String input = eText.getText().toString()+"         "+timeRemind;

        ContentValues values = new ContentValues();
        values.put(InputContract.TaskEntry.REM_VALUE,   input);

       //
        Long insVal = db.insertWithOnConflict(InputContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        if(insVal!=0L){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Reminder added!")
                    .setTitle("Done");

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        Bundle bundle = new Bundle();


        remindersList = home.updateUI(getActivity());
        //remindersList.add(new Reminders(eText.getText().toString()+" "+timeRemind));
        bundle.putSerializable("reminders",remindersList);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        home.setArguments(bundle);
       // home.onSaveInstanceState(bundle);
        //ft.replace(R.id.frameHome, home).addToBackStack(null).commit();
       // fm.popBackStack();
//        if (fm.getBackStackEntryCount() > 0){
//            boolean done = fm.popBackStackImmediate();
//            ft.commit();
//        }




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

}