package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.FriendRequest;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRequestDAO {
    ////////////////////////////////////////////

    // 1- Insert New Friend Request
    // 2- Get Friend Requests by User ID
    // 3- Accept Friend Request
    // 4- Decline Friend Request
    // 5- Prevent Duplicate Friend Requests
    // 6- Check if Two Users are Already Friends
    // 7- Get all pending friend requests for a user


    ////////////////////////////////////////////

    // IF THERE IS REQUEST FROM ONE USER TO ANOTHER, THEN WE INSERT A RECORD IN FRIEND_REQUEST TABLE
    // 1- Insert a new friend request
    // Used IN ALL USERS SERVICE
    public boolean addFriendRequest(int senderId, int receiverId) {
        String sql = "INSERT INTO friend_request (sender_id, receiver_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Fails to add friend request");
        }

    }

    // WHEN WE OPEN THE FRIEND REQUESTS PAGE, WE GET ALL PENDING REQUESTS FOR THE LOGGED IN USER
    // 2- Get friend requests by user ID
    // Used IN FRIEND REQUESTS SERVICE
    public FriendRequest getRequestById(int requestId) {
        String sql = "SELECT * FROM friend_request WHERE req_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FriendRequest request = new FriendRequest();
                request.setReqId(rs.getInt("req_id"));
                request.setSenderId(rs.getInt("sender_id"));
                request.setReceiverId(rs.getInt("receiver_id"));
                request.setStatus(rs.getString("status"));
                return request;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fails to get friend request by ID");
        }
        return null;
    }

    // AFTER ACCEPTING A FRIEND REQUEST, IN FREND REQUEST PAGE, WE UPDATE THE STATUS TO 'ACCEPTED'
    // 3- Accept Friend Request
    // Used IN FRIEND REQUESTS SERVICE
    public boolean acceptedFriendRequest(int requestId) {
        String sql = "UPDATE friend_request SET status = 'ACCEPTED' WHERE req_id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Fails to accept friend request");
        }
    }

    // AFTER DECLINING A FRIEND REQUEST, IN FREND REQUEST PAGE, WE UPDATE THE STATUS TO 'DECLINED'
    // 4- Decline Friend Request
    // Used IN FRIEND REQUESTS SERVICE
    public boolean declineFriendRequest(int requestId) {
        String sql = "UPDATE friend_request SET status = 'DECLINED' WHERE req_id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Fails to decline friend request");
        }
    }

    //////////////// SOME CHECKS ////////////////////////
    
    // 5- check duplicate friend requests
    // Used in AllUsersService before sending a new friend request
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
            throw new RuntimeException("Fails to check duplicate friend request");
        }
        return false;
    }

    // 6- Check if two users are already friends
    // Used in AllUsersService before sending a new friend request
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
            throw new RuntimeException("Fails to check if users are friends");
        }
        return false;
    }

    // 7- Get all pending friend requests for a user
    // Used IN FRIEND REQUESTS SERVICE
    public List<FriendRequest> getPendingFriendRequests(int userId) {
        List<FriendRequest> requests = new ArrayList<>();
        
        String sql = "SELECT * FROM friend_request WHERE receiver_id = ? AND status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                
                FriendRequest request = new FriendRequest();

                request.setReqId(rs.getInt("req_id"));
                request.setSenderId(rs.getInt("sender_id"));
                request.setReceiverId(rs.getInt("receiver_id"));
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fails to get pending friend requests");
        }
        return requests;
    }

}