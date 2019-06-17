package com.time_logger.routing;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Argument {
    List<Option> options = new ArrayList<>();
    private String name;
    private String description;

    Argument(String name, String description) {
        this.name = name;
        this.description = description;
    }

    void invoke() {
        checkOptions();
    }

    public void run(String... args) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].matches("-.*")) {
                    List<String> argsForCurrent = new ArrayList<>();
                    Option currentOption = getOption(args[i]);
                    while (i + 1 < args.length && !args[i + 1].matches("-.*")) {
                        argsForCurrent.add(args[i + 1]);
                        i++;
                    }
                    if (argsForCurrent.size() > 0)
                        currentOption.invoke(argsForCurrent.toArray(new String[argsForCurrent.size()]));
                    else
                        currentOption.invoke();
                }
            }
            invoke();
        } catch(Exception exception){
            System.err.println(exception.getClass().getCanonicalName());
            exception.printStackTrace();
        }
    }

    private void checkOptions() {
        for (Option option : options) {
            if (option.isRequired())
                throw new InvalidParameterException(String.format("Argument '%s' is required for action '%s'", option.getName(), getName()));
            option.useDefault();
        }
    }

    private Option getOption(String command) {
        for (Option option : options) {
            if (option.getCommandList().stream().anyMatch(command::equals)) {
                return option;
            }
        }
        return null;
    }

    public String getArgumentDescription() {
        String optionsDescription = options.stream().map(option -> String.format("%s%n\t%s", option.getName(), option.getOptionInfo().stream().collect(Collectors.joining("\n\t")))).collect(Collectors.joining("\n\n"));
        return String.format("Argument: %s%nDescription: %s%nOptions:%n%s", name, description, optionsDescription);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}