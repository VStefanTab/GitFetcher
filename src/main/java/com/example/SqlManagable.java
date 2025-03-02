package com.example;

import java.sql.*;
//import java.sql.ResultSetMetaData;
//import java.sql.Statement;
import java.util.Arrays;

public interface SqlManagable {
    public void Insert();

    public void Update();

    public void Delete(String user);

    default boolean doesDatabaseExist() {
        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL())) {

            System.out.println("Connection with the MySQL server was successful\n");

            // Query to check if the database exists
            String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, "Profiler");

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Database exists: " + "Profiler");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if database exists: " + e.getMessage());
            Logger.logs(null, "Error checking if database exists: " + e.getMessage(), Arrays.asList(e));
        }

        System.out.println("Database does not exist: " + "Profiler");
        return false;
    }

    // default Integer countingFields(String tableName) {
    //     String sql = "SELECT * FROM " + tableName + " LIMIT 1";
    //     int fieldCount = 0;

    //     try (Connection conn = DatabaseManager.Connect(DatabaseManager.getURL(),"counting fields");
    //             Statement stmt = conn.createStatement();
    //             ResultSet rs = stmt.executeQuery(sql)) {

    //         // Get metadata from the ResultSet
    //         ResultSetMetaData metaData = rs.getMetaData();
    //         fieldCount = metaData.getColumnCount();

    //     } catch (SQLException sqlE) {
    //         System.err.println("Error counting fields: " + sqlE.getMessage());
    //         Logger.logs(null,"Couldn't count fields", Arrays.asList(sqlE));
    //     }

    //     return fieldCount;
    // }

}