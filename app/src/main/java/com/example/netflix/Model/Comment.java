package com.example.netflix.Model;

import com.google.firebase.database.ServerValue;

public class Comment {
private String content,uid,userName;
private Object timeStamp;

    public Comment() {
    }

    public Comment(String content, String uid, String userName) {
        this.content = content;
        this.uid = uid;
        this.userName = userName;
        this.timeStamp= ServerValue.TIMESTAMP;
    }

    public Comment(String content, String uid, String userName, Object timeStamp) {
        this.content = content;
        this.uid = uid;
        this.userName = userName;
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
