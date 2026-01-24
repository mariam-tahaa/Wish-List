package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.NotificationDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Notification;

import java.util.List;

public class NotificationService {

    private NotificationDAO ndao = new NotificationDAO();

   
    
    // Notify gift completed
    public boolean notifyCompleted(int ownerUserId, int giftId, String giftName) {
        Notification not = new Notification();
        not.setUserId(ownerUserId);
        not.setGiftId(giftId);           
        not.setContent(giftName + " is completed");
        not.setStatus("UNREAD");         

        return ndao.addNotification(not);
    }

    // Notify contribution
    public boolean notifyContribution(int giftOwnerId, int giftId, String contributorName, String giftName) {
        Notification not = new Notification();
        not.setUserId(giftOwnerId);         
        not.setGiftId(giftId);
        not.setContent(contributorName + " contributes with you in " + giftName);
        not.setStatus("UNREAD");

        return ndao.addNotification(not);
    }

    // Mark notification as read
    public boolean markNotificationAsRead(int notificationId) {
        return ndao.markAsRead(notificationId);
    }

    // Get all notifications for the current user
    public List<Notification> getAllNotifications() {
        int currentUserId = SessionManager.getCurrentUser().getUserId();
        return ndao.getAllNotificationsByUserId(currentUserId);
    }
}
