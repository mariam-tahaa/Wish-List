package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.GiftDAO;
import com.mycompany.wishlist.Models.Gift;

import java.math.BigDecimal;
import java.util.List;

public class GiftService {
    private GiftDAO giftDAO = new GiftDAO();

    /**
     * Add a new gift with validation
     */
    public String addGift(Gift gift) {
        // Validate gift name
        if (gift.getGiftName() == null || gift.getGiftName().trim().isEmpty()) {
            return "Gift name is required";
        }

        // Validate price
        if (gift.getPrice() == null || gift.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return "Price must be greater than 0";
        }

        // Validate status
        if (gift.getStatus() == null || gift.getStatus().trim().isEmpty()) {
            gift.setStatus("active"); // Default status
        }

        // Validate user ID
        if (gift.getOwnerUserId() <= 0) {
            return "Invalid user ID";
        }

        // Try to add gift to database
        boolean success = giftDAO.addGift(gift);

        if (success) {
            return "SUCCESS";
        } else {
            return "Failed to add gift. Please try again.";
        }
    }

    /**
     * Get all gifts for a user
     */
    public List<Gift> getUserGifts(int userId) {
        if (userId <= 0) {
            return List.of(); // Return empty list for invalid user ID
        }

        return giftDAO.getGiftsByUser(userId);
    }

    /**
     * Delete a gift with validation
     */
    public String deleteGift(int giftId, int userId) {
        if (giftId <= 0) {
            return "Invalid gift ID";
        }

        if (userId <= 0) {
            return "Invalid user ID";
        }

        // Verify the gift belongs to the user before deleting
        List<Gift> userGifts = giftDAO.getGiftsByUser(userId);
        boolean giftBelongsToUser = userGifts.stream()
                .anyMatch(gift -> gift.getGiftId() == giftId);

        if (!giftBelongsToUser) {
            return "You don't have permission to delete this gift";
        }

        boolean success = giftDAO.deleteGift(giftId, userId);

        if (success) {
            return "SUCCESS";
        } else {
            return "Failed to delete gift. Please try again.";
        }
    }
    
    public boolean updateGift(Gift gift){
        return giftDAO.updateGift(gift);
    }
    
    public Gift getGiftById(int id){
        return giftDAO.getGiftById(id);
    }
}