package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.FriendshipDAO;
import com.mycompany.wishlist.DAO.UserDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.User;

import java.util.List;

public class FriendsService {
    private FriendshipDAO friendshipDAO = new FriendshipDAO();
    private UserDAO userDAO = new UserDAO();
    
    ///////////////////// A. Get all friends ////////////////////////
    public List<Integer> getAllFriends() {
        int userId = SessionManager.getCurrentUser().getUserId();
        // Function number 7 in friendshipDAO
        return friendshipDAO.getAllFriends(userId);
    }

    ///////////////////// B. Unfriend method ////////////////////////
    public boolean unfriend(int friendId) {   
        int userId = SessionManager.getCurrentUser().getUserId();     
        // Function number 4 in friendshipDAO
        return friendshipDAO.deleteFriendshipByUserIds(userId, friendId);
    }
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
}
