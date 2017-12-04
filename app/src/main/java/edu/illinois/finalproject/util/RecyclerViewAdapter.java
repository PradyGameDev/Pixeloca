package edu.illinois.finalproject.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.activities.PostDetailViewActivity;
import edu.illinois.finalproject.schemas.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private Context context;
    private List<Post> posts;
    //This Map is used to figure out which row item has been clicked on, so that the detail view
    // is able to display the right information
    Map<String, Integer> postMap = new HashMap<>();

    public RecyclerViewAdapter(Context context,
                               List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    /**
     * Inflates a row item and binds views to PostViewHolder. This is only called once.
     * {@inheritDoc}
     */
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item, null);
        return new PostViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.postThumbnail.setImageURI(Uri.parse(posts.get(position)
                                                           .toString()));
        holder.postCaption.setText(posts.get(position)
                                           .getCaption());
        holder.postUsername.setText(posts.get(position)
                                            .getUsername());
        holder.postLocation.setText(posts.get(position)
                                            .getLocation());
        holder.itemView.setOnClickListener(view -> {
            Intent detailViewIntent = new Intent(context, PostDetailViewActivity.class);
            detailViewIntent
                    .putExtra(Post.class.getSimpleName(), posts
                            .get(position));
            view.getContext()
                    .startActivity(detailViewIntent);
        });
        postMap.put(posts.get(position).getImageUri().toString(), position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
