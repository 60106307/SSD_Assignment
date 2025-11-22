import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

public class Appointment {

    private  String id;
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

    //    public static void createAppointment(Appointment appointment){
//    public static ArrayList<PropertyListing> getAllAppointments(){}
//    public static ArrayList<PropertyListing> getAppointmentsByRole(String role, String  username){}

}