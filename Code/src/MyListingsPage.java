import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;


public class MyListingsPage {
    private Scene MyListingPageScene;
    private Stage stage;
    private String username;
    private String role;

    public MyListingsPage(Stage stage, String username, String role) {
        this.stage = stage;
        this.username = username;
        this.role = role;
    }

    public void initializeComponents() {
        VBox MyListingPageLayout = new VBox(10);
        MyListingPageLayout.setPadding(new Insets(10));
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

        Label pageTitle = new Label("My Listings");
        pageTitle.setFont(new Font("Arial", 20));

        MyListingPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );

        // get listings based on role
        ArrayList<PropertyListing> listings = getPayedPropertyListings(role, username);

        // Create table
        TableView<PropertyListing> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(1000);

        //table.setEditable(true);
        TableColumn<PropertyListing, String> title = new TableColumn<>("Title");
        TableColumn<PropertyListing, String> description = new TableColumn<>("Description");
        TableColumn<PropertyListing, String> address = new TableColumn<>("Address");
        TableColumn<PropertyListing, String> bedrooms = new TableColumn<>("Bedrooms");
        TableColumn<PropertyListing, String> bathrooms = new TableColumn<>("Bathrooms");
        TableColumn<PropertyListing, String> size = new TableColumn<>("Size");
        TableColumn<PropertyListing, String> price = new TableColumn<>("Price");
        TableColumn<PropertyListing, String> status = new TableColumn<>("Status");
        TableColumn<PropertyListing, String> furnitureType = new TableColumn<>("Furniture type");

        table.getColumns().addAll(
                title, description, address, bedrooms, bathrooms, size, price, status, furnitureType
        );
        table.getColumns().add(getMaintenanceRequestColumn());


        // Bind columns to PropertyListing fields
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        bedrooms.setCellValueFactory(new PropertyValueFactory<>("nOfBedrooms"));
        bathrooms.setCellValueFactory(new PropertyValueFactory<>("nOfBathrooms"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        furnitureType.setCellValueFactory(new PropertyValueFactory<>("furnitureType"));


        // Convert ArrayList to ObservableList
        ObservableList<PropertyListing> data = FXCollections.observableArrayList(listings);
        // Add data to the table
        table.setItems(data);
        double fieldWidth = 120;

        MyListingPageLayout.getChildren().add(1, table);

        MyListingPageScene = new Scene(MyListingPageLayout, 1000, 700);
        stage.setTitle("Listings");
        stage.setScene(MyListingPageScene);
        stage.show();
    }

    private ArrayList<PropertyListing> getPayedPropertyListings(String role, String username) {
        return PropertyListing.getPayedPropertyListings(role, username);
    }

    private TableColumn<PropertyListing, Void> getMaintenanceRequestColumn() {
        TableColumn<PropertyListing, Void> maintenanceRequestCol = new TableColumn<>("Request Maintenance");

        maintenanceRequestCol.setCellFactory(param -> new TableCell<PropertyListing, Void>() {
            private final Button btn = new Button("Request Maintenance");

            {
                btn.setOnAction(e -> {
                    PropertyListing listing = getTableView().getItems().get(getIndex());
                    String listingId = listing.getId(); // the UUID stored in DB
                    try {
                        // Open the booking page
                        MaintenanceRequestPage page = new MaintenanceRequestPage(stage, username, role, listingId);
                        page.initializeComponents();
                    } catch (Exception ex) {
                        Alerts.showAlert("Error", "Unable to open Maintenance Request page.", Alert.AlertType.ERROR);
                    }
                });
            }

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
        maintenanceRequestCol.setPrefWidth(120);
        return maintenanceRequestCol;
    }
}
