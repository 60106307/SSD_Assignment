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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class BookAppointmentPage {
    private Scene BookAppointmentPageScene;
    private Stage stage;
    private String username;
    private String role;
    private String listingId;

    public BookAppointmentPage(Stage stage, String username, String role, String listingId) {
        this.stage = stage;
        this.username = username;
        this.role = role;
        this.listingId = listingId;
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

        Label pageTitle = new Label("Book Viewing Appointment");
        pageTitle.setFont(new Font("Arial", 20));
        AppointmentPageLayout.getChildren().addAll(pageTitle, mainPage);

        PropertyListing listing = PropertyListing.getListingById(listingId);
        Label title = new Label("New Appointment for: " + listing.getTitle());
        title.setFont(new Font("Arial", 16));

        //fields for adding new appointment
        Label dateLabel = new Label("Select Date:");
        DatePicker viewingDate = new DatePicker();
        viewingDate.setValue(LocalDate.now()); // default to today


        // appointment time to stay in the same time format
        Label timeLabel = new Label("Select Time:");
        ComboBox<String> viewingTime = new ComboBox<>();
        viewingTime.getItems().addAll("8:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        viewingTime.setValue("09:00"); // default

        Button bookButton = new Button("Book Appointment");

        bookButton.setOnAction(event -> {
            LocalDate date = viewingDate.getValue();
            LocalTime time = LocalTime.parse(viewingTime.getValue());

            //checking for null values
            if (date == null || time == null) {
                Alerts.showAlert("Input Error", "Please select a date and time.", Alert.AlertType.ERROR);
                return;
            }

            //checking for trying to book appointment in the past
            LocalDateTime selectedDateTime = LocalDateTime.of(date, time);
            if (selectedDateTime.isBefore(LocalDateTime.now())) {
                Alerts.showAlert("Input Error", "Please select a date in the future.", Alert.AlertType.ERROR);
                return;
            }

            //checking for format
            if (!ValidationUtils.validateDate(date.toString()) || !ValidationUtils.validateTime(time.toString()+":00")) {
                Alerts.showAlert("Input Error", "Please select a valid date and time.", Alert.AlertType.ERROR);
                return;
            }

            //checking for scripts
            if (!ValidationUtils.validateNoScript(date.toString()) && !ValidationUtils.validateNoScript(time.toString())) {
                Alerts.showAlert("Invalid Input", "Input contains invalid characters!", Alert.AlertType.ERROR);
                return;
            }

            if(role.equals("Renter")){ //only renters can create appointments
            Appointment.createAppointment(new Appointment(username, listing.getManagerUsername(), listing.getOwnerUsername(), date, time, LocalDateTime.now(), AppointmentStatus.PENDING, listingId, LocalDateTime.now()));

            Alerts.showAlert("Success", "Appointment booked successfully!", Alert.AlertType.INFORMATION);

            //go to appointment page
            AppointmentPage appointmentPage = new AppointmentPage(stage, username, role);
            appointmentPage.initializeComponents();
            }

        });

        // Layout for form
        HBox addForm = new HBox(10);
        addForm.getChildren().addAll(dateLabel, viewingDate, timeLabel, viewingTime, bookButton);

        AppointmentPageLayout.getChildren().addAll(title, addForm);


        BookAppointmentPageScene = new Scene(AppointmentPageLayout, 600, 600);
        stage.setTitle("Book Appointment");
        stage.setScene(BookAppointmentPageScene);
        stage.show();
    }


}
