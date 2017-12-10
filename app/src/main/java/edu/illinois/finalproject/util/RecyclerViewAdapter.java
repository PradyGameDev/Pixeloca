package edu.illinois.finalproject.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.activities.PostDetailViewActivity;
import edu.illinois.finalproject.schemas.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {
    //This Map is used to figure out which row item has been clicked on, so that the detail view
    // is able to display the right information
    Map<String, Integer> postMap = new HashMap<>();
    private Context context;
    private List<Post> posts;

    public RecyclerViewAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    /**
     * Inflates a row item and binds views to PostViewHolder. This is only called once.
     * {@inheritDoc}
     */
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_item, null);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        try {
            Log.v("Image URI", posts.get(position)
                    .getImageUri());
            Log.v("Caption", posts.get(position)
                    .getCaption());
            Picasso.with(context)
                    .load(Uri.parse(posts.get(position)
                                            .getImageUri()))
                    .into(holder.postThumbnail);
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
        } catch (Exception e) {
            Log.d("ASDF", "Something wrong with post.");
        }
        postMap.put(posts.get(position)
                            .getImageUri(), position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
