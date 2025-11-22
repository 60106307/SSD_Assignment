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

public class AppointmentPage {
    private Scene AppointmentPageScene;
    private Stage stage;
    private String username;
    private String role;

    public AppointmentPage(Stage stage, String username, String role) {
        this.stage = stage;
        this.username = username;
        this.role = role;
    }

    public void initializeComponents() {
        VBox AppointmentPageLayout = new VBox(10);
        AppointmentPageLayout.setPadding(new Insets(10));
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

        Label pageTitle = new Label("Appointments");
        pageTitle.setFont(new Font("Arial", 20));
        AppointmentPageLayout.getChildren().addAll(pageTitle, mainPage);

        // get Appointments based on role
        //HERE
        ArrayList<Appointment> appointments = getAppointments(role, username);

        // Create table
        TableView<Appointment> table = new TableView<>();

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(900);

        TableColumn<Appointment, String> renterCol = new TableColumn<>("Renter");
        TableColumn<Appointment, String> managerCol = new TableColumn<>("Manager");
        TableColumn<Appointment, String> ownerCol = new TableColumn<>("Owner");
        TableColumn<Appointment, String> listingCol = new TableColumn<>("Listing");
        TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        TableColumn<Appointment, Void> manageBooking = new TableColumn<>("Manage Booking");

        table.getColumns().addAll(renterCol, managerCol, ownerCol, listingCol, dateCol, timeCol, statusCol, manageBooking);

        // Bind columns to Appointment fields
        renterCol.setCellValueFactory(new PropertyValueFactory<>("renterUsername"));
        managerCol.setCellValueFactory(new PropertyValueFactory<>("managerUsername"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        listingCol.setCellValueFactory(new PropertyValueFactory<>("listingId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        // manage booking
        manageBooking.setCellFactory(param -> new TableCell<Appointment, Void>() {
            private final Button btn = new Button("Manage Booking");
            {
                btn.setOnAction(e -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    String appointmentId = appointment.getId(); // the UUID stored in DB
                    try {
                        // Open the booking page
                        ManageBookingPage page = new ManageBookingPage(stage, username, role, appointmentId);
                        page.initializeComponents();
                    } catch (Exception ex) {
                        Alerts.showAlert("Error", "Unable to open manage booking page.", Alert.AlertType.ERROR);
                    }
                });
//                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
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
        manageBooking.setPrefWidth(120);
        // Convert ArrayList to ObservableList
        ObservableList<Appointment> data = FXCollections.observableArrayList(appointments);

        // Add data to the table
        table.setItems(data);


        AppointmentPageLayout.getChildren().addAll(table);


        AppointmentPageScene = new Scene(AppointmentPageLayout, 900, 700);
        stage.setTitle("Appointments");
        stage.setScene(AppointmentPageScene);
        stage.show();
    }


    private ArrayList<Appointment> getAppointments(String role, String username) {
        return Appointment.getAppointmentsByRole(role, username);
    }
}
