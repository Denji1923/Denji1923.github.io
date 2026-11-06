package com.campus.lostfound.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * FILE HANDLING: appends every important event to a log file.
 * On Tomcat the file is  [Tomcat base folder]/logs/campus-activity.log ; otherwise  ./logs/campus-activity.log
 */
public final class AppLogger {
    private static final Path LOG_FILE = Paths.get(System.getProperty("catalina.base", "."), "logs", "campus-activity.log");
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AppLogger() { }

    public static synchronized void log(String message) {
        try {
            Files.createDirectories(LOG_FILE.getParent());
            String line = LocalDateTime.now().format(FORMAT) + " | " + message + System.lineSeparator();
            Files.writeString(LOG_FILE, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not write log file: " + e.getMessage());
        }
    }
}
