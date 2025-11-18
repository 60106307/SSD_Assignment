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
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserSignUp {
    private Scene signUpScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private ComboBox<String> roleField = new ComboBox<>();
    private TextField nameField = new TextField();
    private TextField emailField = new TextField();
    private TextField phoneField = new TextField();
    private TextField companyNameField = new TextField();

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
        //role combo box to select from options
        roleField.getItems().addAll("Manager", "Renter", "Owner");
        roleField.setValue("Renter");

        //company label to make it dynamic
        Label companyLabel = new Label("Company Name:");

        //The text added and the signuP button
        signUpLayout.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Select Role:"), roleField,
                new Label("Name:"), nameField,
                new Label("Email:"), emailField,
                new Label("Phone:"), phoneField,
                companyLabel, companyNameField,
                loginButton
        );
        companyLabel.setVisible(false);
        companyNameField.setVisible(false);

        // listener to show/hide company name
        roleField.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean isManager ="Manager".equals(newValue);
            //if manager is selected it will shjow
            companyNameField.setVisible(isManager);
            companyLabel.setVisible(isManager);
        });

        signUpScene = new Scene(signUpLayout, 300, 500);
        stage.setTitle("User Sign up");
        stage.setScene(signUpScene);
        stage.show();
    }

    private void registerUser() throws NoSuchAlgorithmException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getValue();
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String companyName = null;
        if (role.equals("Manager")) {
            companyName = companyNameField.getText();
        }
        //validating fields before adding to db
        if (!ValidationUtils.validateUsername(username)) {
            showAlert("Invalid Input", "Username must be 2-10 lowercase letters, digits or hyphens.");
            return;
        }

        if (!ValidationUtils.validateEmail(email)) {
            showAlert("Invalid Input", "Email is invalid!");
            return;
        }

        if (!ValidationUtils.validateQaPhoneNumber(phone)) {
            showAlert("Invalid Input", "Phone number must follow Qatar format (+974XXXXXXXX) Or (974)XXXXXXXX.");
            return;
        }

        //check that all fields have no scripts
        if (!ValidationUtils.validateNoScript(name) && !ValidationUtils.validateNoScript(email) && !ValidationUtils.validateNoScript(phone) && !ValidationUtils.validateNoScript(username) && !ValidationUtils.validateNoScript(password)) {
            showAlert("Invalid Input", "Input contains invalid characters!");
            return;
        }




        byte[] salt = PasswordUtils.createSalt();
        String hashedPassword = PasswordUtils.generateHash(password, salt);
        String saltString = Base64.getEncoder().encodeToString(salt);

        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO `users` (`username`, `password`, `role`, `name`, `email`,`phone`, `salt`, `companyName`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, role);
            statement.setString(4, name);
            statement.setString(5, email);
            statement.setString(6, phone);
            statement.setString(7, saltString);
            statement.setString(8, companyName);
            int rs = statement.executeUpdate();

            if (rs == 1) {
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
