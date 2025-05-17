package application;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void saveTransaction(Transaction tx) throws SQLException {
        String sql = "INSERT INTO transactions (account_number, type, amount, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tx.getAccountNumber());
            pstmt.setString(2, tx.getType());
            pstmt.setDouble(3, tx.getAmount());
            pstmt.setString(4, tx.getDate().toString());
            pstmt.executeUpdate();
        }
    }

    public List<Transaction> getTransactions(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY date DESC";
        List<Transaction> list = new ArrayList<>();

        try (Connection conn = DBUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction tx = new Transaction(
                        rs.getString("account_number"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        LocalDateTime.parse(rs.getString("date"))
                );
                list.add(tx);
            }
        }
        return list;
    }
}
