package com.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatabaseManager {

    // SQLite Database configuration
    private static final String DATABASE_URL = "jdbc:sqlite:profiler.db"; // Path to SQLite database file

    // Method to establish a connection
    public static Connection connect(String databaseURL) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(databaseURL);
            System.out.println("Connection with the database was successful\n");
        } catch (SQLException sqlE) {
            System.err.println("Connection error: " + sqlE.getMessage());
        }
        return conn;
    }

    // Check if a table exists in SQLite
    public static boolean doesTableExist(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
            ResultSet rs = stmt.executeQuery(query);
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }

    public static Boolean isTableEmpty(Connection conn, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int rowCount = rs.getInt(1);
                if (rowCount == 0)
                    return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static void createTables(String databaseURL) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        Runnable createProfilesTask = () -> {
            try (Connection conn = connect(databaseURL);
                 Statement stmt = conn.createStatement()) {
                if (!doesTableExist(conn, "Profiles")) {
                    String createProfilesTable = """
                            CREATE TABLE IF NOT EXISTS Profiles (
                                username TEXT PRIMARY KEY,
                                CreationTime TEXT,
                                LastUpdate TEXT,
                                PublicRepos INTEGER,
                                Followers INTEGER,
                                Following INTEGER,
                                profileType TEXT,
                                Name TEXT,
                                Location TEXT,
                                Company TEXT,
                                InsertDate TEXT
                            )
                            """;
                    stmt.execute(createProfilesTable);
                    System.out.println("Profiles table created!");
                }
            } catch (SQLException e) {
                System.err.println("Error creating Profiles table: " + e.getMessage());
            }
        };

        Runnable createRepositoriesTask = () -> {
            try (Connection conn = connect(databaseURL);
                 Statement stmt = conn.createStatement()) {
                if (!doesTableExist(conn, "Repositories")) {
                    String createRepositoriesTable = """
                            CREATE TABLE IF NOT EXISTS Repositories (
                                RepoName TEXT,
                                Description TEXT,
                                URL TEXT,
                                Stars INTEGER,
                                Watchers INTEGER,
                                Language TEXT,
                                License TEXT,
                                CreatedAt TEXT,
                                UpdatedAt TEXT,
                                PushedAt TEXT,
                                CloneURL TEXT,
                                AllowForking BOOLEAN,
                                Forks INTEGER,
                                SHA TEXT,
                                InsertDate TEXT,
                                username TEXT,
                                PRIMARY KEY (RepoName, username)
                            );
                            """;
                    stmt.execute(createRepositoriesTable);
                    System.out.println("Repositories table created!");
                }
            } catch (SQLException e) {
                System.err.println("Error creating Repositories table: " + e.getMessage());
            }
        };

        Runnable createPushesTask = () -> {
            try (Connection conn = connect(databaseURL);
                 Statement stmt = conn.createStatement()) {
                if (!doesTableExist(conn, "Pushes")) {
                    String createPushesTable = """
                            CREATE TABLE IF NOT EXISTS Pushes (
                                pushUUID TEXT PRIMARY KEY,
                                username TEXT NOT NULL,
                                repoName TEXT NOT NULL,
                                authorName TEXT,
                                email TEXT,
                                branch TEXT,
                                commitsNumber INTEGER,
                                commitSHA TEXT,
                                commitMessage TEXT,
                                createdAt TEXT,
                                insertedAt TEXT
                            );
                            """;
                    stmt.execute(createPushesTable);
                    System.out.println("Pushes table created!");
                }
            } catch (SQLException e) {
                System.err.println("Error creating Pushes table: " + e.getMessage());
            }
        };

        executor.submit(createProfilesTask);
        executor.submit(createRepositoriesTask);
        executor.submit(createPushesTask);

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Table creation tasks were interrupted.");
        }
        System.out.println("All tasks completed.");
    }

    public static Map<String, List<Repos>> groupReposFromDatabase(String databaseURL, String lang) {
        Map<String, List<Repos>> groupedRepos = new HashMap<>();

        String sql = "SELECT username, RepoName, Description, URL, Stars, Watchers, Language, License, CreatedAt, UpdatedAt, PushedAt, CloneURL, AllowForking, Forks, SHA FROM Repositories";

        try (Connection conn = connect(databaseURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String owner = rs.getString("username");
                String repoName = rs.getString("RepoName");
                String description = rs.getString("Description");
                String url = rs.getString("URL");
                Integer stars = rs.getInt("Stars");
                Integer watchers = rs.getInt("Watchers");
                String language = rs.getString("Language");
                String license = rs.getString("License");
                String createdAt = rs.getString("CreatedAt");
                String updatedAt = rs.getString("UpdatedAt");
                String pushedAt = rs.getString("PushedAt");
                String cloneUrl = rs.getString("CloneURL");
                Boolean allowForking = rs.getBoolean("AllowForking");
                Integer forks = rs.getInt("Forks");
                String sha = rs.getString("SHA");

                // Create Repos object
                Repos repo = new Repos(owner, repoName, description, url, stars, watchers, language, license, createdAt,
                        updatedAt, pushedAt, cloneUrl, allowForking, forks, sha);

                // Group repositories by language
                if (language != null && !language.isEmpty()) {
                    groupedRepos.putIfAbsent(language, new ArrayList<>());
                    groupedRepos.get(language).add(repo);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }

        // Return only the repositories grouped by the specified language, if provided
        if (lang != null && !lang.isEmpty()) {
            groupedRepos.entrySet().removeIf(entry -> !entry.getKey().equalsIgnoreCase(lang));
        }

        return groupedRepos;
    }

    // Example: Fetch profiles from the database
    public static List<Profile> getUsersFromDatabase(String databaseURL) {
        List<Profile> users = new ArrayList<>();
        String sql = "SELECT username, CreationTime, LastUpdate, PublicRepos, Followers, Following, profileType, Name, Location, Company FROM Profiles";
        try (Connection conn = connect(databaseURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new Profile(
                        rs.getString("username"),
                        rs.getString("CreationTime"),
                        rs.getString("LastUpdate"),
                        rs.getInt("PublicRepos"),
                        rs.getInt("Followers"),
                        rs.getInt("Following"),
                        rs.getString("profileType"),
                        rs.getString("Name"),
                        rs.getString("Location"),
                        rs.getString("Company")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching profiles: " + e.getMessage());
        }
        return users;
    }

    public static String getURL() {
        return DATABASE_URL;
    }
}
