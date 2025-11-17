public class Manager extends User {
    private String companyName;

    public Manager(String userName, String name, String role, String email, String phoneNumber,  String companyName) {
        super(userName, name, role, email, phoneNumber);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
