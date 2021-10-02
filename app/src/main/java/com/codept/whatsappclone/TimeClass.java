package com.codept.whatsappclone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeClass {
    Long timestamp;
    String time;


    public TimeClass(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getTime() {
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("HH:mm");
//        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        this.time=format.format(date);

        return time;
    }






}
