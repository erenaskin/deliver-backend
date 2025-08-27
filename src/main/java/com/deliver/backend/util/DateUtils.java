package com.deliver.backend.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIMEZONE = "UTC";
    
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    
    /**
     * Get current date time in UTC
     */
    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneId.of(DEFAULT_TIMEZONE));
    }
    
    /**
     * Format date time to string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }
    
    /**
     * Format date time to string with custom pattern
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Calculate delivery time estimate based on distance
     */
    public static LocalDateTime calculateDeliveryTime(double distanceKm) {
        // Base time: 15 minutes + 2 minutes per km
        int estimatedMinutes = 15 + (int) Math.ceil(distanceKm * 2);
        return nowUtc().plusMinutes(estimatedMinutes);
    }
    
    /**
     * Check if delivery time is within business hours
     */
    public static boolean isWithinBusinessHours(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return hour >= 8 && hour <= 22; // 8 AM to 10 PM
    }
    
    /**
     * Calculate minutes between two dates
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }
    
    /**
     * Add business days to a date (excluding weekends)
     */
    public static LocalDateTime addBusinessDays(LocalDateTime date, int days) {
        LocalDateTime result = date;
        int addedDays = 0;
        
        while (addedDays < days) {
            result = result.plusDays(1);
            // Skip weekends (Saturday = 6, Sunday = 7)
            if (result.getDayOfWeek().getValue() < 6) {
                addedDays++;
            }
        }
        
        return result;
    }
}
