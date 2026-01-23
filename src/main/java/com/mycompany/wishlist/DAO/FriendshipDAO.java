package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

////////////////////////////////////////////

// 1- Send Friend Request
// 2- Check if two users are already friends
// 3- check if there pending friend request between two users
// 4- Delete friendship by user IDs
// 5- Get all users in the system with their friendship status with the given user
// 6- Insert a new friendship
// 7- Get all friends of a user

////////////////////////////////////////////

public class FriendshipDAO {

    // 1- Add Friend
    // When he presses add friend button on GUI, we insert a new record in
    // friendrequest table with pending status as default
    public boolean sendfriendrequest(int sender_id, int receiver_id) {
        int u1 = Math.min(sender_id, receiver_id);
        int u2 = Math.max(sender_id, receiver_id);

        String sql = "INSERT INTO friend_request (sender_id, receiver_id, status) VALUES (?, ?, 'PENDING')";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, u1);
            ps.setInt(2, u2);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    } // handled by MyFriendRequestService
      ///////////////////////////////////////////////////////////////////////////////////

    // 2- Check if two users are already friends
    public boolean areUsersFriends(int userId, int friendId) {
        int u1 = Math.min(userId, friendId);
        int u2 = Math.max(userId, friendId);

        String sql = "SELECT 1 FROM friendship WHERE user_id = ? AND friend_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, u1);
            ps.setInt(2, u2);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3- check if there pending friend request between two users
    public boolean isPendingRequestExists(int senderId, int receiverId) {
        int u1 = Math.min(senderId, receiverId);
        int u2 = Math.max(senderId, receiverId);

        String sql = "SELECT 1 FROM friend_request WHERE sender_id = ? AND receiver_id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, u1);
            ps.setInt(2, u2);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4- Delete friendship by user IDs
    public boolean deleteFriendshipByUserIds(int userId, int friendId) {
        String sql = "DELETE FROM friendship WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            ps.setInt(3, friendId);
            ps.setInt(4, userId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5- Get all users in the system with their friendship status with the given
    public List<String> getAllUsersWithFriendshipStatus(int userId) {
        List<String> usersWithStatus = new ArrayList<>();

        String sql = "SELECT u.user_id, u.username, " +
                "CASE " +
                "WHEN f.user_id IS NOT NULL THEN 'FRIEND' " +
                "WHEN fr.status = 'PENDING' AND fr.sender_id = ? THEN 'REQUEST_SENT' " +
                "WHEN fr.status = 'PENDING' AND fr.receiver_id = ? THEN 'REQUEST_RECEIVED' " +
                "ELSE 'NOT_FRIEND' " +
                "END AS friendship_status " +
                "FROM app_user u " +
                "LEFT JOIN friendship f " +
                "ON ( (u.user_id = f.friend_id AND f.user_id = ?) OR (u.user_id = f.user_id AND f.friend_id = ?) ) " +
                "LEFT JOIN friend_request fr " +
                "ON ( (u.user_id = fr.receiver_id AND fr.sender_id = ?) OR (u.user_id = fr.sender_id AND fr.receiver_id = ?) ) "
                +
                "WHERE u.user_id != ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 7; i++)
                ps.setInt(i, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userInfo = "Username: " + rs.getString("username") +
                        " Status: " + rs.getString("friendship_status");
                usersWithStatus.add(userInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersWithStatus;
    }

    // 6- Insert a new friendship
    public boolean addFriendship(int senderId, int receiverId) {
        int u1 = Math.min(senderId, receiverId);
        int u2 = Math.max(senderId, receiverId);

        // check already friends
        if (areUsersFriends(u1, u2))
            return false;

        String sql = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, u1);
            ps.setInt(2, u2);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 7- Get all friends of a user
    public List<Integer> getAllFriends(int userId) {
        List<Integer> friends = new ArrayList<>();
        String sql = "SELECT CASE WHEN user_id = ? THEN friend_id ELSE user_id END AS friend_id " +
                "FROM friendship WHERE user_id = ? OR friend_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                friends.add(rs.getInt("friend_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

}