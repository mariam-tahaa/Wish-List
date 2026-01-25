package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.NotificationDAO;
import com.mycompany.wishlist.Models.Notification;

import com.mycompany.wishlist.Helpers.SessionManager;
import java.util.List;

public class NotificationService {

    
    // Get all notifications
    public List<Notification> getAllNotifications() {

        int currentUserId = SessionManager.getCurrentUser().getUserId();
        NotificationDAO notificationDAO = new NotificationDAO();

        List<Notification> notifications = notificationDAO.getAllNotificationsByUserId(currentUserId);

    // Check if there is notifications
    if (notifications.isEmpty())
        return null;

    return notifications;

    }

    // Mark a notification as read
    public boolean markNotificationAsRead(int notificationId) {

        // Mark notification as read
        NotificationDAO notificationDAO = new NotificationDAO();
        return notificationDAO.markAsRead(notificationId);
    }
}
