package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DatabaseLoadingPushesGUI implements Initializable {
    @FXML
    private MenuBar PushesDatabaseMenuBar;
    @FXML
    private TableColumn<Pushes, String> PushesUsername;
    @FXML
    private TableColumn<Pushes, String> PushesRepoName;
    @FXML
    private TableColumn<Pushes, String> PushesAuthorName;
    @FXML
    private TableColumn<Pushes, String> PushesEmail;
    @FXML
    private TableColumn<Pushes, String> PushesBranch;
    @FXML
    private TableColumn<Pushes, Integer> PushesCommitNumber;
    @FXML
    private TableColumn<Pushes, String> PushesCommitSha;
    @FXML
    private TableColumn<Pushes, String> PushesCommitMessage;
    @FXML
    private TableColumn<Pushes, String> PushesCreatedAt;
    @FXML
    private TableColumn<Pushes, String> PushesInsertedAt;
    @FXML
    private TableView<Pushes> PushesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PushesUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        PushesAuthorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        PushesRepoName.setCellValueFactory(new PropertyValueFactory<>("repoName"));
        PushesEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        PushesBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        PushesCommitNumber.setCellValueFactory(new PropertyValueFactory<>("commitsNumber"));
        PushesCommitSha.setCellValueFactory(new PropertyValueFactory<>("commitSHA"));
        PushesCommitMessage.setCellValueFactory(new PropertyValueFactory<>("commitMessage"));
        PushesCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        PushesInsertedAt.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));

        System.out.println("Loading Pushes...");
        PushesTable.setItems(LoadPushesDatabase());
    }

    public ObservableList<Pushes> LoadPushesDatabase() {
        ObservableList<Pushes> data = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Pushes";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String repoName = rs.getString("repoName");
                String authorName = rs.getString("authorName");
                String email = rs.getString("email");
                String branch = rs.getString("branch");
                Integer commitsNumber = rs.getInt("commitsNumber");
                String commitSHA = rs.getString("commitSHA");
                String commitMessage = rs.getString("commitMessage");
                String createdAt = rs.getString("createdAt");
                String insertedAt = rs.getString("insertedAt");

                data.add(new Pushes(username, repoName, authorName, email, branch, commitsNumber, commitSHA, commitMessage, createdAt, insertedAt));
            }
        } catch (Exception e) {
            Logger.logs(null, "Couldn't load the table view for Pushes!", Arrays.asList(e));
        }
        return data;
    }

    @FXML
    private void HomeSceneMenu() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.HomeSceneMenu();
    }

    @FXML
    private void ProfileScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.ProfileScene();
    }

    @FXML
    private void ReposScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.ReposScene();
    }

    @FXML
    private void PushesScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.PushesScene();
    }

    @FXML
    private void ProfileDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.ProfileDatabaseScene();
    }

    @FXML
    private void ReposDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.ReposDatabaseScene();
    }

    public void LogOut() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.LogOut();
    }

    public void GithubPostsScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(PushesDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("pushesDatabase");
        sceneController.GithubPostsScene();
    }
}
