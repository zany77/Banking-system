package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Bank";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASS = "gayatri74@74"; // Change to your MySQL password

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}