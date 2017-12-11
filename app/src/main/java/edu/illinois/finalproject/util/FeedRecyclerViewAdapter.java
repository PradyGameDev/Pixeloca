package edu.illinois.finalproject.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.activities.PostDetailViewActivity;
import edu.illinois.finalproject.schemas.Post;

public class FeedRecyclerViewAdapter<P extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<PostViewHolder> {
    public static final int DESIRED_HEIGHT_OF_POST = 400;
    public static final String DETAIL_VIEW_OPEN = "OPEN_DETAILED_VIEW";
    //This Map is used to figure out which row item has been clicked on, so that the detail view
    // is able to display the right information
    Map<String, Integer> postMap = new HashMap<>();
    private Context context;
    private List<Post> posts;

    public FeedRecyclerViewAdapter(Context context, List<Post> posts) {
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
                .inflate(R.layout.post_row_item, null);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context
                                                                                           .WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Log.v("Image URI", posts.get(position)
                    .getImageUri());
            Log.v("Caption", posts.get(position)
                    .getCaption());
            Picasso.with(context)
                    .load(Uri.parse(posts.get(position)
                                            .getImageUri()))
                    .resize(display.getWidth(), (int) (context.getResources()
                            .getDisplayMetrics()
                            .density * DESIRED_HEIGHT_OF_POST))
                    .centerCrop()
                    .into(holder.postThumbnail);
            holder.postCaption.setText(posts.get(position)
                                               .getCaption());
            holder.postUsername.setText(posts.get(position)
                                                .getUsername());
            holder.postLocation.setText(posts.get(position)
                                                .getLocation());
            holder.postDate.setText(posts.get(position)
                                            .getUserDisplayDate());
            holder.postCommentButton.setOnClickListener(view -> {
                Log.v("PostSent", posts.get(position)
                        .toString());
                Intent detailViewIntent = new Intent(context, PostDetailViewActivity.class);
                detailViewIntent
                        .putExtra(DETAIL_VIEW_OPEN, posts
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
