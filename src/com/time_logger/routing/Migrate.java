package com.time_logger.routing;

import com.time_logger.DateContainer;
import com.time_logger.FileController;
import com.time_logger.LogPost;
import com.time_logger.Properties;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Migrate extends Argument {
    private final static String FILE_LOCATION = "C:/projects/timelog.%s.%s";
    private Properties.Encoding toFormat;
    private Properties.Encoding fromFormat;
    private DateContainer dateContainer = new DateContainer();

    public Migrate() {
        super("migrate", "Migrates events between Encodings.");
        options.add(new ToEncoding());
        options.add(new FromEncoding());

    }

    @Override
    void invoke() {
        super.invoke();
        if (toFormat.equals(fromFormat)) {
            System.err.println("Cannot migrate to the same Encoding.");
        } else {
            String fileSuffix = dateContainer.getFileSuffix();
            FileController toController = new FileController(String.format(FILE_LOCATION, fileSuffix, toFormat.fileEnding()));
            FileController fromController = new FileController(String.format(FILE_LOCATION, fileSuffix, fromFormat.fileEnding()));

            System.out.printf("Migrating from file '%s' with encoding '%s' to file '%s' with encoding '%s'.%n", fromController.getFilePath(), fromFormat.name(), toController.getFilePath(), toFormat.name());
            List<LogPost> posts = new ArrayList<>();
            try {
                if (fromFormat.equals(Properties.Encoding.OBJECT)) {
                    posts = fromController.readLogPostsFromFile();
                } else if (fromFormat.equals(Properties.Encoding.TEXT)) {
                    posts = fromController.readFile()
                            .stream()
                            .map(LogPost::new)
                            .collect(Collectors.toList());
                }
                if (toFormat.equals(Properties.Encoding.OBJECT)) {
                    toController.writeLogPostsToFile(posts);
                } else if (fromFormat.equals(Properties.Encoding.TEXT)) {
                    posts.forEach(post -> {
                        try {
                            toController.writeToFile(post.toString());
                        } catch (IOException e) {
                            System.err.println("Could not write event to file");
                            System.err.println(e.getLocalizedMessage());
                        }
                    });
                }
                System.out.printf("%d events migrated.", posts.size());
            } catch (Exception e) {
                System.err.println("Could not migrate events.");
                System.err.println(e.getLocalizedMessage());
            }
        }
    }

    private class ToEncoding extends Option {

        Consumer<String[]> consumer = arr -> {
            toFormat = Properties.Encoding.valueOf(arr[0].toUpperCase());
        };

        ToEncoding() {
            super("to_encoding", "Encoding to migrate to.", "-toencoding", "-te");
            setConsumer(consumer);
            setRequired(true);
        }
    }

    private class FromEncoding extends Option {

        Consumer<String[]> consumer = arr -> {
            fromFormat = Properties.Encoding.valueOf(arr[0].toUpperCase());
        };

        FromEncoding() {
            super("from_encoding", "Encoding to migrate from.", "-fromencoding", "-fe");
            setConsumer(consumer);
            setRequired(true);
        }
    }

    private class Date extends Option {

        Consumer<String[]> consumer = arr -> {
            dateContainer.setSuffix(arr[0]);
        };

        Date() {
            super("date", "Date for event.", "-date", "-time");
            setConsumer(consumer);
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
            setDefault(format.format(localDateTime));
            setRegex("\\d{4}-\\d{2}");
        }
    }
}
