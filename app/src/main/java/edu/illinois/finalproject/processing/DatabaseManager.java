package edu.illinois.finalproject.processing;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import edu.illinois.finalproject.schemas.Post;
import edu.illinois.finalproject.util.RecyclerViewAdapter;

/**
 * Allows auto-syncing of the database, as well as operations to add posts.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class DatabaseManager {
    private FirebaseDatabase firebaseDatabase;
    private static List<Post> posts = new ArrayList<>();
    private DatabaseReference newPostReference;
    private DatabaseReference parentReference;

    public DatabaseManager(Context context, RecyclerView recyclerView) {
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

    public void fetchData() {

    }
}
