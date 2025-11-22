import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainPage {
    private Scene MainPageScene;
    private Stage stage;
    private String username;
    private String role;

    public MainPage(Stage stage, String username, String role) {
        this.stage = stage;
        this.username = username;
        this.role = role;
    }

    public void initializeComponents() {
        VBox MainPageLayout = new VBox(10);
        MainPageLayout.setPadding(new Insets(10));
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

        //change password button
        Button changePassword = new Button("Change Password");
        changePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //go to change Password
                UserChangePassword changePassword1 = new UserChangePassword(stage, username);
                changePassword1.initializeComponents();
            }
        });


        //listing button
        Button Listings = new Button("View Listings");
        Listings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //go to listings page
                ListingPage listingPage = new ListingPage(stage, username, role);
                listingPage.initializeComponents();
            }
        });

        //appointment button
        Button appointment = new Button("View Appointment");
        appointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //go to appointment page
                AppointmentPage appointmentPage = new AppointmentPage(stage, username, role);
                appointmentPage.initializeComponents();
            }
        });
        Button payment = new Button("View Payments");
        payment.setVisible(false);
        if (!role.equals("Renter")) {
            //Payments button
            payment.setVisible(true);
            payment.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //go to Payment page
                    PaymentPage paymentPage = new PaymentPage(stage, username, role);
                    paymentPage.initializeComponents();
                }
            });
        }

        Button myListings = new Button("View My Listings");
        myListings.setVisible(false);
        if (role.equals("Renter")) {
            //Payments button
            myListings.setVisible(true);
            myListings.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //go to My Listings
                    MyListingsPage myListingsPage = new MyListingsPage(stage, username, role);
                    myListingsPage.initializeComponents();
                }
            });
        }


        Button maintenance = new Button("View Maintenance Requests");
        maintenance.setVisible(false);
        if (role.equals("Renter") || role.equals("Owner")) {
            //Payments button
            maintenance.setVisible(true);
            maintenance.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //go to My Listings
                    MaintenancePage maintenancePage = new MaintenancePage(stage, username, role);
                    maintenancePage.initializeComponents();
                }
            });
        }


        MainPageLayout.getChildren().addAll(
                new Label("Welcome " + username + "!"), logout, changePassword,
                Listings,
                appointment,
                payment,
                myListings,
                maintenance

        );

        MainPageScene = new Scene(MainPageLayout, 500, 500);
        stage.setTitle("Main Page");
        stage.setScene(MainPageScene);
        stage.show();
    }


}
