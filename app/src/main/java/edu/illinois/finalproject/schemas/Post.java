package edu.illinois.finalproject.schemas;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Post implements Parcelable, Comparable<Post> {
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
    private String date;

    public Post(String username, String imageUri, String caption, String location, String date) {
        this.username = username;
        this.imageUri = imageUri;
        this.caption = caption;
        this.location = location;
        this.date = date;
    }

    public Post() {

    }

    protected Post(Parcel in) {
        this.username = in.readString();
        this.imageUri = in.readString();
        this.caption = in.readString();
        this.location = in.readString();
        this.date = in.readString();
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{" +
                "username='" + username + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", caption='" + caption + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                '}';
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
        dest.writeString(this.date);
    }

    /**
     * Compares posts by chronological order of upload time.
     *
     * @param o The object to compare the current post with.
     * @return -1 if the current object was uploaded later.
     * 1 if the argument Post was uploaded later.
     * 0 if they were posted at the exact same date and time (highly unlikely).
     */
    @Override
    public int compareTo(
            @NonNull
                    Post o) {
        String date1 = this.getDate();
        String date2 = o.getDate();
        for (int i = 0; i < date1.length(); i++) {
            if (date1.charAt(i) > date2.charAt(i)) {
                return -1;
            } else if (date1.charAt(i) < date2.charAt(i)) {
                return 1;
            }
        }
        return 0;
    }
}
