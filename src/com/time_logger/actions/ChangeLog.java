package com.time_logger.actions;

import com.time_logger.DateContainer;
import com.time_logger.FileController;
import com.time_logger.LogPost;
import com.time_logger.Properties;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ChangeLog {

    private final static String FILE_LOCATION = "C:/projects/timelog.%s.%s";
    private DateContainer dateContainer = new DateContainer();
    private Properties.Encoding encoding = Properties.Encoding.OBJECT;
    private ChangeProperties properties = new ChangeProperties();

    public void change() throws Exception {
        FileController controller = new FileController(String.format(FILE_LOCATION, dateContainer.getFileSuffix(), encoding.fileEnding()));
        LogPost post = new LogPost();
        post.id(properties.id);
        LogPost oldPost = controller.getPost(properties.id);

        if (properties.date != null) {
            post.date(properties.date);
        } else {
            post.date(oldPost.getDate());
        }

        if (properties.hours != null) {
            post.hours(properties.hours);
        } else {
            post.hours(Integer.toString(oldPost.getHours()));
        }

        if (properties.minutes != null) {
            post.minutes(properties.minutes);
        } else {
            post.minutes(Integer.toString(oldPost.getMinutes()));
        }

        if (properties.category != null) {
            post.category(properties.category);
        } else {
            post.category(oldPost.getCategory());
        }

        if (properties.description != null) {
            post.description(properties.description);
        } else {
            post.description(oldPost.getDescription());
        }
        try {
            if (post.getMinutes() == 0 && post.getHours() == 0) {
                throw new InvalidParameterException("Both hours and minutes cannot be 0.");
            }

            List<LogPost> posts = controller.readLogPostsFromFile();
            List<LogPost> newPosts = new ArrayList<>();

            for(LogPost currentPost : posts){
                if (!currentPost.getId().equals(properties.id)){
                    newPosts.add(currentPost);
                }
            }
            newPosts.add(post);
            controller.writeLogPostsToFile(newPosts);
            System.out.println(post.eventChanged());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setEncoding(Properties.Encoding encoding) {
        this.encoding = encoding;
    }

    public ChangeProperties properties() {
        return properties;
    }

    public class ChangeProperties {
        private String id = null;
        private String date = null;
        private String hours = null;
        private String minutes = null;
        private String category = null;
        private String description = null;

        public void setId(String id) {
            this.id = id;
        }

        public void setDate(String date) {
            dateContainer.setDate(date);
            this.date = date;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
