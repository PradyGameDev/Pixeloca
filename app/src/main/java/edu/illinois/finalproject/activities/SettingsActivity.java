package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;

import static edu.illinois.finalproject.activities.IntroActivity.USERNAME;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.username_edit_text)
    EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    public void setUsername(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = usernameEditText.getText()
                .toString();
        if (IntroActivity.isValidUsername(username)) {
            editor.putString(USERNAME, username);
            editor.commit();
            goToFeed();
        } else {
            Toast.makeText(this, "Invalid username.", Toast
                    .LENGTH_SHORT)
                    .show();
        }
    }

    private void goToFeed() {
        Intent goToFeedIntent = new Intent(this, FeedActivity.class);
        startActivity(goToFeedIntent);
    }
}
