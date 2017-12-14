package edu.illinois.finalproject.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.processing.DatabaseManager;
import edu.illinois.finalproject.schemas.Comment;
import edu.illinois.finalproject.schemas.Post;
import edu.illinois.finalproject.util.CommentRecyclerViewAdapter;
import edu.illinois.finalproject.util.FeedRecyclerViewAdapter;

import static edu.illinois.finalproject.activities.IntroActivity.USERNAME;
import static edu.illinois.finalproject.activities.IntroActivity.USERNAME_NOT_SET;

public class CommentViewActivity extends AppCompatActivity {

    @BindView(R.id.comment_recycler_view)
    RecyclerView commentRecyclerView;
    @BindView(R.id.add_comment_button)
    Button addCommentButton;
    @BindView(R.id.new_comment_textview)
    TextView newCommentTextView;
    private Post tappedPost;
    private CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);
        ButterKnife.bind(this);
        tappedPost = getIntent().getParcelableExtra(FeedActivity.COMMENT_VIEW_OPEN);
        //Retrieve the post from the database using data from tappedPost
        databaseManager = new DatabaseManager(this);
        feedRecyclerViewAdapter = getIntent().getParcelableExtra(FeedRecyclerViewAdapter
                                                                         .FEED_RECYCLER_VIEW);
        List<Comment> commentList = tappedPost.getCommentList();
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter
                (this, commentList);
        commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        addCommentButton.setOnClickListener(view -> {
            uploadComment(commentList, feedRecyclerViewAdapter);
            newCommentTextView.setText("");
        });
        databaseManager.updateComment(tappedPost, tappedPost.getCommentList(),
                                      commentRecyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (commentRecyclerViewAdapter != null) {
            commentRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void uploadComment(List<Comment> commentList,
                               FeedRecyclerViewAdapter feedRecyclerViewAdapter) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String commenterName = sharedPreferences.getString(USERNAME, USERNAME_NOT_SET);
        String commentText = newCommentTextView.getText()
                .toString()
                .trim();
        Date time = Calendar.getInstance()
                .getTime();
        String internalDate = new SimpleDateFormat(DatabaseManager.INTERNAL_DATE_PATTERN)
                .format(time);
        String displayDate = new SimpleDateFormat(DatabaseManager.USER_DISPLAY_DATE_PATTERN)
                .format(time);
        String userPhotoUri = sharedPreferences.getString(IntroActivity.USER_PHOTO_URI,
                                                          IntroActivity.PHOTO_NOT_SET);
        Comment newComment =
                new Comment(commenterName, commentText, internalDate, displayDate, userPhotoUri);
        //tappedPost.getCommentList()
        //        .add(newComment);
        commentRecyclerViewAdapter.getList()
                .add(newComment);
        commentRecyclerViewAdapter.notifyDataSetChanged();
        //this.commentRecyclerView.setAdapter(
        //        new CommentRecyclerViewAdapter(this, tappedPost.getCommentList()));
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.updatePost(tappedPost, feedRecyclerViewAdapter);
        databaseManager.updateComment(tappedPost, commentList, commentRecyclerViewAdapter);
    }
}
