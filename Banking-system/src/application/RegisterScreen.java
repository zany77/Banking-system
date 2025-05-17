package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterScreen extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Register New User");

        TextField fullNameField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField dobField = new TextField();
        TextField fatherNameField = new TextField();
        ComboBox<String> accountTypeBox = new ComboBox<>();
        accountTypeBox.getItems().addAll("Savings", "Current");

        fullNameField.setPromptText("Full Name");
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        phoneField.setPromptText("Phone Number");
        addressField.setPromptText("Address");
        dobField.setPromptText("Date of Birth (YYYY-MM-DD)");
        fatherNameField.setPromptText("Father's Name");
        accountTypeBox.setPromptText("Account Type");

        Button registerButton = new Button("Create Account");
        Button backButton = new Button("Back to Login");

        Label message = new Label();

        registerButton.setOnAction(e -> {
            try {
                UserDAO userDAO = new UserDAO();
                boolean success = userDAO.registerUser(
                    usernameField.getText(),
                    passwordField.getText(),
                    fullNameField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    dobField.getText(),
                    fatherNameField.getText(),
                    accountTypeBox.getValue()
                );
                if (success) {
                    message.setText("Registration successful!");
                } else {
                    message.setText("Username already exists.");
                }
            } catch (Exception ex) {
                message.setText("Error: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> {
            try {
                new LoginScreen().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                message.setText("Error: " + ex.getMessage());
            }
        });

        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setPadding(new Insets(20));
        form.add(new Label("Full Name:"), 0, 0); form.add(fullNameField, 1, 0);
        form.add(new Label("Username:"), 0, 1); form.add(usernameField, 1, 1);
        form.add(new Label("Password:"), 0, 2); form.add(passwordField, 1, 2);
        form.add(new Label("Phone:"), 0, 3); form.add(phoneField, 1, 3);
        form.add(new Label("Address:"), 0, 4); form.add(addressField, 1, 4);
        form.add(new Label("DOB:"), 0, 5); form.add(dobField, 1, 5);
        form.add(new Label("Father's Name:"), 0, 6); form.add(fatherNameField, 1, 6);
        form.add(new Label("Account Type:"), 0, 7); form.add(accountTypeBox, 1, 7);

        VBox vbox = new VBox(10, form, registerButton, backButton, message);
        vbox.setPadding(new Insets(10));
        primaryStage.setScene(new Scene(vbox, 450, 500));
        primaryStage.show();
    }
}