package com.time_logger;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

public class LoggingData {
    private final static Calendar calendar = new GregorianCalendar();

    private final static String FILE_LOCATION = String.format("C:/projects/timelog.%d-%s.log", calendar.get(Calendar.YEAR) , calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));

    public static void printCurrent() {
        print(calendar.get(Calendar.MONTH));
    }

    public static void print(int month) {
        String fileName = FILE_LOCATION;
        try {
            fileName = FILE_LOCATION.replaceAll("-.*", "-" + new DateFormatSymbols(Locale.US).getMonths()[month] + ".log");
        } catch (Exception exception) {
            System.err.println("Invalid month");
        }
        System.out.println(fileName);
        try {
            System.out.printf("Printing month %s.%n", calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));
            List<LogPost> posts = FileController.readFile(fileName).stream().map(line -> new LogPost(line)).collect(Collectors.toList());
            posts.sort(Comparator.comparing(LogPost::getCategory));
            HashMap<String, Double> categories = new HashMap<>();
            double total = 0;
            for (LogPost post : posts) {
                double time = post.hours + (double) post.minutes / 60;
                total += time;
                if (categories.containsKey(post.category)) {
                    time += categories.get(post.category);
                }
                categories.put(post.category, time);
                System.out.println(post.getEvent());
            }
            categories.entrySet();
            System.out.println("-----------------------------------");
            for (Map.Entry<String, Double> entry : categories.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("Total: " + total);
        } catch (Exception exception) {
            System.err.println("Could not read file: " + fileName);
            exception.printStackTrace();
        }
    }

    public void add(String[] args) {
        try {
            LogPost post = new LogPost(Arrays.copyOfRange(args, 1, args.length));
            FileController.writeToFile(post.toString(), FILE_LOCATION);
            System.out.println(post.eventAdded());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void dateAdd(String[] args){
        try {
            LogPost post = new LogPost(Arrays.copyOfRange(args, 2, args.length));
            FileController.writeToFile(post.toString(), FILE_LOCATION);
            System.out.println(post.eventAdded());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    static class LogPost {
        private String category, description, month;
        private int hours, minutes, dayOfMonth, year;

        LogPost(String arg) {
            this(Arrays.copyOfRange(arg.split(","), 1, arg.split(",").length));
        }

        LogPost(String... args) {
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            year = calendar.get(Calendar.YEAR);
            hours = Integer.parseInt(args[0]);
            int index = 1;
            if (args.length > index) {
                if (args[index].matches("\\d+")) {
                    minutes = Integer.parseInt(args[index++]);
                } else {
                    minutes = 0;
                }
                category = args[index++].toLowerCase();
                String[] descArr = Arrays.copyOfRange(args, index, args.length);
                description = Arrays.stream(descArr).collect(Collectors.joining(" "));
            }
        }
        public String getCategory(){
            return category;
        }
        public String getEvent() {
            return String.format("%d-%s-%d, (%s) %d hours and %d minutes, description: %s.", year, month, dayOfMonth, category, hours, minutes , description);
        }

        public String eventAdded() {
            return String.format("Added %d hours and %d minutes to date %d-%s-%d for category %s.", hours, minutes, year, month, dayOfMonth, category);
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%s", buildDate(), hours, minutes, category, description);
        }

        private String buildDate() {
            return String.format("%s-%s-%s", year, month, dayOfMonth);
        }
    }
}
