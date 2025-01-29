package com.example;

public interface Fetchable {
    void fetchProfile(String username, String APItoken);

    void fetchUserRepos(String username, String APItoken);

    void fetchPushes(String username, String APItoken);
}
