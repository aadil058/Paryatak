package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserModal {

    private String uid;
    private String imageUrl;
    private String name;
    private String country;
    private String phone;
    private String handle;
    private List<String> Followers; // ID's of followers
    private List<String> Following; // ID's of following
    private boolean registered;
    private List<String> FavoritePosts; // ID's of favorite Posts

    public UserModal() {
        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public UserModal(String url, String name, String country, String phone, boolean registered) {
        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.imageUrl = url;
        this.name = name;
        this.country = country;
        this.phone = phone;
        this.registered = registered;
        this.FavoritePosts = null;
    }

    public UserModal(String url, String name, String country, String phone, boolean registered, List<String> Followers, List<String> Following, String handle) {
        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.imageUrl = url;
        this.name = name;
        this.country = country;
        this.phone = phone;
        this.registered = registered;
        this.Followers = Followers;
        this.Following = Following;
        this.handle = handle;
        this.FavoritePosts = null;
    }

    public List<String> getFollowers() {
        return Followers;
    }

    public List<String> getFollowing() {
        return Following;
    }

    public void setFollowers(List<String> followers) {
        Followers = followers;
    }

    public void setFollowing(List<String> following) {
        Following = following;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getHandle() {
        return handle;
    }

    public String getUid() {
        return uid;
    }

    public String getCountry() {
        return country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean getRegistered() {
        return this.registered;
    }

    public void setFavoritePosts(List<String> favoritePosts) {
        FavoritePosts = favoritePosts;
    }

    public List<String> getFavoritePosts() {
        return FavoritePosts;
    }
}
