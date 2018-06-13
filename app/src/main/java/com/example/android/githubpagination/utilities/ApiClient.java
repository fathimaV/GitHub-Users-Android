package com.example.android.githubpagination.utilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by icreator on 6/12/18.
 */

public class ApiClient {

    public static final String BASE_URL = "https://api.github.com";

    private static Retrofit retrofit = null;

    //initailizing retrofit
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
