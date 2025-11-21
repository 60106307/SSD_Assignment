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

        Label pageTitle = new Label("Book Appointment");
        pageTitle.setFont(new Font("Arial", 20));
        AppointmentPageLayout.getChildren().addAll(
                pageTitle, mainPage, new Label(listingId)
        );




        BookAppointmentPageScene = new Scene(AppointmentPageLayout, 600, 600);
        stage.setTitle("Book Appointment");
        stage.setScene(BookAppointmentPageScene);
        stage.show();
    }


}
