package edu.illinois.finalproject.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.illinois.finalproject.R;

/**
 * When the first item in the RecyclerView needs to be inflated, references to all views are
 * stored in this class to avoid repeated calls to findViewById for the purpose of binding data.
 */
public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView postThumbnail;
    TextView postCaption;
    TextView postUsername;
    TextView postLocation;

    public PostViewHolder(View itemView) {
        super(itemView);
        postThumbnail = (ImageView) itemView.findViewById(R.id.post_thumbnail);
        postCaption = (TextView) itemView.findViewById(R.id.post_caption);
        postUsername = (TextView) itemView.findViewById(R.id.post_username);
        postLocation = (TextView) itemView.findViewById(R.id.display_location);
    }

    @Override
    public void onClick(View v) {

    }
}
