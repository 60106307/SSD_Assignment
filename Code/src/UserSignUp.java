import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserSignUp {
    private Scene signUpScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private TextField roleField = new TextField();
    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();

    private Stage stage;

    public UserSignUp(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox signUpLayout = new VBox(10);
        signUpLayout.setPadding(new Insets(10));
        Button loginButton = new Button("Sign In");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    registerUser();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //The text added and the signuP button
        signUpLayout.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Role:"), roleField,
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField,
                loginButton);

        signUpScene = new Scene(signUpLayout, 300, 400);
        stage.setTitle("User Sign up");
        stage.setScene(signUpScene);
        stage.show();
    }

    private void registerUser() throws NoSuchAlgorithmException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        byte[] salt= PasswordUtils.createSalt();
        String hashedPassword= PasswordUtils.generateHash(password,salt);
        String saltString = Base64.getEncoder().encodeToString(salt);

        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO `users` (`username`, `password`, `role`, `firstname`, `lastname`, `salt`) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, role);
            statement.setString(4, firstName);
            statement.setString(5, lastName);
            statement.setString(6, saltString);
            int rs = statement.executeUpdate();

            if (rs ==1) {
                showAlert("Success", "User registered!");

                UserChangePassword changePassword = new UserChangePassword(stage, username);
                changePassword.initializeComponents();
            } else {
                showAlert("Authentication Failed", "Failed to register the user");
            }
            DBUtils.closeConnection(con, statement);
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
