import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;


public class PaymentPage {
    private Scene PaymentPageScene;
    private Stage stage;
    private String username;
    private String role;

    public PaymentPage(Stage stage, String username, String role) {
        this.stage = stage;
        this.username = username;
        this.role = role;
    }

    public void initializeComponents() {
        // only manager and owner can access
        if (!role.equals("Manager") && !role.equals("Owner")) {
            Alerts.showAlert("Access Denied", "You do not have permission to view this page.", Alert.AlertType.ERROR);
            return;
        }

        VBox PaymentPageLayout = new VBox(10);
        PaymentPageLayout.setPadding(new Insets(10));

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

        Label pageTitle = new Label("Payments");
        pageTitle.setFont(new Font("Arial", 20));
        PaymentPageLayout.getChildren().addAll(pageTitle, mainPage, logout);

        // get Appointments based on role
        ArrayList<Payment> payments = getPayments(role, username);

        // Create table
        TableView<Payment> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(900);

        TableColumn<Payment, String> listingCol = new TableColumn<>("Listing");
        TableColumn<Payment, String> renterCol = new TableColumn<>("Renter");
        TableColumn<Payment, String> managerCol = new TableColumn<>("Manager");
        TableColumn<Payment, String> ownerCol = new TableColumn<>("Owner");
        TableColumn<Payment, Double> amountCol = new TableColumn<>("Amount");
        TableColumn<Payment, String> dateCol = new TableColumn<>("Payment Date");
        TableColumn<Payment, String> monthCol = new TableColumn<>("Month");
        TableColumn<Payment, String> methodCol = new TableColumn<>("Method");

        table.getColumns().addAll(listingCol, renterCol, managerCol, ownerCol, amountCol, dateCol, monthCol, methodCol);


        // Bind columns to Payment fields
        listingCol.setCellValueFactory(new PropertyValueFactory<>("listingId"));
        renterCol.setCellValueFactory(new PropertyValueFactory<>("renterUsername"));
        managerCol.setCellValueFactory(new PropertyValueFactory<>("managerUsername"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("paymentMonth"));
        methodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        // Convert ArrayList to ObservableList
        ObservableList<Payment> data = FXCollections.observableArrayList(payments);
        table.setItems(data);
        PaymentPageLayout.getChildren().addAll(table);

        PaymentPageScene = new Scene(PaymentPageLayout, 900, 700);
        stage.setTitle("Payments");
        stage.setScene(PaymentPageScene);
        stage.show();
    }

    private ArrayList<Payment> getPayments(String role, String username) {
        return Payment.getPaymentsByRole(role, username);
    }
}
