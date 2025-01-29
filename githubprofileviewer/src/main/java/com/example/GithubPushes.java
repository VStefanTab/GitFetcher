package com.example;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class GithubPushes {
    @FXML
    private TextField RemoteURL;
    @FXML
    private TextField FileLocation;
    @FXML
    private TextField Branch;
    @FXML
    private TextField CommitMessage;
    @FXML
    private TextField Add;
    @FXML
    private TextField UsernameCredential;
    @FXML
    private MenuBar MenuBarPosts;

    public void commitAndPushChanges()  {
        String repoDir = FileLocation.getText();
        String remoteURL = RemoteURL.getText();
        String branch = Branch.getText();
        String commitMessage = CommitMessage.getText();
        String addFile = Add.getText();
        String username = UsernameCredential.getText();
        String token = AppState.getInstance().getApiKey();

        try {
            // Open the local repository
            Git git = Git.open(new File(repoDir));

            // Add files to staging area
            AddCommand add = git.add();
            add.addFilepattern(addFile); // Use the file pattern (e.g., "*" for all files)
            add.call();
            System.out.println("Files added to staging area.");

            // Commit changes with the specified message
            CommitCommand commit = git.commit();
            commit.setMessage(commitMessage);  // Use the hardcoded commit message
            commit.call();
            System.out.println("Changes committed with message: " + commitMessage);

            // Push changes to remote repository
            PushCommand push = git.push();
            push.setRemote(remoteURL);

            // Set credentials for GitHub push (use your username and token)
            UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, token);
            push.setCredentialsProvider(credentialsProvider);
            push.call();
            System.out.println("Changes pushed successfully to " + remoteURL + " on branch " + branch);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while performing Git operations.");
        }
    }

    // Method to show window for adding files
    private void showAddFilesWindow() {
        Stage addFilesStage = new Stage();
        VBox vbox = new VBox();

        TextArea addFilesTextArea = new TextArea();
        addFilesTextArea.setPromptText("Enter files to add (e.g., * for all files)");
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(event -> {
            // Store the files added from the text area
            String filesToAdd = addFilesTextArea.getText();
            Add.setText(filesToAdd); // Set the "Add" text field with the selected files
            addFilesStage.close();   // Close the add files window
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> addFilesStage.close());

        vbox.getChildren().addAll(addFilesTextArea, confirmButton, cancelButton);

        Scene scene = new Scene(vbox, 300, 200);
        addFilesStage.setTitle("Add Files to Git");
        addFilesStage.setScene(scene);
        addFilesStage.show();
    }

    // Method to clear fields and GitOperations object
    public void clearFields() {
        FileLocation.clear();
        RemoteURL.clear();
        Branch.clear();
        CommitMessage.clear();
        Add.clear();
        UsernameCredential.clear();

        //AppState.getInstance().clearGitOperations(); // Clear the stored GitOperations in singleton
    }

    // Utility function to show alerts for basic info/error messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void HomeSceneMenu() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(MenuBarPosts);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.HomeSceneMenu();
    }

    public void ProfileScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(MenuBarPosts);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.ProfileScene();
    }

    public void ReposScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(MenuBarPosts);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.ReposScene();
    }

    public void PushesScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(MenuBarPosts);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.PushesScene();
    }

    public void ReposDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(MenuBarPosts);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.ReposDatabaseScene();
    }

    public void PushesDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(MenuBarPosts);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.PushesDatabaseScene();
    }

    public void LogOut() {
        SceneController sceneController = new SceneController();
        sceneController.LogOut();
    }

    public void ProfileDatabaseScene() {
        SceneController sceneController = new SceneController();
        sceneController.setMenuBar(MenuBarPosts);
        AppState.getInstance().setCurrentScene("profileDatabase");
        sceneController.ProfileDatabaseScene();
    }
}