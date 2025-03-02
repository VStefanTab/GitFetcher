package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Logger {
    static Integer iterator = 1;
    static String type;
    static String subject;

    // For Profiles and repos
    public static void logs(String path, String attribute, List<Exception> exceptions, Integer... size) {
        if (path == null) {
            path = "Logs.txt";
        }
        File file = new File(path);
        LocalDateTime dateInserted = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
        } catch (IOException exF) {
            System.err.println("There was an error while creating the file!");
            exF.printStackTrace();
        }

        if (("Profile".equals(attribute) || "Repositories".equals(attribute)) && exceptions == null) {
            // Writing profile/Repo data to the file
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write((file.length() != 0 && iterator == 1
                        ? "\n=========================================================================\n"
                        : "=========================================================================\n"));
                if ("Profile".equals(attribute)) {
                    writer.write("Type: Success\n");
                    writer.write("Date: " + dateInserted.format(formatter) + "\n");
                } else if ("Repositories".equals(attribute)) {
                    writer.write("Type: Success\n");
                    writer.write("Date: " + dateInserted.format(formatter) + "\n");
                    writer.write("Repo " + iterator + " out of " + size[0] + " got inserted successfully!\n");
                }
                writer.write(attribute + " info saved successfully!\n");
            } catch (IOException exPR) {
                // If an error occurs while writing to the file, log the error to the file
                try (FileWriter writer = new FileWriter(file, true)) {
                    writer.write("\nAn error occurred while writing to the file: " + exPR.getMessage() + "\n");
                    exPR.printStackTrace(new java.io.PrintWriter(writer)); // Print stack trace to file
                } catch (IOException ex) {
                    System.err.println("Error writing the error to the log file: " + ex.getMessage());
                }
            }
        } else {
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write((file.length() != 0
                        ? "\n=========================================================================\n"
                        : "=========================================================================\n"));
                writer.write("Type: error\n");
                writer.write("Date: " + dateInserted.format(formatter) + "\n");
                writer.write("Subject: " + attribute + "\n");
                if (exceptions != null && !exceptions.isEmpty()) {
                    for (Exception exception : exceptions) {
                        writer.write("Error: " + exception.getMessage() + "\n");
                        exception.printStackTrace(new PrintWriter(writer));
                    }
                } else {
                    writer.write("No specific error provided.\n");
                }
            } catch (IOException ex) {
                System.err.println("Error while writing the error to the log file: " + ex.getMessage());
            }
        }
    }
}