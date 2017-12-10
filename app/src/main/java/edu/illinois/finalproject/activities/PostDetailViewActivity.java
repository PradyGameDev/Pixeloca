package edu.illinois.finalproject.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.schemas.Post;
import edu.illinois.finalproject.util.RecyclerViewAdapter;

public class PostDetailViewActivity extends AppCompatActivity {
    @BindView(R.id.display_image)
    ImageView displayImage;
    @BindView(R.id.display_caption)
    TextView displayCaption;
    @BindView(R.id.display_name)
    TextView displayName;
    @BindView(R.id.display_location)
    TextView displayLocation;
    private int rotationInDegrees = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail_view);
        ButterKnife.bind(this);
        Post tappedPost = getIntent().getParcelableExtra(RecyclerViewAdapter.DETAIL_VIEW_OPEN);
        Log.v("PostReceived", tappedPost.toString());
        Picasso.with(this)
                .load(Uri.parse(tappedPost.getImageUri()))
                .rotate(rotationInDegrees)
                .into(displayImage);
        displayCaption.setText(tappedPost.getCaption());
        displayName.setText(tappedPost.getUsername());
        displayLocation.setText(tappedPost.getLocation());
    }
}
