package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.ContributionDAO;
import com.mycompany.wishlist.DAO.GiftDAO;
import com.mycompany.wishlist.Models.Contribution;
import com.mycompany.wishlist.Models.Gift;
import java.math.BigDecimal;
import java.util.List;


public class ContributionService {

    private ContributionDAO contributionDAO = new ContributionDAO();
    private GiftDAO giftDAO = new GiftDAO();

   // Insert new Contribution with validation
    public boolean addContribution(Contribution contribution) {
        // 1. Basic validation: percentage must be between 0 (exclusive) and 100 (inclusive)
        if (contribution.getPercentage() == null || 
            contribution.getPercentage().compareTo(BigDecimal.ZERO) <= 0 ||
            contribution.getPercentage().compareTo(new BigDecimal("100")) > 0) {
            return false;
        }

        // 2. Check if the gift exists and is not already completed
        Gift gift = giftDAO.getGiftById(contribution.getGiftId());
        if (gift == null) {
        System.out.println("Debug: Gift not found in db");
        return false;
    }
    if ("Completed".equalsIgnoreCase(gift.getStatus())) {
        System.out.println("Debug: Gift is already completed!");
        return false;
    }

        // 3. Check total percentage for the gift to ensure it doesn't exceed 100%
        List<Contribution> existingContributions = contributionDAO.getContributionsByGiftId(contribution.getGiftId());
        BigDecimal currentTotal = BigDecimal.ZERO;
        for (Contribution c : existingContributions) {
            currentTotal = currentTotal.add(c.getPercentage());
        }

        // Calculate new total if this contribution is added
        BigDecimal newTotal = currentTotal.add(contribution.getPercentage());
        if (newTotal.compareTo(new BigDecimal("100")) > 0) {
            System.out.println("Debug: Total percentage would exceed 100%!");
            return false;
        }

        // 4. Insert the contribution
        boolean success = contributionDAO.addContribution(contribution);
        ///////////TODO: call notification function with content (new contributor : user_name contributed in your Gift)

        // 5. If gift is now 100% funded, update its status to 'Completed'
        if (success && newTotal.compareTo(new BigDecimal("100")) == 0) {
            gift.setStatus("Completed");
        //////////TODO: call notification: All users function (content is : gift_name completed)

            giftDAO.updateGift(gift);
        }
        
        return success;
    }

    // Retrieve all contributions for a specific gift
    public List<Contribution> getContributionsByGiftId(int giftId) {
        return contributionDAO.getContributionsByGiftId(giftId);
    }

    // Update contribution percentage with validation
    public boolean updateContributionPercentage(int contributionId, BigDecimal newPercentage) {
        // 1. Validate new percentage range
        if (newPercentage == null || 
            newPercentage.compareTo(BigDecimal.ZERO) <= 0 ||
            newPercentage.compareTo(new BigDecimal("100")) > 0) {
            return false;
        }

        // 2. Fetch the existing contribution to find the associated gift
        // Note: Assuming ContributionDAO or a similar method can fetch a contribution by its ID.
        
        Contribution existing = contributionDAO.getContributionById(contributionId);
        if (existing == null) {
            return false;
        }

        // 3. Calculate the new total for the gift
        List<Contribution> contributions = contributionDAO.getContributionsByGiftId(existing.getGiftId());
        BigDecimal totalWithoutCurrent = BigDecimal.ZERO;
        for (Contribution c : contributions) {
            if (c.getContributionId() != contributionId) {
                totalWithoutCurrent = totalWithoutCurrent.add(c.getPercentage());
            }
        }

        BigDecimal newTotal = totalWithoutCurrent.add(newPercentage);
        if (newTotal.compareTo(new BigDecimal("100")) > 0) {
            return false;
        }

        // 4. Update the contribution
        boolean success = contributionDAO.updateContributionPercentage(contributionId, newPercentage);
        
        // 5. Update gift status if necessary
        if (success) {
            Gift gift = giftDAO.getGiftById(existing.getGiftId());
            if (gift != null) {
                String newStatus = newTotal.compareTo(new BigDecimal("100")) == 0 ? "Completed" : "Incomplete";
                if (!newStatus.equalsIgnoreCase(gift.getStatus())) {
                    gift.setStatus(newStatus);
                    // call notification: All users function (content is : gift_name completed)
                    giftDAO.updateGift(gift);
                }
            }
        }
        
        return success;
    }

   // Delete contribution by ID with gift status update
    public boolean deleteContribution(int contributionId) {
        Contribution existing = contributionDAO.getContributionById(contributionId);
        if (existing == null) {
            return false;
        }

        boolean success = contributionDAO.deleteContribution(contributionId);
        
        if (success) {
            // After deletion, the gift status should be 'Incomplete' if it was 'Completed'
            Gift gift = giftDAO.getGiftById(existing.getGiftId());
            if (gift != null && "Completed".equalsIgnoreCase(gift.getStatus())) {
                gift.setStatus("Incomplete");
                giftDAO.updateGift(gift);
            }
        }
        
        return success;
    }
}
