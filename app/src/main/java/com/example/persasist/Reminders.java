package com.example.persasist;

import android.widget.TimePicker;

public class  Reminders {


    private String description;
    private String time;
    private String mobileNo;


    public Reminders(String description ,String time,String mobileNo) {
        this.description = description;
        this.time=time;
        this.mobileNo=mobileNo;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}