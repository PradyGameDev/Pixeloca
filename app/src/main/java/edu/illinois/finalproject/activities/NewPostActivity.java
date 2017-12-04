package edu.illinois.finalproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.illinois.finalproject.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu2, menu);
        return true;
    }
}
