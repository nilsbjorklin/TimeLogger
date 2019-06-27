package com.time_logger.routing;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

abstract class Option {
    private List<String> commandList = new ArrayList<>();
    private String description;
    private String name;
    private boolean required = false;
    private String[] defaultValue = null;
    private String regex = ".*";
    private int minArguments = 1, maxArguments = 1;
    private Consumer<String[]> invoke;

    Option(String name, String description, int minArguments, int maxArguments, String... commands) {
        this.name = name;
        this.description = description;
        this.minArguments = minArguments;
        this.maxArguments = maxArguments;
        for (String command : commands) {
            commandList.add(command);
            Collections.addAll(Arrays.asList(commands));
        }
    }

    Option(String name, String description, String... commands) {
        this(name, description, 1, 1, commands);
    }

    List<String> getOptionInfo() {
        List<String> extraData = new ArrayList<>();
        extraData.add("Commands: " + commandList.stream()
                .collect(Collectors.joining(", ")));
        extraData.add("Description: " + description);
        if (required) {
            extraData.add("Required: true");
        } else {
            extraData.add("Required: false");
            if (defaultValue != null) {
                if (defaultValue.length == 1) {
                    extraData.add("Default: " + defaultValue[0]);
                } else {
                    extraData.add(String.format("Default: [%s]", Arrays.stream(defaultValue)
                            .collect(Collectors.joining(", "))));
                }
            }
        }
        extraData.add(String.format("Regex to match: '%s'", regex));
        if (minArguments == maxArguments) {
            extraData.add("Number of arguments: " + maxArguments);
        } else {
            extraData.add("Number of arguments, min: " + minArguments + " max: " + maxArguments);
        }

        return extraData;
    }

    List<String> getCommandList() {
        return commandList;
    }

    String getName() {
        return name;
    }

    public void setDefault(String... defaultValue) {
        this.defaultValue = defaultValue;
        this.required = false;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
        if (required) {
            defaultValue = null;
        }
    }

    public void useDefault() {
        if (defaultValue != null) {
            invoke(defaultValue);
        }
    }

    public void invoke(String... args) {
        if (args.length < minArguments) {
            throw new InvalidParameterException(String.format("\tTo few arguments were provided.%n\t%s arguments received for argument option '%s', minimum %s arguments must be provided.", args.length, getName(), minArguments));
        } else if (args.length > maxArguments) {
            throw new InvalidParameterException(String.format("\tTo many arguments were provided.%n\t%s arguments received, maximum %s arguments for option '%s'.", args.length, maxArguments, getName()));
        }
        for (String arg : args) {
            if (!arg.matches(regex)) {
                throw new InvalidParameterException(String.format("Argument '%s' with value '%s' must match '%s'.%n", getName(), arg, regex));
            }
        }
        setRequired(false);
        setDefault(null);
        invoke.accept(args);
    }

    public void setConsumer(Consumer<String[]> consumer) {
        this.invoke = consumer;
    }
}