package com.example.persasist.ui.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.persasist.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private InputDbHelper mHelper;
    ArrayList<Reminders> remindersList = new ArrayList<>();
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    HomeFragment home = new HomeFragment();
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_null);
//        if(remindersList.isEmpty()) {
//            notificationsViewModel.getText().observe(this, new Observer<String>() {
//                @Override
//                public void onChanged(@Nullable String s) {
//                    textView.setText(s);
//                }
//            });
//        }
        remindersList =updateNotifications(getActivity());
        if(remindersList.isEmpty()){
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Nudger's Message");
            alertDialog.setMessage("No Active Reminders for this hour");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                        }

                    });
            alertDialog.show();
        }
        recyclerView = (RecyclerView)root.findViewById(R.id.reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyListAdapter(remindersList);
        recyclerView.setAdapter(mAdapter);
        return root;
    }
    public  ArrayList<Reminders> updateNotifications(Activity activity) {
        ArrayAdapter adapter=null;
        ArrayList<Reminders> taskList = new ArrayList<>();

            mHelper=  new InputDbHelper(getActivity().getBaseContext());

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        String hour = String.valueOf(hour24hrs);
        Cursor cursor = db.rawQuery("select * from remindersAutoSend where time like ?",new String[]{"%"+hour+":%"});
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(InputContract.TaskEntry.REM_VALUE);
            int idxTime = cursor.getColumnIndex(InputContract.TaskEntry.REM_TIME);
            int idxMobile = cursor.getColumnIndex(InputContract.TaskEntry.MOBILE_NO);
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