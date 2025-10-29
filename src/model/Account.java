package model;

// basic account model
public class Account {

    protected int id;
    protected int clientId;
    protected String iban;
    protected double balance;
    protected String currency;
    protected String type;

    public Account() {}

    public Account(int id, int clientId, String iban, double balance, String currency, String type) {
        this.id = id;
        this.clientId = clientId;
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", iban='" + iban + "', balance=" + balance + " " + currency + "}";
    }
}
