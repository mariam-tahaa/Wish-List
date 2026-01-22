package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.FriendshipDAO;
import com.mycompany.wishlist.DAO.FriendRequestDAO;
import com.mycompany.wishlist.Helpers.SessionManager;

//////////////////////  ALL USERS  ////////////////////////////

// Send friend request method
// Unfriend method

//////////////////////////////////////////////////

// Service layer to handle friendRequest operations
public class AllUsersService {
    private FriendshipDAO friendshipDAO = new FriendshipDAO();
    private FriendRequestDAO friendRequestDAO = new FriendRequestDAO();

    
    public boolean sendFriendRequest(int receiverId) {
        // Get current user id from session
        int senderId = SessionManager.getCurrentUser().getUserId();

        // Cannot send friend request to yourself
        if (senderId == receiverId){
            return false; 
        }

        // Check if they already friends
        if (friendshipDAO.areUsersFriends(senderId, receiverId)) {
            return false; 
        }

        // Check for existing pending request
        if (friendRequestDAO.isDuplicateRequest(senderId, receiverId)) {
            return false; 
        }

        // send request
        return friendRequestDAO.addFriendRequest(senderId, receiverId);

    }

    // unfriend
    public boolean unfriend(int friendId) {
        int userId = SessionManager.getCurrentUser().getUserId();
        return friendshipDAO.deleteFriendshipByUserIds(userId, friendId);
    }
}
