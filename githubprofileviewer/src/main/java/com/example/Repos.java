package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Repos implements SqlManagable, Comparable<Repos> {
    private String owner;
    private String repoName;
    private String description;
    private String html_url;
    private Integer stargazers_count;
    private Integer watchers_count;
    private String language;
    private String license;
    private String created_at;
    private String updated_at;
    private String pushed_at;
    private String clone_url;
    private Boolean allow_forking;
    private Integer forks;
    private String sha;
    private String InsertedAt;

    private static String attribute = "default";

    @Override
    public int compareTo(Repos other) {
        try {
            switch (attribute.toLowerCase()) {
                case "stars" -> {
                    return this.stargazers_count.compareTo(other.stargazers_count);
                }
                case "forks" -> {
                    return this.forks.compareTo(other.forks);
                }
                case "watchers" -> {
                    return this.watchers_count.compareTo(other.watchers_count);
                }
                case "default" -> {
                    return this.repoName.compareTo(other.repoName);
                }
                default -> throw new Exception("None of the attributes inserted were correct!");
            }
        } catch (Exception e) {
            Logger.logs(null, "Comparing failed!", Arrays.asList(e));
            System.err.println("Comparing failed!");
        }
        return this.repoName.compareTo(other.repoName); // Default fallback (alphabetical)
    }

    // Static method to set the comparison attribute (called once)
    public static void setComparisonAttribute(String attributeInput) {
        attribute = attributeInput;
    }

    public Repos() {
    }

    public Repos(String owner, String repoName, String description, String html_url, Integer stargazer_count,
                 Integer watchers_count, String language,
                 String license, String created_at, String updated_at, String pushed_at, String clone_url,
                 Boolean allow_forking,
                 Integer forks, String... oarg) {
        this.owner = owner;
        this.repoName = repoName;
        this.description = description;
        this.html_url = html_url;
        this.stargazers_count = stargazer_count;
        this.watchers_count = watchers_count;
        this.language = language;
        this.license = license;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.pushed_at = pushed_at;
        this.clone_url = clone_url;
        this.allow_forking = allow_forking;
        this.forks = forks;
        this.sha = (oarg != null && oarg.length > 0) ? oarg[0] : null;
        this.InsertedAt = (oarg != null && oarg.length > 1) ? oarg[1] : null;
    }

    public String getValue() {
        return "Owner: " + owner +
                "\nRepo Name: " + repoName +
                "\nDescription: " + description +
                "\nURL: " + html_url +
                "\nStars: " + stargazers_count +
                "\nWatchers: " + watchers_count +
                "\nLanguage: " + language +
                "\nLicense: " + license +
                "\nCreated At: " + created_at +
                "\nUpdated At: " + updated_at +
                "\nPushed At: " + pushed_at +
                "\nClone URL: " + clone_url +
                "\nAllow Forking: " + allow_forking +
                "\nForks: " + forks +
                "\nSHA: " + sha;
    }

    // sql
    public boolean doesFieldExist(String owner, String repoName) {
        String sql = "SELECT COUNT(*) FROM Repositories WHERE username = ? AND RepoName = ?";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, owner);
            pstmt.setString(2, repoName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException sqlE) {
            System.err.println("Error checking if repository exists: " + sqlE.getMessage());
            Logger.logs(null, "Couldn't check if the field exists", Arrays.asList(sqlE));
        }
        return false;
    }

    // Create the database in case it doesn't exist
    @Override
    public void Insert() {
        String InsertDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


        String sql = "INSERT INTO Repositories(RepoName, Description, URL, Stars, Watchers, Language, License, CreatedAt, UpdatedAt, PushedAt, CloneURL, AllowForking, Forks, SHA, InsertDate, username) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, repoName);
            pstmt.setString(2, description);
            pstmt.setString(3, html_url);
            pstmt.setInt(4, stargazers_count);
            pstmt.setInt(5, watchers_count);
            pstmt.setString(6, language);
            pstmt.setString(7, license);
            pstmt.setString(8, created_at);
            pstmt.setString(9, updated_at);
            pstmt.setString(10, pushed_at);
            pstmt.setString(11, clone_url);
            pstmt.setBoolean(12, allow_forking);
            pstmt.setInt(13, forks);
            pstmt.setString(14, sha);
            pstmt.setString(15, InsertDate);
            pstmt.setString(16, owner);
            pstmt.executeUpdate();
            System.out.println("Repository added successfully to the database!");
        } catch (Exception e) {
            // If an error occurs while writing to the file, log the error to the file
            Logger.logs(null, "An error occurred while writing to the file(Inserting repos into the database): "
                    + e.getMessage() + "\n", Arrays.asList(e));
        }
    }

    @Override
    public void Update() {
        String InsertDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = "UPDATE Repositories SET "
                + "Description = ?, "
                + "URL = ?, "
                + "Stars = ?, "
                + "Watchers = ?, "
                + "Language = ?, "
                + "License = ?, "
                + "CreatedAt = ?, "
                + "UpdatedAt = ?, "
                + "PushedAt = ?, "
                + "CloneURL = ?, "
                + "AllowForking = ?, "
                + "Forks = ?, "
                + "SHA = ?, "
                + "InsertDate = ? "
                + "WHERE username = ? AND RepoName = ?";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, description);
            pstmt.setString(2, html_url);
            pstmt.setInt(3, stargazers_count);
            pstmt.setInt(4, watchers_count);
            pstmt.setString(5, language);
            pstmt.setString(6, license);
            pstmt.setString(7, created_at);
            pstmt.setString(8, updated_at);
            pstmt.setString(9, pushed_at);
            pstmt.setString(10, clone_url);
            pstmt.setBoolean(11, allow_forking);
            pstmt.setInt(12, forks);
            pstmt.setString(13, sha);
            pstmt.setString(14, InsertDate);
            pstmt.setString(15, owner);
            pstmt.setString(16, repoName);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Repositories updated successfully!");
            } else {
                System.err.println("Repositories couldn't be updated");
                throw new SQLException("Repositories couldn't be updated!");
            }
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
            Logger.logs(null, sqlE.getMessage(), Arrays.asList(sqlE));
        }
    }

    @Override
    public void Delete(String user) {

        String sql = "DELETE FROM Repositories WHERE username = ?";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user); // Assuming username is the unique identifier
            pstmt.executeUpdate();
            System.out.println("Repositories deleted successfully!");
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
            Logger.logs(null, sqlE.getMessage(), Arrays.asList(sqlE));
        }
    }

    // getters
    public String getRepoName() {
        return repoName;
    }

    public String getRepoDescription() {
        return description;
    }

    public String getUrl() {
        return html_url;
    }

    public Integer getStargazerCount() {
        return stargazers_count;
    }

    public Integer getWatchersCount() {
        return watchers_count;
    }

    public String getLanguage() {
        return language;
    }

    public String getLicense() {
        return license;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getPushedAt() {
        return pushed_at;
    }

    public String getCloneUrl() {
        return clone_url;
    }

    public Boolean getAllowForking() {
        return allow_forking;
    }

    public Integer getForks() {
        return forks;
    }

    public String getSHA() {
        return sha;
    }

    public String getOwner() {
        return owner;
    }

    public String getInsertedAt() {
        return InsertedAt;
    }
}