package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.processing.DatabaseManager;
import edu.illinois.finalproject.schemas.Post;

import static edu.illinois.finalproject.activities.IntroActivity.USERNAME;
import static edu.illinois.finalproject.activities.IntroActivity.USERNAME_NOT_SET;

public class FeedActivity extends AppCompatActivity {

    public static final String COMMENT_VIEW_OPEN = "Detail View Opened";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_to_refresh_listview)
    PullToRefreshListView pullToRefreshListView;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        pullToRefreshListView.setOnRefreshListener(
                new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        // Do work to refresh the list here.
                        new GetDataTask().execute();
                    }
                });
        welcomeUser();
        DatabaseManager feedDatabase = new DatabaseManager(this, recyclerView);
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
                //Toast.makeText(this, "New post created.", Toast.LENGTH_SHORT)
                //        .show();
                openNextActivity();
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

    private void openNextActivity() {
        Intent openNextActivityIntent = new Intent(this, NewPostActivity.class);
        startActivity(openNextActivityIntent);
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

    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Call onRefreshComplete when the list has been refreshed.
            pullToRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }
}
