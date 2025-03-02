package com.example;

public class ResponseCodeError extends Exception {

    public ResponseCodeError(int code) {
        super(getErrorMessage(code));
    }

    public ResponseCodeError(String message, int code) {
        super(message + " " + getErrorMessage(code));
    }

    public ResponseCodeError(String message, int code, Throwable cause) {
        super(message + " " + getErrorMessage(code), cause);
    }

    // Custom method to generate a detailed error message based on the response code
    private static String getErrorMessage(int code) {
        return switch (code) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            default -> "Unknown Error (Code: " + code + ")";
        };
    }
}