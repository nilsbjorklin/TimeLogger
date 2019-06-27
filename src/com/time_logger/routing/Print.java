package com.time_logger.routing;

import com.time_logger.Properties;
import com.time_logger.actions.PrintLog;

import java.util.Calendar;
import java.util.function.Consumer;

public class Print extends Argument {
    private static PrintLog data = new PrintLog();
    private Calendar calendar = Calendar.getInstance();
    private PrintLog.PrintProperties props;

    public Print() {
        super("print", "Prints all events for month");
        props = data.getPrintProperties();
        options.add(new Month());
        options.add(new Year());
        options.add(new Order());
        options.add(new Encoding());
    }

    @Override
    void invoke() {
        super.invoke();
        data.print(props);
    }

    private class Month extends Option {

        Consumer<String[]> consumer = arr -> props.month(arr[0]);

        Month() {
            super("month", "Which month to print.", "-month", "-m");
            setConsumer(consumer);
            setDefault(Integer.toString(calendar.get(Calendar.MONTH) + 1));
            setRegex("\\d{1,2}");
        }
    }

    private class Year extends Option {

        Consumer<String[]> consumer = arr -> props.year(arr[0]);

        Year() {
            super("year", "Which year the month is in.", "-year", "-y");
            setConsumer(consumer);
            setDefault(Integer.toString(calendar.get(Calendar.YEAR)));
            setRegex("\\d{4}");
        }
    }

    private class Order extends Option {

        Consumer<String[]> consumer = arr -> props.order(arr[0]);

        Order() {
            super("order", "How the result should be ordered.", "-order", "-o");
            setConsumer(consumer);
            setDefault(PrintLog.Order.DATE.name());
        }
    }

    private class Encoding extends Option {

        Consumer<String[]> consumer = arr -> props.encoding(arr[0]);

        Encoding() {
            super("encoding", "Encoding type when printing from file.", "-encoding", "-e");
            setConsumer(consumer);
            setDefault(Properties.Encoding.OBJECT.name());
        }
    }
}
