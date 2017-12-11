package edu.illinois.finalproject.schemas;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

public class Post implements Parcelable, Comparable<Post> {
    public static final Creator<Post> CREATOR = new Creator<Post>() {
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
    private String internalDate;
    private String userDisplayDate;
    private List<Comment> commentList;

    public Post(String username, String imageUri, String caption, String location, String
            internalDate, String userDisplayDate) {
        this.username = username;
        this.imageUri = imageUri;
        this.caption = caption;
        this.location = location;
        this.internalDate = internalDate;
        this.userDisplayDate = userDisplayDate;
    }

    public Post() {

    }

    protected Post(Parcel in) {
        this.username = in.readString();
        this.imageUri = in.readString();
        this.caption = in.readString();
        this.location = in.readString();
        this.internalDate = in.readString();
        this.userDisplayDate = in.readString();
        this.commentList = in.createTypedArrayList(Comment.CREATOR);
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
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

    public String getInternalDate() {
        return internalDate;
    }

    public void setInternalDate(String internalDate) {
        this.internalDate = internalDate;
    }

    public String getUserDisplayDate() {
        return userDisplayDate;
    }

    public void setUserDisplayDate(String userDisplayDate) {
        this.userDisplayDate = userDisplayDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "username='" + username + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", caption='" + caption + '\'' +
                ", location='" + location + '\'' +
                ", internalDate='" + internalDate + '\'' +
                ", userDisplayDate='" + userDisplayDate + '\'' +
                ", commentList=" + commentList +
                '}';
    }

    /**
     * Compares posts by chronological order of upload time.
     *
     * @param o The object to compare the current post with.
     * @return -1 if the current object was uploaded later.
     * 1 if the argument Post was uploaded later.
     * 0 if they were posted at the exact same internalDate and time (highly unlikely).
     */
    @Override
    public int compareTo(
            @NonNull
                    Post o) {
        String date1 = this.getInternalDate();
        String date2 = o.getInternalDate();
        for (int i = 0; i < date1.length(); i++) {
            if (date1.charAt(i) > date2.charAt(i)) {
                return -1;
            } else if (date1.charAt(i) < date2.charAt(i)) {
                return 1;
            }
        }
        return 0;
    }

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
        dest.writeString(this.internalDate);
        dest.writeString(this.userDisplayDate);
        dest.writeTypedList(this.commentList);
    }
}
