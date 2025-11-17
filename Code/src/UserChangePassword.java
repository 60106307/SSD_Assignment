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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserChangePassword {
    private Scene changePasswordScene;
    private PasswordField newPasswordField = new PasswordField();
    private Stage stage;
    private String username;

    public UserChangePassword(Stage primaryStage, String username){
        this.stage = primaryStage;
        this.username = username;
    }

    public void initializeComponents() {
        VBox changePasswordLayout = new VBox(10);
        changePasswordLayout.setPadding(new Insets(10));
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                //changePassword();
                try {
                    changePassword();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                //go to login page
                UserLogin login = new UserLogin(stage);
                login.initializeComponents();
            }
        });
        changePasswordLayout.getChildren().addAll(new Label("Welcome " + username), new Label("New Password:"), newPasswordField, changePasswordButton, logout);

        changePasswordScene = new Scene(changePasswordLayout, 300, 300);
        stage.setTitle("Change Password");
        stage.setScene(changePasswordScene);
        stage.show();
    }

    private void changePassword() throws NoSuchAlgorithmException {
        String newPassword = newPasswordField.getText();
        byte[] salt= PasswordUtils.createSalt();
        String hashedPassword= PasswordUtils.generateHash(newPassword,salt);
        String saltString = Base64.getEncoder().encodeToString(salt);

        String query = "UPDATE users SET password=?, salt=? WHERE username =?;";
        try(Connection con = DBUtils.establishConnection();
            PreparedStatement statement = con.prepareStatement(query)){
            //i put it inside the try to make sure it closes if there is an error
            statement.setString(1, hashedPassword);
            statement.setString(2,saltString );
            statement.setString(3, username);
            int result = statement.executeUpdate();
            if (result == 1) {
                showAlert("Success", "Password successfully changed");
            } else {
                showAlert("Failure", "Failed to update password");
            }
            DBUtils.closeConnection(con, statement);
        }catch(Exception e){
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
