import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
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

        ListingPageLayout.getChildren().addAll(
                new Label("Listings"), mainPage
        );

        // get listings based on role
        ArrayList<PropertyListing> listings = getPropertyListings(role);

        // Create table
        TableView<PropertyListing> table = new TableView<>();

        //table.setEditable(true);
        TableColumn title = new TableColumn("Title");
        TableColumn description = new TableColumn("Description");
        TableColumn address = new TableColumn("Address");
        TableColumn bedrooms = new TableColumn("Bedrooms");
        TableColumn bathrooms = new TableColumn("Bathrooms");
        TableColumn size = new TableColumn("Size");
        TableColumn price = new TableColumn("Price");
        TableColumn status = new TableColumn("Status");
        TableColumn furnitureType = new TableColumn("Furniture type");

        table.getColumns().addAll(
                title, description, address, bedrooms, bathrooms, size, price, status, furnitureType
        );

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


        //listings display
//        for (PropertyListing listing : listings) {

//            Label listingLabel = new Label(
//                    "Title: " + listing.getTitle() +
//                            "\nAddress: " + listing.getAddress() +
//                            "\nBedrooms: " + listing.getnOfBedrooms() +
//                            "\nBathrooms: " + listing.getnOfBathrooms() +
//                            "\nSize: " + listing.getSize() +
//                            "\nPrice: " + listing.getPrice() +
//                            "\nStatus: " + listing.getStatus() +
//                            "\nFurniture: " + listing.getFurnitureType() +
//                            "\nOwner: " + listing.getOwnerUsername() +
//                            "\nManager: " + listing.getManagerUsername()
//
//            );
//            listingLabel.setStyle("-fx-border-color: gray; -fx-padding: 5;"); // simple styling
//            ListingPageLayout.getChildren().add(listingLabel); //adds it to the page
//        }

        ListingPageLayout.getChildren().addAll(table);


        ListingPageScene = new Scene(ListingPageLayout, 600, 600);
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


}
