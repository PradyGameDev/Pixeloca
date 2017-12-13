package edu.illinois.finalproject.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.processing.DatabaseManager;
import edu.illinois.finalproject.processing.LocationHandler;
import edu.illinois.finalproject.schemas.Post;

public class NewPostActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.locationTextView)
    TextView locationTextView;
    @BindView(R.id.captionEditText)
    EditText captionEditText;
    private Uri imageUri;
    private DatabaseManager photoDatabase;
    private LocationHandler locationHandler;
    private float rotationInDegrees = 90f;
    //User input
    private String username;
    private String caption;
    private String location;
    private String imageLink;
    private Date time;
    private String internalDate;
    private String displayDate;
    private String userPhotoUri;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        photoDatabase = new DatabaseManager(this);
        locationHandler = new LocationHandler(locationTextView, this);
        Log.v("Debug", "Is this even getting called?" + imageUri);
        capturePhoto();
        askForLocationPermissions();
        //Starts trying to get the user's location
        locationHandler.setUpLocationGathering();
    }

    public void askForLocationPermissions() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d("ASDF", "setUpLocationGathering: ");
            this.requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LocationHandler.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu_create_new_post, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_upload_post: {
                onPostButtonClick();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]
            grantResults) {
        Log.d("ASDF", String.format("Request Code: %d", requestCode));
        locationHandler.setAppHasLocationPermissions(
                requestCode == LocationHandler.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
    }
    public void updateLocationTextView(String latestFormattedAddress) {
        captionEditText.setText(latestFormattedAddress);
    }

    /**
     * Called when the Post button on the Toolbar is tapped. Combines the data into a Post object
     * and uploads it to Firebase. Finally, take the user back to the feed activity.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onPostButtonClick() {
        storeUserInput();
        photoDatabase.uploadPost(new Post(username, imageLink, caption, location,
                                          internalDate, displayDate, userPhotoUri));
        goToFeedActivity();
    }

    private void storeUserInput() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences
                .getString(IntroActivity.USERNAME, IntroActivity.USERNAME_NOT_SET);
        caption = captionEditText.getText()
                .toString();
        location = locationTextView.getText()
                .toString();
        imageLink = photoDatabase.getLastImageFirebaseUrl();
        time = Calendar.getInstance()
                .getTime();
        internalDate =
                new SimpleDateFormat(DatabaseManager.INTERNAL_DATE_PATTERN, Locale.getDefault())
                        .format(time);
        displayDate = new SimpleDateFormat(DatabaseManager.USER_DISPLAY_DATE_PATTERN,
                                           Locale.getDefault()).format(time);
        userPhotoUri = sharedPreferences.getString(IntroActivity.USER_PHOTO_URI,
                                                   IntroActivity.PHOTO_NOT_SET);

    }

    private void goToFeedActivity() {
        Intent goToFeedActivityIntent = new Intent(this, FeedActivity.class);
        startActivity(goToFeedActivityIntent);
    }

    private void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            imageUri = photoDatabase.createImageFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, DatabaseManager.CAPTURE_REQUEST_CODE);
        }
    }

    /**
     * The callback after the focus returns to the MainActivity from the Camera app
     *
     * @param requestCode The Camera intent's request code
     * @param resultCode  Represents whether the intent was successful
     * @param data        The reverse intent containing extras such as a thumbnail
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DatabaseManager.CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Picasso.with(this)
                    .load(imageUri)
                    .rotate(rotationInDegrees)
                    .into(imageView);
            photoDatabase.storeImageInFirebase(imageUri);
        }
    }
}