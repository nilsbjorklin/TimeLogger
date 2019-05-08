package com.time_logger;


import com.sun.javafx.util.Logging;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Optional;

public class Main {

    static LoggingData data = new LoggingData();

    public static void main(String[] args) {
        if (args.length == 0) {
            help();
        } else {
            Optional<String> firstArg = Optional.ofNullable(args[0]);

            switch (firstArg.orElse("help")) {
                case "print":
                    print(Arrays.copyOfRange(args, 1, args.length));
                    break;
                case "add":
                    add(Arrays.copyOfRange(args, 1, args.length));
                    break;
                default:
                    help();
                    break;
            }
        }
    }

    private static void help() {
        System.out.println("help");
    }
    private static void add(String[] args){
        LogPost post = new LogPost();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                //Date
                case "-date":
                    i++;
                    if (i < args.length) {
                        post.date(args[i]);
                    }
                    break;
                //Hours
                case "-h":
                    i++;
                    if (i < args.length) {
                        post.hours(args[i]);
                    }
                    break;
                //Minutes
                case "-m":
                    i++;
                    if (i < args.length) {
                        post.minutes(args[i]);
                    }
                    break;
                //Category
                case "-c":
                    i++;
                    if (i < args.length) {
                        post.category(args[i]);
                    }
                    break;
                //Description
                case "-d":
                    i++;
                    while (i < args.length && !args[i].matches("-.*")) {
                        post.description(args[i]);
                        i++;
                    }
                    i--;
                    break;

            }
        }
        data.add(post);

    }
    private static void print(String[] args) {
        LoggingData.PrintProperties props = data.getPrintProperties();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                //Month
                case "-m":
                    i++;
                    if (i < args.length) {
                        props.month(args[i]);
                    }
                    break;
                //Year
                case "-y":
                    i++;
                    if (i < args.length) {
                        props.year(args[i]);
                    }
                    break;
                //Order
                case "-o":
                    i++;
                    if (i < args.length) {
                        props.order(args[i]);
                    }
                    break;

            }
        }
        data.print(props);
    }
}
