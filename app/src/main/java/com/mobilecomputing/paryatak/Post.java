package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;

public class Post {
    String PostID;
    String PostTime;
    String WhoPublishedIt;
    String PostContent;
    String MediaType; // IMAGE OR VIDEO
    String MediaURL;
    String Upvotes;
    String Downvotes;

    public Post(String Content, String MediaType, String MediaURL) {
        String Time = String.valueOf(System.currentTimeMillis());
        this.PostID = FirebaseAuth.getInstance().getCurrentUser().getUid() + Time;
        this.PostTime = Time;
        this.WhoPublishedIt = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.PostContent = Content;
        this.MediaType = MediaType;
        this.MediaURL = MediaURL;
        this.Upvotes = null;
        this.Downvotes = null;
    }

    public String getPostID() {
        return PostID;
    }

    public String getPostTime() {
        return PostTime;
    }

    public String getWhoPublishedIt() {
        return WhoPublishedIt;
    }

    public String getPostContent() {
        return PostContent;
    }

    public String getMediaType() {
        return MediaType;
    }

    public String getMediaURL() {
        return MediaURL;
    }

    public void setUpvotes(String upvotes) {
        Upvotes = upvotes;
    }

    public void setDownvotes(String downvotes) {
        Downvotes = downvotes;
    }

    public String getDownvotes() {
        return Downvotes;
    }

    public String getUpvotes() {
        return Upvotes;
    }
}