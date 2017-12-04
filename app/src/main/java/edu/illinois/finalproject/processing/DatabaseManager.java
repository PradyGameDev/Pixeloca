package edu.illinois.finalproject.processing;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import edu.illinois.finalproject.schemas.Post;
import edu.illinois.finalproject.util.RecyclerViewAdapter;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Allows auto-syncing of the database, as well as operations to add posts.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class DatabaseManager {
    private Context context;
    //Realtime Database variables
    private FirebaseDatabase firebaseDatabase;
    private static List<Post> posts = new ArrayList<>();
    private DatabaseReference newPostReference;
    private DatabaseReference parentReference;

    //Firebase Storage variables
    private Uri imageUri;
    private String absoluteFilePath;
    public static final String PATTERN = "yyyyMMdd_HHmmss";
    public static final String DOWNLOAD_URL = "downloadURL";
    public static final String IMAGES_SUBTREE = "images/%s";
    public static final int CAPTURE_REQUEST_CODE = 1;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager(Context context, RecyclerView recyclerView) {
        this.context = context;
        final CountDownLatch writeSignal = new CountDownLatch(1);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        parentReference = database.getReference("posts");
        parentReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                posts.add(dataSnapshot.getValue(Post.class));
                RecyclerViewAdapter recyclerViewAdapter =
                        new RecyclerViewAdapter(context, posts);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void createAndUploadPost(Post post) {
        newPostReference =
                firebaseDatabase.getReference("posts/" + Math.abs(new Random().nextLong()));
        newPostReference.setValue(post);
    }

    public static List<Post> getPosts() {
        return posts;
    }

    public Uri createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(PATTERN).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */

                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)      /*
                    directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        absoluteFilePath = image.getAbsoluteFile()
                .getAbsolutePath();
        // Save a file: path for use with ACTION_VIEW intents
        Log.v("Debug", String.valueOf(image.getAbsoluteFile()));
        return FileProvider.getUriForFile(context, "edu.illinois.techdemonstration",
                                          image.getAbsoluteFile());
    }

    /**
     * Attempts to get a  Firebase Cloud Storage reference and store the returned file in a
     */
    public void storeImageInFirebase(Uri imageUri, ImageView imageView) {
        File file = new File(absoluteFilePath);
        //Uri imageUri = Uri.fromFile(file);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootStorageReference = firebaseStorage.getReference();
        StorageReference imageReference =
                rootStorageReference.child(String.format(IMAGES_SUBTREE, file.getName()));
        imageReference.putFile(imageUri)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.v(DOWNLOAD_URL, String.valueOf(downloadUrl));
                                String outputString = String.format("Image was uploaded with URL:" +
                                                                            " %s", downloadUrl
                                                                            .toString());
                                Toast.makeText(imageView.getContext(), outputString,
                                               LENGTH_SHORT)
                                        .show();
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(
                            @NonNull
                                    Exception e) {
                        Toast.makeText(imageView.getContext(), "Image did not upload to Firebase",
                                       LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
