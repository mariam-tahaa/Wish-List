package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
////////////////////////////////////////////

// Insert New Notification
// Get Notifications by User ID
// Mark Notification as Read

////////////////////////////////////////////

public class NotificationDAO {

    // Insert a new notification
    public boolean addNotification(Notification notification) {
        String sql = "INSERT INTO notification (user_id, content, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notification.getUserId());
            ps.setString(2, notification.getContent());
            ps.setString(3, notification.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    // Get notifications by user ID
    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE user_id = ? ORDER BY not_time DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotId(rs.getInt("not_id"));
                notification.setUserId(rs.getInt("user_id"));
                notification.setContent(rs.getString("content"));
                notification.setNotTime(rs.getTimestamp("not_time"));
                notification.setStatus(rs.getString("status"));
                notifications.add(notification);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    // Mark notification as read
    public boolean markAsRead(int notId) {
        String sql = "UPDATE notification SET status = 'READ' WHERE not_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}