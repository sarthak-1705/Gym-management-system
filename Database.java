package Interface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnect {

    // Establish connection to the database
    public static Connection connect() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Connect to the database with the specified URL, username, and password
            String url = "jdbc:mysql://localhost:3306/database_name";
            String username = "sarthak";
            String password = "sarthak1705";
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }
}
