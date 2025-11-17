public class User {
    private String userName;
    private String name;
    private String role;
    private String email;
    private String phoneNumber;

    public User(String userName, String name, String role, String email, String phoneNumber) {
        this.userName = userName;
        this.name = name;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    //getters and setters
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
