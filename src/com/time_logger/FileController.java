package com.time_logger;

import java.io.*;
import java.util.*;

class FileController {
    static List<String> readFile(String filePath) throws FileNotFoundException {
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

    static void writeToFile(String line, String filePath) throws IOException {
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
}
