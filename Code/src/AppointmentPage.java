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
        AppointmentPageLayout.getChildren().addAll(
                pageTitle, mainPage
        );

        // get Appointments based on role
        //HERE
//        ArrayList<Appointment> appointments = getAppointments(role);

        // Create table
        TableView<Appointment> table = new TableView<>();

        //table.setEditable(true);
//        TableColumn title = new TableColumn("Title");
//        TableColumn description = new TableColumn("Description");
//        TableColumn address = new TableColumn("Address");
//        TableColumn bedrooms = new TableColumn("Bedrooms");
//        TableColumn bathrooms = new TableColumn("Bathrooms");
//        TableColumn size = new TableColumn("Size");
//        TableColumn price = new TableColumn("Price");
//        TableColumn status = new TableColumn("Status");
//        TableColumn furnitureType = new TableColumn("Furniture type");

//        table.getColumns().addAll(
//                title, description, address, bedrooms, bathrooms, size, price, status, furnitureType
//        );

        // Bind columns to Appointment fields
//        title.setCellValueFactory(new PropertyValueFactory<>("title"));
//        description.setCellValueFactory(new PropertyValueFactory<>("description"));
//        address.setCellValueFactory(new PropertyValueFactory<>("address"));
//        bedrooms.setCellValueFactory(new PropertyValueFactory<>("nOfBedrooms"));
//        bathrooms.setCellValueFactory(new PropertyValueFactory<>("nOfBathrooms"));
//        size.setCellValueFactory(new PropertyValueFactory<>("size"));
//        price.setCellValueFactory(new PropertyValueFactory<>("price"));
//        status.setCellValueFactory(new PropertyValueFactory<>("status"));
//        furnitureType.setCellValueFactory(new PropertyValueFactory<>("furnitureType"));

        // Convert ArrayList to ObservableList
        //HERE
//        ObservableList<Appointment> data = FXCollections.observableArrayList(appointments);

        // Add data to the table
        //HERE
//        table.setItems(data);


        AppointmentPageLayout.getChildren().addAll(table);


        AppointmentPageScene = new Scene(AppointmentPageLayout, 600, 600);
        stage.setTitle("Appointments");
        stage.setScene(AppointmentPageScene);
        stage.show();
    }

    //HERE

//    private ArrayList<Appointment> getAppointment(String role) {
//        if (role.equals("Renter")) {
//            //gets all Appointments
//            return Appointment.getAllAppointments();
//        } else {
//            //gets his Appointments using get Appointment per role method (owner or manager)
//            return Appointment.getAppointmentsByRole(role, username);
//        }
//    }


}
