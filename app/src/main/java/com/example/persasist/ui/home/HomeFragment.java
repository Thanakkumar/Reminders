package com.example.persasist.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.persasist.MyListAdapter;
import com.example.persasist.R;
import com.example.persasist.Reminders;
import com.example.persasist.ui.dashboard.DashboardViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    ArrayList<Reminders> remindersList = new ArrayList<Reminders>();
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            String reminder = bundle.getString("desc");
            String time = bundle.getString("time");
            remindersList.add(new Reminders("kumar  has added the value"));
            remindersList.add(new Reminders(reminder+" "+time));
        }
            // View rootDashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);

//
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
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


}