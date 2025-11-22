import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


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

        MaintenanceRequestPageScene = new Scene(MaintenanceRequestPageLayout, 900, 700);
        stage.setTitle("Manage Booking");
        stage.setScene(MaintenanceRequestPageScene);
        stage.show();
    }
}
