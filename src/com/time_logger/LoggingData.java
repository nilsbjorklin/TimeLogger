package com.time_logger;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class LoggingData {

    private final static String FILE_LOCATION = "C:/projects/timelog.%s.log";

    void print(PrintProperties props) {
        String fileName = String.format(FILE_LOCATION, props.getFileSuffix());
        try {
            System.out.printf("Printing month %s from file %s.%n", new DateFormatSymbols(Locale.US).getMonths()[props.date.get(Calendar.MONTH)], fileName);
            List<LogPost> posts = FileController.readFile(fileName).stream().map(LogPost::new).collect(Collectors.toList());
            if (props.order.equals(Order.CATEGORY))
                posts.sort(Comparator.comparing(LogPost::getCategory).thenComparing(LogPost::getDate));
            if (props.order.equals(Order.DATE))
                posts.sort(Comparator.comparing(LogPost::getDate).thenComparing(LogPost::getCategory));
            HashMap<String, Double> categories = new HashMap<>();
            double total = 0;
            for (LogPost post : posts) {
                double time = post.getHours() + (double) post.getMinutes() / 60;
                total += time;
                if (categories.containsKey(post.getCategory())) {
                    time += categories.get(post.getCategory());
                }
                categories.put(post.getCategory(), time);
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

    PrintProperties getPrintProperties() {
        return new PrintProperties();
    }

    void add(LogPost post) {
        try {
            FileController.writeToFile(post.toString(), String.format(FILE_LOCATION, post.getDate().substring(0, post.getDate().lastIndexOf("-"))));
            System.out.println(post.eventAdded());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    enum Order {
        CATEGORY,
        DATE
    }

    class PrintProperties {
        private Calendar date = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
        private Order order = Order.DATE;

        void month(String monthString) {
            try {
                setMonth(Integer.parseInt(monthString) - 1);
            } catch (Exception exception) {
                System.err.printf("%s is not a valid month.%n", monthString);
            }
        }
        String getFileSuffix(){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            return simpleDateFormat.format(date.getTime());
        }
        void year(String yearString) {
            try {
                setYear(Integer.parseInt(yearString));
            } catch (Exception exception) {
                System.err.printf("%s is not a valid year.%n", yearString);
            }
        }

        void order(String orderString) {
            try {
                this.order = Order.valueOf(orderString.toUpperCase());
            } catch (Exception exception) {
                System.err.printf("It's not possible to order results by %s.%n", orderString);

            }
        }

        private void setMonth(int monthInt) {
            if (monthInt < 0 || monthInt > 11) {
                throw new IllegalArgumentException();
            } else {
                date.set(Calendar.MONTH, monthInt);
            }
        }

        private void setYear(int yearInt) {
            if (yearInt < date.get(Calendar.YEAR) - 1 || yearInt > date.get(Calendar.YEAR)) {
                throw new IllegalArgumentException();
            } else {
                date.set(Calendar.YEAR, yearInt);
            }
        }
    }

}
