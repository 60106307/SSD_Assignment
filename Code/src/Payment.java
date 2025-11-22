import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

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


}
