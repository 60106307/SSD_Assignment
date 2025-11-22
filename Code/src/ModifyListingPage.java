import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        Label pageTitle = new Label("Modify Listing");
        pageTitle.setFont(new Font("Arial", 20));

        ModifyListingPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );

        ModifyListingPageScene = new Scene(ModifyListingPageLayout, 900, 700);
        stage.setTitle("Modify Listing");
        stage.setScene(ModifyListingPageScene);
        stage.show();
    }
}
