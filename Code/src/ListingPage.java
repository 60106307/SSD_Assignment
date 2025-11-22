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

public class ListingPage {
    private Scene ListingPageScene;
    private Stage stage;
    private String username;
    private String role;

    public ListingPage(Stage stage, String username, String role) {
        this.stage = stage;
        this.username = username;
        this.role = role;
    }

    public void initializeComponents() {
        VBox ListingPageLayout = new VBox(10);
        ListingPageLayout.setPadding(new Insets(10));
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

        Label pageTitle = new Label("Listings");
        pageTitle.setFont(new Font("Arial", 20));

        ListingPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );

        // get listings based on role
        ArrayList<PropertyListing> listings = getPropertyListings(role);

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
        if (role.equals("Renter")) {
            table.getColumns().add(getBookAppointmentColumn());
        } else {
            table.getColumns().add(getModifyListingColumn());
            if (role.equals("Manager")) {
                table.getColumns().add(getRecordPaymentColumn());
            }
        }


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
        //this should show only if the person is Manager
        if (role.equals("Manager")) {

            final TextField addTitle = new TextField();
            addTitle.setPromptText("Title");
            addTitle.setMaxWidth(fieldWidth);

            final TextField addDescription = new TextField();
            addDescription.setPromptText("Description");
            addDescription.setMaxWidth(fieldWidth);

            final TextField addAddress = new TextField();
            addAddress.setPromptText("Address");
            addAddress.setMaxWidth(fieldWidth);

            final TextField addNOfBedrooms = new TextField();
            addNOfBedrooms.setPromptText("NOfBedrooms");
            addNOfBedrooms.setMaxWidth(fieldWidth);

            final TextField addNOfBathrooms = new TextField();
            addNOfBathrooms.setPromptText("NOfBathrooms");
            addNOfBathrooms.setMaxWidth(fieldWidth);

            final TextField addSize = new TextField();
            addSize.setPromptText("Size");
            addSize.setMaxWidth(fieldWidth);

            final TextField addPrice = new TextField();
            addPrice.setPromptText("Price");
            addPrice.setMaxWidth(fieldWidth);

            // this should be a dropdown with three options: FURNISHED, SEMI_FURNISHED, UNFURNISHED
            final ComboBox<String> addFurnitureType = new ComboBox<>();
            addFurnitureType.getItems().addAll("FURNISHED", "SEMI_FURNISHED", "UNFURNISHED");
            addFurnitureType.setValue("UNFURNISHED");
            addFurnitureType.setMaxWidth(fieldWidth);

            final TextField addOwnerUsername = new TextField();
            addOwnerUsername.setPromptText("Owner Username");
            addOwnerUsername.setMaxWidth(fieldWidth);


            //input validation
            if (
                    !ValidationUtils.validateNoScript(addAddress.getText())
                            || !ValidationUtils.validateNoScript(addNOfBedrooms.getText())
                            || !ValidationUtils.validateNoScript(addNOfBathrooms.getText())
                            || !ValidationUtils.validateNoScript(addSize.getText())
                            || !ValidationUtils.validateNoScript(addTitle.getText())
                            || !ValidationUtils.validateNoScript(addDescription.getText())
                            || !ValidationUtils.validateNoScript(addPrice.getText())
                            || !ValidationUtils.validateNoScript(addOwnerUsername.getText())
            ) {
                Alerts.showAlert("Invalid Input", "Input contains invalid characters!", Alert.AlertType.ERROR);
                return;
            }

            final Button addButton = new Button("Add Listing");
            addButton.setOnAction(e -> {
                try {
                    PropertyListing.createPropertyListings(
                            new PropertyListing(
                                    addAddress.getText(),
                                    Integer.parseInt(addNOfBedrooms.getText()),
                                    Integer.parseInt(addNOfBathrooms.getText()),
                                    Integer.parseInt(addSize.getText()),
                                    addTitle.getText(),
                                    addDescription.getText(),
                                    Double.parseDouble(addPrice.getText()),
                                    Status.AVAILABLE,
                                    FurnitureType.valueOf(addFurnitureType.getValue()),
                                    addOwnerUsername.getText(),
                                    username
                            )
                    );

                    // clear inputs
                    addAddress.clear();
                    addNOfBedrooms.clear();
                    addNOfBathrooms.clear();
                    addSize.clear();
                    addTitle.clear();
                    addDescription.clear();
                    addPrice.clear();
                    addOwnerUsername.clear();

                    // reload page
                    ListingPage refreshed = new ListingPage(stage, username, role);
                    refreshed.initializeComponents();

                } catch (Exception ex) {
                    Alerts.showAlert("Invalid Input", "Check your inputs.", Alert.AlertType.ERROR);
                }
            });

            //to make them all next to each other (hbox instead of vbox)
            HBox addForm = new HBox(10);
            addForm.getChildren().addAll(
                    addAddress,
                    addNOfBedrooms,
                    addNOfBathrooms,
                    addSize,
                    addTitle,
                    addDescription,
                    addPrice,
                    addFurnitureType,
                    addOwnerUsername,
                    addButton
            );

            ListingPageLayout.getChildren().addAll(
                    new Label("Add New Listing"),
                    addForm
            );

        }

