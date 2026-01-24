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
        String sql = "INSERT INTO notification (user_id, gift_id, content, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notification.getUserId());
            ps.setInt(2, notification.getGiftId());
            ps.setString(3, notification.getContent());
            ps.setString(4, notification.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    // Get notifications by user ID
    public List<Notification> getAllNotificationsByUserId(int userId) {

        List<Notification> notifications = new ArrayList<>();

        String sql = " SELECT n.not_id, n.content, n.status, n.not_time, n.user_id, n.gift_id, g.gift_name "
                   + " FROM notification n "
                   + " JOIN gift g ON g.gift_id = n.gift_id "
                   + " WHERE n.user_id = ? "
                   + " AND n.status = 'UNREAD' "
                   + " ORDER BY n.not_time DESC ";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotId(rs.getInt("not_id"));
                n.setGiftId(rs.getInt("gift_id"));
                n.setGiftName(rs.getString("gift_name"));
                n.setContent(rs.getString("content"));
                n.setNotTime(rs.getTimestamp("not_time"));
                n.setStatus(rs.getString("status"));

                notifications.add(n);
            }

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