package com.time_logger;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileController {
    private String filePath;

    public FileController(String filePath) {
        this.filePath = filePath;
    }

    public List<String> readFile() throws FileNotFoundException {
        File file = new File(filePath);
        if (file.exists()) {
            Scanner s = new Scanner(file);
            ArrayList<String> list = new ArrayList<>();
            while (s.hasNext()) {
                list.add(s.nextLine());
            }
            s.close();
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public void writeToFile(String line) throws IOException {
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filePath, file.exists())
            );
            writer.newLine();
            writer.write(line);
            writer.close();
        } else {
            System.out.println("Creating new file: " + filePath);
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(line);
            fileWriter.flush();
            fileWriter.close();
        }
    }

    public LogPost getPost(String id) throws Exception {
        List<LogPost> posts = readLogPostsFromFile();
        for (LogPost post : posts) {
            if (post.getId()
                    .equals(id)) {
                return post;
            }
        }
        throw new InvalidObjectException("Could not find object with ID: " + id);
    }

    public List<LogPost> readLogPostsFromFile() {
        try {
            FileInputStream fi = new FileInputStream(new File(filePath));
            ObjectInputStream oi = new ObjectInputStream(fi);

            List<LogPost> logPosts = new ArrayList<>();
            while (true) {
                try {
                    logPosts.add((LogPost) oi.readObject());
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }

            oi.close();
            fi.close();
            return logPosts;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void add(LogPost post) {
        try {
            if (post.getMinutes() == 0 && post.getHours() == 0) {
                throw new InvalidParameterException("Both hours and minutes cannot be 0.");
            }
            List<LogPost> posts = readLogPostsFromFile();
            posts.add(post);
            writeLogPostsToFile(posts);
            System.out.println(post.eventAdded());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void writeLogPostsToFile(List<LogPost> posts) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write objects to file
            for (LogPost post : posts) {
                objectOutputStream.writeObject(post);
            }

            objectOutputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
}
