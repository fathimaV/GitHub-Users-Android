package com.example.android.githubpagination.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icreator on 6/12/18.
 */

public class UsersList {

    @SerializedName("total_count")
    public Integer total_count;
    @SerializedName("incomplete_results")
    public Boolean incomplete_results;

    @SerializedName("items")
    public ArrayList<UserData> items = new ArrayList<UserData>();

    public class UserData {

        @SerializedName("login")
        public String login;
        @SerializedName("avatar_url")
        public String avatar_url;
        @SerializedName("id")
        public String id;

        public UserData(String login, String avatar_url) {
            this.login = login;
            this.avatar_url = avatar_url;
        }
    }


}
