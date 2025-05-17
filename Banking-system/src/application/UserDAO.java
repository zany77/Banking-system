package application;

import java.sql.*;

public class UserDAO {

    // Validate login credentials
    public boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    // Register new user
    public boolean registerUser(String username, String password, String fullName, String phone, String address,
                                String dob, String fatherName, String accountType) throws SQLException {
        String sql = "INSERT INTO users (username, password, full_name, phone, address, dob, father_name, account_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, dob); // format: yyyy-mm-dd
            pstmt.setString(7, fatherName);
            pstmt.setString(8, accountType);
            pstmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // username already exists
        }
    }
}
