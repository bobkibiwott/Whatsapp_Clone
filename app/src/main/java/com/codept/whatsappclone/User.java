package com.codept.whatsappclone;

public class User {
    private String username,profilePic,uid,status;

    public User() {
    }

    public User(String username, String profilePic, String uid, String status) {
        this.username = username;
        this.profilePic = profilePic;
        this.uid = uid;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
