package com.mycompany.wishlist.DAO;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    
     
    // Get user by ID
    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT * FROM app_user WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setMail(rs.getString("mail"));
                user.setPass(rs.getString("pass"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Insert a new user
    public boolean addUser(User user) {
        String sql = "INSERT INTO app_user (user_name, mail, pass) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getMail());
            ps.setString(3, user.getPass());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
