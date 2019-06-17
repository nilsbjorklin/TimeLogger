package com.time_logger;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogPost implements Serializable {
    private String id;
    private String date;
    private StringBuilder description = null;
    private Category category = Category.OTHER;
    private int hours = 0, minutes = 0;

    public LogPost(String arg) {
        String[] args = arg.split(",");
        id(args[0]);
        date(args[1]);
        hours(args[2]);
        minutes(args[3]);
        category(args[4]);
        for (int i = 5; i < args.length; i++) {
            description(args[i]);
        }
    }

    public LogPost() {
        id(Long.toHexString(System.currentTimeMillis() / 1000));
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        date = format.format(localDateTime);
    }

    public void description(String word) {
        if (description == null) {
            description = new StringBuilder(word);
        } else {
            description.append(" " + word);
        }
    }

    public void id(String idString) {
        this.id = idString;
    }

    public void date(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(dateString);
            date = dateString;
        } catch (ParseException e) {
            System.err.println("Invalid date: " + date);
        }
    }

    public void hours(String hoursString) {
        try {
            this.hours += Integer.parseInt(hoursString);
        } catch (Exception exception) {
            System.err.printf("%s is not a valid integer.%n", hoursString);
        }
    }

    public void category(String categoryString) {
        try {
            category = Category.valueOf(categoryString.toUpperCase());
        } catch (Exception exception) {
            System.err.printf("%s is not a valid Category.%n", categoryString);
        }
    }

    public void minutes(String minutesString) {
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

    public String getDescription() {
        if (description != null) {
            return description.toString();
        } else {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getCategory() {
        return category.name();
    }

    public String getEvent() {
        return String.format("[%s] %s, (%s) %d hours and %d minutes, description: %s", getId(), getDate(), getCategory(), getHours(), getMinutes(), getDescription());
    }

    public String getDate() {
        return date;
    }

    public String eventAdded() {
        return String.format("Added event(%s) %d hours and %d minutes to date %s for category %s.", getId(), getHours(), getMinutes(), getDate(), getCategory());
    }

    public String eventChanged() {
        return String.format("Change event(%s) to %d hours and %d minutes to date %s for category %s.", getId(), getHours(), getMinutes(), getDate(), getCategory());
    }

    @Override
    public String toString() {
        return String.format("%s, %s,%s,%s,%s,%s", getId(), getDate(), getHours(), getMinutes(), getCategory(), getDescription());
    }

    enum Category {
        INCIDENT,
        MICROSERVICE,
        MIGRATION,
        OTHER
    }
}