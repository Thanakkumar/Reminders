package com.example.persasist.ui.dashboard;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.persasist.R;
import com.example.persasist.Reminders;
import com.example.persasist.ui.home.HomeFragment;

public class DashboardFragment extends Fragment implements View.OnClickListener {

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
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

            }
        });

        return root;
    }
    @Override
    public void onClick(View v)
    {if(tp!=null&&eText!=null) {
        String hour = String.valueOf(tp.getCurrentHour());
        String mins = String.valueOf(tp.getCurrentMinute());
        String timeRemind = hour + ":" + mins;
        HomeFragment home = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("desc", eText.getText().toString());
        bundle.putString("time", timeRemind);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        home.setArguments(bundle);
        ft.replace(R.id.nav_host_fragment, home);
        ft.commit();
    }


    }
}