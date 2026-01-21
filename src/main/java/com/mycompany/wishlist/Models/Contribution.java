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
import java.text.DecimalFormat;

public class Contribution {

    private int contributionId;     // maps to contribution_id
    private int contributorId;      // maps to contributor_id (FK)
    private int giftId;             // maps to gift_id (FK)
    private BigDecimal percentage;  // maps to percentage (0 < x <= 100)

    // Constructors
    public Contribution() {}

    public Contribution(int contributorId, int giftId, BigDecimal percentage) {
        this.contributorId = contributorId;
        this.giftId = giftId;
        this.percentage = percentage;
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
