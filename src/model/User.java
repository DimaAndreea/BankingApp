package model;

// base class for admin and client users
public abstract class User {

    protected int id;
    protected String username;
    protected String password;

    public User() {}

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // prints basic user info
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}
