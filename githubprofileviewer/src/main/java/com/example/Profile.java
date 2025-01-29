package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Profile implements SqlManagable, Comparable<Profile> {
    private String username;
    private String CreationTime;
    private String LastUpdate;
    private Integer PublicRepos;
    private Integer Followers;
    private Integer Following;
    private String profileType;
    private String Name;
    private String Location;
    private String Company;
    private String InsertDate;

    public Profile() {
    }

    public Profile(String Username, String CreationTime, String LastUpdated, Integer PublicRepos, Integer Followers,
                   Integer Following, String profileType,
                   String Name, String Location, String Company, String... InsertDate) {
        this.username = Username;
        this.CreationTime = CreationTime;
        this.LastUpdate = LastUpdated;
        this.PublicRepos = PublicRepos;
        this.Followers = Followers;
        this.Following = Following;
        this.profileType = profileType;
        this.Name = Name;
        this.Location = Location;
        this.Company = Company;
        this.InsertDate = (InsertDate != null && InsertDate.length > 0) ? InsertDate[0] : "DefaultInsertDate";
    }

    public void printValues(Profile profile) {
        System.out.println("Username: " + profile.username);
        System.out.println("Creation Time: " + profile.CreationTime);
        System.out.println("Last Update: " + profile.LastUpdate);
        System.out.println("Public Repos: " + profile.PublicRepos);
        System.out.println("Followers: " + profile.Followers);
        System.out.println("Following: " + profile.Following);
        System.out.println("Profile Type: " + profile.profileType);
        System.out.println("Name: " + profile.Name);
        System.out.println("Location: " + profile.Location);
        System.out.println("Company: " + profile.Company + "\n");
    }

    public static boolean isValidGitHubApiKey(String apiKey) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.github.com/user"))
                    .header("Authorization", "Bearer " + apiKey) // Add the API key as a Bearer token
                    .header("Accept", "application/vnd.github+json")
                    .GET()
                    .build();

            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status code
            if (response.statusCode() == 200) {
                // API key is valid
                System.out.println("GitHub API key is valid.");
                return true;
            } else {
                // API key is invalid
                System.out.println("GitHub API key is invalid. Status code: " + response.statusCode());
                return false;
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., network errors, malformed URI, etc.)
            System.err.println("Error validating GitHub API key: " + e.getMessage());
            return false;
        }
    }

    public static String setUsername(String usernameInput) {
        try {
            // Validate the username
            if (usernameInput.length() < 2 || usernameInput.length() > 38) {
                throw new Exception("Invalid input: Username length must be between 2 and 38 characters!");
            }
            if (!usernameInput.matches("[a-zA-Z0-9_.-]+")) {
                throw new Exception(
                        "Invalid input: Username should only contain letters, numbers, underscores, periods, or dashes!");
            }
        } catch (Exception e) {
            Logger.logs(null, e.getMessage(), Arrays.asList(e));
            return null;
        }
        return usernameInput;
    }

    @Override
    public int compareTo(Profile otherUser) {
        Integer thisFollowers = this.Followers != null ? this.Followers : 0;
        Integer otherFollowers = otherUser.Followers != null ? otherUser.Followers : 0;

        return Integer.compare(otherFollowers, thisFollowers); // Sorting by followers
    }

    // SQL

    public boolean doesFieldExist(String user) {
        String sql = "SELECT COUNT(*) FROM Profiles WHERE username = ?";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException sqlE) {
            System.err.println("Error checking if profile exists: " + sqlE.getMessage());
            Logger.logs(null, "Couldn't search for the field requested", Arrays.asList(sqlE));
        }

        return false;
    }

    @Override
    public void Insert() {
        InsertDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = "INSERT INTO Profiles(username, CreationTime, LastUpdate, PublicRepos, Followers, Following, profileType, Name, Location, Company, InsertDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, CreationTime);
            pstmt.setString(3, LastUpdate);
            pstmt.setInt(4, PublicRepos);
            pstmt.setInt(5, Followers);
            pstmt.setInt(6, Following);
            pstmt.setString(7, profileType);
            pstmt.setString(8, Name);
            pstmt.setString(9, Location);
            pstmt.setString(10, Company);
            pstmt.setString(11, InsertDate);

            pstmt.executeUpdate();
            System.out.println("Profile added successfully to the database!");
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
            Logger.logs(null, "Couldn't insert the data for the Profile inside the database", Arrays.asList(sqlE));
        }
    }


    @Override
    public void Update() {
        InsertDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = "UPDATE Profiles SET "
                + "CreationTime = ?, "
                + "LastUpdate = ?, "
                + "PublicRepos = ?, "
                + "Followers = ?, "
                + "Following = ?, "
                + "profileType = ?, "
                + "Name = ?, "
                + "Location = ?, "
                + "Company = ?, "
                + "InsertDate = ? "
                + "WHERE username = ?";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, CreationTime);
            pstmt.setString(2, LastUpdate);
            pstmt.setInt(3, PublicRepos);
            pstmt.setInt(4, Followers);
            pstmt.setInt(5, Following);
            pstmt.setString(6, profileType);
            pstmt.setString(7, Name);
            pstmt.setString(8, Location);
            pstmt.setString(9, Company);
            pstmt.setString(10, InsertDate);
            pstmt.setString(11, username);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Profile updated successfully!");
            } else {
                System.err.println("Profile couldn't be updated!");
                throw new SQLException("Profile couldn't be updated!");
            }
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
            Logger.logs(null, sqlE.getMessage(), Arrays.asList(sqlE));
        }
    }

    @Override
    public void Delete(String user) {
        if (user == null) {
            System.err.println("Invalid username provided. Deletion aborted.");
            return;
        }

        String sql = "DELETE FROM Profiles WHERE username = ?";
        user = user.trim();

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user); // Assuming username is the unique identifier
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Profile deleted successfully!");
            } else {
                System.err.println("No profile found with the given username.");
                throw new SQLException("no rows were affected by the delete!");
            }

        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
            Logger.logs(null, "Couldn't delete the profile", Arrays.asList(sqlE));
        }
    }

    // getters
    public String getCompany() {
        return Company;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public Integer getFollowers() {
        return Followers;
    }

    public Integer getFollowing() {
        return Following;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public String getLocation() {
        return Location;
    }

    public String getName() {
        return Name;
    }

    public String getProfileType() {
        return profileType;
    }

    public Integer getPublicRepos() {
        return PublicRepos;
    }

    public String getUsername() {
        return username;
    }

    public String getInsertDate() {
        return InsertDate;
    }
}