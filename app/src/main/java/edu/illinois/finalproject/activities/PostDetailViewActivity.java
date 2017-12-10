package edu.illinois.finalproject.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.schemas.Post;

public class PostDetailViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail_view);
        Post tappedPost = getIntent().getParcelableExtra(FeedActivity.DETAIL_VIEW_OPEN);

    }
}
