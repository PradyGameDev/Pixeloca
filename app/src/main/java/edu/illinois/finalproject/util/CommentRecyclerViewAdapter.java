package edu.illinois.finalproject.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.schemas.Comment;

public class CommentRecyclerViewAdapter<P extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<CommentViewHolder> {
    private Context context;
    private List<Comment> commentList;

    public CommentRecyclerViewAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    public List<Comment> getList() {
        return commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.comment_row_item, null);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Log.v("CommentPos", String.valueOf(position));
        Log.v("CommentPos", commentList.get(position)
                .getText());
        Log.v("CommentPos", "-------------------------");
        holder.commenterName.setText(commentList.get(position)
                                             .getName());
        holder.commentText.setText(commentList.get(position)
                                           .getText());
        holder.commentDate.setText(commentList.get(position)
                                           .getDisplayDate());
        Picasso.with(context)
                .load(commentList.get(position)
                              .getUserPhotoUri())
                .into(holder.commenterPhoto);
    }

    @Override
    public int getItemCount() {
        if (commentList == null) {
            return 0;
        }
        return commentList.size();
    }
}
