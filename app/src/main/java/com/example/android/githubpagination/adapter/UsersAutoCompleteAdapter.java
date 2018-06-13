package com.example.android.githubpagination.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.githubpagination.R;
import com.example.android.githubpagination.model.UsersList;

import java.util.ArrayList;

/**
 * Created by icreator on 6/13/18.
 */

public class UsersAutoCompleteAdapter extends ArrayAdapter<UsersList.UserData> {

    private ArrayList<UsersList.UserData> mUserList;
    private Context mContext;



    public UsersAutoCompleteAdapter(Context context, ArrayList<UsersList.UserData> user){
        super(context, 0, user);
        mUserList = user;
        mContext = context;

    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null){

            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_item,parent,false);
        }
        // Get the data item from filtered list.
        TextView mUserName = listItem.findViewById(R.id.user_name);
        ImageView mImageView = listItem.findViewById(R.id.imageView);

        if(mUserList != null){
            UsersList.UserData userData = mUserList.get(position);
            mUserName.setText(userData.login);
            Glide.with(mContext)
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform().dontAnimate())
                    .setDefaultRequestOptions(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .circleCrop())
                    .load(userData.avatar_url)
                    .into(mImageView);

        }


        return listItem;
    }


}
