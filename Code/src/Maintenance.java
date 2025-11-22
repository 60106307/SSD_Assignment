import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Maintenance {
    private String id;
    private String listingId;
    private String renterUsername;
    private String ownerUsername;
    private String title;
    private String description;
    private MaintenanceStatus status;
    private MaintenancePriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private LocalDateTime resolvedAt;

    public Maintenance(String listingId, String renterUsername, String ownerUsername, String title, String description, MaintenanceStatus status, MaintenancePriority priority, LocalDateTime createdAt, LocalDateTime lastUpdated, LocalDateTime resolvedAt) {
        this.id = UUID.randomUUID().toString();
        this.listingId = listingId;
        this.renterUsername = renterUsername;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.resolvedAt = resolvedAt;
    }

    public Maintenance(String id, String listingId, String renterUsername, String ownerUsername, String title, String description, MaintenanceStatus status, MaintenancePriority priority, LocalDateTime createdAt, LocalDateTime lastUpdated, LocalDateTime resolvedAt) {
        this.id = id;
        this.listingId = listingId;
        this.renterUsername = renterUsername;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.resolvedAt = resolvedAt;
    }

    public static void createMaintenanceRequest(Maintenance maintenance) {
        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO maintenance (id, listingId, renterUsername, ownerUsername, title, description, status, priority, createdAt, lastUpdated, resolvedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, maintenance.id);
            statement.setString(2, maintenance.listingId);
            statement.setString(3, maintenance.renterUsername);
            statement.setString(4, maintenance.ownerUsername);
            statement.setString(5, maintenance.title);
            statement.setString(6, maintenance.description);
            statement.setString(7, maintenance.status.toString());
            statement.setString(8, maintenance.priority.toString());
            statement.setObject(9, Timestamp.valueOf(maintenance.createdAt));
            statement.setObject(10, Timestamp.valueOf(maintenance.lastUpdated));
            statement.setObject(11, null);
            int rs = statement.executeUpdate();

        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
    }


}
