package edu.illinois.finalproject.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.schemas.Comment;
import edu.illinois.finalproject.schemas.Post;
import edu.illinois.finalproject.util.CommentRecyclerViewAdapter;

public class CommentViewActivity extends AppCompatActivity {

    @BindView(R.id.add_comment_button)
    FloatingActionButton addCommentButton;
    RecyclerView commentRecyclerView;
    private Post tappedPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);
        ButterKnife.bind(this);
        tappedPost = getIntent().getParcelableExtra(FeedActivity.COMMENT_VIEW_OPEN);
        List<Comment> commentList = tappedPost.getCommentList();
        CommentRecyclerViewAdapter commentRecyclerViewAdapter = new CommentRecyclerViewAdapter
                (this, commentList);
        commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        addCommentButton.setOnClickListener(view -> {

        });
    }
}
