import javafx.scene.control.Alert;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PropertyListing {
    private String address;
    private int nOfBedrooms;
    private int nOfBathrooms;
    private int size;
    private String title;
    private String description;
    private double price;
    private Status status;
    private FurnitureType furnitureType;
    private String ownerUsername;
    private String managerUsername;

    public PropertyListing(String address, int nOfBedrooms, int nOfBathrooms, int size, String title, String description, double price, Status status, FurnitureType furnitureType, String ownerUsername, String managerUsername) {
        this.address = address;
        this.nOfBedrooms = nOfBedrooms;
        this.nOfBathrooms = nOfBathrooms;
        this.size = size;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.furnitureType = furnitureType;
        this.ownerUsername = ownerUsername;
        this.managerUsername = managerUsername;
    }

    public static void createPropertyListings(PropertyListing propertyListing){
        //creates listing and adds it to Sql
        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO `property_listings` (`address`, `nOfBedrooms`, `nOfBathrooms`, `size`, `title`, `description`, `price`, `status`, `furnitureType`, `ownerUsername`, `managerUsername`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, propertyListing.address);
            statement.setInt(2, propertyListing.nOfBedrooms);
            statement.setInt(3, propertyListing.nOfBathrooms);
            statement.setInt(4, propertyListing.size);
            statement.setString(5, propertyListing.title);
            statement.setString(6, propertyListing.description);
            statement.setDouble(7, propertyListing.price);
            statement.setString(8, propertyListing.status.toString());
            statement.setString(9, propertyListing.furnitureType.toString());
            statement.setString(10, propertyListing.ownerUsername);
            statement.setString(11, propertyListing.managerUsername);
            int rs = statement.executeUpdate();

        }catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            showAlert("Database Error", "Failed to connect to the database.");
        }

    }

    public static ArrayList<PropertyListing> getAllListings(){
        ArrayList<PropertyListing> listings = new ArrayList<>();
        Connection con = DBUtils.establishConnection();
        String query = "SELECT * FROM property_listings";
        try{
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){//this loop creates property listing objects for all listings then adds them to list and returns them
                PropertyListing listing = new PropertyListing(
                        rs.getString("address"),
                        rs.getInt("nOfBedrooms"),
                        rs.getInt("nOfBathrooms"),
                        rs.getInt("size"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        Status.valueOf(rs.getString("status")),
                        FurnitureType.valueOf(rs.getString("furnitureType")),
                        rs.getString("ownerUsername"),
                        rs.getString("managerUsername")
                );
                listings.add(listing);
            }
        }catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            showAlert("Database Error", "Failed to connect to the database.");
        }
        return listings;
    }

    public static ArrayList<PropertyListing> getListingsByRole(String role, String  username){
        ArrayList<PropertyListing> listings = new ArrayList<>();
        Connection con = DBUtils.establishConnection();
        String query = "";
        if (role.equals("Manager")) {
            query = "SELECT * FROM property_listings WHERE managerUsername = ?";
        } else if (role.equals("Owner")) {
            query = "SELECT * FROM property_listings WHERE ownerUsername = ?";
        } else {
            // If other roles (like Renter), just return empty list
            return getAllListings();
        }
        try {
            PreparedStatement statement = con.prepareStatement(query);

            statement.setString(1, username); //puts the username either manager or owner
            ResultSet rs = statement.executeQuery();
            while (rs.next()) { //same as get all listings
                PropertyListing listing = new PropertyListing(
                        rs.getString("address"),
                        rs.getInt("nOfBedrooms"),
                        rs.getInt("nOfBathrooms"),
                        rs.getInt("size"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        Status.valueOf(rs.getString("status")),
                        FurnitureType.valueOf(rs.getString("furnitureType")),
                        rs.getString("ownerUsername"),
                        rs.getString("managerUsername")
                );
                listings.add(listing);
            }
        }catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            showAlert("Database Error", "Failed to connect to the database.");
        }
        return listings;
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public String getAddress() {
        return address;
    }

    public int getNOfBedrooms() {
        return nOfBedrooms;
    }

    public int getNOfBathrooms() {
        return nOfBathrooms;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Status getStatus() {
        return status;
    }

    public FurnitureType getFurnitureType() {
        return furnitureType;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getManagerUsername() {
        return managerUsername;
    }
}