        ListingPageLayout.getChildren().add(1, table);

        ListingPageScene = new Scene(ListingPageLayout, 1000, 700);
        stage.setTitle("Listings");
        stage.setScene(ListingPageScene);
        stage.show();
    }

    private ArrayList<PropertyListing> getPropertyListings(String role) {
        if (role.equals("Renter")) {
            //gets all listings
            return PropertyListing.getAllListings();
        } else {
            //gets his listings using get listing per role method (owner or manager)
            return PropertyListing.getListingsByRole(role, username);
        }
    }

    private TableColumn<PropertyListing, Void> getBookAppointmentColumn() {
        TableColumn<PropertyListing, Void> bookAppointmentCol = new TableColumn<>("Book Appointment");

        bookAppointmentCol.setCellFactory(param -> new TableCell<PropertyListing, Void>() {
            private final Button btn = new Button("Book Appointment");

            {
                btn.setOnAction(e -> {
                    PropertyListing listing = getTableView().getItems().get(getIndex());
                    String listingId = listing.getId(); // the UUID stored in DB
                    try {
                        // Open the booking page
                        BookAppointmentPage page = new BookAppointmentPage(stage, username, role, listingId);
                        page.initializeComponents();
                    } catch (Exception ex) {
                        Alerts.showAlert("Error", "Unable to open appointment page.", Alert.AlertType.ERROR);
                    }
                });
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
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
        bookAppointmentCol.setPrefWidth(120);
        return bookAppointmentCol;
    }

    private TableColumn<PropertyListing, Void> getModifyListingColumn() {
        TableColumn<PropertyListing, Void> modifyListingCol = new TableColumn<>("Modify Listing");

        modifyListingCol.setCellFactory(param -> new TableCell<PropertyListing, Void>() {
            private final Button btn = new Button("Modify Listing");

            {
                btn.setOnAction(e -> {
                    PropertyListing listing = getTableView().getItems().get(getIndex());
                    String listingId = listing.getId(); // the UUID stored in DB
                    try {
                        // Open the booking page
                        ModifyListingPage page = new ModifyListingPage(stage, username, role, listingId);
                        page.initializeComponents();
                    } catch (Exception ex) {
                        Alerts.showAlert("Error", "Unable to open Modify listing page.", Alert.AlertType.ERROR);
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
        modifyListingCol.setPrefWidth(120);
        return modifyListingCol;
    }

    private TableColumn<PropertyListing, Void> getRecordPaymentColumn() {
        TableColumn<PropertyListing, Void> recordPaymentCol = new TableColumn<>("Record Payment");

        recordPaymentCol.setCellFactory(param -> new TableCell<PropertyListing, Void>() {
            private final Button btn = new Button("Record Payment");
            {
                btn.setOnAction(e -> {
                    PropertyListing listing = getTableView().getItems().get(getIndex());
                    String listingId = listing.getId(); // the UUID stored in DB
                    try {
                        // Open the Payment Page
                        RecordPaymentPage page = new RecordPaymentPage(stage, username, role, listingId);
                        page.initializeComponents();
                    } catch (Exception ex) {
                        Alerts.showAlert("Error", "Unable to open Record Payment page.", Alert.AlertType.ERROR);
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
        recordPaymentCol.setPrefWidth(120);
        return recordPaymentCol;
    }


}
