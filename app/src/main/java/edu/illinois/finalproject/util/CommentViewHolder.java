package edu.illinois.finalproject.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.illinois.finalproject.R;

class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView commenterName;
    public TextView commentText;
    public TextView commentDate;
    public ImageView commenterPhoto;

    public CommentViewHolder(View itemView) {
        super(itemView);
        commenterName = (TextView) itemView.findViewById(R.id.commenter_name);
        commentText = (TextView) itemView.findViewById(R.id.comment_text);
        commentDate = (TextView) itemView.findViewById(R.id.comment_date);
        commenterPhoto = (ImageView) itemView.findViewById(R.id.commenter_photo);
    }

    @Override
    public void onClick(View v) {

    }
}
