package edu.illinois.finalproject.activities;

import android.content.Intent;
import android.media.ExifInterface;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.processing.DatabaseManager;
import edu.illinois.finalproject.processing.LocationHandler;
import edu.illinois.finalproject.schemas.Post;

public class NewPostActivity extends AppCompatActivity {

    @BindView(R.id.capturePhotoButton)
    Button capturePhotoButton;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.locationButton)
    Button locationButton;
    @BindView(R.id.locationTextView)
    TextView locationTextView;
    @BindView(R.id.captionEditText)
    EditText captionEditText;
    @BindView(R.id.postButton)
    Button postButton;
    private Uri imageUri;
    private DatabaseManager databaseManager;
    private LocationHandler locationHandler;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        locationHandler = new LocationHandler(locationTextView, this);
        databaseManager = new DatabaseManager(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onPhotoButtonClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            imageUri = databaseManager.createImageFile();
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

            ExifInterface exif = null;
            int rotationInDegrees = 90;
            Picasso.with(this)
                    .load(imageUri)
                    .rotate(rotationInDegrees)
                    .into(imageView);
            databaseManager.storeImageInFirebase(imageUri, imageView);
        }
    }

    public void onLocationButtonClick(View v) {
        locationHandler.setupLocationGathering();
    }

    /**
     * Called when the Post button is tapped. Combines the data into a Post object and uploads it
     * to Firebase.
     *
     * @param v A reference to the Post button.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onPostButtonClick(View v) {
        String username = PreferenceManager.getDefaultSharedPreferences(this)
                .getString
                        (IntroActivity.USERNAME, IntroActivity.USERNAME_NOT_SET);
        String caption = captionEditText.getText()
                .toString();
        String location = locationTextView.getText()
                .toString();
        String imageLink = databaseManager.getLastImageFirebaseUrl();
        location = locationHandler.getLastKnownLocation();
        Log.d("Location", location);
        databaseManager.createAndUploadPost(new Post(username, imageLink, caption, location));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu2, menu);
        return true;
    }

}