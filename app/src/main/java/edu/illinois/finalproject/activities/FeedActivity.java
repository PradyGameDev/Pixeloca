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
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.processing.DatabaseManager;

import static edu.illinois.finalproject.activities.IntroActivity.USERNAME;
import static edu.illinois.finalproject.activities.IntroActivity.USERNAME_NOT_SET;

public class FeedActivity extends AppCompatActivity {

    public static final String COMMENT_VIEW_OPEN = "Comment View Opened";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_to_refresh_listview)
    PullToRefreshListView pullToRefreshListView;
    private DatabaseManager feedDatabase;

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
        feedDatabase = new DatabaseManager(this, recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (feedDatabase != null) {
            feedDatabase.updateFeed();
        }
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
                Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivityIntent);
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

    private class GetDataTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Call onRefreshComplete when the list has been refreshed.
            if (recyclerView.getAdapter() != null) {
                recyclerView.getAdapter()
                        .notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            }
            super.onPostExecute(result);
        }
    }
}
