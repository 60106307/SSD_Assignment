import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ModifyListingPage {
    private Scene ModifyListingPageScene;
    private Stage stage;
    private String username;
    private String role;
    private String listingId;

    ModifyListingPage(Stage stage, String username, String role, String listingId) {
        this.stage = stage;
        this.username = username;
        this.role = role;
        this.listingId = listingId;
    }

    public void initializeComponents() {
        VBox ModifyListingPageLayout = new VBox(10);
        ModifyListingPageLayout.setPadding(new Insets(10));


        Button mainPage = new Button("Go back to Main Page");
        mainPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPage mainPage = new MainPage(stage, username, role);
                mainPage.initializeComponents();
            }
        });

        if (role.equals("Renter")) { //send the renter to the main page if he enters by mistake (RBAC)
            MainPage mainPageC = new MainPage(stage, username, role);
            mainPageC.initializeComponents();
        }

        Label pageTitle = new Label("Modify Listing");
        pageTitle.setFont(new Font("Arial", 20));

        ModifyListingPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );
        PropertyListing listing = PropertyListing.getListingById(listingId);
        Label title = new Label("Modify listing for: " + listing.getTitle());
        Label currentStatus = new Label("Current status: " + listing.getStatus());
        title.setFont(new Font("Arial", 16));

        //fields for modifying the listing's status
        Label statusLabel = new Label("Select Status:");
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll(Status.AVAILABLE.toString(), Status.RENTED.toString());

        Button modifyButton = new Button("Modify Listing");
        modifyButton.setOnAction(e -> {
            if (statusField.getValue() == null) {
                Alerts.showAlert("Invalid Input", "Input cannot be null", Alert.AlertType.ERROR);
                return;
            }
            Status status = Status.valueOf(statusField.getValue());
            if (!role.equals("Renter")) {
                PropertyListing.modifyListingStatus(listingId, status);
                //redirects to listing page
                ListingPage listingPage = new ListingPage(stage, username, role);
                listingPage.initializeComponents();
            }
        });
        ModifyListingPageLayout.getChildren().addAll(
                title, currentStatus, statusLabel, statusField, modifyButton
        );


        ModifyListingPageScene = new Scene(ModifyListingPageLayout, 900, 700);
        stage.setTitle("Modify Listing");
        stage.setScene(ModifyListingPageScene);
        stage.show();
    }
}
