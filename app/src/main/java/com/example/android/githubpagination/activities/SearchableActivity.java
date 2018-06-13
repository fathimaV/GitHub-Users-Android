package com.example.android.githubpagination.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.githubpagination.R;
import com.example.android.githubpagination.adapter.GitHubAdapter;
import com.example.android.githubpagination.adapter.SearchUserAdapter;
import com.example.android.githubpagination.model.User;
import com.example.android.githubpagination.model.UsersList;
import com.example.android.githubpagination.utilities.ApiClient;
import com.example.android.githubpagination.utilities.UserInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity {

    UserInterface apiInterface;
    SearchUserAdapter mAdapter;
    public TextView mErrorMessageDisplayTextView;
    private ProgressBar progress;
    private RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    int mTotalResult;
    ArrayList<UsersList.UserData> usersList = new ArrayList<>();
    String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(mQuery != null){
            getSupportActionBar().setTitle(mQuery);
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mErrorMessageDisplayTextView = (TextView)findViewById(R.id.tv_error_message_display);
        progress = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        progress.setVisibility(View.INVISIBLE);
        handleIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            apiInterface = ApiClient.getClient().create(UserInterface.class);
            getData(mQuery);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(String query) {
        // progress.setVisibility(View.VISIBLE);
        /**
         GET List Users
         **/
        Call<UsersList> call2 = apiInterface.getUsersSearchResult(query);

        call2.enqueue(new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call2, Response<UsersList> response) {

                UsersList userList = response.body();

                mTotalResult = userList.total_count;

                if (userList.items.size() == 0 || userList == null) {
                    mErrorMessageDisplayTextView.setText(getResources().getString(R.string.error_message));
                }

                ArrayList<UsersList.UserData> userData = userList.items;
                mAdapter = new SearchUserAdapter(SearchableActivity.this, userData);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.notifyDataSetChanged();
                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                call.cancel();
            }
        });
    }


}
