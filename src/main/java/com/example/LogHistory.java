package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class LogHistory {
    private ArrayList<Placeholder> historyList;

    // Display options and process the user's selection
    public void displayMenu() {
        try (Scanner digit = InputDevice.getScanner()) {
            int option;

            System.out.println(
                    "Options:\n" +
                            "1. Get the most recent logs\n" +
                            "2. Get only the errors\n" +
                            "3. Get the most recent errors\n" +
                            "4. Get Successful logs\n" +
                            "5. Get everything inside the file\n" +
                            "6. Exit\n(Choose the digit corresponding to what you want to get)");
            do {
                while (!digit.hasNextInt()) {
                    System.out.println("Not a number");
                    digit.next();
                }
                option = digit.nextInt();

                if (option < 1 || option > 6) {
                    System.out.println("You need to choose a number from 1-6");
                } else if (option != 6) {
                    LoadHistoryList(option);
                    break;
                }
            } while (option != 6);
        } catch (ScannerInputException siE) {
            System.err.println("There was an error initializng the scanner:" + siE.getMessage());
            Logger.logs(null,"there was an error initializing the scanner!", Arrays.asList(siE));
        }
    }

    // This method loads the history from the file into the ArrayList based on the
    // selected option
    private void LoadHistoryList(int option) {
        historyList.clear();

        try (BufferedReader br = new BufferedReader(new FileReader("Logs.txt"))) {
            String line;
            StringBuilder currentLog = new StringBuilder();

            // Read through the file line by line and group the log entries
            while ((line = br.readLine()) != null) {
                // Collect logs in one entry until we hit a separator
                if (line.trim().equals("=========================================================================")) {
                    if (currentLog.length() > 0) {
                        parseLog(currentLog.toString());
                        currentLog.setLength(0); // Reset the log entry
                    }
                } else {
                    currentLog.append(line).append("\n");
                }
            }
            // Parse the last log if there's any remaining content
            if (currentLog.length() > 0) {
                parseLog(currentLog.toString());
            }

            // Filter the logs based on the selected option
            switch (option) {
                case 1:
                    printRecentLogs();
                    break;
                case 2:
                    printErrors();
                    break;
                case 3:
                    printRecentErrors();
                    break;
                case 4:
                    printSuccessfulLogs();
                    break;
                case 5:
                    printAllLogs();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the history file.");
            Logger.logs(null, "An error occured while reading the history file.", Arrays.asList(e));
            e.printStackTrace();
        }
    }

    // This method parses each log entry and categorizes them into Success or Error
    private void parseLog(String log) {
        if (log.contains("Type: Success")) {
            // Extract success log details
            String repoInfo = extractRepoInfo(log);
            String date = extractDate(log);
            historyList.add(new Placeholder("Success", repoInfo, date));
        } else if (log.contains("Type: error")) {
            // Extract error log details
            String errorDate = extractDate(log);
            String errorMessage = extractErrorMessage(log);
            historyList.add(new Placeholder("Error", errorMessage, errorDate));
        }
    }

    // Extract repository information from the success log
    private String extractRepoInfo(String log) {
        String repoInfo = log.split("\n")[2];
        return repoInfo.trim();
    }

    // Extract the date from the success log
    private String extractDate(String log) {
        String dateLine = log.split("\n")[1];
        return dateLine.split("Date:")[1].trim();
    }

    // Extract the Subject from the error log
    private String extractErrorMessage(String log) {
        String errorMessage = log.split("\n")[2];
        return errorMessage.split(":")[1].trim();
    }

    // Print the most recent logs (the latest logs at the end of the list)
    public void printRecentLogs() {
        int size = historyList.size();
        if (size > 0) {
            System.out.println("Most recent logs:");
            // Display the latest log (last element)
            Placeholder latestLog = historyList.get(size - 1);
            System.out.println(latestLog);
        } else {
            System.out.println("No logs available.");
        }
    }

    // Print only error logs
    public void printErrors() {
        System.out.println("Error logs:");
        boolean foundError = false;
        for (Placeholder log : historyList) {
            if (log.getType().equals("Error")) {
                System.out.println(log);
                foundError = true;
            }
        }
        if (!foundError) {
            System.out.println("No error logs found.");
        }
    }

    // Print the most recent error logs
    public void printRecentErrors() {
        int size = historyList.size();
        boolean foundError = false;
        for (int i = size - 1; i >= 0; i--) {
            Placeholder log = historyList.get(i);
            if (log.getType().equals("Error")) {
                System.out.println("Most recent error log: " + log);
                foundError = true;
                break; // Stop once we find the most recent error
            }
        }
        if (!foundError) {
            System.out.println("No error logs found.");
        }
    }

    // Print only successful logs
    public void printSuccessfulLogs() {
        System.out.println("Successful logs:");
        boolean foundSuccess = false;
        for (Placeholder log : historyList) {
            if (log.getType().equals("Success")) {
                System.out.println(log);
                foundSuccess = true;
            }
        }
        if (!foundSuccess) {
            System.out.println("No successful logs found.");
        }
    }

    // Print all logs in the file
    public void printAllLogs() {
        if (historyList.isEmpty()) {
            System.out.println("No logs available.");
        } else {
            System.out.println("All logs:");
            for (Placeholder log : historyList) {
                System.out.println(log);
            }
        }
    }
}