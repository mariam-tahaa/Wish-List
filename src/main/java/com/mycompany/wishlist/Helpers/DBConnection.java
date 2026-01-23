package com.mycompany.wishlist.Helpers;
 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Helper class to manage the connection to the Oracle Database.
 * This class should be placed in the com.mycompany.wishlist.Helpers package.
 */
public class DBConnection {

 
    
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/XE"; 
    private static final String DB_USER = "Wishlist"; 
    private static final String DB_PASSWORD = "1234"; 

    /**
     * Establishes and returns a connection to the Oracle database.
     * 
     * @return A valid Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            // Ensure the driver is loaded (though often automatic)
            // Class.forName("oracle.jdbc.driver.OracleDriver"); 

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Successfully connected to the Oracle Database!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
            throw e; // Re-throw the exception for the calling method to handle
        }
    }

    /**
     * Closes the provided database connection.
     * 
     * @param connection The Connection object to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Example main method for testing the connection
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = getConnection();
            // Perform a simple test query if needed
        } catch (SQLException e) {
            // Connection failed, already printed error details
        } finally {
            closeConnection(conn);
        }
    }
}
