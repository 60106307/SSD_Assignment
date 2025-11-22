import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

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
        if (role.equals("Manager")) {
            Alerts.showAlert("Access Denied", "Only Owners and renters can view maintenance requests.", Alert.AlertType.ERROR);
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

        Label pageTitle = new Label("Maintenance Requests");
        pageTitle.setFont(new Font("Arial", 20));

        MaintenancePageLayout.getChildren().addAll(
                pageTitle, mainPage, logout
        );

        ArrayList<Maintenance> requests = getMaintenanceRequests();

        // Create TableView
        TableView<Maintenance> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(1000);

        TableColumn<Maintenance, String> titleCol = new TableColumn<>("Title");
        TableColumn<Maintenance, String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Maintenance, String> statusCol = new TableColumn<>("Status");
        TableColumn<Maintenance, String> priorityCol = new TableColumn<>("Priority");
        TableColumn<Maintenance, String> renterCol = new TableColumn<>("Renter");
        TableColumn<Maintenance, String> ownerCol = new TableColumn<>("Owner");

        table.getColumns().addAll(titleCol, descriptionCol, statusCol, priorityCol, renterCol, ownerCol);

        // Bind columns to Maintenance fields
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        renterCol.setCellValueFactory(new PropertyValueFactory<>("renterUsername"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));

        // Add Modify button if user is Owner
        if (role.equals("Owner")) {
            table.getColumns().add(getModifyRequestColumn());
        }

        ObservableList<Maintenance> data = FXCollections.observableArrayList(requests);
        table.setItems(data);

        MaintenancePageLayout.getChildren().addAll(
                table
        );


        MaintenancePageScene = new Scene(MaintenancePageLayout, 900, 700);
        stage.setTitle("Maintenance");
        stage.setScene(MaintenancePageScene);
        stage.show();


    }

    private ArrayList<Maintenance> getMaintenanceRequests() {
        return Maintenance.getMaintenanceByRole(role, username);
    }

    private TableColumn<Maintenance, Void> getModifyRequestColumn() {
        TableColumn<Maintenance, Void> modifyCol = new TableColumn<>("Modify Request");
        modifyCol.setCellFactory(param -> new TableCell<Maintenance, Void>() {
            private final Button btn = new Button("Modify");

            {
                btn.setOnAction(e -> {
                    Maintenance maintenance = getTableView().getItems().get(getIndex());
                    String maintenanceId = maintenance.getId();
                    try {
                        ModifyMaintenancePage page = new ModifyMaintenancePage(stage, username, role, maintenanceId);
                        page.initializeComponents();
                    } catch (Exception ex) {
                        Alerts.showAlert("Error", "Unable to open Modify Request page.", Alert.AlertType.ERROR);
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

        modifyCol.setPrefWidth(120);
        return modifyCol;
    }

}
