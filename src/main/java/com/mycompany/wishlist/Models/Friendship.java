/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wishlist.Models;

/**
 *
 * @author HELLo
 */
public class Friendship {

    private int userId;      // maps to user_id
    private int friendId;    // maps to friend_id

    // Constructors
    public Friendship() {}

    public Friendship(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
}
