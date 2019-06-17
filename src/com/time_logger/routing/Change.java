package com.time_logger.routing;

import com.time_logger.Properties;
import com.time_logger.actions.ChangeLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Change extends Argument {

    private Properties.Encoding encoding = Properties.Encoding.OBJECT;
    private ChangeLog changeLog = new ChangeLog();

    public Change() {
        super("change", "changes event");
        options.add(new Id());
        options.add(new Date());
        options.add(new Hours());
        options.add(new Minutes());
        options.add(new Category());
        options.add(new Description());
    }

    @Override
    void invoke() {
        super.invoke();
        try {
            changeLog.change();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private class Id extends Option {

        Consumer<String[]> consumer = arr -> {
            changeLog.properties()
                    .setId(arr[0]);
        };

        Id() {
            super("id", "Id for event.", "-id", "-i");
            setConsumer(consumer);
            setRequired(true);
            setRegex(".*");
        }
    }

    private class Date extends Option {

        Consumer<String[]> consumer = arr -> {
            changeLog.properties()
                    .setDate(arr[0]);
        };

        Date() {
            super("date", "Date for event.", "-date", "-time");
            setConsumer(consumer);
            setRegex("\\d{4}-\\d{2}-\\d{2}");
        }
    }

    private class Hours extends Option {

        Consumer<String[]> consumer = arr -> {
            changeLog.properties()
                    .setHours(arr[0]);
        };

        Hours() {
            super("hours", "Amount of hours worked.", "-hours", "-h");
            setConsumer(consumer);
            setRegex("\\d+");
        }
    }

    private class Minutes extends Option {

        Consumer<String[]> consumer = arr -> {
            changeLog.properties()
                    .setMinutes(arr[0]);
        };

        Minutes() {
            super("minutes", "Amount of minutes worked.", "-minutes", "-m");
            setConsumer(consumer);
            setRegex("\\d{1,2}");
        }
    }

    private class Category extends Option {

        Consumer<String[]> consumer = arr -> {
            changeLog.properties()
                    .setCategory(arr[0]);
        };

        Category() {
            super("category", "Category of work.", "-category", "-c");
            setConsumer(consumer);
        }
    }

    private class Description extends Option {

        Consumer<String[]> consumer = arr -> {
            changeLog.properties()
                    .setDescription(Arrays.stream(arr)
                            .collect(Collectors.joining(" ")));
        };

        Description() {
            super("description", "Description of work.", 1, 100, "-description", "-d");
            setConsumer(consumer);
        }
    }
}
