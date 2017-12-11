package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;

import static edu.illinois.finalproject.activities.IntroActivity.USERNAME;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.username_edit_text)
    EditText usernameEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (IntroActivity.isValidUsername(username)) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(
                                @NonNull
                                        Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "User profile updated.",
                                               Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
            editor.putString(USERNAME, username);
            editor.apply();
            goToFeed();
        } else {
            Toast.makeText(this, "Invalid display name.", Toast
                    .LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Called when the log out button is tapped on.
     *
     * @param v The log out button.
     */
    public void logOut(View v) {
        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestProfile()
                        .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(
                            @NonNull
                                    Task<Void> task) {
                        Toast.makeText(SettingsActivity.this, "You have been signed out.", Toast
                                .LENGTH_SHORT)
                                .show();
                        goToSignInScreen();
                    }
                });
    }

    private void goToSignInScreen() {
        Intent goToSignInScreenIntent = new Intent(this, IntroActivity.class);
        startActivity(goToSignInScreenIntent);
    }

    private void goToFeed() {
        Intent goToFeedIntent = new Intent(this, FeedActivity.class);
        startActivity(goToFeedIntent);
    }
}
