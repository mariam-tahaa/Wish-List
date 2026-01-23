package com.mycompany.wishlist.Services;


import com.mycompany.wishlist.DAO.UserDAO;
import com.mycompany.wishlist.Models.User;

public class UserService {
// User related services can be implemented here
//Register Empty name, Invalid email, Password < 6 chars, Password mismatch, Duplicate email
//Login Invalid email, Wrong password, User not found
private UserDAO userDAO = new UserDAO();

 public boolean register(User user, String rePassword) {

        
        if (user == null) return false;

        // Name validation
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            return false;
        }

        // Email validation
        if (!isValidEmail(user.getMail())) {
            return false;
        }
        // Password validation
        if (!isValidPassword(user.getPass())) {
            return false;
        }
        // Password match validation
         if (!user.getPass().equals(rePassword)) {
            return false;
        }

        // Check for duplicate email
        if (userDAO.existsByEmail(user.getMail())) return false;

        // If all validations pass, add the user
        return userDAO.addUser(user);
    
    }

    public User login(String email, String password) {

        if (!isValidEmail(email) || isEmpty(password)) {
            return null;
        }

        User user = userDAO.getUserByEmail(email);

        if (user == null) return null;

        // Plain password comparison (hash later)
        if (!user.getPass().equals(password)) {
            return null;
        }

        return user;
    }



     private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
        private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
    
    


