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

////////////////////////////////////////////

public class FriendshipDAO {
    // 1- Insert a new friendship
    public boolean addFriendship(Friendship friendship) {
        int u1 = Math.min(friendship.getUserId(), friendship.getFriendId());
        int u2 = Math.max(friendship.getUserId(), friendship.getFriendId());

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
