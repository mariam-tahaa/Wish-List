package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.Contribution;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContributionDAO {
////////////////////////////////////////////

    // Insert new Contribution
    // Get Contributions by Gift ID
    // Update Contribution Percentage
    //TODO
    //method take current user id returns all gifts in which he contributed + this gift owner

////////////////////////////////////////////

    public boolean addContribution(Contribution contribution) {
        String sql = "Insert Into contribution (contributor_id, gift_id, percentage) Values (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, contribution.getContributorId());
            ps.setInt(2, contribution.getGiftId());
            ps.setBigDecimal(3, contribution.getPercentage());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
    //
    public Contribution getContributionById(int contributionId) {
        String sql = "SELECT * FROM contribution WHERE contribution_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, contributionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Contribution contribution = new Contribution();
                contribution.setContributionId(rs.getInt("contribution_id"));
                contribution.setContributorId(rs.getInt("contributor_id"));
                contribution.setGiftId(rs.getInt("gift_id"));
                contribution.setPercentage(rs.getBigDecimal("percentage"));
                return contribution;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all contributions by Contributor ID
    // Used to find all gifts a user has contributed to
    // Used in ContributionService
    // to notify gift owners about contributions
    // when a user contributes to a gift
    // or updates their contribution
    // or deletes their contribution
    // Returns list of contributions made by the specified contributor
    // Each contribution includes gift ID and percentage contributed
    public List<Contribution> getContributionsByContributorId(int contributorId) {
    List<Contribution> list = new ArrayList<>();
    String sql = "SELECT * FROM contribution WHERE contributor_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, contributorId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Contribution c = new Contribution();
            c.setContributionId(rs.getInt("contribution_id"));
            c.setContributorId(rs.getInt("contributor_id"));
            c.setGiftId(rs.getInt("gift_id"));
            c.setPercentage(rs.getBigDecimal("percentage"));
            list.add(c);
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return list;
}


     // Delete contribution by ID
    public boolean deleteContribution(int contributionId) {
        String sql = "DELETE FROM contribution WHERE contribution_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, contributionId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all contributions for a specific gift
    public List<Contribution> getContributionsByGiftId(int giftId) {
        List<Contribution> contributuins = new ArrayList<>();
        String sql = "SELECT * FROM contribution WHERE gift_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, giftId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contribution contribution = new Contribution();
                contribution.setContributionId(rs.getInt("contribution_id"));
                contribution.setContributorId(rs.getInt("contributor_id"));
                contribution.setGiftId(rs.getInt("gift_id"));
                contribution.setPercentage(rs.getBigDecimal("percentage"));
                contributuins.add(contribution);
            }
            return contributuins;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(); 
    }

    public boolean updateContributionPercentage(int contributionId, BigDecimal newPercentage) {
        String sql = "UPDATE contribution SET percentage = ? WHERE contribution_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, newPercentage);
            ps.setInt(2, contributionId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

}
