package com.campus.lostfound.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Formats date/time values for display (web pages and CSV files). */
public final class Formats {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Formats() { }

    public static String time(LocalDateTime time) { return time == null ? "" : time.format(TIME); }
}
