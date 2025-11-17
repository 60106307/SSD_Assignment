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

public class UserLogin {
    private Scene loginScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Stage stage;

    public UserLogin(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));
        Button loginButton = new Button("Sign In");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { authenticate(); }
        });
        //The text added and the signuP button
        Label orLabel = new Label("OR");
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserSignUp signUpPage = new UserSignUp(stage);
                signUpPage.initializeComponents();
            }
        });
        loginLayout.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginButton,orLabel, signUpButton);

        loginScene = new Scene(loginLayout, 300, 300);
        stage.setTitle("User Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Connection con = DBUtils.establishConnection();
        String query = "SELECT * FROM users WHERE username=?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                String dbPass=rs.getString("password");
//                byte[] salt=rs.getString("salt").getBytes();

                String saltString = rs.getString("salt");

                // Decode the salt back to bytes
                byte[] salt = Base64.getDecoder().decode(saltString);

                String hashedPassword= PasswordUtils.generateHash(password, salt);
                if(dbPass.equals(hashedPassword)){
                    UserChangePassword changePassword = new UserChangePassword(stage, username);
                    changePassword.initializeComponents();
                }else {
                    showAlert("Authentication Failed", "Invalid username or password.");
                }

            } else {
                showAlert("Authentication Failed", "Something Went Wrong.");
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
