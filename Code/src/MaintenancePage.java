import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MaintenancePage {

    private Scene MaintenancePageScene;
    private Stage stage;
    private String username;
    private String role;


    MaintenancePage(Stage stage, String username, String role) {
        this.stage = stage;
        this.username = username;
        this.role = role;
    }

    public void initializeComponents() {
        VBox MaintenancePageLayout = new VBox(10);
        MaintenancePageLayout.setPadding(new Insets(10));

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

        MaintenancePageLayout.getChildren().addAll(
                pageTitle, mainPage
        );




        MaintenancePageScene = new Scene(MaintenancePageLayout, 900, 700);
        stage.setTitle("Maintenance");
        stage.setScene(MaintenancePageScene);
        stage.show();


    }
