package com.example;

import java.util.Scanner;

public class InputDevice {
    private static Scanner scanner;

    public static Scanner getScanner() throws ScannerInputException{
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    public static void close() throws ScannerInputException{
        if (scanner != null) {
            scanner.close();
        }
    }
}