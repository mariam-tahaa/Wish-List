package com.mycompany.wishlist.Helpers;

import com.mycompany.wishlist.Models.User;

public class SessionManager {
    private static User currentUser;
    private static Integer friendId; // Add this

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }
    public static void setFriendId(int id) {
        friendId = id;
    }

    public static Integer getFriendId() {
        return friendId;
    }

    public static void clearFriendId() {
        friendId = null;
    }
}