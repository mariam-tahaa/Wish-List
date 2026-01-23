package com.mycompany.wishlist.Services;

import java.util.List;

import com.mycompany.wishlist.DAO.FriendRequestDAO;
import com.mycompany.wishlist.DAO.FriendshipDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.FriendRequest;

public class FriendRequestsService {

//////////////////////////  FRIEND REQUESTS  //////////////////

// A- Get all pending friend requests for current user       --> (WHEN WE OPEN FRIEND REQUESTS PAGE)
// B- Accept Friend Request                                  --> (WHEN WE PRESS ON RIGHT BUTTON)
// C- Decline Friend Request                                 --> (WHEN WE PRESS ON FALSE BUTTON)

    public class FriendRequestService {

        private FriendRequestDAO friendRequestDAO = new FriendRequestDAO();
        private FriendshipDAO friendshipDAO = new FriendshipDAO();

        ////////////////// A- Get all pending friend requests for current user ////////////////////////
        public List<FriendRequest> getPendingFriendRequests() {
            int userId = SessionManager.getCurrentUser().getUserId();

            // Function number 7 in friendRequestDAO
            return friendRequestDAO.getPendingFriendRequests(userId);
        }


        /////////////////// B- Accept a friend request ////////////////////////
        public boolean acceptRequest(int requestId) {
            int currentUserId = SessionManager.getCurrentUser().getUserId();

            // 1. check if request exists
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
            // Function number 3 in friendRequestDAO
            boolean updated = friendRequestDAO.acceptedFriendRequest(requestId);
            if (!updated)
                return false;

            // 5. Insert into friendship table
            // Function number 1 in friendshipDAO
            boolean added = friendshipDAO.addFriendship(request.getSenderId(), request.getReceiverId());
            return added;
        }

        ///////////////////// C- Decline a friend request ////////////////////////
        public boolean declineRequest(int requestId) {
            int currentUserId = SessionManager.getCurrentUser().getUserId();

            // 1. check if request exists
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
            // Function number 4 in friendRequestDAO
            return friendRequestDAO.declineFriendRequest(requestId);

        }

    }

}
