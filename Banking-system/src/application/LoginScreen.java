package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Banking Application");

        Label title = new Label("Banking Application");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Hyperlink registerLink = new Hyperlink("Don't have an account? Register Here");

        Label message = new Label();

        loginButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                String password = passwordField.getText();
                UserDAO userDAO = new UserDAO();
                if (userDAO.validateUser(username, password)) {
                    message.setText("Login successful!");
                    new Main().start(new Stage());
                    primaryStage.close();
                } else {
                    message.setText("Invalid credentials.");
                }
            } catch (Exception ex) {
                message.setText("Error: " + ex.getMessage());
            }
        });

        registerLink.setOnAction(e -> {
            try {
                new RegisterScreen().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                message.setText("Error: " + ex.getMessage());
            }
        });

        VBox vbox = new VBox(10, title, usernameField, passwordField, loginButton, registerLink, message);
        vbox.setPadding(new Insets(20));
        primaryStage.setScene(new Scene(vbox, 350, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}