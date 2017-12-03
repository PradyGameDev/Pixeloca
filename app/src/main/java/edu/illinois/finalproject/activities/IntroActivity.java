package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;

public class IntroActivity extends AppCompatActivity {
    public static final String USERNAME = "username";
    public static final String USERNAME_NOT_SET = "null";
    @BindView(R.id.username)
    TextView usernameTextView;
    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
            (this);
    SharedPreferences.Editor editor = sharedPreferences.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        /*Checks if username already set.
        * If set, go to feed.
        * Else allow user to set it*/
        if (sharedPreferences.getString(USERNAME, USERNAME_NOT_SET).equals(USERNAME_NOT_SET)) {
            Intent openFeedIntent = new Intent(this, FeedActivity.class);
            startActivity(openFeedIntent);
        }
    }

    public void setUsername(View view) {
        String username = usernameTextView.getText()
                .toString();
        if (isValidUsername(username)) {
            editor.putString("username", username);
        } else {
            Toast.makeText(this, "Invalid username.", Toast
                    .LENGTH_SHORT)
                    .show();
        }
    }
    //TODO:Implement username validation

    /**
     * Checks if a username is valid. Valid usernames cannot contain empty strings, and must be
     * less than equal to 15 characters.
     *
     * @param username The username to validate.
     * @return true if the username is valid, else false.
     */
    private boolean isValidUsername(String username) {
        return true;
    }
}
