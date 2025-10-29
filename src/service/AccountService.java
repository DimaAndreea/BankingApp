package service;

import dao.AccountDAO;
import model.Account;
import model.SavingsAccount;

import java.util.List;
import java.util.Scanner;

// service for account operations + cli wrappers to keep main unchanged
public class AccountService {

    private static AccountService instance;
    private final AccountDAO accountDAO = AccountDAO.getInstance();
    private final AuditService audit = AuditService.getInstance();

    private AccountService() {}

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    public void createAccount(Account account) {
        accountDAO.create(account);
        audit.logAction("create_account");
    }

    public List<Account> getAccountsByClientId(int clientId) {
        audit.logAction("get_accounts_by_client_id");
        return accountDAO.getByClientId(clientId);
    }

    public void updateBalance(int accountId, double newBalance) {
        accountDAO.updateBalance(accountId, newBalance);
        audit.logAction("update_balance");
    }

    public void deleteAccount(int id) {
        accountDAO.deleteById(id);
        audit.logAction("delete_account");
    }

    public List<Account> getAllAccounts() {
        audit.logAction("get_all_accounts");
        return accountDAO.readAll();
    }

    // apply interest to all savings accounts
    public void applyInterestToSavingsAccounts() {
        List<Account> all = accountDAO.readAll();
        for (Account a : all) {
            if (a instanceof SavingsAccount sa) {
                double newBalance = a.getBalance() * (1.0 + sa.getInterestRate());
                accountDAO.updateBalance(a.getId(), newBalance);
            }
        }
        audit.logAction("apply_interest_savings");
    }

    // handles account creation from console
    public void handleAddAccount(Scanner scanner) {
        System.out.print("Client ID: ");
        int clientId = readInt(scanner);
        System.out.print("IBAN: ");
        String iban = scanner.nextLine();
        System.out.print("Currency: ");
        String currency = scanner.nextLine();
        System.out.print("Type (checking/savings): ");
        String type = scanner.nextLine().trim().toLowerCase();

        if ("savings".equals(type)) {
            System.out.print("Interest rate (as decimal, e.g. 0.03): ");
            double rate = readDouble(scanner);
            createAccount(new SavingsAccount(0, clientId, iban, 0.0, currency, rate));
        } else {
            createAccount(new Account(0, clientId, iban, 0.0, currency, "checking"));
        }
        System.out.println("Account created.");
    }

    // handles deposit operation
    public void handleDeposit(Scanner scanner) {
        System.out.print("Account ID: ");
        int id = readInt(scanner);
        System.out.print("Amount: ");
        double amount = readDouble(scanner);

        Account a = accountDAO.readById(id);
        if (a == null) {
            System.out.println("Account not found.");
            return;
        }

        updateBalance(id, a.getBalance() + amount);
        System.out.println("Deposit completed successfully.");
    }

    // handles withdrawal operation
    public void handleWithdraw(Scanner scanner) {
        System.out.print("Account ID: ");
        int id = readInt(scanner);
        System.out.print("Amount: ");
        double amount = readDouble(scanner);

        Account a = accountDAO.readById(id);
        if (a == null) {
            System.out.println("Account not found.");
            return;
        }
        if (a.getBalance() < amount) {
            System.out.println("Insufficient funds.");
            return;
        }

        updateBalance(id, a.getBalance() - amount);
        System.out.println("Withdrawal completed successfully.");
    }

    // handles transfer between accounts
    public void handleTransfer(Scanner scanner, TransactionService txService) {
        System.out.print("Source account ID: ");
        int src = readInt(scanner);
        System.out.print("Destination account ID: ");
        int dst = readInt(scanner);
        System.out.print("Amount: ");
        double amount = readDouble(scanner);

        txService.transfer(src, dst, amount);
        System.out.println("Transfer completed successfully.");
    }

    // displays all accounts
    public void handleShowAll() {
        List<Account> list = getAllAccounts();
        if (list.isEmpty()) System.out.println("No accounts found.");
        else list.forEach(System.out::println);
    }

    // deletes account from console
    public void handleDeleteAccount(Scanner scanner) {
        System.out.print("Account ID: ");
        int id = readInt(scanner);
        deleteAccount(id);
        System.out.println("Account deleted.");
    }

    private int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a number: ");
            sc.next();
        }
        int n = sc.nextInt();
        sc.nextLine();
        return n;
    }

    private double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a number: ");
            sc.next();
        }
        double d = sc.nextDouble();
        sc.nextLine();
        return d;
    }
}
