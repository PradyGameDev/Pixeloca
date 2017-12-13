package edu.illinois.finalproject.util;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
    Button postLocation;
    TextView postDate;
    Button postCommentButton;
    ImageView userImageView;

    public PostViewHolder(View itemView) {
        super(itemView);
        postThumbnail = (ImageView) itemView.findViewById(R.id.post_thumbnail);
        postCaption = (TextView) itemView.findViewById(R.id.post_caption);
        postUsername = (TextView) itemView.findViewById(R.id.post_username);
        postLocation = (Button) itemView.findViewById(R.id.display_location);
        postDate = (TextView) itemView.findViewById(R.id.post_date);
        postCommentButton = (Button) itemView.findViewById(R.id.post_comments);
        userImageView = (ImageView) itemView.findViewById(R.id.userImageView);
        postLocation.setOnClickListener(view -> {
            Uri locationUri = Uri.parse(String.format("https://google.com/maps/search/%s",
                                                      postLocation
                                                              .getText()));
            Intent locationIntent = new Intent(Intent.ACTION_VIEW, locationUri);
            if (locationIntent.resolveActivity(view.getContext()
                                                       .getPackageManager()) != null) {
                view.getContext()
                        .startActivity(locationIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
