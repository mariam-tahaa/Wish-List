package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.FriendRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDAO {
////////////////////////////////////////////

// 1- Insert New Friend Request
// 2- Get Friend Requests by User ID
// 3- Accept Friend Request
// 4- Decline Friend Request
// 5- Prevent Duplicate Friend Requests
// 6- Check if Two Users are Already Friends


////////////////////////////////////////////

    // 1- Insert a new friend request
    public boolean addFriendRequest(int senderId, int receiverId) {
        String sql = "INSERT INTO friend_request (sender_id, receiver_id, status) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    } // Handled by MyFriendRequestService
    /////////////////////////////////////////////////////////////////////////////////

    // 2- Get friend requests by user ID
    public List<FriendRequest> getFriendRequestsByUserId(int userId) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        String sql = "SELECT * FROM friend_request WHERE receiver_id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setReqId(rs.getInt("request_id"));
                friendRequest.setSenderId(rs.getInt("sender_id"));
                friendRequest.setReceiverId(rs.getInt("receiver_id"));
                friendRequest.setStatus(rs.getString("status"));
                friendRequests.add(friendRequest);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

    // 3- Accept Friend Request
    public boolean acceptedFriendRequest(int requestId) {
        String sql = "UPDATE friend_request SET status = 'ACCEPTED' WHERE request_id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4- Decline Friend Request
    public boolean declineFriendRequest(int requestId) {
        String sql = "UPDATE friend_request SET status = 'DECLINED' WHERE request_id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5- Prevent duplicate friend requests
    public boolean isDuplicateRequest(int senderId, int receiverId) {
        String sql = "SELECT COUNT(*) FROM friend_request WHERE sender_id = ? AND receiver_id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6- Check if two users are already friends
    public boolean areUsersFriends(int userId1, int userId2) {
        String sql = "SELECT COUNT(*) FROM friendship WHERE " +
                     "((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) " +
                     "AND status = 'ACCEPTED'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}