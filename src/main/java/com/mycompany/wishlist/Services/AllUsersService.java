package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.FriendshipDAO;
import com.mycompany.wishlist.DAO.FriendRequestDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.FriendRequest;
import java.util.List;

//////////////////////  ALL USERS  ////////////////////////////

// A. Get all users with friendship status    --> (WHEN WE OPEN ALL USERS PAGE)
// B. Send friend request                     --> (WHEN WE PRESS ON ADD FRIEND BUTTON)
// C. Unfriend method                         --> (WHEN WE PRESS ON UNFRIEND BUTTON)  

//////////////////////////////////////////////////

public class AllUsersService {
    private FriendshipDAO friendshipDAO = new FriendshipDAO();
    private FriendRequestDAO friendRequestDAO = new FriendRequestDAO();

    ///////////////// A. Get all users with friendship status ///////////////////
    public List<String> getAllUsersWithFriendshipStatus(int currentUserId) {
        int userId = SessionManager.getCurrentUser().getUserId();
        // Function number 4 in friendshipDAO
        return friendshipDAO.getAllUsersWithFriendshipStatus(userId);
    }

    ///////////////////// B. Send friend request ////////////////////////
    public boolean sendFriendRequest(int receiverId) {
        // 1. Get current user id from session
        int senderId = SessionManager.getCurrentUser().getUserId();

        // 2. Cannot send friend request to yourself
        if (senderId == receiverId){
            return false; 
        }

        // 3. check that the current user is the sender
        // Function number 2 in friendRequestDAO
        FriendRequest request = friendRequestDAO.getRequestById(receiverId);

        if (request != null && request.getSenderId() != senderId) {
            return false; 
        }

        // 4. Check if they already friends
        // Function number 2 in friendshipDAO
        if (friendshipDAO.areUsersFriends(senderId, receiverId)) {
            return false; 
        }

        // 5. Check for existing pending request
        // Function number 5 in friendRequestDAO
        if (friendRequestDAO.isDuplicateRequest(senderId, receiverId)) {
            return false; 
        }

        // 6. send request
        // Function number 1 in friendRequestDAO
        return friendRequestDAO.addFriendRequest(senderId, receiverId);

    }

    ///////////////////// C. Unfriend method ////////////////////////
    public boolean unfriend(int friendId) {
        int userId = SessionManager.getCurrentUser().getUserId();
        // Function number 3 in friendshipDAO
        return friendshipDAO.deleteFriendshipByUserIds(userId, friendId);
    }
}
