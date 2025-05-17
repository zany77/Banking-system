package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;

public class Main extends Application {
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    private Stack<Transaction> undoStack = new Stack<>();
    private TableView<Account> accountTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SmartBank");

        TextField accNumField = new TextField();
        accNumField.setPromptText("Account Number");

        TextField depositField = new TextField();
        depositField.setPromptText("Deposit Amount");

        TextField withdrawField = new TextField();
        withdrawField.setPromptText("Withdraw Amount");

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button undoBtn = new Button("Undo Last Transaction");
        Button viewHistoryBtn = new Button("View Transactions");
        Button refreshAccountsBtn = new Button("Refresh Accounts");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(150);

        // Deposit Action
        depositBtn.setOnAction(e -> {
            try {
                String acc = accNumField.getText().trim();
                double amount = Double.parseDouble(depositField.getText().trim());

                accountDAO.deposit(acc, amount);
                Transaction tx = new Transaction(acc, "deposit", amount, LocalDateTime.now());
                transactionDAO.saveTransaction(tx);
                undoStack.push(tx);

                outputArea.setText("Deposit successful.");
                refreshAccountTable();
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        // Withdraw Action
        withdrawBtn.setOnAction(e -> {
            try {
                String acc = accNumField.getText().trim();
                double amount = Double.parseDouble(withdrawField.getText().trim());

                accountDAO.withdraw(acc, amount);
                Transaction tx = new Transaction(acc, "withdraw", amount, LocalDateTime.now());
                transactionDAO.saveTransaction(tx);
                undoStack.push(tx);

                outputArea.setText("Withdrawal successful.");
                refreshAccountTable();
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        // Undo Action
        undoBtn.setOnAction(e -> {
            try {
                if (undoStack.isEmpty()) {
                    outputArea.setText("No transaction to undo.");
                    return;
                }

                Transaction lastTx = undoStack.pop();
                if (lastTx.getType().equals("deposit")) {
                    accountDAO.withdraw(lastTx.getAccountNumber(), lastTx.getAmount());
                } else if (lastTx.getType().equals("withdraw")) {
                    accountDAO.deposit(lastTx.getAccountNumber(), lastTx.getAmount());
                }

                outputArea.setText("Undid: " + lastTx.getType() + " $" + lastTx.getAmount());
                refreshAccountTable();
            } catch (Exception ex) {
                outputArea.setText("Undo failed: " + ex.getMessage());
            }
        });

        // View History
        viewHistoryBtn.setOnAction(e -> {
            try {
                String acc = accNumField.getText().trim();
                List<Transaction> txList = transactionDAO.getTransactions(acc);
                StringBuilder sb = new StringBuilder();
                for (Transaction tx : txList) {
                    sb.append(tx.getDate()).append(" - ")
                            .append(tx.getType()).append(": $")
                            .append(tx.getAmount()).append("\n");
                }
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                outputArea.setText("Error loading history: " + ex.getMessage());
            }
        });

        // Refresh account table
        refreshAccountsBtn.setOnAction(e -> refreshAccountTable());

        // Setup Table Columns
        TableColumn<Account, String> colAcc = new TableColumn<>("Account No");
        colAcc.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<Account, String> colName = new TableColumn<>("Holder Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("holderName"));

        TableColumn<Account, Double> colBal = new TableColumn<>("Balance");
        colBal.setCellValueFactory(new PropertyValueFactory<>("balance"));

        accountTable.getColumns().addAll(colAcc, colName, colBal);
        accountTable.setPrefHeight(200);

        VBox vbox = new VBox(10,
                accNumField,
                depositField, depositBtn,
                withdrawField, withdrawBtn,
                undoBtn, viewHistoryBtn,
                refreshAccountsBtn,
                accountTable,
                outputArea
        );
        vbox.setPadding(new Insets(15));

        primaryStage.setScene(new Scene(vbox, 500, 700));
        primaryStage.show();

        refreshAccountTable(); // Load data at startup
    }

    private void refreshAccountTable() {
        try {
            List<Account> list = accountDAO.getAllAccounts();
            ObservableList<Account> data = FXCollections.observableArrayList(list);
            accountTable.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
