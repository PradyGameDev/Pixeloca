package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.processing.DatabaseManager;
import edu.illinois.finalproject.schemas.Post;

import static edu.illinois.finalproject.activities.IntroActivity.USERNAME;
import static edu.illinois.finalproject.activities.IntroActivity.USERNAME_NOT_SET;

public class FeedActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        welcomeUser();
        DatabaseManager dbManager = new DatabaseManager(this, recyclerView);
        //setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    public void welcomeUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String welcomeString = String.format("Welcome %s", sharedPreferences.getString(USERNAME,
                                                                                       USERNAME_NOT_SET));
        Toast.makeText(this, welcomeString, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_create_new_post: {
                Toast.makeText(this, "New post created.", Toast.LENGTH_SHORT)
                        .show();
                openNewPostActivity();
                //Intent goBackIntent = new Intent(this, FeedActivity.class);
                //startActivity(goBackIntent);
                return true;
            }
            case R.id.action_settings: {
                Toast.makeText(this, "Opened settings.", Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(menuItem);
            }
        }
    }

    private void openNewPostActivity() {
        Intent newPostActivityIntent = new Intent(this, NewPostActivity.class);
        startActivity(newPostActivityIntent);
    }

    class ListItemOnClickListener implements View.OnClickListener {
        private ArrayList<Post> posts;
        //The position of the item in the RecyclerView that was clicked
        private int position;

        public ListItemOnClickListener(ArrayList<Post> posts) {
            this.posts = posts;
        }

        @Override
        public void onClick(View v) {
            position = recyclerView.getChildAdapterPosition(v);
            Intent detailViewIntent = new Intent(v.getContext(), Post.class);
            detailViewIntent
                    .putExtra(Post.class.getSimpleName(), posts
                            .get(position));
            v.getContext()
                    .startActivity(detailViewIntent);
        }
    }
}
