package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.Gift;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiftDAO {

    // Add new gift
    public boolean addGift(Gift gift) {
        String sql = "INSERT INTO gift (gift_name, price, status, owner_user_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gift.getGiftName());
            ps.setBigDecimal(2, gift.getPrice());
            ps.setString(3, gift.getStatus());
            ps.setInt(4, gift.getOwnerUserId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get all gifts by user
    public List<Gift> getGiftsByUser(int userId) {
        List<Gift> gifts = new ArrayList<>();
        String sql = "SELECT * FROM gift WHERE owner_user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Gift gift = new Gift();
                gift.setGiftId(rs.getInt("gift_id"));
                gift.setGiftName(rs.getString("gift_name"));
                gift.setPrice(rs.getBigDecimal("price"));
                gift.setStatus(rs.getString("status"));
                gift.setOwnerUserId(rs.getInt("owner_user_id"));
                gifts.add(gift);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gifts;
    }
}
