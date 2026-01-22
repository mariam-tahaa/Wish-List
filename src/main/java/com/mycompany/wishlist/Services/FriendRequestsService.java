package com.mycompany.wishlist.Services;

import java.util.List;

import com.mycompany.wishlist.DAO.FriendRequestDAO;
import com.mycompany.wishlist.DAO.FriendshipDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.FriendRequest;

public class FriendRequestsService {

//////////////////////////  FRIEND REQUESTS  //////////////////

// 1- Accept Friend Request
// 2- Decline Friend Request
// 3- Get all pending friend requests for current user

////////////////////////////////////////////

    public class FriendRequestService {

        private FriendRequestDAO friendRequestDAO = new FriendRequestDAO();
        private FriendshipDAO friendshipDAO = new FriendshipDAO();

        // 1- Accept a friend request
        //////////////////////////////////////////
        public boolean acceptRequest(int requestId) {
            int currentUserId = SessionManager.getCurrentUser().getUserId();

            // 1. Get request by ID (DAO method)
            FriendRequest request = friendRequestDAO.getRequestById(requestId);
            if (request == null)
                return false;

            // 2. Ensure current user is the receiver
            if (request.getReceiverId() != currentUserId)
                return false;

            // 3. Ensure request is pending
            if (!"PENDING".equals(request.getStatus()))
                return false;

            // 4. Update status to ACCEPTED
            boolean updated = friendRequestDAO.acceptedFriendRequest(requestId);
            if (!updated)
                return false;

            // 5. Insert into friendship table
            boolean added = friendshipDAO.addFriendship(request.getSenderId(), request.getReceiverId());
            return added;
        }

        // 2- Decline a friend request
        //////////////////////////////////////////
        public boolean declineRequest(int requestId) {
            int currentUserId = SessionManager.getCurrentUser().getUserId();

            // 1. Get request by ID
            FriendRequest request = friendRequestDAO.getRequestById(requestId);
            if (request == null)
                return false;

            // 2. Ensure current user is the receiver
            if (request.getReceiverId() != currentUserId)
                return false;

            // 3. Ensure request is pending
            if (!"PENDING".equals(request.getStatus()))
                return false;

            // 4. Update status to DECLINED
            return friendRequestDAO.declineFriendRequest(requestId);
        }

        // 3- Get all pending friend requests for current user
        ///////////////////////////////////////////
        public List<FriendRequest> getPendingFriendRequests() {
            return friendRequestDAO.getPendingFriendRequests();
        }

    }

}
