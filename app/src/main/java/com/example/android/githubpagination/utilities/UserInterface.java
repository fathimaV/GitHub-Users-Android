package com.example.android.githubpagination.utilities;

import com.example.android.githubpagination.model.User;
import com.example.android.githubpagination.model.UsersList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by icreator on 6/12/18.
 */

public interface UserInterface {

    @GET("/users?")
    Call<ArrayList<User>> doGetUserList(@Query("since") int page, @Query("per_page") int noOfUsers);

    @GET("/search/users?")
    Call<UsersList> getUsersSearchResult(@Query("q") String userName);

}
