package com.ilbolan.piesstore.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.*;

/**
 * Logger class with predetermined functionality
 */
public class AppLogger implements Serializable {

    private Logger logger;
    private static Handler[] handlers;

    // static declaration of handlers and filters
    static {
        handlers = new Handler[3];
        try {
            // declare log files
            handlers[0] = new FileHandler("info.log",1 ,100*1024);
            handlers[1] = new FileHandler("fine.log", 1 ,100*1024);
            handlers[2] = new FileHandler("severe.log", true);

            handlers[0].setFormatter(new SimpleFormatter());
            handlers[1].setFormatter(new SimpleFormatter());
            handlers[2].setFormatter(new SimpleFormatter());

            // set filters for each handler
            handlers[0].setFilter(record -> record.getLevel().equals(Level.INFO));
            handlers[1].setFilter(record -> record.getLevel().equals(Level.SEVERE));
            handlers[2].setFilter(record -> record.getLevel().equals(Level.FINE) |
                    record.getLevel().equals(Level.FINER) |
                    record.getLevel().equals(Level.FINEST));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public AppLogger(Class<?> calleeClass) {
        logger = Logger.getLogger(calleeClass.getName());
        logger.addHandler(handlers[0]);
        logger.addHandler(handlers[1]);
        logger.addHandler(handlers[2]);
    }

    /**
     * Logs a message to its correct file with severity level criteria
     * @param logLevel determines the severity level of the log message
     * @param message message to be logged
     */
    public void log(Level logLevel, String message){
        logger.log(logLevel, message);
    }
}
