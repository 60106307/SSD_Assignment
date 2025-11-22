import com.sun.tools.javac.Main;
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


public class ModifyMaintenancePage {
    private Scene ModifyMaintenancePageScene;
    private Stage stage;
    private String username;
    private String role;
    private String maintenanceId;

    ModifyMaintenancePage(Stage stage, String username, String role, String maintenanceId) {
        this.stage = stage;
        this.username = username;
        this.role = role;
        this.maintenanceId = maintenanceId;
    }
    public void initializeComponents() {
        VBox ModifyMaintenancePageLayout = new VBox(10);
        ModifyMaintenancePageLayout.setPadding(new Insets(10));


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

        ModifyMaintenancePageLayout.getChildren().addAll(
                pageTitle, mainPage
        );
        Maintenance maintenance = Maintenance.getMaintenanceById(maintenanceId);
        Label title = new Label("Modify Maintenance for: " + maintenance.getTitle());
        Label currentStatus = new Label("Current status: " + maintenance.getStatus());
        title.setFont(new Font("Arial", 16));

        //fields for modifying the listing's status
        Label statusLabel = new Label("Select Status:");
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll(MaintenanceStatus.IN_PROGRESS.toString(), MaintenanceStatus.COMPLETED.toString());

        Button modifyButton = new Button("Modify Maintenance");
        modifyButton.setOnAction(e -> {
            if (statusField.getValue() == null) {
                Alerts.showAlert("Invalid Input", "Input cannot be null", Alert.AlertType.ERROR);
                return;
            }
            MaintenanceStatus maintenanceStatus = MaintenanceStatus.valueOf(statusField.getValue());
            if (role.equals("Owner")) {
                Maintenance.modifyMaintenanceStatus(maintenanceId, maintenanceStatus);
                //redirects to Maintenance page
                MaintenancePage maintenancePage = new MaintenancePage(stage, username, role);
                maintenancePage.initializeComponents();
            }
        });
        ModifyMaintenancePageLayout.getChildren().addAll(
                title, currentStatus, statusLabel, statusField, modifyButton
        );


        ModifyMaintenancePageScene = new Scene(ModifyMaintenancePageLayout, 900, 700);
        stage.setTitle("Modify Listing");
        stage.setScene(ModifyMaintenancePageScene);
        stage.show();
    }


}
