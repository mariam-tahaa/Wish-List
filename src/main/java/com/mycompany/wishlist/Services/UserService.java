package com.mycompany.wishlist.Services;

import com.mycompany.wishlist.DAO.UserDAO;
import com.mycompany.wishlist.Models.User;

public class UserService {
// User related services can be implemented here
//Register Empty name, Invalid email, Password < 6 chars, Password mismatch, Duplicate email
//Login Invalid email, Wrong password, User not found
private UserDAO userDAO = new UserDAO();

    public String register(User user, String rePassword) {
        if (user == null) return "Invalid user data";

        // Name validation
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            return "Name is required";
        }

        // Email validation
        if (!isValidEmail(user.getMail())) {
            return "Invalid email format";
        }

        // Password validation
        String passwordError = isValidPassword(user.getPass());
        if (passwordError != null) {
            return passwordError;
        }

        // Password match validation
        if (!user.getPass().equals(rePassword)) {
            return "Passwords do not match";
        }

        // Check for duplicate email
        if (userDAO.existsByEmail(user.getMail())) {
            return "Email already registered";
        }

        // If all validations pass, add the user
        boolean success = userDAO.addUser(user);
        return success ? "SUCCESS" : "Registration failed. Please try again.";
    }

    public String login(String email, String password) {
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return "User not found";
        }

        // Plain password comparison (hash later)
        if (!user.getPass().equals(password)) {
            return "Incorrect password";
        }

        // Return SUCCESS if login is successful
        return "SUCCESS";
    }

    // Return user object along with success status
    public User loginAndGetUser(String email, String password) {
        String result = login(email, password);
        if ("SUCCESS".equals(result)) {
            return userDAO.getUserByEmail(email);
        }
        return null;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private String isValidPassword(String password) {
        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        return null; // No error
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    public User getUserById(int userId){
        return userDAO.getUserById(userId);
    }
}