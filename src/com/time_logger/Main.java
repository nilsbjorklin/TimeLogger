package com.time_logger;

import java.util.Optional;

public class Main {

    static LoggingData data = new LoggingData();
    public static void main(String[] args) {
        if(args.length == 0){
            help();
        } else{
            Optional<String> firstArg = Optional.ofNullable(args[0]);
            switch (firstArg.orElse("default")) {
                case "print":
                    if (args.length > 1) {
                        data.print(Integer.parseInt(args[1]));
                    } else {
                        data.printCurrent();
                    }
                    break;
                case "add":
                    data.add(args);
                    break;
                case "dateadd":
                    data.dateAdd(args);
                    break;
                default:
                    help();
                    break;
            }
        }
    }

    public static void help() {
        System.out.println("help");
    }
}
