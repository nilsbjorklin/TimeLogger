package com.time_logger.actions;

import com.time_logger.FileController;
import com.time_logger.LogPost;
import com.time_logger.Properties;

import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PrintLog {

    private final static String FILE_LOCATION = "C:/projects/timelog.%s.%s";

    public void print(PrintProperties props) {
        FileController controller = new FileController(String.format(FILE_LOCATION, props.getFileSuffix(), props.getFileEnding()));
        try {
            System.out.printf("Printing month %s from file %s.%n", new DateFormatSymbols(Locale.US).getMonths()[props.date.get(Calendar.MONTH)], controller.getFilePath());
            List<LogPost> posts;
            if (props.getEncoding()
                    .equals(Properties.Encoding.OBJECT)) {
                posts = controller.readLogPostsFromFile();
            } else {
                posts = controller.readFile()
                        .stream()
                        .map(LogPost::new)
                        .collect(Collectors.toList());
            }
            if (props.order.equals(Order.CATEGORY)) {
                posts.sort(Comparator.comparing(LogPost::getCategory)
                        .thenComparing(LogPost::getDate));
            }
            if (props.order.equals(Order.DATE)) {
                posts.sort(Comparator.comparing(LogPost::getDate)
                        .thenComparing(LogPost::getCategory));
            }
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
            System.err.println("Could not read file: " + controller.getFilePath());
            exception.printStackTrace();
        }
    }

    public PrintProperties getPrintProperties() {
        return new PrintProperties();
    }

    public void addAsText(LogPost post) {
        try {
            if (post.getMinutes() == 0 && post.getHours() == 0) {
                throw new InvalidParameterException("Both hours and minutes cannot be 0.");
            }
            new FileController(String.format(FILE_LOCATION, post.getDate()
                    .substring(0, post.getDate()
                            .lastIndexOf("-")), Properties.Encoding.TEXT.fileEnding())).writeToFile(post.toString());

            System.out.println(post.eventAdded());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public enum Order {
        CATEGORY,
        DATE
    }

    public class PrintProperties {
        private Calendar date = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
        private Order order = Order.DATE;
        private Properties.Encoding encoding = Properties.Encoding.OBJECT;

        public void month(String monthString) {
            try {
                setMonth(Integer.parseInt(monthString) - 1);
            } catch (Exception exception) {
                System.err.printf("%s is not a valid month.%n", monthString);
            }
        }

        public void encoding(String encodingType) {
            this.encoding = Properties.Encoding.valueOf(encodingType.toUpperCase());
        }

        private Properties.Encoding getEncoding() {
            return encoding;
        }

        private String getFileEnding() {
            return encoding.fileEnding();
        }

        public void year(String yearString) {
            try {
                setYear(Integer.parseInt(yearString));
            } catch (Exception exception) {
                System.err.printf("%s is not a valid year.%n", yearString);
            }
        }

        public void order(String orderString) {
            try {
                this.order = Order.valueOf(orderString.toUpperCase());
            } catch (Exception exception) {
                System.err.printf("It's not possible to order results by %s.%n", orderString);

            }
        }

        String getFileSuffix() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            return simpleDateFormat.format(date.getTime());
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
