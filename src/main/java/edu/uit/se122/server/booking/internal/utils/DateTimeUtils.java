package edu.uit.se122.server.booking.internal.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd.HHmmss");

    public static String formatToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return ""; // Hoặc return null; tùy thuộc vào logic hệ thống của bạn
        }
        return dateTime.format(FORMATTER);
    }
}
