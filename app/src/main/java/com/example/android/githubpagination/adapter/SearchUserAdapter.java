package com.example.android.githubpagination.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.githubpagination.R;
import com.example.android.githubpagination.model.User;
import com.example.android.githubpagination.model.UsersList;

import java.util.ArrayList;

/**
 * Created by icreator on 6/13/18.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder> {
    ArrayList<UsersList.UserData> mUserList;
    Context mContext;

    public SearchUserAdapter(Context context, ArrayList<UsersList.UserData> user){
        mUserList = user;
        mContext = context;

    }

    @NonNull
    @Override
    public SearchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new SearchUserAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.MyViewHolder holder, int position) {

        if(mUserList != null){
            UsersList.UserData userData = mUserList.get(position);
            holder.mUserName.setText(userData.login);
            Glide.with(mContext)
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform().dontAnimate())
                    .setDefaultRequestOptions(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .circleCrop())
                    .load(userData.avatar_url)
                    .into(holder.mImageView);

        }

    }

    @Override
    public int getItemCount() {
        if(mUserList != null){
            return mUserList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName;
        ImageView mImageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.user_name);
            mImageView = itemView.findViewById(R.id.imageView);
        }
    }
}
