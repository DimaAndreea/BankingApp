package model;

// admin user for managing system-level operations
public class Admin extends User {

    public Admin() {}

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String toString() {
        return "Admin{id=" + id + ", username='" + username + "'}";
    }
}
