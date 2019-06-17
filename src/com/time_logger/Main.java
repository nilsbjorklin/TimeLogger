package com.time_logger;

import com.time_logger.actions.PrintLog;
import com.time_logger.routing.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private final static String FILE_LOCATION = "C:/projects/timelog.%s.%s";
    static PrintLog data = new PrintLog();

    public static void main(String[] args) throws Exception {
        DateContainer cont = new DateContainer();
        cont.setSuffix("2019-02-05");
        System.out.println(cont.getFileSuffix());
        /*List<Argument> arguments = new ArrayList<>();
        arguments.add(new Add());
        arguments.add(new Print());
        arguments.add(new Change());
        arguments.add(new Delete());
        //arguments.add(new Migrate());
        if (args.length == 0) {
            help(arguments);
        } else {
            if (args[0].toLowerCase()
                    .equals("help")) {
                if (args.length > 1) {
                    try {
                        help(getArgumentWithName(arguments, args[1]));
                    } catch (Exception exception) {
                        System.err.printf("Argument '%s' not found", args[1]);
                    }
                } else {
                    help(arguments);
                }
            } else {
                Argument currentArgument;
                try {
                    currentArgument = getArgumentWithName(arguments, args[0]);
                    if (currentArgument == null) {
                        throw new InvalidParameterException(String.format("%s is not a valid argument.", args[0]));
                    }
                    try {
                        if (args.length >= 2) {
                            String[] currentArgs = Arrays.copyOfRange(args, 1, args.length);
                            currentArgument.run(currentArgs);
                        } else {
                            currentArgument.run();
                        }
                    } catch (Exception couldNotRunArgument) {
                        couldNotRunArgument.printStackTrace();
                    }
                } catch (Exception argumentNotFoundException) {
                    System.err.printf("Argument '%s' not found.%n", args[0]);
                }
            }
        }*/
    }

    private static void help(List<Argument> arguments) {
        System.out.printf("This program has the following arguments [%s], type 'help $ARGUMENT' to get more information about a argument.%n", arguments.stream()
                .map(Argument::getName)
                .collect(Collectors.joining(", ")));
    }

    private static void help(Argument argument) {
        System.out.println(argument.getArgumentDescription());
    }

    private static Argument getArgumentWithName(List<Argument> arguments, String name) {
        for (Argument argument : arguments) {
            if (argument.getName()
                    .toLowerCase()
                    .equals(name.toLowerCase())) {
                return argument;
            }
        }
        return null;
    }
}
