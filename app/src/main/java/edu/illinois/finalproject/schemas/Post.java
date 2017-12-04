package edu.illinois.finalproject.schemas;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    private String username;
    private String imageUri;
    private String caption;
    private String location;

    public Post(String username, String imageUri, String caption, String location) {
        this.username = username;
        this.imageUri = imageUri;
        this.caption = caption;
        this.location = location;
    }

    public Post() {

    }

    protected Post(Parcel in) {
        this.username = in.readString();
        this.imageUri = in.readString();
        this.caption = in.readString();
        this.location = in.readString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("Post {username='%s', imageUri='%s', caption='%s', location='%s'}",
                             username, imageUri, caption, location);
    }

    //Parcelable boilerplate
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.imageUri);
        dest.writeString(this.caption);
        dest.writeString(this.location);
    }
}
