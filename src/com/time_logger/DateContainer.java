package com.time_logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateContainer {
    private String date;
    private String fileSuffix;

    public DateContainer() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        setDate(format.format(localDateTime));
    }

    public String getDate() {
        return date;
    }

    public void setDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat suffixFormat = new SimpleDateFormat("yyyy-MM");
        try {
            Date dateObject = dateFormat.parse(dateString);
            date = dateFormat.format(dateObject);
            fileSuffix = suffixFormat.format(dateObject);
        } catch (ParseException e) {
            System.err.println("Invalid date: " + date);
        }
    }

    public void setSuffix(String suffixString) {
        SimpleDateFormat suffixFormat = new SimpleDateFormat("yyyy-MM");
        try {
            fileSuffix = suffixFormat.format(suffixFormat.parse(suffixString));
        } catch (ParseException e) {
            System.err.println("Invalid date: " + date);
        }
    }

    public String getFileSuffix() {
        return fileSuffix;
    }
}
