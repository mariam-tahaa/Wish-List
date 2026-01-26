package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.ContributionDAO;
import com.mycompany.wishlist.DAO.GiftDAO;
import com.mycompany.wishlist.DAO.NotificationDAO;
import com.mycompany.wishlist.DAO.UserDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Contribution;
import com.mycompany.wishlist.Models.Gift;
import com.mycompany.wishlist.Models.Notification;
import com.mycompany.wishlist.Models.User;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContributionService {

    private ContributionDAO contributionDAO = new ContributionDAO();
    private GiftDAO giftDAO = new GiftDAO();
    private FriendsService friendsService = new FriendsService();
    private NotificationDAO notificationDAO = new NotificationDAO();

    /**
     * Adds a new contribution with validation, notifies the owner,
     * and handles gift completion logic.
     */
    public boolean addContribution(Contribution contribution) {
        // 1. Basic validation
        if (contribution.getPercentage() == null ||
                contribution.getPercentage().compareTo(BigDecimal.ZERO) <= 0 ||
                contribution.getPercentage().compareTo(new BigDecimal("100")) > 0) {
            return false;
        }

        // 2. Check gift existence and status
        Gift gift = giftDAO.getGiftById(contribution.getGiftId());
        if (gift == null) {
            System.out.println("Debug: Gift not found in db");
            return false;
        }
        if ("Completed".equalsIgnoreCase(gift.getStatus())) {
            System.out.println("Debug: Gift is already completed!");
            return false;
        }

        // 3. Check total percentage
        List<Contribution> existingContributions = contributionDAO.getContributionsByGiftId(contribution.getGiftId());
        BigDecimal currentTotal = BigDecimal.ZERO;
        for (Contribution c : existingContributions) {
            currentTotal = currentTotal.add(c.getPercentage());
        }

        BigDecimal newTotal = currentTotal.add(contribution.getPercentage());
        if (newTotal.compareTo(new BigDecimal("100")) > 0) {
            System.out.println("Debug: Total percentage would exceed 100%!");
            return false;
        }

        // 4. Insert the contribution
        boolean success = contributionDAO.addContribution(contribution);

        BigDecimal totalContributed = BigDecimal.ZERO;
        List<Contribution> contributions = getContributionsByGiftId(gift.getGiftId());
        for (Contribution c : contributions) {
            totalContributed = totalContributed.add(c.getPercentage());
        }

        if (!success)
            return false;

        // 5. Notify gift owner about new contribution
        try {
            String currentUserName = SessionManager.getCurrentUser().getUserName();

            BigDecimal addedPercentage = contribution.getPercentage();

            BigDecimal contributedAmount = gift.getPrice()
                    .multiply(addedPercentage)
                    .divide(new BigDecimal("100"));

            Notification ownerNotification = new Notification();
            ownerNotification.setUserId(gift.getOwnerUserId());
            ownerNotification.setGiftId(gift.getGiftId());
            ownerNotification.setContent(currentUserName + " contributed to your gift: " + gift.getGiftName()
                                         + " with price " + contributedAmount);

            notificationDAO.addNotification(ownerNotification);

        } catch (Exception e) {
            System.out.println("Note: Notification failed but contribution was saved.");
        }

        // 6. If gift is now fully funded, update status and notify all contributors
        if (newTotal.compareTo(new BigDecimal("100")) == 0) {
            gift.setStatus("Completed");
            giftDAO.updateGift(gift);

            List<Contribution> allContributions = contributionDAO.getContributionsByGiftId(contribution.getGiftId());

            Set<Integer> contributorIds = new HashSet<>();
            for (Contribution c : allContributions) {
                contributorIds.add(c.getContributorId());
            }

            for (int user_id : contributorIds) {
                Notification contribNotification = new Notification();
                contribNotification.setUserId(user_id);
                contribNotification.setContent("Gift '" + gift.getGiftName() + "' is now fully funded!");
                contribNotification.setGiftId(gift.getGiftId());
                notificationDAO.addNotification(contribNotification);
            }

            User friend = friendsService.getUserById(gift.getOwnerUserId());
            Notification contribNotification = new Notification();
            contribNotification.setUserId(friend.getUserId());
            contribNotification.setContent("Gift '" + gift.getGiftName() + "' is now fully funded!");
            contribNotification.setGiftId(gift.getGiftId());
            notificationDAO.addNotification(contribNotification);
        }

        return true;
    }

    /**
     * Retrieves all contributions for a user, including nested Gift and Friend
     * (Owner) objects.
     * This supports the "Contributions Page" UI requirements.
     */
    public List<Contribution> getFullUserContributions(int userId) {
        List<Contribution> list = contributionDAO.getContributionsByContributorId(userId);
        for (Contribution c : list) {
            Gift gift = giftDAO.getGiftById(c.getGiftId());
            if (gift != null) {
                c.setGift(gift);
                User friend = friendsService.getUserById(gift.getOwnerUserId());
                c.setFriend(friend);
            }
        }
        return list;
    }

    public List<Contribution> getContributionsByGiftId(int giftId) {
        return contributionDAO.getContributionsByGiftId(giftId);
    }

    /**
     * Updates contribution percentage, validates limits, and syncs gift status.
     */
    public boolean updateContributionPercentage(int contributionId, BigDecimal newPercentage) {
        if (newPercentage == null ||
                newPercentage.compareTo(BigDecimal.ZERO) <= 0 ||
                newPercentage.compareTo(new BigDecimal("100")) > 0) {
            return false;
        }

        Contribution existing = contributionDAO.getContributionById(contributionId);
        if (existing == null)
            return false;

        List<Contribution> contributions = contributionDAO.getContributionsByGiftId(existing.getGiftId());
        BigDecimal totalWithoutCurrent = BigDecimal.ZERO;
        for (Contribution c : contributions) {
            if (c.getContributionId() != contributionId) {
                totalWithoutCurrent = totalWithoutCurrent.add(c.getPercentage());
            }
        }

        BigDecimal newTotal = totalWithoutCurrent.add(newPercentage);
        if (newTotal.compareTo(new BigDecimal("100")) > 0)
            return false;

        boolean success = contributionDAO.updateContributionPercentage(contributionId, newPercentage);
        if (!success)
            return false;

        Gift gift = giftDAO.getGiftById(existing.getGiftId());
        if (gift != null) {
            String newStatus = newTotal.compareTo(new BigDecimal("100")) == 0 ? "Completed" : "Incomplete";
            if (!newStatus.equalsIgnoreCase(gift.getStatus())) {
                gift.setStatus(newStatus);
                giftDAO.updateGift(gift);

                if ("Completed".equalsIgnoreCase(newStatus)) {
                    List<Contribution> allContributions = contributionDAO
                            .getContributionsByGiftId(existing.getGiftId());
                    for (Contribution c : allContributions) {
                        Notification n = new Notification();
                        n.setUserId(c.getContributorId());
                        n.setStatus("Gift '" + gift.getGiftName() + "' is now fully funded!");
                        n.setGiftId(gift.getGiftId());
                        notificationDAO.addNotification(n);
                    }
                }
            }
        }

        return true;
    }

    public boolean deleteContribution(int contributionId) {
        Contribution existing = contributionDAO.getContributionById(contributionId);
        if (existing == null)
            return false;

        boolean success = contributionDAO.deleteContribution(contributionId);
        if (!success)
            return false;

        Gift gift = giftDAO.getGiftById(existing.getGiftId());
        if (gift != null && "Completed".equalsIgnoreCase(gift.getStatus())) {
            gift.setStatus("Incomplete");
            giftDAO.updateGift(gift);
        }

        return true;
    }
}
