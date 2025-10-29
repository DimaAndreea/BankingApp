import service.*;
import model.Client;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // get service instances
        ClientService clientService = ClientService.getInstance();
        AccountService accountService = AccountService.getInstance();
        TransactionService transactionService = TransactionService.getInstance();
        AdminService adminService = AdminService.getInstance();

        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Register new client");
            System.out.println("2. Login as admin");
            System.out.println("3. Login as client");
            System.out.println("4. Create new admin");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int option = readInt(scanner);

            switch (option) {
                case 1 -> handleRegisterClient(scanner, clientService);
                case 2 -> handleAdminLogin(scanner, adminService, clientService, accountService);
                case 3 -> handleClientLogin(scanner, clientService, accountService, transactionService);
                case 4 -> adminService.handleAddAdmin(scanner);
                case 5 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    // reads int safely
    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        int num = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return num;
    }

    // handles client registration
    private static void handleRegisterClient(Scanner scanner, ClientService clientService) {
        clientService.handleAddClient(scanner);
    }

    // handles admin login flow
    private static void handleAdminLogin(Scanner scanner, AdminService adminService,
                                         ClientService clientService, AccountService accountService) {
        System.out.print("Admin username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (adminService.login(username, password)) {
            System.out.println("Welcome, admin!");
            handleAdminMenu(scanner, clientService, accountService, adminService);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    // handles client login flow
    private static void handleClientLogin(Scanner scanner, ClientService clientService,
                                          AccountService accountService, TransactionService transactionService) {
        System.out.print("Client email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Client client = clientService.loginClient(email, password);
        if (client != null) {
            System.out.println("Welcome, " + client.getUsername() + "!");
            handleClientMenu(scanner, client.getId(), accountService, transactionService);
        } else {
            System.out.println("Invalid client credentials.");
        }
    }

    // client menu
    private static void handleClientMenu(Scanner scanner, int clientId,
                                         AccountService accountService, TransactionService transactionService) {
        boolean active = true;

        while (active) {
            System.out.println("\n=== CLIENT MENU ===");
            System.out.println("1. Request account creation");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. View my accounts");
            System.out.println("5. Transfer funds");
            System.out.println("6. View total balance");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");

            int option = readInt(scanner);

            switch (option) {
                case 1 -> accountService.handleAddAccount(scanner);
                case 2 -> accountService.handleDeposit(scanner);
                case 3 -> accountService.handleWithdraw(scanner);
                case 4 -> {
                    var accounts = accountService.getAccountsByClientId(clientId);
                    if (accounts.isEmpty()) System.out.println("No accounts found.");
                    else accounts.forEach(System.out::println);
                }
                case 5 -> accountService.handleTransfer(scanner, transactionService);
                case 6 -> {
                    double total = accountService.getAccountsByClientId(clientId)
                            .stream()
                            .mapToDouble(a -> a.getBalance())
                            .sum();
                    System.out.println("Total balance: " + total);
                }
                case 7 -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // admin menu
    private static void handleAdminMenu(Scanner scanner, ClientService clientService,
                                        AccountService accountService, AdminService adminService) {
        boolean active = true;

        while (active) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View account creation requests");
            System.out.println("2. Approve account creation");
            System.out.println("3. View all clients");
            System.out.println("4. Delete client");
            System.out.println("5. View all accounts");
            System.out.println("6. Delete account");
            System.out.println("7. Apply interest to savings accounts");
            System.out.println("8. Update client");
            System.out.println("9. Update admin");
            System.out.println("10. Logout");
            System.out.print("Choose an option: ");

            int option = readInt(scanner);

            switch (option) {
                case 1 -> adminService.handleViewAccountRequests();
                case 2 -> adminService.handleApproveAccountRequest(scanner);
                case 3 -> clientService.handleShowAll();
                case 4 -> clientService.handleDeleteClient(scanner);
                case 5 -> accountService.handleShowAll();
                case 6 -> accountService.handleDeleteAccount(scanner);
                case 7 -> {
                    accountService.applyInterestToSavingsAccounts();
                    System.out.println("Interest applied to all savings accounts.");
                }
                case 8 -> adminService.handleUpdateClient(scanner);
                case 9 -> adminService.handleUpdateAdmin(scanner);
                case 10 -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
