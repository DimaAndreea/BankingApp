package model;

import java.time.LocalDateTime;

// represents a transaction between two accounts
public class Transaction {

    private int id;
    private int sourceAccountId;
    private int destinationAccountId;
    private double amount;
    private LocalDateTime timestamp;
    private String transactionType;

    public Transaction() {}

    public Transaction(int id, int sourceAccountId, int destinationAccountId,
                       double amount, LocalDateTime timestamp, String transactionType) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(int sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public int getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(int destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", amount=" + amount +
                ", type='" + transactionType + "', timestamp=" + timestamp + "}";
    }
}
