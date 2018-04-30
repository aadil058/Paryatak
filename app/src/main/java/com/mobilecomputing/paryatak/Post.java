package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class Post {
    private String PostID;
    private String PostTime;
    private String WhoPublishedIt;
    private String PostContent;
    private String PostImageURL;
    private String Upvotes;
    private String Downvotes;

    public Post(String PostID, String Time, String Content, String PostImageURl) {
        this.PostID = PostID;
        this.PostTime = Time;
        this.WhoPublishedIt = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.PostContent = Content;
        this.PostImageURL = PostImageURl;
        this.Upvotes = null;
        this.Downvotes = null;
    }

    public Post() {}

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
        this.Upvotes = upvotes;
    }

    public void setDownvotes(String downvotes) {
        this.Downvotes = downvotes;
    }

    public String getDownvotes() {
        return this.Downvotes;
    }

    public String getUpvotes() {
        return Upvotes;
    }

    public static Post snapshotToPost(Map<String, Object> snapshot) {
        Post post = new Post();
        if (snapshot.get("postID") != null) post.setPostID(snapshot.get("postID").toString());
        if (snapshot.get("postTime") != null) post.setPostTime(snapshot.get("postTime") .toString());
        if (snapshot.get("whoPublishedIt") != null) post.setWhoPublishedIt(snapshot.get("whoPublishedIt").toString());
        if (snapshot.get("postContent") != null) post.setPostContent(snapshot.get("postContent").toString());
        if (snapshot.get("downvotes") != null) post.setDownvotes(snapshot.get("downvotes").toString());
        if (snapshot.get("postImageURL") != null) post.setPostImageURL(snapshot.get("postImageURL").toString());
        if (snapshot.get("upvotes") != null) post.setUpvotes(snapshot.get("upvotes").toString());
        return post;
    }
}