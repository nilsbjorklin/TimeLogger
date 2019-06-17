package com.time_logger.routing;

import com.time_logger.FileController;
import com.time_logger.LogPost;
import com.time_logger.Properties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Add extends Argument {

    private final static String FILE_LOCATION = "C:/projects/timelog.%s.%s";

    private static FileController controller;
    private LogPost post;
    private Properties.Encoding encoding = Properties.Encoding.OBJECT;

    public Add() {
        super("add", "Adds event to log");
        post = new LogPost();
        options.add(new Date());
        options.add(new Hours());
        options.add(new Minutes());
        options.add(new Category());
        options.add(new Description());
        //options.add(new Encoding());
    }

    @Override
    void invoke() {
        super.invoke();
        String dateString = post.getDate();
        String fileEnding = encoding.fileEnding();
        String fileSuffix = post.getDate()
                .substring(0, post.getDate()
                        .lastIndexOf("-"));
        String filePath = String.format(FILE_LOCATION, fileSuffix, fileEnding);

        FileController controller = new FileController(filePath);

        controller.add(post);
    }

    private class Date extends Option {

        Consumer<String[]> consumer = arr -> {
            post.date(arr[0]);
        };

        Date() {
            super("date", "Date for event.", "-date", "-time");
            setConsumer(consumer);
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            setDefault(format.format(localDateTime));
            setRegex("\\d{4}-\\d{2}-\\d{2}");
        }
    }

    private class Encoding extends Option {

        Consumer<String[]> consumer = arr -> {
            encoding = Properties.Encoding.valueOf(arr[0].toUpperCase());
        };

        Encoding() {
            super("encoding", "Encoding for event.", "-encoding", "-e");
            setConsumer(consumer);
            setDefault(Properties.Encoding.OBJECT.name());
        }
    }

    private class Hours extends Option {

        Consumer<String[]> consumer = arr -> {
            post.hours(arr[0]);
        };

        Hours() {
            super("hours", "Amount of hours worked.", "-hours", "-h");
            setDefault("0");
            setConsumer(consumer);
            setRegex("\\d+");
        }
    }

    private class Minutes extends Option {

        Consumer<String[]> consumer = arr -> {
            post.minutes(arr[0]);
        };

        Minutes() {
            super("minutes", "Amount of minutes worked.", "-minutes", "-m");
            setDefault("0");
            setConsumer(consumer);
            setRegex("\\d{1,2}");
        }
    }

    private class Category extends Option {

        Consumer<String[]> consumer = arr -> {
            post.category(arr[0]);
        };

        Category() {
            super("category", "Category of work.", "-category", "-c");
            setRequired(true);
            setConsumer(consumer);
        }
    }

    private class Description extends Option {

        Consumer<String[]> consumer = arr -> {
            post.description(Arrays.stream(arr)
                    .collect(Collectors.joining(" ")));
        };

        Description() {
            super("description", "Description of work.", 1, 100, "-description", "-d");
            setRequired(true);
            setConsumer(consumer);
        }
    }
}
