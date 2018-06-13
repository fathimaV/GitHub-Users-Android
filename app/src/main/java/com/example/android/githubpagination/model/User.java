package com.example.android.githubpagination.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by icreator on 6/12/18.
 */

public class User {

    @SerializedName("login")
    public String login;
    @SerializedName("avatar_url")
    public String avatar_url;
    @SerializedName("id")
    public String id;

    public User(String login, String avatar_url) {
        this.login = login;
        this.avatar_url = avatar_url;
    }
}
