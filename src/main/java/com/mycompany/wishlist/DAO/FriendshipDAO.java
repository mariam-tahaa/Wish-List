package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.Friendship;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

////////////////////////////////////////////

// 1- Insert a new friendship
// 2- Get friends by user ID
// 3- Check if two users are already friends
// 4- Delete friendship by user IDs

//TODO                                                                                             0         1
// method take current user id , returns all system users except friends with status indication(not friend, pending)

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
    }
    ///////////////////////////////////////////////////////////////////////////////////

    // 2- Get friends by user ID
    public List<Friendship> getFriendsByUserId(int userId) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT * FROM friendship WHERE user_id = ? OR friend_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Friendship friendship = new Friendship();
                friendship.setUserId(rs.getInt("user_id"));
                friendship.setFriendId(rs.getInt("friend_id"));
                friendships.add(friendship);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    // 3- Check if two users are already friends
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

    // check if there pending friend request between two users
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

}
