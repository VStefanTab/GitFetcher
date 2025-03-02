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

public class DatabaseLoadingReposGUI implements Initializable {
    @FXML
    private TableView<Repos> ReposTable;
    @FXML
    private MenuBar ReposDatabaseMenuBar;
    @FXML
    private TableColumn<Repos, String> ReposRepoName;
    @FXML
    private TableColumn<Repos, String> ReposDescription;
    @FXML
    private TableColumn<Repos, String> ReposUrl;
    @FXML
    private TableColumn<Repos, Integer> ReposStars;
    @FXML
    private TableColumn<Repos, String> ReposLanguage;
    @FXML
    private TableColumn<Repos, Integer> ReposWatcher;
    @FXML
    private TableColumn<Repos, String> ReposCreatedAt;
    @FXML
    private TableColumn<Repos, String> ReposUpdatedAt;
    @FXML
    private TableColumn<Repos, String> ReposPushedAt;
    @FXML
    private TableColumn<Repos, String> ReposLicense;
    @FXML
    private TableColumn<Repos, String> ReposCloneUrl;
    @FXML
    private TableColumn<Repos, Boolean> ReposAllowForking;
    @FXML
    private TableColumn<Repos, Integer> ReposForks;
    @FXML
    private TableColumn<Repos, String> ReposSHA;
    @FXML
    private TableColumn<Repos, String> ReposInsertedAt;
    @FXML
    private TableColumn<Repos, String> ReposUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ReposRepoName.setCellValueFactory(new PropertyValueFactory<>("RepoName"));
        ReposDescription.setCellValueFactory(new PropertyValueFactory<>("RepoDescription"));
        ReposUrl.setCellValueFactory(new PropertyValueFactory<>("Url"));
        ReposStars.setCellValueFactory(new PropertyValueFactory<>("StargazerCount"));
        ReposWatcher.setCellValueFactory(new PropertyValueFactory<>("WatchersCount"));
        ReposLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        ReposLicense.setCellValueFactory(new PropertyValueFactory<>("license"));
        ReposCreatedAt.setCellValueFactory(new PropertyValueFactory<>("CreatedAt"));
        ReposUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("UpdatedAt"));
        ReposPushedAt.setCellValueFactory(new PropertyValueFactory<>("PushedAt"));
        ReposCloneUrl.setCellValueFactory(new PropertyValueFactory<>("CloneUrl"));
        ReposAllowForking.setCellValueFactory(new PropertyValueFactory<>("AllowForking"));
        ReposForks.setCellValueFactory(new PropertyValueFactory<>("Forks"));
        ReposSHA.setCellValueFactory(new PropertyValueFactory<>("SHA"));
        ReposInsertedAt.setCellValueFactory(new PropertyValueFactory<>("InsertedAt"));
        ReposUsername.setCellValueFactory(new PropertyValueFactory<>("owner"));

        // Load the data from the database and set it in the TableView
        ReposTable.setItems(LoadReposDatabase());
    }


    public ObservableList<Repos> LoadReposDatabase() {
        ObservableList<Repos> data = FXCollections.observableArrayList();

        String sql = "select * from Repositories";

        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                String repoName = rs.getString("RepoName");
                String Description = rs.getString("Description");
                String URL = rs.getString("URL");
                Integer Stars = rs.getInt("Stars");
                Integer Watchers = rs.getInt("Watchers");
                String Language = rs.getString("Language");
                String License = rs.getString("License");
                String CreatedAt = rs.getString("CreatedAt");
                String UpdatedAt = rs.getString("UpdatedAt");
                String PushedAt = rs.getString("PushedAt");
                String CloneURL = rs.getString("CloneURL");
                Boolean AllowForking = rs.getBoolean("AllowForking");
                Integer Forks = rs.getInt("Forks");
                String SHA = rs.getString("SHA");
                String InsertDate = rs.getString("InsertDate");
                String username = rs.getString("username");
                data.add(new Repos(username, repoName, Description, URL, Stars, Watchers, Language, License, CreatedAt, UpdatedAt, PushedAt, CloneURL, AllowForking, Forks, SHA, InsertDate));
            }
        } catch (Exception e) {
            Logger.logs(null, "Couldn't get the data for the Profile database TableView", Arrays.asList(e));
        }
        return data;
    }

    public void PushesDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.PushesDatabaseScene();
    }

    public void ProfileDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.ProfileDatabaseScene();
    }

    public void ReposScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.ReposScene();
    }

    public void PushesScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.PushesScene();
    }

    public void ProfileScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.ProfileScene();
    }

    public void HomeSceneMenu() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.HomeSceneMenu();
    }

    public void LogOut() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.LogOut();
    }

    public void GithubPostsScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ReposDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("reposDatabase");
        sceneController.GithubPostsScene();
    }
}
