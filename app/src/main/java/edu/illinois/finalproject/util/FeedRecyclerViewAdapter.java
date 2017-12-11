package edu.illinois.finalproject.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.activities.CommentViewActivity;
import edu.illinois.finalproject.activities.FeedActivity;
import edu.illinois.finalproject.schemas.Post;

public class FeedRecyclerViewAdapter<P extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<PostViewHolder> implements Parcelable {
    public static final int DESIRED_HEIGHT_OF_POST = 400;
    public static final String FEED_RECYCLER_VIEW = "Feed RecyclerViewAdapter being sent";
    public static final Parcelable.Creator<FeedRecyclerViewAdapter> CREATOR =
            new Parcelable.Creator<FeedRecyclerViewAdapter>() {
                @Override
                public FeedRecyclerViewAdapter createFromParcel(Parcel source) {
                    return new FeedRecyclerViewAdapter(source);
                }

                @Override
                public FeedRecyclerViewAdapter[] newArray(int size) {
                    return new FeedRecyclerViewAdapter[size];
                }
            };
    private Context context;
    private List<Post> posts;

    public FeedRecyclerViewAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    protected FeedRecyclerViewAdapter(Parcel in) {
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
                Intent detailViewIntent = new Intent(context, CommentViewActivity.class);
                detailViewIntent
                        .putExtra(FeedActivity.COMMENT_VIEW_OPEN, posts
                                .get(position));

                detailViewIntent.putExtra(FEED_RECYCLER_VIEW, this);
                view.getContext()
                        .startActivity(detailViewIntent);
            });
        } catch (Exception e) {
            Log.d("ASDF", "Something wrong with post.");
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
