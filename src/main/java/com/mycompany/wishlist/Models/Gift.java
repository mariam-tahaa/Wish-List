package com.mycompany.wishlist.Models;



import java.math.BigDecimal;

public class Gift {

    private int giftId;           // maps to gift_id
    private String giftName;      // maps to gift_name
    private BigDecimal price;     // maps to price
    private String status;        // maps to status ('Incomplete' or 'Completed')
    private int ownerUserId;      // maps to owner_user_id (foreign key)

    // Constructors
    public Gift() {
        // default constructor
    }

    public Gift(String giftName, BigDecimal price, String status, int ownerUserId) {
        this.giftName = giftName;
        this.price = price;
        this.status = status;
        this.ownerUserId = ownerUserId;
    }

    // Getters and Setters
    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
}
