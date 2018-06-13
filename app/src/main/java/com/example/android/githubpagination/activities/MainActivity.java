package com.example.android.githubpagination.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.githubpagination.R;
import com.example.android.githubpagination.adapter.GitHubAdapter;
import com.example.android.githubpagination.adapter.UsersAutoCompleteAdapter;
import com.example.android.githubpagination.model.User;
import com.example.android.githubpagination.model.UsersList;
import com.example.android.githubpagination.utilities.ApiClient;
import com.example.android.githubpagination.utilities.UserInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public TextView mErrorMessageDisplayTextView;
    private ProgressBar progress;
    private GitHubAdapter mAdapter;
    UserInterface apiInterface;
    ArrayList<User> mUserArrayList = new ArrayList<>();
    LinearLayoutManager mLayoutManager;
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    int mPageNum = 0 , noOfUsers = 32;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mErrorMessageDisplayTextView = (TextView)findViewById(R.id.tv_error_message_display);
        progress = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        apiInterface = ApiClient.getClient().create(UserInterface.class);

        // Pagination via infinite loading and loading the data as u scroll.
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrolling = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    mPageNum = mPageNum + noOfUsers;
                    System.out.println("mPageNum ******** : "+mPageNum);
                    getData(mPageNum, noOfUsers);
                }
            }
        });
        getData(mPageNum, noOfUsers);

    }


    private void getData(final int pageNum, final int noOfUsers)
    {

        progress.setVisibility(View.VISIBLE);
        /**
         GET List Users
         **/
        Call<ArrayList<User>> call1 = apiInterface.doGetUserList(pageNum, noOfUsers);

        call1.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call1, Response<ArrayList<User>> response) {

                ArrayList<User> userList = response.body();
//                System.out.println("size :" +userList.size());
                if(userList.size() == 0 || userList == null){
                    mErrorMessageDisplayTextView.setText(getResources().getString(R.string.error_message));
                    progress.setVisibility(View.GONE);
                }

                mUserArrayList.addAll(userList);
                mAdapter = new GitHubAdapter(MainActivity.this, mUserArrayList);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.notifyDataSetChanged();
                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                call.cancel();
                mErrorMessageDisplayTextView.setText(getResources().getString(R.string.error_message));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        //SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, SearchableActivity.class);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        searchAutoComplete.setTextColor(this.getResources().getColor(R.color.colorAccent));
//        searchAutoComplete.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        searchAutoComplete.setDropDownWidth(2000);


        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                UsersList.UserData queryString=(UsersList.UserData)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString.login);
                Toast.makeText(MainActivity.this, "you clicked " + queryString.login, Toast.LENGTH_LONG).show();
            }
        });

        //the search auto complete requires minimum 2 characters. Reason: to avoid unecessary backend calls
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                System.out.println("query submit");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length() > 1){
                    getSearchData(newText, searchAutoComplete);

                }else if(newText.trim().length() == 0){

                    searchAutoComplete.setAdapter(null);

                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
        //return true;

    }


    //getting the search results and setting results in the dropdown.
    private void getSearchData(String query, final SearchView.SearchAutoComplete searchAutoComplete) {
        // progress.setVisibility(View.VISIBLE);
        /**
         GET List Users
         **/
        Call<UsersList> call3 = apiInterface.getUsersSearchResult(query);

        call3.enqueue(new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call3, Response<UsersList> response) {

                UsersList userList = response.body();
                if (userList == null || userList.items.size() == 0 ) {
                    mErrorMessageDisplayTextView.setText(getResources().getString(R.string.error_message));
                }

               List<String> list = new ArrayList<String>();
                if(userList != null){
                    for(UsersList.UserData data: userList.items)
                    {
                        list.add(data.login);

                    }

                }
                if(userList != null){
                    UsersAutoCompleteAdapter autoCompleteAdapter = new UsersAutoCompleteAdapter(MainActivity.this, userList.items);
                    searchAutoComplete.setAdapter(autoCompleteAdapter);
                    autoCompleteAdapter.notifyDataSetChanged();

                }else{
                    mErrorMessageDisplayTextView.setText(getResources().getString(R.string.error_message));
                }

            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                System.out.println(call.request().body());
                call.cancel();
            }
        });
    }


    @Override
    public boolean onSearchRequested() {

        return super.onSearchRequested();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //start search dialog
                onSearchRequested();

                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
