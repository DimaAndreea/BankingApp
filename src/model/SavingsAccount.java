package model;

// savings account with interest rate
public class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount() {}

    public SavingsAccount(int id, int clientId, String iban, double balance, String currency, double interestRate) {
        super(id, clientId, iban, balance, currency, "savings");
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "SavingsAccount{id=" + id + ", iban='" + iban + "', balance=" + balance +
                " " + currency + ", interestRate=" + interestRate + "}";
    }
}
