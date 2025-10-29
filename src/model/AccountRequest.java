package model;

// represents a client request for a new account
public class AccountRequest {

    private int id;
    private int clientId;
    private String accountType;
    private String status;

    public AccountRequest() {}

    public AccountRequest(int id, int clientId, String accountType, String status) {
        this.id = id;
        this.clientId = clientId;
        this.accountType = accountType;
        this.status = status;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountRequest{id=" + id + ", clientId=" + clientId +
                ", type='" + accountType + "', status='" + status + "'}";
    }
}
