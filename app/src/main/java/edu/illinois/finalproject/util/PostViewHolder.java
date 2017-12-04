package edu.illinois.finalproject.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;

/**
 * When the first item in the RecyclerView needs to be inflated, references to all views are
 * stored in this class to avoid repeated calls to findViewById for the purpose of binding data.
 */
public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.post_thumbnail) ImageView postThumbnail;
    @BindView(R.id.post_caption) TextView postCaption;
    @BindView(R.id.post_username) TextView postUsername;
    @BindView(R.id.post_location) TextView postLocation;

    public PostViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind((Activity) context);
    }

    @Override
    public void onClick(View v) {

    }
}
