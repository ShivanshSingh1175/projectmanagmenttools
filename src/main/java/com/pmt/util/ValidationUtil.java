package com.pmt.util;

import java.util.regex.Pattern;

/**
 * Validation Utility Class - Provides validation methods
 * Demonstrates OOP principle: Utility class with static methods
 */
public class ValidationUtil {
    private static final String EMAIL_PATTERN = 
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    private static final String PASSWORD_PATTERN = 
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    
    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern PASSWORD_REGEX = Pattern.compile(PASSWORD_PATTERN);

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    /**
     * Validate password strength
     * Requirements: Min 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_REGEX.matcher(password).matches();
    }

    /**
     * Validate non-empty string
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validate name (alphanumeric and spaces only)
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$") && name.trim().length() >= 2;
    }

    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[\\d\\-\\+\\s]{10,}$");
    }

    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(int value) {
        return value > 0;
    }

    /**
     * Validate date is not null
     */
    public static boolean isValidDate(java.time.LocalDate date) {
        return date != null;
    }

    /**
     * Validate end date is after start date
     */
    public static boolean isValidDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return startDate != null && endDate != null && endDate.isAfter(startDate);
    }
}
