package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SceneController {
    @FXML
    private MenuBar menuBarTV;
    @FXML
    private MenuBar MenuBarPushes;
    @FXML
    private TextField ProfileFieldPushes;
    @FXML
    private TextField ProfileSearch;
    @FXML
    private TextField ProfileField;
    @FXML
    private MenuBar MenuBarRepos;
    @FXML
    private PasswordField profileApiPlaceholder;
    @FXML
    private MenuBar MenuBarOg;
    @FXML
    private MenuBar MenuBarProfile;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String validatedUsername;

    public void setMenuBar(MenuBar menuBar) {
        menuBarTV = menuBar;
    }

    public MenuBar getMenuBar() {
        return menuBarTV;
    }

    @FXML
    private void LogIn(ActionEvent event) {
        String apiKey = profileApiPlaceholder.getText();

        if (apiKey == null || apiKey.isBlank() || !Profile.isValidGitHubApiKey(apiKey)) {
            System.out.println("API Key is missing or invalid.");
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid API Key.");
            Logger.logs(null, "API Key is missing or invalid.", null);
            return;
        }

        // Store the username and API key in AppState
        AppState state = AppState.getInstance();
        state.setApiKey(apiKey);

        System.out.println("Switching to the home scene...");

        switchToHomeScene(event);
    }

    public void LogOut() {
        AppState state = AppState.getInstance();
        state.setApiKey(null);
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Profile!", null);
            return;
        }
        switchToLogIn(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: Add a header if needed
        alert.setContentText(message);
        alert.showAndWait();
    }

    private MenuBar setMenuBar(String scene) {
        return switch (scene) {
            case "home" -> MenuBarOg;
            case "profile" -> MenuBarProfile;
            case "repos" -> MenuBarRepos;
            case "pushes" -> MenuBarPushes;
            case "profileDatabase", "reposDatabase", "pushesDatabase", "posts" -> menuBarTV;
            default -> {
                System.out.println("Unknown scene: " + scene);
                Logger.logs(null, "Changed to an unknown scene!", null);
                yield null;
            }
        };
    }

    @FXML
    private void openGroupReposStage() {
        String selectedLanguage = ProfileField.getText().trim();

        if (selectedLanguage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Language is Required");
            alert.setContentText("Please enter a programming language to filter the repositories.");
            alert.showAndWait();
            return;
        }

        Map<String, List<Repos>> groupedRepos = DatabaseManager.groupReposFromDatabase(DatabaseManager.getURL(), selectedLanguage);
        GroupReposController controller = new GroupReposController(groupedRepos, selectedLanguage);
        controller.showGroupReposStage();
    }

    public void ProfileScene() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Profile!", null);
            return;
        }
        switchToProfileScene(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    public void ReposScene() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Repos!", null);
            return;
        }
        switchToReposScene(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    public void HomeSceneMenu() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Home!", null);
            return;
        }
        switchToHomeSceneMenu(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    public void PushesScene() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Pushes!", null);
            return;
        }
        switchToPushesScene(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    public void ProfileDatabaseScene() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Pushes!", null);
            return;
        }
        switchToProfileDatabase(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    public void ReposDatabaseScene() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Pushes!", null);
            return;
        }
        switchToReposDatabase(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    public void PushesDatabaseScene() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in Pushes!", null);
            return;
        }
        switchToPushesDatabase(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    public void GithubPostsScene() {
        if (AppState.getInstance().getCurrentScene() == null) {
            System.err.println("Error: currentScene is null!");
            showAlert(Alert.AlertType.ERROR, "Error", "currentScene is null!");
            Logger.logs(null, "Current scene is null in GithubPosts!", null);
            return;
        }
        switchToGithubPosts(Objects.requireNonNull(setMenuBar(AppState.getInstance().getCurrentScene())));
    }

    @FXML
    private void getProfile() {
        AppState state = AppState.getInstance();
        String username = ProfileSearch.getText();
        validatedUsername = Profile.setUsername(username);
        if (validatedUsername == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            Logger.logs(null, "Username is missing or invalid(method: getProfile).", null);
            return;
        }

        System.out.println("Getting profile for username: " + validatedUsername);
        DatabaseManager.createTables(DatabaseManager.getURL());

        GitHubAPIService service = new GitHubAPIService();
        service.fetchProfile(validatedUsername, state.getApiKey());
    }

    @FXML
    private void getRepositories() {
        AppState state = AppState.getInstance();
        String username = ProfileField.getText();
        validatedUsername = Profile.setUsername(username);
        if (validatedUsername == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            Logger.logs(null, "Username is missing or invalid(method: getRepository).", null);
            return;
        }
        System.out.println("Getting repositories for username: " + validatedUsername);
        DatabaseManager.createTables(DatabaseManager.getURL());
        GitHubAPIService service = new GitHubAPIService();
        service.fetchUserRepos(validatedUsername, state.getApiKey());
    }

    @FXML
    private void getPushes() {
        AppState state = AppState.getInstance();
        String username = ProfileFieldPushes.getText();
        validatedUsername = Profile.setUsername(username);

        if (validatedUsername == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            Logger.logs(null, "Username is missing or invalid(method: getPushes).", null);
            return;
        }

        System.out.println("Getting pushes for username: " + validatedUsername);
        DatabaseManager.createTables(DatabaseManager.getURL());
        GitHubAPIService service = new GitHubAPIService();
        service.fetchPushes(validatedUsername, state.getApiKey());
    }

    @FXML
    private void deleteProfile() {
        AppState state = AppState.getInstance();
        String username = ProfileSearch.getText();
        validatedUsername = Profile.setUsername(username);

        if (validatedUsername == null || !DatabaseManager.doesTableExist(DatabaseManager.connect(DatabaseManager.getURL()), "Profiles")
                || DatabaseManager.isTableEmpty(DatabaseManager.connect(DatabaseManager.getURL()), "Profiles")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            Logger.logs(null, "Username is missing or invalid(method: deleteProfile).", null);
            return;
        }
        System.out.println("Deleting profile for username: " + validatedUsername);
        DatabaseManager.createTables(DatabaseManager.getURL());
        Profile profile = new Profile();
        profile.Delete(validatedUsername);
    }

    @FXML
    private void deleteRepositories() {
        AppState state = AppState.getInstance();
        String username = ProfileField.getText();
        validatedUsername = Profile.setUsername(username);
        if (validatedUsername == null || !DatabaseManager.doesTableExist(DatabaseManager.connect(DatabaseManager.getURL()), "Repositories")
                || DatabaseManager.isTableEmpty(DatabaseManager.connect(DatabaseManager.getURL()), "Repositories")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            Logger.logs(null, "Username is missing or invalid(method: deleteRepository).", null);
            return;
        }
        System.out.println("Deleting repositories for username: " + validatedUsername);
        DatabaseManager.createTables(DatabaseManager.getURL());
        Repos repo = new Repos();
        repo.Delete(validatedUsername);
    }

    @FXML
    private void deletePushes() {
        AppState state = AppState.getInstance();
        String username = ProfileFieldPushes.getText();
        validatedUsername = Profile.setUsername(username);
        if (validatedUsername == null || !DatabaseManager.doesTableExist(DatabaseManager.connect(DatabaseManager.getURL()), "Pushes")
                || DatabaseManager.isTableEmpty(DatabaseManager.connect(DatabaseManager.getURL()), "Pushes")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            Logger.logs(null, "Username is missing or invalid(method: deletePushes).", null);
            return;
        }
        System.out.println("Deleting pushes for username: " + validatedUsername);
        DatabaseManager.createTables(DatabaseManager.getURL());
        Pushes pushes = new Pushes();
        pushes.Delete(validatedUsername);
    }

    public void switchToHomeScene(ActionEvent event) {
        try {

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/HomePage.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("home");
        } catch (IOException e) {
            System.err.println("Error changing to home scene!");
            Logger.logs(null, "Error changing to home scene!", Arrays.asList(e));
        }
    }

    public void switchToHomeSceneMenu(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/HomePage.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("home");
        } catch (IOException e) {
            System.err.println("Error changing to home scene!");
            Logger.logs(null, "Error changing to home scene via menu!", Arrays.asList(e));
        }
    }

    public void switchToProfileScene(MenuBar menuBar) {
        try {

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ProfileScene.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("profile");
        } catch (IOException e) {
            System.err.println("Error changing to profile scene!");
            Logger.logs(null, "Error changing to profile scene!", Arrays.asList(e));
        }
    }

    public void switchToReposScene(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ReposScene.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("repos");
        } catch (IOException e) {
            System.err.println("Error changing to repos scene!");
            Logger.logs(null, "Error changing to repositories scene!", Arrays.asList(e));
        }
    }

    public void switchToPushesScene(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/PushesScene.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("pushes");
        } catch (IOException e) {
            System.err.println("Error changing to pushes scene!");
            Logger.logs(null, "Error changing to pushes scene!", Arrays.asList(e));
        }
    }

    public void switchToProfileDatabase(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/DatabaseProfile.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("profileDatabase");
        } catch (Exception e) {
            System.err.println("Error changing to profile database!");
            Logger.logs(null, "Error changing to profile database scene!", Arrays.asList(e));
        }
    }

    public void switchToPushesDatabase(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/DatabasePushes.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("pushesDatabase");
        } catch (Exception e) {
            System.err.println("Error changing to pushes database!");
            Logger.logs(null, "Error changing to profile database scene!", Arrays.asList(e));
        }
    }

    public void switchToReposDatabase(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/DatabaseRepos.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("reposDatabase");
        } catch (Exception e) {
            System.err.println("Error changing to repos database!");
            Logger.logs(null, "Error changing to profile database scene!", Arrays.asList(e));
        }
    }

    public void switchToGithubPosts(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GithubPosts.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("githubPosts");
        } catch (Exception e) {
            System.err.println("Error changing to github posts!");
            Logger.logs(null, "Error changing to github posts!", Arrays.asList(e));
        }
    }

    public void switchToLogIn(MenuBar menuBar) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LogInScene.fxml")));
            stage = (Stage) menuBar.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            AppState.getInstance().setCurrentScene("login");
        } catch (IOException e) {
            System.err.println("Error changing to login!");
            Logger.logs(null, "Error changing to login scene!", Arrays.asList(e));
        }
    }
}