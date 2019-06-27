package com.time_logger.routing;

import com.time_logger.DateContainer;
import com.time_logger.FileController;
import com.time_logger.LogPost;
import com.time_logger.Properties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class Delete extends Argument {

    private final static String FILE_LOCATION = "C:/projects/timelog.%s.%s";
    private Properties.Encoding encoding = Properties.Encoding.OBJECT;
    private DateContainer dateContainer = new DateContainer();
    private String id;


    public Delete() {
        super("delete", "deletes event");
        options.add(new Id());
        options.add(new Date());
    }

    @Override
    void invoke() {
        super.invoke();

        FileController controller = new FileController(String.format(FILE_LOCATION, dateContainer.getFileSuffix(), encoding.fileEnding()));

        List<LogPost> posts = controller.readLogPostsFromFile();
        List<LogPost> newPosts = new ArrayList<>();

        for(LogPost currentPost : posts){
            if (!currentPost.getId().equals(id)){
                newPosts.add(currentPost);
            }
        }
        controller.writeLogPostsToFile(newPosts);
        System.out.printf("Event %s deleted.", id);
    }

    private class Id extends Option {

        Consumer<String[]> consumer = arr -> id = arr[0];

        Id() {
            super("id", "Id for event.", "-id", "-i");
            setConsumer(consumer);
            setRequired(true);
            setRegex(".*");
        }
    }

    private class Date extends Option {

        Consumer<String[]> consumer = arr -> dateContainer.setSuffix(arr[0]);

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
