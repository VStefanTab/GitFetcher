package com.example;

public class Placeholder {
    private final String type; // Type of the log, e.g., "Success" or "Error"
    private final String message; // Log message, e.g., repo info or error details
    private final String date; // Date of the log

    // Constructor to initialize the log type, message, and date
    public Placeholder(String type, String message, String date) {
        this.type = type;
        this.message = message;
        this.date = date;
    }

    // Getter for 'type' field
    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    // Override toString to provide a readable format for the log entry
    @Override
    public String toString() {
        return "Type: " + type + "\n" +
                "Date: " + date + "\n" +
                "Message: " + message + "\n";
    }
}