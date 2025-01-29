package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

public class Pushes implements SqlManagable {
    private String username;
    private String authorName;
    private String repoName;
    private String email;
    private String branch;
    private Integer commitsNumber;
    private String commitSHA;
    private String commitMessage;
    private String createdAt;
    private String insertedAt;

    public Pushes() {
    }

    public Pushes(String username, String authorName, String repoName, String email, String branch,
                  Integer commitsNumber, String commitSHA,
                  String commitMessage, String createdAt, String... InsertedAt) {
        this.username = username;
        this.authorName = authorName;
        this.repoName = repoName;
        this.email = email;
        this.branch = branch;
        this.commitsNumber = commitsNumber;
        this.commitSHA = commitSHA;
        this.commitMessage = commitMessage;
        this.createdAt = createdAt;
        this.insertedAt = (InsertedAt != null && InsertedAt.length > 0) ? InsertedAt[0] : null;
    }

    public boolean doesFieldExist() {
        String sql = "SELECT COUNT(*) FROM Pushes WHERE username = ? AND repoName = ? AND commitSHA = ?";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, repoName);
            pstmt.setString(3, commitSHA);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Field existence check: count = " + count);
                    return count > 0;
                }
            }
        } catch (SQLException sqlE) {
            System.err.println("Error checking if the push exists: " + sqlE.getMessage());
            Logger.logs(null, "Couldn't form a connection with the database", Arrays.asList(sqlE));
        }

        return false;
    }

    @Override
    public void Insert() {
        String insertDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = "INSERT INTO Pushes (pushUUID, username, repoName, authorName, email, branch, commitsNumber, commitSHA, commitMessage, createdAt, insertedAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, username);
            pstmt.setString(3, repoName);
            pstmt.setString(4, authorName);
            pstmt.setString(5, email);
            pstmt.setString(6, branch);
            pstmt.setInt(7, commitsNumber);
            pstmt.setString(8, commitSHA);
            pstmt.setString(9, commitMessage);
            pstmt.setString(10, createdAt);
            pstmt.setString(11, insertDate);
            pstmt.executeUpdate();
            System.out.println("Push event added successfully to the database!");
        } catch (SQLException sqlE) {
            // If an error occurs while writing to the file, log the error to the file
            System.err.println("There was an error inserting the data into the database. " + sqlE.getSQLState());
            Logger.logs(null, "Couldn't insert the data into the database", Arrays.asList(sqlE));
        }
    }

    @Override
    public void Update() {
        String insertDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = """
                UPDATE Pushes SET
                        authorName = ?,
                        email = ?,
                        branch = ?,
                        commitsNumber = ?,
                        commitMessage = ?,
                        createdAt = ?,
                        insertedAt = ?
                        WHERE username = ? AND repoName = ? AND commitSHA = ?
                """;

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, authorName);
            pstmt.setString(2, email);
            pstmt.setString(3, branch);
            pstmt.setInt(4, commitsNumber);
            pstmt.setString(5, commitMessage);
            pstmt.setString(6, createdAt);
            pstmt.setString(7, insertDate);
            pstmt.setString(8, username);
            pstmt.setString(9, repoName);
            pstmt.setString(10, commitSHA);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Pushes updated successfully in the table!");
            } else {
                System.err.println("Pushes couldn't be updated in the table!");
                throw new SQLException("Pushes couldn't be updated in the table " + username
                        + " !One of the reasons might be that there is no need for an update because everythin is up-to-date");
            }
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
            Logger.logs(null, "Couldn't insert the data into the database", Arrays.asList(sqlE));
        }
    }

    // this should delete the table instead
    @Override
    public void Delete(String user) {
        String sql = "DELETE FROM Pushes WHERE username = ?";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Deletion completed!");
            } else {
                System.err.println("Couldn't delete the Pushes of the user searched!");
                throw new SQLException("Couldn't delete the Pushes of the user searched!");
            }
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
            Logger.logs(null, "Couldn't delete the data from the table Pushes", Arrays.asList(sqlE));
        }
    }

    public String getUsername() {
        return this.username;
    }

    public String getRepoName() {
        return this.repoName;
    }

    public String getBranch() {
        return this.branch;
    }

    public Integer getCommitsNumber() {
        return this.commitsNumber;
    }

    public String getCommitSHA() {
        return this.commitSHA;
    }

    public String getCommitMessage() {
        return this.commitMessage;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getInsertedAt() {
        return this.insertedAt;
    }
}
