package com.mycompany.wishlist.Models;


import java.sql.Timestamp;

public class Notification {

    private int notId;          // maps to not_id
    private int userId;         // maps to user_id (foreign key)
    private String content;     // maps to content
    private Timestamp notTime;  // maps to not_time
    private String status;      // maps to status ('UNREAD' or 'READ')

    // Constructors
    public Notification() {
        // default constructor
    }

    public Notification(int userId, String content, String status) {
        this.userId = userId;
        this.content = content;
        this.status = status;
    }

    // Getters and Setters
    public int getNotId() {
        return notId;
    }

    public void setNotId(int notId) {
        this.notId = notId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getNotTime() {
        return notTime;
    }

    public void setNotTime(Timestamp notTime) {
        this.notTime = notTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}