package edu.illinois.finalproject.processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.illinois.finalproject.schemas.Comment;
import edu.illinois.finalproject.schemas.Post;
import edu.illinois.finalproject.util.CommentRecyclerViewAdapter;
import edu.illinois.finalproject.util.FeedRecyclerViewAdapter;
import id.zelory.compressor.Compressor;

/**
 * Manages all database operations of the app.
 * Interfaces between various capplication components and Firebase Realtime DB + Storage
 * Allows auto-syncing of the database, as well as operations to add posts.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class DatabaseManager {
    public static final String INTERNAL_DATE_PATTERN = "yyyy-MM-dd_HHmmss";
    public static final String DOWNLOAD_URL = "downloadURL";
    public static final String IMAGES_SUBTREE = "images/%s";
    public static final int CAPTURE_REQUEST_CODE = 1;
    public static final java.lang.String USER_DISPLAY_DATE_PATTERN = "d MMMM yyyy HH:mm:ss";
    private static List<Post> posts = new ArrayList<>();
    private Context context;
    //Realtime Database variables
    private FirebaseDatabase database;
    private DatabaseReference newPostReference;
    private DatabaseReference parentReference;
    //Firebase Storage variables
    private Uri imageUri;
    private String absoluteFilePath;
    private String lastImageFirebaseUrl;
    private FeedRecyclerViewAdapter<RecyclerView.ViewHolder> feedRecyclerViewAdapter;

    public DatabaseManager(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance();
    }

    //This constructor is used by the Feed RecyclerView
    public DatabaseManager(Context context, RecyclerView recyclerView) {
        this.context = context;
        database = FirebaseDatabase.getInstance();
        parentReference = database.getReference("posts");
        feedRecyclerViewAdapter =
                new FeedRecyclerViewAdapter<RecyclerView.ViewHolder>(context, posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(feedRecyclerViewAdapter);
        parentReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateFeedAfterNewData(dataSnapshot, context, recyclerView);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("lmao", "onChildChanged called");
                updateFeedAfterNewData(dataSnapshot, context, recyclerView);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateFeedAfterNewData(dataSnapshot, context, recyclerView);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                updateFeedAfterNewData(dataSnapshot, context, recyclerView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static List<Post> getPosts() {
        return posts;
    }

    /**
     * Called when the post has been modified(e.g. through a comment add) or when a post has been
     * added.
     * Retrieves the entire "posts/" reference.
     * Next, it orders the posts and removes duplicates.
     * Checks if the feed is empty.
     * <p>If the feed is empty, it does the following.</p>
     * <p>It binds everything to row items in the RecyclerView</p>
     * <p>
     * <p>If the feed simply has to be updated, it simply notifies the
     * RecyclerViewAdapter that the data set of existing posts has been modified.</p>
     *
     * @param dataSnapshot A snapshot of the parent post reference
     * @param context      The FeedActivity context
     * @param recyclerView The feed RecyclerView
     */
    private void updateFeedAfterNewData(DataSnapshot dataSnapshot, Context context,
                                        RecyclerView recyclerView) {
        Post newPost = dataSnapshot.getValue(Post.class);
        if (newPost.getInternalDate() == null) {
            return;
        }
        posts.add(newPost);
        //Remove duplicate items from the ArrayList
        Set<Post> postSet = new TreeSet<>();
        postSet.addAll(posts);
        posts.clear();
        posts.addAll(postSet);
        if (feedRecyclerViewAdapter == null) {
            feedRecyclerViewAdapter =
                    new FeedRecyclerViewAdapter<RecyclerView.ViewHolder>(context, posts);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(feedRecyclerViewAdapter);

        } else {
            feedRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void updateFeed() {
        if (feedRecyclerViewAdapter != null) {
            feedRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    //Used to retrieve the URL of the uploaded image
    public String getLastImageFirebaseUrl() {
        return lastImageFirebaseUrl;
    }

    public void uploadPost(Post post) {
        newPostReference =
                database.getReference("posts/" + post.getInternalDate());
        newPostReference.setValue(post);
        Toast.makeText(context, "Post successfully uploaded!", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Creates a new file with a name generated using the current date and time.
     *
     * @return A android.net.Uri to the new file.
     */
    public Uri createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(INTERNAL_DATE_PATTERN).format(new Date());
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
        return FileProvider.getUriForFile(context, "edu.illinois.finalproject",
                                          image.getAbsoluteFile());
    }

    /**
     * Compresses the file.
     * Gets a reference to Firebase Cloud Storage.
     * Stores the file in the cloud.
     * <p>
     * Compression is essential to prevent the application from running out-of-memory.
     */
    public void storeImageInFirebase(Uri imageUri) {
        File file = new File(absoluteFilePath);
        try {
            File newFile = new Compressor(context).compressToFile(file);
            FileOutputStream fileOutputStream = new FileOutputStream(absoluteFilePath);
            Bitmap newFileBitmap = BitmapFactory.decodeFile(String.valueOf(newFile));
            newFileBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
            Log.d("Compressor", "Successfully compressed image.");
        } catch (IOException e) {
            Log.d("Compressor", "Compressing the file didn't work.");
            e.printStackTrace();
        }
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
                                lastImageFirebaseUrl = downloadUrl.toString();
                                Log.v(DOWNLOAD_URL, String.valueOf(downloadUrl));
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(
                            @NonNull
                                    Exception e) {
                    }
                });
    }

    /**
     * If a post has been modified, it overwrites the original post with the modified contents.
     *
     * @param tappedPost              The post with modified content.
     * @param feedRecyclerViewAdapter
     */
    public void updatePost(Post tappedPost,
                           FeedRecyclerViewAdapter feedRecyclerViewAdapter) {
        //Gets reference of last added post
        String referencePath = String.format("posts/%s", tappedPost.getInternalDate());
        DatabaseReference toBeUpdatedReference = database.getReference(referencePath);
        toBeUpdatedReference.setValue(tappedPost);
        //feedRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void forceShit() {
        String referencePath = String.format("posts/trigger/adder");
        DatabaseReference lul = database.getReference(referencePath);
        ArrayList<Double> wtf = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            wtf.add(Math.random());
        }
        Log.d("lmao", "Writing new things: " + wtf.toString());
        lul.setValue(wtf);
    }

    public void updateComment(Post tappedPost, List<Comment> commentList,
                              CommentRecyclerViewAdapter adapter) {
        DatabaseReference reference = database.getReference(String.format("posts/%s/commentList/",
                                                                          tappedPost
                                                                                  .getInternalDate()));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren()
                        .iterator();
                commentList.clear();
                while (it.hasNext()) {
                    Comment comment = it.next()
                            .getValue(Comment.class);
                    commentList.add(comment);
                    Log.v("NewComment", String.valueOf(comment));
                }
                //Comment comment = dataSnapshot.getValue(Comment.class);
                //commentList.add(comment);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
