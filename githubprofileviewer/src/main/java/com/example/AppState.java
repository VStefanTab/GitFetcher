package com.example;

public class AppState {
    private static AppState instance;

    private String currentScene;
    private String apiKey; // Global API key
    private GitOperations gitOperations;

    private AppState() {
        currentScene = "home";
    }

    // Singleton Instance
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    // Scene Management
    public String getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(String currentScene) {
        this.currentScene = currentScene;
        System.out.println("Updated current scene to: " + currentScene);
    }

    // API Key Management
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        System.out.println("API key updated.");
    }

    // GitOperations Management
    public GitOperations getGitOperations() {
        return gitOperations;
    }

    public void setGitOperations(GitOperations gitOperations) {
        this.gitOperations = gitOperations;
    }

    // Method to clear the GitOperations object (for reset)
    public void clearGitOperations() {
        this.gitOperations = null;
    }
}
