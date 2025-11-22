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

public class ModifyAppointmentPage {
    private Scene ModifyAppointmentPageScene;
    private Stage stage;
    private String username;
    private String role;
    private String appointmentId;

    public ModifyAppointmentPage(Stage stage, String username, String role, String appointmentId) {
        this.stage = stage;
        this.username = username;
        this.role = role;
        this.appointmentId = appointmentId;
    }

    public void initializeComponents() {
        VBox ModifyAppointmentPageLayout = new VBox(10);
        ModifyAppointmentPageLayout.setPadding(new Insets(10));

        Button mainPage = new Button("Go back to Main Page");
        mainPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPage mainPage = new MainPage(stage, username, role);
                mainPage.initializeComponents();
            }
        });

        Label pageTitle = new Label("Modify Appointment");
        pageTitle.setFont(new Font("Arial", 20));
        ModifyAppointmentPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );

        Appointment appointment = Appointment.getAppointmentById(appointmentId);
        Label title = new Label("Modify Appointment for: " + appointment.getId());
        Label currentStatus = new Label("Current status: " + appointment.getStatus());
        title.setFont(new Font("Arial", 16));
        //fields for modifying the listing's status
        Label statusLabel = new Label("Select Status:");
        ComboBox<String> statusField = new ComboBox<>();
        if (role.equals("Renter")) {
            statusField.getItems().addAll(AppointmentStatus.CANCELLED.toString());
        } else if (role.equals("Manager")) {
            statusField.getItems().addAll(AppointmentStatus.CANCELLED.toString(), AppointmentStatus.APPROVED.toString(), AppointmentStatus.REJECTED.toString(), AppointmentStatus.COMPLETED.toString());
        }

        Button modifyButton = new Button("Modify Appointment");
        modifyButton.setOnAction(e -> {
            if (statusField.getValue() == null) {
                Alerts.showAlert("Invalid Input", "Input cannot be null", Alert.AlertType.ERROR);
                return;
            }

            AppointmentStatus status = AppointmentStatus.valueOf(statusField.getValue());
            if (!role.equals("Renter")) {
                Appointment.modifyAppointmentStatus(appointmentId, status);
            } else if (role.equals("Renter")) {
                //renters can only cancel the appointment
                if (!statusField.getValue().equals(AppointmentStatus.CANCELLED.toString())) {
                    Alerts.showAlert("Invalid Input", "Invalid Status", Alert.AlertType.ERROR);
                    return;
                } else {
                    Appointment.modifyAppointmentStatus(appointmentId, status);
                }
            }
            //redirects to Appointment page
            AppointmentPage appointmentPage = new AppointmentPage(stage, username, role);
            appointmentPage.initializeComponents();
        });
        ModifyAppointmentPageLayout.getChildren().addAll(
                title, currentStatus, statusLabel, statusField, modifyButton
        );
        ModifyAppointmentPageScene = new Scene(ModifyAppointmentPageLayout, 900, 700);
        stage.setTitle("Modify Listing");
        stage.setScene(ModifyAppointmentPageScene);
        stage.show();


    }
}
