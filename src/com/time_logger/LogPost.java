package com.time_logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class LogPost {
    enum Category{
        INCIDENT,
        MICROSERVICE,
        MIGRATION,
        OTHER
    }
    private String date;
    private StringBuilder description = null;
    private Category category = Category.OTHER;
    private int hours = 0, minutes = 0;

    LogPost(String arg) {
        String[] args = arg.split(",");
        date(args[0]);
        hours(args[1]);
        minutes(args[2]);
        category(args[3]);
        for (int i = 4; i < args.length; i++) {
            description(args[i]);
        }
    }

    LogPost() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        date = format.format(localDateTime);
    }

    void description(String word){
        if(description == null)
            description = new StringBuilder(word);
        else{
            description.append(" " + word);
        }
    }
    void date(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(dateString);
            date = dateString;
        } catch (ParseException e) {
            System.err.println("Invalid date: " + date);
        }
    }

    void hours(String hoursString) {
        try {
            this.hours += Integer.parseInt(hoursString);
        } catch (Exception exception) {
            System.err.printf("%s is not a valid integer.%n", hoursString);
        }
    }

    void category(String categoryString){
        try {
            category = Category.valueOf(categoryString.toUpperCase());
        } catch (Exception exception) {
            System.err.printf("%s is not a valid Category.%n", categoryString);
        }
    }

    void minutes(String minutesString) {
        try {
            this.minutes += Integer.parseInt(minutesString);
            if (this.minutes > 59) {
                this.hours += this.minutes / 60;
                this.minutes = this.minutes % 60;
            }
        } catch (Exception exception) {
            System.err.printf("%s is not a valid integer.%n", minutesString);
        }
    }

    String getDescription(){
        return description.toString();
    }

    int getHours() {
        return hours;
    }

    int getMinutes() {
        return minutes;
    }

    String getCategory() {
        return category.name();
    }

    String getEvent() {
        return String.format("%s, (%s) %d hours and %d minutes, description: %s.", getDate(), getCategory(), getHours(), getMinutes(), getDescription());
    }

    String getDate() {
        return date;
    }

    String eventAdded() {
        return String.format("Added %d hours and %d minutes to date %s for category %s.", getHours(), getMinutes(), getDate(), getCategory());
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", getDate(), getHours(), getMinutes(), getCategory(), getDescription());
    }
}