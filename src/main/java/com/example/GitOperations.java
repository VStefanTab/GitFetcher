package com.example;

import com.mysql.cj.log.Log;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class GitOperations {
    private String repoDir;
    private String branch;
    private String commitMessage;
    private String addFile;
    private String username;
    private String token;
    private String remoteURL;

    public GitOperations(String repoDir, String remoteURL, String branch, String commitMessage, String addFile, String username, String token) {
        this.repoDir = repoDir;
        this.remoteURL = remoteURL;
        this.branch = branch;
        this.commitMessage = commitMessage;
        this.addFile = addFile;
        this.username = username;
        this.token = token;
    }

    public void initRepository(boolean forceReinitialize) {
        try {
            File repoPath = new File(repoDir);
            File gitDir = new File(repoPath, ".git");

            if (!repoPath.exists()) {
                System.out.println("The specified directory does not exist: " + repoDir);
                return;
            }

            if (gitDir.exists()) {
                if (forceReinitialize) {
                    deleteDirectory(gitDir);
                    System.out.println(".git directory removed for reinitialization.");
                } else {
                    System.out.println("The directory is already a Git repository at " + repoDir);
                    return;
                }
            }

            Git.init().setDirectory(repoPath).call();
            System.out.println("Git repository initialized at " + repoDir);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.logs(null, "Couldn't do 'git init'!", Arrays.asList(e));
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
            directory.delete();
        }
    }

    public void addFiles(String filePath) {
        try {
            File repoPath = new File(repoDir);
            Git git = Git.open(repoPath);

            // Debug: Check Git status before adding files
            Status status = git.status().call();
            System.out.println("🔍 Checking Git status BEFORE add...");
            System.out.println("Untracked files: " + status.getUntracked());

            AddCommand addCommand = git.add();

            // If no file is specified, add all files
            if (filePath == null || filePath.trim().isEmpty()) {
                System.out.println("⚠️ No file specified. Adding all files.");
                addCommand.addFilepattern(".");  // Add all files
            } else {
                // Convert absolute path to relative path within repo
                Path repoPathObj = Paths.get(repoDir);
                Path filePathObj = Paths.get(filePath);
                String relativePath = repoPathObj.relativize(filePathObj).toString();

                System.out.println("📌 Adding file: " + relativePath);
                addCommand.addFilepattern(relativePath);
            }

            addCommand.call();

            // Debug: Check Git status after adding files
            status = git.status().call();
            System.out.println("🔍 Checking Git status AFTER add...");
            System.out.println("Untracked files: " + status.getUntracked());
            System.out.println("Staged files: " + status.getAdded());

            System.out.println("✅ Files successfully added to staging.");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logs(null, "Couldn't do the 'git add'!", Arrays.asList(e));
        }
    }




    public void commitChanges(String message) {
        try {
            File repoPath = new File(repoDir);
            Git git = Git.open(repoPath);

            Status status = git.status().call();
            if (status.isClean()) {
                System.out.println("No changes to commit.");
                return;
            }

            CommitCommand commitCommand = git.commit();
            commitCommand.setMessage(message);
            commitCommand.call();
            System.out.println("Changes committed with message: " + message);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.logs(null, "Couldn't do the 'git commit'!", Arrays.asList(e));
        }
    }

    public void pushToGitHub() {
        try {
            File repoPath = new File(repoDir);
            Git git = Git.open(repoPath);

            // Debugging statements
            System.out.println("Pushing changes to remote: " + remoteURL);
            System.out.println("Branch: " + branch);
            System.out.println("Using username: " + username);

            // Set up credentials for GitHub (using Personal Access Token)
            UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, token);

            // Push to remote repository
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(credentialsProvider);
            pushCommand.setRemote(remoteURL);

            // Ensuring we push to the correct branch
            pushCommand.setRefSpecs(new RefSpec(branch + ":" + branch));

            pushCommand.call();

            System.out.println("Changes pushed successfully to " + remoteURL + " on branch " + branch);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logs(null, "Couldn't do the 'git push'!", Arrays.asList(e));
        }
    }

}