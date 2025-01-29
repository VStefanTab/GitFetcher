package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;

public class GroupReposController {

    @FXML
    private ListView<String> languageListView;  // ListView for displaying languages
    @FXML
    private TableView<Repos> reposTableView;   // TableView for displaying repos
    @FXML
    private TableColumn<Repos, String> repoNameColumn;
    @FXML
    private TableColumn<Repos, String> ownerColumn;

    private String language;
    private Map<String, List<Repos>> groupedRepos;

    public GroupReposController(Map<String, List<Repos>> groupedRepos, String language) {
        this.groupedRepos = groupedRepos;  // Inject grouped repository data
        this.language = language;
    }

    public void initializeComponents() {
        // Initialize TableView and its columns
        reposTableView = new TableView<>();
        TableColumn<Repos, String> nameColumn = new TableColumn<>("Repository Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("repoName"));

        TableColumn<Repos, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));

        TableColumn<Repos, Integer> starsColumn = new TableColumn<>("Stars");
        starsColumn.setCellValueFactory(new PropertyValueFactory<>("stargazerCount"));

        reposTableView.getColumns().addAll(nameColumn, ownerColumn, starsColumn);
    }

    private void displayRepositories(String language) {
        List<Repos> reposList = groupedRepos.get(language);
        if (reposList != null) {
            System.out.println("Repositories for language " + language + ": " + reposList);
            ObservableList<Repos> reposObservableList = FXCollections.observableArrayList(reposList);
            reposTableView.setItems(reposObservableList);
        } else {
            System.out.println("No repositories found for language: " + language);
            reposTableView.setItems(FXCollections.emptyObservableList());
        }
    }


    public void showGroupReposStage() {
        try {
            // Initialize components if not already done
            if (reposTableView == null) {
                initializeComponents();
            }

            // Display repositories for the pre-set language
            displayRepositories(language);

            // Use a BorderPane for layout
            BorderPane layout = new BorderPane();
            layout.setCenter(reposTableView);

            // Create and set the scene
            Scene scene = new Scene(layout, 900, 600);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Repositories for Language: " + language); // Show the language in the title
            stage.show();
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            Logger.logs(null, "Couldn't show the repos Stage!", Arrays.asList(e));
        }
    }

    public String getLanguage()
    {
        return language;
    }
}
