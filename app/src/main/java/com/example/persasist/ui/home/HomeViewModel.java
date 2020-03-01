package com.example.persasist.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.persasist.Reminders;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Reminders>> sessionsLiveData = new MutableLiveData<List<Reminders>>();
   // ArrayList<Reminders> remindersList = new ArrayList<Reminders>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        //sessionsLiveData.setValue(remindersList);
    }

    public LiveData<String> getText() {
        return mText;
    }
//    public LiveData<List<Reminders>> getReminders(){
//        return sessionsLiveData;
//    }
}