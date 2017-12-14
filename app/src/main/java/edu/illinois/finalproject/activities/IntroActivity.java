package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;

public class IntroActivity extends AppCompatActivity {
    public static final String USERNAME = "username";
    public static final String USERNAME_NOT_SET = "USERNAME_NOT_SET_YET";
    public static final int RC_SIGN_IN = 123;
    public static final String USER_NAME = "Username";
    public static final String USER_PHOTO_URI = "User Photo URL";
    public static final String PHOTO_NOT_SET = "USER_PHOTO_NOT_SET_YET";
    public static final String DEFAULT_PROFILE_PICTURE_URI =
            "https://gammarad.fbk.eu/sites/default/files/default_images/avatar.png";
    private static final String FIREBASE_AUTH_TAG = "FirebaseAuth";
    @BindView(R.id.display_name_edittext)
    EditText displayNameEditText;
    @BindView(R.id.usernameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    /**
     * Checks if a username is valid. Valid usernames cannot contain empty strings, and must be
     * less than equal to 15 characters.
     *
     * @param username The username to validate.
     * @return true if the username is valid, else false.
     */
    public static boolean isValidUsername(String username) {
        boolean isValidUsername = true;
        if (username.isEmpty() || username.length() > 15) {
            isValidUsername = false;
        }
        return isValidUsername;
    }

    /**
     * Removes the menu
     *
     * @param menu The toolbar
     * @return False so no menu is rendered
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!(firebaseUser == null || sharedPreferences.getString(USERNAME, USERNAME_NOT_SET)
                .equals(USERNAME_NOT_SET))) {
            openFeed(firebaseUser);
        }
    }

    /**
     * Allows users to create an account with their email address, name and password.
     *
     * @param v Sign up with email button.
     */
    public void signUpWithEmail(View v) {
        if (displayNameEditText.getVisibility() != View.VISIBLE) {
            //Reveals the display name EditText
            displayNameEditText.setVisibility(View.VISIBLE);
        } else {
            String name = displayNameEditText.getText()
                    .toString();
            String email = usernameEditText.getText()
                    .toString();
            String password = passwordEditText.getText()
                    .toString();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(
                                @NonNull
                                        Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(FIREBASE_AUTH_TAG, "createUserWithEmail:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates =
                                        new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(
                                                    @NonNull
                                                            Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(FIREBASE_AUTH_TAG, "User profile " +
                                                            "updated.");
                                                }
                                            }
                                        });

                                openFeed(user);
                            } else {
                                // If sign up fails, display a message to the user.
                                Log.w(FIREBASE_AUTH_TAG, "createUserWithEmail:failure",
                                      task.getException());
                                Toast.makeText(IntroActivity.this, "Authentication failed. " +
                                                       "Your password must be at least 6 " +
                                                       "characters, " +
                                                       "and hard to guess",
                                               Toast.LENGTH_SHORT)
                                        .show();
                            }

                            // ...
                        }
                    });
            displayNameEditText.setVisibility(View.GONE);
        }
    }

    /**
     * Sign in with email when the appropriate button is tapped.
     *
     * @param v The sign in with email button.
     */
    public void onEmailSignIn(View v) {
        String email = usernameEditText.getText()
                .toString();
        String password = passwordEditText.getText()
                .toString();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(
                            @NonNull
                                    Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FIREBASE_AUTH_TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            openFeed(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FIREBASE_AUTH_TAG, "signInWithEmail:failure",
                                  task.getException());
                            Toast.makeText(IntroActivity.this, "Authentication failed. Try " +
                                                   "again.",
                                           Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    /**
     * Allows a user to sign in with their Google account.
     */
    public void signInWithGoogle(View v) {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestProfile()
                        .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //After Google sign-in
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(FIREBASE_AUTH_TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(FIREBASE_AUTH_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(
                            @NonNull
                                    Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FIREBASE_AUTH_TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            openFeed(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FIREBASE_AUTH_TAG, "signInWithCredential:failure",
                                  task.getException());
                            //Toast.makeText(this, "Authentication failed.",
                            //               Toast.LENGTH_SHORT).show();
                            openFeed(null);
                        }

                        // ...
                    }
                });
    }

    /**
     * Opens the FeedView activity.
     *
     * @param user
     */
    public void openFeed(FirebaseUser user) {
        Intent openFeedIntent = new Intent(this, FeedActivity.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, user.getDisplayName());
        if (user.getPhotoUrl() != null) {
            editor.putString(USER_PHOTO_URI, user.getPhotoUrl()
                    .toString());
        } else {
            editor.putString(USER_PHOTO_URI, String.valueOf(
                    Uri.parse(
                            DEFAULT_PROFILE_PICTURE_URI)));
        }
        editor.commit();
        startActivity(openFeedIntent);
    }
}
