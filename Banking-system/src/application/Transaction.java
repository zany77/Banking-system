package application;

import java.time.LocalDateTime;

public class Transaction {
    private String accountNumber;
    private String type; // "deposit" or "withdraw"
    private double amount;
    private LocalDateTime date;

    public Transaction(String accountNumber, String type, double amount, LocalDateTime date) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }
}
