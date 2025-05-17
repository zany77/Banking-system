package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // Create a new account
    public void createAccount(String accNum, String name, double balance) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, holder_name, balance) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNum);
            pstmt.setString(2, name);
            pstmt.setDouble(3, balance);
            pstmt.executeUpdate();
        }
    }

    // Deposit money
    public void deposit(String accNum, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accNum);
            pstmt.executeUpdate();
        }
    }

    // Withdraw money
    public void withdraw(String accNum, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";

        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accNum);
            pstmt.executeUpdate();
        }
    }

    // Get current balance
    public double getBalance(String accNum) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";

        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNum);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        }
        return -1;
    }

    // Get all accounts (for TableView display)
    public List<Account> getAllAccounts() throws SQLException {
        String sql = "SELECT * FROM accounts";
        List<Account> list = new ArrayList<>();

        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Account acc = new Account(
                        rs.getString("account_number"),
                        rs.getString("holder_name"),
                        rs.getDouble("balance")
                );
                list.add(acc);
            }
        }
        return list;
    }
}
