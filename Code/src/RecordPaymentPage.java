import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class RecordPaymentPage {
    private Scene RecordPaymentPageScene;
    private Stage stage;
    private String username;
    private String role;
    private String listingId;

    RecordPaymentPage(Stage stage, String username, String role, String listingId) {
        this.stage = stage;
        this.username = username;
        this.role = role;
        this.listingId = listingId;
    }

    public void initializeComponents() {
        VBox RecordPaymentPageLayout = new VBox(10);
        RecordPaymentPageLayout.setPadding(new Insets(10));

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

        //redirects other users to main page with error
        if (!role.equals("Manager")) {
            Alerts.showAlert("Access Denied", "Only managers can record payments.", Alert.AlertType.ERROR);
            MainPage mainPage = new MainPage(stage, username, role);
            mainPage.initializeComponents();
            return;
        }


        Button mainPage = new Button("Go back to Main Page");
        mainPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPage mainPage = new MainPage(stage, username, role);
                mainPage.initializeComponents();
            }
        });

        Label pageTitle = new Label("Record Payment Page");
        pageTitle.setFont(new Font("Arial", 20));

        RecordPaymentPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );

        // Amount
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter payment amount");

        // Payment Method
        Label methodLabel = new Label("Payment Method:");
        ComboBox<String> methodField = new ComboBox<>();
        methodField.getItems().addAll(PaymentMethod.CASH.toString(), PaymentMethod.BANK_TRANSFER.toString(), PaymentMethod.CARD.toString());

        // renter username
        Label renterLabel = new Label("Renter username:");
        TextField renterField = new TextField();
        renterField.setPromptText("Enter renter username");


        // Record Button
        Button recordBtn = new Button("Record Payment");
        recordBtn.setOnAction(e -> {
            // Validation no empty
            if (amountField.getText().isEmpty() || renterField.getText().isEmpty() || methodField.getValue() == null) {
                Alerts.showAlert("Error", "Inputs cannot be empty.", Alert.AlertType.ERROR);
                return;
            }

            //validation no scripts
            if (!ValidationUtils.validateNoScript(amountField.getText()) || !ValidationUtils.validateNoScript(renterField.getText())) {
                Alerts.showAlert("Invalid Input", "Input contains invalid characters!", Alert.AlertType.ERROR);
                return;
            }

            //validate amount
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    Alerts.showAlert("Error", "Amount must be > 0", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException ex) {
                Alerts.showAlert("Error", "Amount must be numeric.", Alert.AlertType.ERROR);
                return;
            }

            PropertyListing listing = PropertyListing.getListingById(listingId);
            Payment payment = new Payment(
                    listingId,
                    renterField.getText(),
                    username,
                    listing.getOwnerUsername(),
                    amount,
                    LocalDate.now(),
                    YearMonth.now(),
                    PaymentMethod.valueOf(methodField.getValue()),
                    LocalDateTime.now()
            );

            Payment.createPayment(payment);

            //redirects to listing page
            ListingPage listingPage = new ListingPage(stage, username, role);
            listingPage.initializeComponents();

        });

        RecordPaymentPageLayout.getChildren().addAll(
                amountLabel, amountField, methodLabel, methodField, renterLabel, renterField, recordBtn
        );

        RecordPaymentPageScene = new Scene(RecordPaymentPageLayout, 900, 700);
        stage.setTitle("Record Payment");
        stage.setScene(RecordPaymentPageScene);
        stage.show();

    }
}
