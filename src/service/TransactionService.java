package service;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

// service for handling transfers and transaction history
public class TransactionService {

    private static TransactionService instance;
    private final TransactionDAO transactionDAO = TransactionDAO.getInstance();
    private final AccountDAO accountDAO = AccountDAO.getInstance();
    private final AuditService audit = AuditService.getInstance();

    private TransactionService() {}

    public static TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    // transfers money (naive, without db transaction)
    public void transfer(int sourceId, int destId, double amount) {
        audit.logAction("transfer");

        var source = accountDAO.readById(sourceId);
        var dest = accountDAO.readById(destId);

        if (source == null || dest == null) {
            throw new RuntimeException("Invalid account ID.");
        }
        if (amount <= 0) {
            throw new RuntimeException("Amount must be positive.");
        }
        if (source.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds.");
        }

        accountDAO.updateBalance(sourceId, source.getBalance() - amount);
        accountDAO.updateBalance(destId, dest.getBalance() + amount);

        var tx = new Transaction(0, sourceId, destId, amount, LocalDateTime.now(), "transfer");
        transactionDAO.create(tx);
    }

    public List<Transaction> getTransactionsByAccount(int accountId) {
        audit.logAction("get_transactions_by_account");
        return transactionDAO.getByAccountId(accountId);
    }

    public List<Transaction> getAllTransactions() {
        audit.logAction("get_all_transactions");
        return transactionDAO.readAll();
    }
}
