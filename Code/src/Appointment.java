import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

public class Appointment {

    private String id;
    private String renterUsername;
    private String managerUsername;
    private String ownerUsername;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDateTime createdAt;
    private AppointmentStatus status;
    private String listingId;
    private LocalDateTime lastUpdated;

    public Appointment(String renterUsername, String managerUsername, String ownerUsername, LocalDate appointmentDate, LocalTime appointmentTime, LocalDateTime createdAt, AppointmentStatus status, String listingId, LocalDateTime lastUpdated) {
        this.id = UUID.randomUUID().toString();
        this.renterUsername = renterUsername;
        this.managerUsername = managerUsername;
        this.ownerUsername = ownerUsername;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.createdAt = createdAt;
        this.status = status;
        this.listingId = listingId;
        this.lastUpdated = lastUpdated;
    }

    public Appointment(String id, String renterUsername, String managerUsername, String ownerUsername, LocalDate appointmentDate, LocalTime appointmentTime, LocalDateTime createdAt, AppointmentStatus status, String listingId, LocalDateTime lastUpdated) {
        this.id = id;
        this.renterUsername = renterUsername;
        this.managerUsername = managerUsername;
        this.ownerUsername = ownerUsername;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.createdAt = createdAt;
        this.status = status;
        this.listingId = listingId;
        this.lastUpdated = lastUpdated;
    }

    public static void createAppointment(Appointment appointment) {
        //creates Appointment and adds it to Sql
        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO appointment (id, renterUsername, managerUsername, ownerUsername, listingId, appointmentDate, appointmentTime, createdAt, lastUpdated, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, appointment.id);
            statement.setString(2, appointment.renterUsername);
            statement.setString(3, appointment.managerUsername);
            statement.setString(4, appointment.ownerUsername);
            statement.setString(5, appointment.listingId);
            statement.setString(6, appointment.appointmentDate.toString());
            statement.setString(7, appointment.appointmentTime.toString());
            statement.setString(8, appointment.createdAt.toString());
            statement.setString(9, appointment.lastUpdated.toString());
            statement.setString(10, appointment.status.toString());
            int rs = statement.executeUpdate();
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
    }


    public static ArrayList<Appointment> getAppointmentsByRole(String role, String username) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        Connection con = DBUtils.establishConnection();
        String query = "";
        if (role.equals("Manager")) {
            query = "SELECT * FROM appointment WHERE managerUsername = ?";
        } else if (role.equals("Owner")) {
            query = "SELECT * FROM appointment WHERE ownerUsername = ?";
        } else if (role.equals("Renter")){
            query = "SELECT * FROM appointment WHERE renterUsername = ?";
        }
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username); //puts the username either manager or owner
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {//same as get all appointments
                Appointment appointment = new Appointment(
                        rs.getString("id"),
                        rs.getString("renterUsername"),
                        rs.getString("managerUsername"),
                        rs.getString("ownerUsername"),
                        rs.getDate("appointmentDate").toLocalDate(),
                        rs.getTime("appointmentTime").toLocalTime(),
                        rs.getTimestamp("createdAt").toLocalDateTime(),
                        AppointmentStatus.valueOf(rs.getString("status")),
                        rs.getString("listingId"),
                        rs.getTimestamp("lastUpdated").toLocalDateTime()
                );
                appointments.add(appointment);
            }
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
        return appointments;
    }


    public String getId() {
        return id;
    }

    public String getRenterUsername() {
        return renterUsername;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getListingId() {
        return listingId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}