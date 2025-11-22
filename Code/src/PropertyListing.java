import javafx.beans.property.Property;
import javafx.scene.control.Alert;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class PropertyListing {
    private String id;
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
        this.id = UUID.randomUUID().toString();
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

    public PropertyListing(String id, String address, int bedrooms, int bathrooms, int size,
                           String title, String description, double price, Status status,
                           FurnitureType furnitureType, String ownerUsername,
                           String managerUsername) {

        this.id = id;  // don't generate UUID, use the id from DB only for method get all listing and get listings by role
        this.address = address;
        this.nOfBedrooms = bedrooms;
        this.nOfBathrooms = bathrooms;
        this.size = size;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.furnitureType = furnitureType;
        this.ownerUsername = ownerUsername;
        this.managerUsername = managerUsername;
    }

    public static void createPropertyListings(PropertyListing propertyListing) {
        //creates listing and adds it to Sql
        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO `property_listings` (`id`, `address`, `nOfBedrooms`, `nOfBathrooms`, `size`, `title`, `description`, `price`, `status`, `furnitureType`, `ownerUsername`, `managerUsername`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, propertyListing.id);
            statement.setString(2, propertyListing.address);
            statement.setInt(3, propertyListing.nOfBedrooms);
            statement.setInt(4, propertyListing.nOfBathrooms);
            statement.setInt(5, propertyListing.size);
            statement.setString(6, propertyListing.title);
            statement.setString(7, propertyListing.description);
            statement.setDouble(8, propertyListing.price);
            statement.setString(9, propertyListing.status.toString());
            statement.setString(10, propertyListing.furnitureType.toString());
            statement.setString(11, propertyListing.ownerUsername);
            statement.setString(12, propertyListing.managerUsername);
            int rs = statement.executeUpdate();
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
    }

    public static ArrayList<PropertyListing> getAllListings() {
        //gets available listings for renter to view
        ArrayList<PropertyListing> listings = new ArrayList<>();
        Connection con = DBUtils.establishConnection();
        String query = "SELECT * FROM property_listings WHERE status = ?;";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, Status.AVAILABLE.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {//this loop creates property listing objects for all listings then adds them to list and returns them
                PropertyListing listing = new PropertyListing(
                        rs.getString("id"),
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
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
        return listings;
    }

    public static ArrayList<PropertyListing> getListingsByRole(String role, String username) {
        ArrayList<PropertyListing> listings = new ArrayList<>();
        Connection con = DBUtils.establishConnection();
        String query = "";
        if (role.equals("Manager")) {
            query = "SELECT * FROM property_listings WHERE managerUsername = ?";
        } else if (role.equals("Owner")) {
            query = "SELECT * FROM property_listings WHERE ownerUsername = ?";
        } else {
            // If other roles (like Renter)
            return getAllListings();
        }
        try {
            PreparedStatement statement = con.prepareStatement(query);

            statement.setString(1, username); //puts the username either manager or owner
            ResultSet rs = statement.executeQuery();
            while (rs.next()) { //same as get all listings
                PropertyListing listing = new PropertyListing(
                        rs.getString("id"),
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
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
        return listings;
    }


    public static PropertyListing getListingById(String id) {
        Connection con = DBUtils.establishConnection();
        String query = "SELECT * FROM property_listings WHERE id = ?";
        PropertyListing listing = null;
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {// creates property listing objects and returns it
                listing = new PropertyListing(
                        rs.getString("id"),
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
                return listing;
            }
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
        return listing;
    }

    public static void modifyListingStatus(String listingId, Status status) {
        Connection con = DBUtils.establishConnection();
        String query = "UPDATE property_listings SET status = ? WHERE id = ?";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, status.toString());
            statement.setString(2, listingId);
            int rs = statement.executeUpdate();
            Alerts.showAlert("Success", "Status was successfuly updated.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
    }

    public static ArrayList<PropertyListing> getPayedPropertyListings(String role, String username) {
        ArrayList<PropertyListing> listings = new ArrayList<>();
        Connection con = DBUtils.establishConnection();
        String query = "";
        if (role.equals("Renter")) {
            query = "SELECT * FROM property_listings JOIN payment ON property_listings.id = payment.listingId WHERE payment.renterUsername = ?;";
        } else {
            // If other roles
            return listings;
        }
        try {
            PreparedStatement statement = con.prepareStatement(query);

            statement.setString(1, username); //puts the username of the renter

            ResultSet rs = statement.executeQuery();
            while (rs.next()) { //same as get all listings
                PropertyListing listing = new PropertyListing(
                        rs.getString("id"),
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
        } catch (Exception e) {
            //We will still print the exception error in the console to help us in the development
            e.printStackTrace();
            //But we will remove the above line, and display an alert to the user when the app is deployed
            Alerts.showAlert("Database Error", "Failed to connect to the database.", Alert.AlertType.ERROR);
        }
        return listings;

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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
