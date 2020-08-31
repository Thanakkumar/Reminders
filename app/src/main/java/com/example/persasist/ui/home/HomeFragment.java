package com.example.persasist.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.persasist.InputContract;
import com.example.persasist.InputDbHelper;
import com.example.persasist.MyListAdapter;
import com.example.persasist.R;
import com.example.persasist.Reminders;
import com.example.persasist.ui.dashboard.DashboardFragment;
import com.example.persasist.ui.dashboard.DashboardViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private InputDbHelper mHelper;
    ArrayList<Reminders> remindersList = new ArrayList<>();
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
//        if(remindersList.size()==0){
//            DashboardFragment df = new DashboardFragment();
//            remindersList =   df.updateUI();
//
//
//        }
        remindersList = updateUI(getActivity());
        if(remindersList.isEmpty()){
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Nudger's Message");
            alertDialog.setMessage("No Reminders,Please set your Reminder in Set a Reminder option");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
            alertDialog.show();
        }
        if(bundle!=null) {
           remindersList = (ArrayList<Reminders>)bundle.getSerializable("reminders");



//            remindersList.add(new Reminders("kumar  has added the value"));
//            remindersList.add(new Reminders("Thana  has added the value"));


        }
            // View rootDashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);

//        final TextView textView = root.findViewById(R.id.text_null);
//        if(remindersList.isEmpty()){
//            homeViewModel.getText().observe(this, new Observer<String>() {
//                @Override
//                public void onChanged(@Nullable String s) {
//                    textView.setText(s);
//                }
//            });
//
//        }
            // MyListAdapter adapter = new MyListAdapter(remindersList);




            recyclerView = (RecyclerView)root.findViewById(R.id.reminders);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new MyListAdapter(remindersList);
            recyclerView.setAdapter(mAdapter);


        return root;
    }


//    public View onCreateViewView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View rootView=inflater.inflate(R.layout.fragment_home,container, false);
//        final TextView textView = rootView.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//                textView.setText(s);
//            }
//        });
////        MyListAdapter adapter = new MyListAdapter(remindersList);
//
////        recyclerView = (RecyclerView)rootView.findViewById(R.id.reminders);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////        recyclerView.setItemAnimator(new DefaultItemAnimator());
////        mAdapter = new MyListAdapter(remindersList);
////        recyclerView.setAdapter(mAdapter);
//        //REFERENCE
////        rv= (RecyclerView) rootView.findViewById(R.id.text_home);
////
////        //LAYOUT MANAGER
////        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
////
////        //ADAPTER
////        rv.setAdapter(new MyAdapter(getActivity(),spacecrafts));
//
//        return rootView;
//    }

    public  ArrayList<Reminders> updateUI(Activity activity) {
        ArrayAdapter adapter=null;
        ArrayList<Reminders> taskList = new ArrayList<>();
        if(getActivity()!=null)
        mHelper=  new InputDbHelper(getActivity().getBaseContext());
        else
            mHelper=  new InputDbHelper(activity.getBaseContext());
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(InputContract.TaskEntry.TABLE,
                new String[]{InputContract.TaskEntry._ID, InputContract.TaskEntry.REM_VALUE,InputContract.TaskEntry.REM_TIME,InputContract.TaskEntry.MOBILE_NO},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(InputContract.TaskEntry.REM_VALUE);
            int idxTime = cursor.getColumnIndex(InputContract.TaskEntry.REM_TIME);
            int idxMobile= cursor.getColumnIndex(InputContract.TaskEntry.MOBILE_NO);

            taskList.add(new Reminders(cursor.getString(idx),cursor.getString(idxTime),cursor.getString(idxMobile)));
        }

//        if (adapter== null) {
//            adapter= new ArrayAdapter<>(this,  android.R.layout.simple_expandable_list_item_1,
//                    taskList);
//            lv.setAdapter(adapter);
//        } else {
//            adapter.clear();
//            adapter.addAll(taskList);
//            adapter.notifyDataSetChanged();
//        }

        cursor.close();
        db.close();
        return taskList;
    }

}