package edu.illinois.finalproject.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.schemas.Post;

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
        Post tappedPost = getIntent().getParcelableExtra(FeedActivity.DETAIL_VIEW_OPEN);
        Picasso.with(this)
                .load(Uri.parse(tappedPost.getImageUri()))
                .rotate(rotationInDegrees)
                .centerCrop()
                .into(displayImage);
        displayCaption.setText(tappedPost.getCaption());
        displayName.setText(tappedPost.getUsername());
        displayLocation.setText(tappedPost.getLocation());
    }
}
