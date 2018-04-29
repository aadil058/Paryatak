package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;

public class Post {
    String PostID;
    String PostTime;
    String WhoPublishedIt;
    String PostContent;
    String PostImageURL;
    String Upvotes;
    String Downvotes;

    public Post(String Content, String MediaType, String PostImageURl) {
        String Time = String.valueOf(System.currentTimeMillis());
        this.PostID = FirebaseAuth.getInstance().getCurrentUser().getUid() + Time;
        this.PostTime = Time;
        this.WhoPublishedIt = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.PostContent = Content;
        this.PostImageURL =
        this.Upvotes = null;
        this.Downvotes = null;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setWhoPublishedIt(String whoPublishedIt) {
        WhoPublishedIt = whoPublishedIt;
    }

    public String getWhoPublishedIt() {
        return WhoPublishedIt;
    }

    public void setPostContent(String postContent) {
        PostContent = postContent;
    }

    public String getPostContent() {
        return PostContent;
    }

    public void setPostImageURL(String postImageURL) {
        PostImageURL = postImageURL;
    }

    public String getPostImageURL() {
        return PostImageURL;
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