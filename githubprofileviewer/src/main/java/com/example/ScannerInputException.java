package com.example;

public class ScannerInputException extends Exception {

    public ScannerInputException(String message) {
        super(message);
    }

    public ScannerInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScannerInputException(Throwable cause) {
        super(cause);
    }
}