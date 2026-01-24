package com.mycompany.wishlist.Models;


import java.sql.Timestamp;

public class Notification {

    private int notId;
    private int userId;
    private int giftId;
    private String giftName;
    private String content;
    private Timestamp notTime;
    private String status;

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

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    
}