package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GitHubAPIService implements Fetchable {
    private static final String GITHUB_API_URL = "https://api.github.com";

    @Override
    public void fetchUserRepos(String username, String APItoken) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String reposUrl = GITHUB_API_URL + "/users/" + username + "/repos";

        // this is for the normal stuff
        Request request = new Request.Builder()
                .url(reposUrl)
                .header("Authorization", "token " + APItoken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Request failed: " + response.code());
                throw new ResponseCodeError("response code is ", response.code());
            }

            String jsonData = response.body().string();
            JsonArray reposArray = JsonParser.parseString(jsonData).getAsJsonArray();
            List<Repos> repos = new ArrayList<>();

            for (int i = 0; i < reposArray.size(); i++) {
                JsonObject repoObject = reposArray.get(i).getAsJsonObject();

                String owner = "";
                if (repoObject.has("owner")) {
                    JsonObject repoOwner = repoObject.get("owner").getAsJsonObject();
                    owner = repoOwner.has("login") ? repoOwner.get("login").getAsString()
                            : "No owner found";
                }

                String repoName = repoObject.get("name").getAsString();
                String repoDescription = repoObject.has("description")
                        && !repoObject.get("description").isJsonNull()
                        ? repoObject.get("description").getAsString()
                        : "No description";

                String repoUrl = repoObject.has("html_url") ? repoObject.get("html_url").getAsString()
                        : "No Url";

                Integer stargazer_count = repoObject.has("stargazers_count")
                        ? repoObject.get("stargazers_count").getAsInt()
                        : 0;
                Integer watchers_count = repoObject.has("watchers_count")
                        ? repoObject.get("watchers_count").getAsInt()
                        : 0;

                String language = repoObject.has("language") && !repoObject.get("language").isJsonNull()
                        ? repoObject.get("language").getAsString()
                        : "Unknown language";

                String license = repoObject.has("license")
                        && !repoObject.get("license").isJsonNull()
                        ? (repoObject.get("license").isJsonObject()
                        ? "Forked Repo"
                        : repoObject.get("license")
                        .getAsString())
                        : "No license";

                String created_at = repoObject.has("created_at")
                        ? repoObject.get("created_at").getAsString()
                        : "No creation date";
                String updated_at = repoObject.has("updated_at")
                        ? repoObject.get("updated_at").getAsString()
                        : "No update date";
                String pushed_at = repoObject.has("pushed_at")
                        ? repoObject.get("pushed_at").getAsString()
                        : "No push date";

                String clone_url = repoObject.has("clone_url")
                        ? repoObject.get("clone_url").getAsString()
                        : "No clone url";
                Boolean allow_forking = repoObject.get("allow_forking").getAsBoolean();
                Integer forks = repoObject.has("forks") ? repoObject.get("forks").getAsInt() : 0;

                // this is the default value of sha, but hopefully everything goes smoothly and
                // it will be replaced
                String sha = "Couldn't be obtained!";

                if (repoObject.has("git_refs_url")) {
                    Request request2 = new Request.Builder()
                            .url("https://api.github.com/repos/" + username + "/" + repoName + "/git/refs")
                            .header("Authorization", "token " + APItoken)
                            .build();
                    try (Response response2 = client.newCall(request2).execute()) {
                        if (!response2.isSuccessful()) {
                            System.err.println("Couldn't get the sha for the repository " + repoName + "!");
                        } else {
                            String jsonData2 = response2.body().string();
                            JsonArray repoRefArray = JsonParser.parseString(jsonData2).getAsJsonArray();

                            for (int j = 0; j < repoRefArray.size(); j++) {
                                JsonObject refObject = repoRefArray.get(j).getAsJsonObject();

                                if (refObject.has("object")) {
                                    JsonObject object = refObject.get("object").getAsJsonObject();
                                    sha = object.has("sha") ? object.get("sha").getAsString() : "No SHA found";
                                }
                            }
                        }
                    } catch (IOException e) {
                        Logger.logs(null, "Error during fetching the repos", Arrays.asList(e));
                    }
                }

                repos.add(new Repos(owner, repoName, repoDescription, repoUrl, stargazer_count,
                        watchers_count, language, license,
                        created_at, updated_at, pushed_at, clone_url, allow_forking, forks,
                        sha));
            }

            for (Repos rep : repos) {
                System.out.println();

                // Check if the repository already exists in the database
                if (rep.doesFieldExist(rep.getOwner(), rep.getRepoName())) {
                    rep.Update();
                    System.out.println("Repository updated successfully!\n");
                } else {
                    rep.Insert();
                    System.out.println("Repository added successfully!\n");
                }

                System.out.println(rep.getValue() + "\n");
                Logger.logs(null, "Repositories", null, repos.size());
                Logger.iterator++;// Do I need it anymore? Kinda
            }

        } catch (IOException ioe) {
            System.err.println("An I/O error occurred while making the request: " + ioe.getMessage());
            Logger.logs(null, "API Service Repositories", Arrays.asList(ioe));
        } catch (ResponseCodeError re) {
            System.err.println("Connection failed: " + re.getMessage());
            Logger.logs(null, "API Service Repositories", Arrays.asList(re));
        }
    }

    @Override
    public void fetchProfile(String username, String APItoken) {
        OkHttpClient client = new OkHttpClient();

        // Creating the url
        String url = GITHUB_API_URL + "/users/" + username; // Fixed the URL

        // Requesting the access
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "token " + APItoken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error code: " + response.code());
                throw new ResponseCodeError("", response.code());
            }

            String jsonData = response.body().string();
            JsonObject profileObject = JsonParser.parseString(jsonData).getAsJsonObject();

            String Username = profileObject.has("login") && !profileObject.get("login").isJsonNull()
                    ? profileObject.get("login").getAsString()
                    : "Unknown";

            String CreationTime = profileObject.has("created_at")
                    && !profileObject.get("created_at").isJsonNull()
                    ? profileObject.get("created_at").getAsString()
                    : "Unknown";

            String LastUpdate = profileObject.has("updated_at")
                    && !profileObject.get("updated_at").isJsonNull()
                    ? profileObject.get("updated_at").getAsString()
                    : "Unknown";

            Integer PublicRepos = profileObject.has("public_repos")
                    && !profileObject.get("public_repos").isJsonNull()
                    ? profileObject.get("public_repos").getAsInt()
                    : 0;

            Integer Followers = profileObject.has("followers")
                    && !profileObject.get("followers").isJsonNull()
                    ? profileObject.get("followers").getAsInt()
                    : 0;

            Integer Following = profileObject.has("following")
                    && !profileObject.get("following").isJsonNull()
                    ? profileObject.get("following").getAsInt()
                    : 0;

            String profileType = profileObject.has("type") && !profileObject.get("type").isJsonNull()
                    ? profileObject.get("type").getAsString()
                    : "Unknown";

            String Name = profileObject.has("name") && !profileObject.get("name").isJsonNull()
                    ? profileObject.get("name").getAsString()
                    : "Unknown";

            String Location = profileObject.has("location") && !profileObject.get("location").isJsonNull()
                    ? profileObject.get("location").getAsString()
                    : "Unknown";

            String Company = profileObject.has("company") && !profileObject.get("company").isJsonNull()
                    ? profileObject.get("company").getAsString()
                    : "Unknown";

            Profile profile = new Profile(Username, CreationTime, LastUpdate, PublicRepos, Followers,
                    Following, profileType, Name, Location,
                    Company);

            // Printing the info in the console
            profile.printValues(profile);

            // Printing inside the logs that it was done successfully
            Logger.logs(null, "Profile", null);

            if (!profile.doesFieldExist(username)) {
                profile.Insert();
            } else {
                profile.Update();
            }


        } catch (ResponseCodeError re) {
            Logger.logs(null, "API Service Profile", Arrays.asList(re));
            re.printStackTrace();
        } catch (IOException ioe) {
            // Handle IOException, which occurs during HTTP request execution
            System.err.println("An I/O error occurred while making the request: " + ioe.getMessage());
            Logger.logs(null, "API Service Profile", Arrays.asList(ioe)); // Log the IOException as
        }
    }

    @Override
    public void fetchPushes(String username, String APItoken) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String PushesUrl = GITHUB_API_URL + "/users/" + username + "/events?per_page=100&page=1";

        Request request = new Request.Builder()
                .url(PushesUrl)
                .header("Authorization", "token " + APItoken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error code: " + response.code());
                throw new ResponseCodeError("Response code is ", response.code());
            }
            assert response.body() != null;
            String jsonData = response.body().string();
            JsonArray pushArray = JsonParser.parseString(jsonData).getAsJsonArray();
            List<Pushes> pushes = new ArrayList<>();

            String repoName = "";
            String authorName = "";
            String branch = "";
            Integer commitsNumber;
            String commitSHA = "";
            String commitMessage = "";
            String authorEmail = "";
            String createdAt = "";
            for (int i = 0; i < pushArray.size(); i++) {
                JsonObject pushObject = pushArray.get(i).getAsJsonObject();

                String type = pushObject.has("type") ? pushObject.get("type").getAsString()
                        : "";

                if ("PushEvent".equals(type)) {
                    JsonObject actor = pushObject.getAsJsonObject("actor");
                    String user = actor.has("login") ? actor.get("login").getAsString()
                            : "";

                    JsonObject repo = pushObject.getAsJsonObject("repo");
                    repoName = repo.has("name") ? repo.get("name").getAsString()
                            : "";

                    JsonObject payload = pushObject.getAsJsonObject("payload");
                    branch = payload.has("ref") ? payload.get("ref").getAsString()
                            : "";

                    commitsNumber = payload.has("size") ? payload.get("size").getAsInt() : 0;
                    JsonArray commits = payload.has("commits")
                            ? payload.getAsJsonArray("commits")
                            : new JsonArray();

                    for (int j = 0; j < commits.size(); j++) {
                        JsonObject commit = commits.get(j).getAsJsonObject();
                        commitSHA = commit.has("sha")
                                ? commit.get("sha").getAsString()
                                : "";
                        commitMessage = commit.has("message")
                                ? commit.get("message").getAsString()
                                : "";
                        JsonObject author = commit.has("author")
                                ? commit.getAsJsonObject("author")
                                : new JsonObject();
                        authorName = author.has("name")
                                ? author.get("name").getAsString()
                                : "";
                        authorEmail = author.has("email")
                                ? author.get("email").getAsString()
                                : "";

                        // Create and add push details
                        System.out.println("Repo: " + repoName + ", Branch: " + branch +
                                ", Commit SHA: " + commitSHA + ", Message: "
                                + commitMessage +
                                ", Author: " + authorName + " (" + authorEmail
                                + ")");
                        createdAt = pushObject.has("created_at")
                                ? pushObject.get("created_at").getAsString()
                                : "";
                        pushes.add(new Pushes(user, authorName, repoName, authorEmail, branch,
                                commitsNumber, commitSHA,
                                commitMessage, createdAt));
                    }
                }
            }
            for (Pushes pushi : pushes) {
                if (!pushi.doesFieldExist()) {
                    pushi.Insert();
                    System.out.println("Push event was added successfully into the table!\n");
                } else {
                    pushi.Update();
                    System.out.println("Pushes table was updated successfully!\n");
                }
            }
        } catch (ResponseCodeError re) {
            Logger.logs(null, "API couldn't be accessed while trying to fetch the pushes", Arrays.asList(re));
        } catch (IOException ioE) {
            System.err.println("An I/O error occurred while making the request: " + ioE.getMessage());
            Logger.logs(null, "I/O error inside the fetchPushes", Arrays.asList(ioE));
        }
    }
}