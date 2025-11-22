import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDateTime;


public class MaintenanceRequestPage {
    private Scene MaintenanceRequestPageScene;
    private Stage stage;
    private String username;
    private String role;
    private String listingId;

    MaintenanceRequestPage(Stage stage, String username, String role, String listingId) {
        this.stage = stage;
        this.username = username;
        this.role = role;
        this.listingId = listingId;
    }

    public void initializeComponents() {
        VBox MaintenanceRequestPageLayout = new VBox(10);
        MaintenanceRequestPageLayout.setPadding(new Insets(10));

//logout button
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //go to login page
                UserLogin login = new UserLogin(stage);
                login.initializeComponents();
            }
        });

        Button mainPage = new Button("Go back to Main Page");
        mainPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPage mainPage = new MainPage(stage, username, role);
                mainPage.initializeComponents();
            }
        });

        Label pageTitle = new Label("Maintenance Request Page");
        pageTitle.setFont(new Font("Arial", 20));

        MaintenanceRequestPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );


        Label titleLabel = new Label("Title: ");
        TextField titleField = new TextField();

        Label descriptionLabel = new Label("Description: ");
        TextArea descriptionField = new TextArea();
        descriptionField.setPrefRowCount(5);

        Button submitBtn = new Button("Submit Request");
        submitBtn.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();

            if (title.isEmpty() || description.isEmpty()) {
                Alerts.showAlert("Validation Error", "Title and Description are required.", Alert.AlertType.WARNING);
                return;
            }

            //validating input
            if (!ValidationUtils.validateNoScript(title) || !ValidationUtils.validateNoScript(description)) {
                Alerts.showAlert("Invalid Input", "Input contains invalid characters!", Alert.AlertType.ERROR);
                return;
            }


            PropertyListing listing = PropertyListing.getListingById(listingId);
            LocalDateTime now = LocalDateTime.now();

            Maintenance.createMaintenanceRequest(new Maintenance(
                    listingId,
                    username,
                    listing.getOwnerUsername(),
                    title,
                    description,
                    MaintenanceStatus.PENDING,
                    MaintenancePriority.MEDIUM,
                    now,
                    now,
                    null
            ));

            Alerts.showAlert("Success", "Maintenance request submitted successfully.", Alert.AlertType.INFORMATION);
            //redirects to My listing page
            MyListingsPage myListingPage = new MyListingsPage(stage, username, role);
            myListingPage.initializeComponents();
        });

        MaintenanceRequestPageLayout.getChildren().addAll(
                titleLabel, titleField, descriptionLabel, descriptionField, submitBtn
        );

        MaintenanceRequestPageScene = new Scene(MaintenanceRequestPageLayout, 900, 700);
        stage.setTitle("Maintenance Request");
        stage.setScene(MaintenanceRequestPageScene);
        stage.show();
    }


}
