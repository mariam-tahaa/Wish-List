/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wishlist.Models;


/**
 *
 * @author HELLo
 */


import java.math.BigDecimal;

public class Contribution {

    private int contributionId;     // maps to contribution_id
    private int contributorId;      // maps to contributor_id (FK) to-User 
    private int giftId;             // maps to gift_id (FK)
    private BigDecimal percentage;  // maps to percentage (0 < x <= 100)
    private Gift gift;               // Associated Gift object
    private User friend;
    
    // Constructors
    public Contribution() {}

    public Contribution(int contributorId, int giftId, BigDecimal percentage) {
        this.contributorId = contributorId;
        this.giftId = giftId;
        this.percentage = percentage;
    }
    // Associated Gift and Friend getters and setters
    public Gift getGift() { return gift; }
    public void setGift(Gift gift) { this.gift = gift; }
    // Associated Friend getters and setters
    public User getFriend() { return friend; }
    public void setFriend(User friend) { this.friend = friend; }
    // Calculate the monetary amount based on percentage and gift price
    public BigDecimal getCalculatedAmount() {
        if (gift != null && percentage != null) {
            return gift.getPrice().multiply(percentage).divide(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }


    // Getters and Setters
    public int getContributionId() {
        return contributionId;
    }

    public void setContributionId(int contributionId) {
        this.contributionId = contributionId;
    }

    public int getContributorId() {
        return contributorId;
    }

    public void setContributorId(int contributorId) {
        this.contributorId = contributorId;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
