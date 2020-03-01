package com.example.persasist;

import android.widget.TimePicker;

public class  Reminders {


    private String description;
    private TimePicker time;


    public Reminders(String description ) {
        this.description = description;

    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}