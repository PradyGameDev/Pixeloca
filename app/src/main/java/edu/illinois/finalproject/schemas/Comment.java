package edu.illinois.finalproject.schemas;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Comment implements Parcelable, Comparable<Comment> {
    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    private String name;
    private String text;
    //Both dates are the same but internalDate stores it in a format that is easy to compare with
    // others, while display date is a good way to display it.
    private String internalDate;
    private String displayDate;
    //A Uri to a user's profile photo
    private String userPhotoUri;

    public Comment(String name, String text, String internalDate, String displayDate,
                   String userPhotoUri) {
        this.name = name;
        this.text = text;
        this.internalDate = internalDate;
        this.displayDate = displayDate;
        this.userPhotoUri = userPhotoUri;
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.name = in.readString();
        this.text = in.readString();
        this.internalDate = in.readString();
        this.displayDate = in.readString();
        this.userPhotoUri = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getInternalDate() {
        return internalDate;
    }

    public void setInternalDate(String internalDate) {
        this.internalDate = internalDate;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public String getUserPhotoUri() {
        return userPhotoUri;
    }

    public void setUserPhotoUri(String userPhotoUri) {
        this.userPhotoUri = userPhotoUri;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", internalDate='" + internalDate + '\'' +
                ", displayDate='" + displayDate + '\'' +
                ", userPhotoUri='" + userPhotoUri + '\'' +
                '}';
    }

    //Parcelable boilerplate - allows comment objects to be passed as Intent extras
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.text);
        dest.writeString(this.internalDate);
        dest.writeString(this.displayDate);
        dest.writeString(this.userPhotoUri);
    }

    //Compares comments by date of posting. Useful to render comments in reverse-chronological
    // order.
    @Override
    public int compareTo(
            @NonNull
                    Comment o) {
        String comment1 = this.getInternalDate();
        String comment2 = o.getInternalDate();
        for (int i = 0; i < comment1.length(); i++) {
            if (comment1.charAt(i) < comment2.charAt(i)) {
                return -1;
            } else if (comment1.charAt(i) > comment2.charAt(i)) {
                return 1;
            }
        }
        return 0;
    }
}
