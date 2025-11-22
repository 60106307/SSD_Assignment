import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Payment {
    private String id;
    private String listingId;
    private String renterUsername;
    private String managerUsername;
    private String ownerUsername;
    private double amount;
    private LocalDate paymentDate;
    private YearMonth paymentMonth;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;

    public Payment(String listingId, String renterUsername, String managerUsername, String ownerUsername, double amount, LocalDate paymentDate, YearMonth paymentMonth, PaymentMethod paymentMethod, LocalDateTime createdAt) {
        this.id = UUID.randomUUID().toString();
        this.listingId = listingId;
        this.renterUsername = renterUsername;
        this.managerUsername = managerUsername;
        this.ownerUsername = ownerUsername;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMonth = paymentMonth;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

    public Payment(String id, String listingId, String renterUsername, String managerUsername, String ownerUsername, double amount, LocalDate paymentDate, YearMonth paymentMonth, PaymentMethod paymentMethod, LocalDateTime createdAt) {
        this.id = id;
        this.listingId = listingId;
        this.renterUsername = renterUsername;
        this.managerUsername = managerUsername;
        this.ownerUsername = ownerUsername;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMonth = paymentMonth;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

    public static void createPayment(Payment payment) {
        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO payment (id, listingId, renterUsername, managerUsername, ownerUsername, amount, paymentDate, paymentMonth, paymentMethod, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, payment.getId());
            statement.setString(2, payment.getListingId());
            statement.setString(3, payment.getRenterUsername());
            statement.setString(4, payment.getManagerUsername());
            statement.setString(5, payment.getOwnerUsername());
            statement.setDouble(6, payment.getAmount());
            statement.setString(7, payment.getPaymentDate().toString());
            // store YearMonth as "YYYY-MM" string
            statement.setString(8, payment.getPaymentMonth().format(DateTimeFormatter.ofPattern("yyyy-MM")));
            statement.setString(9, payment.getPaymentMethod().toString());
            statement.setString(10, payment.getCreatedAt().toString());
            int rs = statement.executeUpdate();
            Alerts.showAlert("Success", "Payment recorded.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }

    }


    //getters
    public String getId() {
        return id;
    }

    public String getListingId() {
        return listingId;
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

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public YearMonth getPaymentMonth() {
        return paymentMonth;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
