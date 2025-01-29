package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class DatabaseLoadingProfileGUI implements Initializable {
    @FXML
    private TableView<Profile> ProfileTable;
    @FXML
    private TableColumn<Profile, String> ProfileUsername;
    @FXML
    private TableColumn<Profile, String> ProfileCreationTIme;
    @FXML
    private TableColumn<Profile, String> ProfileLastUpdate;
    @FXML
    private TableColumn<Profile, Integer> ProfilePublicRepos;
    @FXML
    private TableColumn<Profile, Integer> ProfileFollowers;
    @FXML
    private TableColumn<Profile, Integer> ProfileFollowing;
    @FXML
    private TableColumn<Profile, String> ProfileProfileType;
    @FXML
    private TableColumn<Profile, String> ProfileName;
    @FXML
    private TableColumn<Profile, String> ProfileLocation;
    @FXML
    private TableColumn<Profile, String> ProfileCompany;
    @FXML
    private TableColumn<Profile, String> ProfileInsertDate;
    @FXML
    private MenuBar ProfileDatabaseMenuBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ProfileUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        ProfileCreationTIme.setCellValueFactory(new PropertyValueFactory<>("CreationTime"));
        ProfileLastUpdate.setCellValueFactory(new PropertyValueFactory<>("LastUpdate"));
        ProfilePublicRepos.setCellValueFactory(new PropertyValueFactory<>("PublicRepos"));
        ProfileFollowers.setCellValueFactory(new PropertyValueFactory<>("Followers"));
        ProfileFollowing.setCellValueFactory(new PropertyValueFactory<>("Following"));
        ProfileProfileType.setCellValueFactory(new PropertyValueFactory<>("profileType"));
        ProfileName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        ProfileLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        ProfileCompany.setCellValueFactory(new PropertyValueFactory<>("Company"));
        ProfileInsertDate.setCellValueFactory(new PropertyValueFactory<>("InsertDate"));

        System.out.println("Setting items in the TableView...");
        ProfileTable.setItems(LoadProfileDatabase());
    }

    public ObservableList<Profile> LoadProfileDatabase() {
        ObservableList<Profile> data = FXCollections.observableArrayList();

        String sql = "select * from Profiles";
        System.out.println("Loading Profiles...");
        try (Connection conn = DatabaseManager.connect(DatabaseManager.getURL());
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String CreationTime = rs.getString("CreationTime");
                String LastUpdate = rs.getString("LastUpdate");
                Integer PublicRepos = rs.getInt("PublicRepos");
                Integer Followers = rs.getInt("Followers");
                Integer Following = rs.getInt("Following");
                String profileType = rs.getString("profileType");
                String Name = rs.getString("Name");
                String Location = rs.getString("Location");
                String Company = rs.getString("Company");
                String InsertDate = rs.getString("InsertDate");
                data.add(new Profile(username, CreationTime, LastUpdate, PublicRepos, Followers, Following, profileType, Name, Location, Company, InsertDate));
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            Logger.logs(null, "Couldn't get the data for the Profile database TableView", Arrays.asList(e));
        }
        return data;
    }

    public void HomeSceneMenu() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.HomeSceneMenu();
    }

    public void ProfileScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.ProfileScene();
    }

    public void ReposScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.ReposScene();
    }

    public void PushesScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.PushesScene();
    }

    public void ReposDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.ReposDatabaseScene();
    }

    public void PushesDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.PushesDatabaseScene();
    }

    public void GithubPostsScene()
    {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.GithubPostsScene();
    }

    public void LogOut() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(ProfileDatabaseMenuBar);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.LogOut();
    }
}
