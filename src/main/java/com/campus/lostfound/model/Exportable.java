package com.campus.lostfound.model;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Week 11 INTERFACE with default + static methods: anything that can be exported to a CSV file. */
public interface Exportable {
    String[] csvHeader();
    String[] csvFields();

    default String toCsvRow() {
        return Arrays.stream(csvFields()).map(Exportable::escape).collect(Collectors.joining(","));
    }

    static String escape(String value) {
        if (value == null) return "";
        // Excel would run a cell that starts with = + - @ as a formula; a leading ' keeps it plain text.
        if (!value.isEmpty() && "=+-@".indexOf(value.charAt(0)) >= 0) value = "'" + value;
        String s = value.replace("\"", "\"\"");
        return (s.contains(",") || s.contains("\"") || s.contains("\n")) ? "\"" + s + "\"" : s;
    }
}
