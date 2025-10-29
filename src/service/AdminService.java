package service;

import dao.AdminDAO;
import model.Admin;

import java.util.List;
import java.util.Scanner;

public class AdminService {

    private static AdminService instance;
    private final AdminDAO adminDAO = AdminDAO.getInstance();
    private final AuditService audit = AuditService.getInstance();

    private AdminService() {}

    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }

    public void createAdmin(Admin admin) {
        adminDAO.create(admin);
        audit.logAction("create_admin");
    }

    public Admin getAdminById(int id) {
        audit.logAction("get_admin_by_id");
        return adminDAO.getById(id);
    }

    public void updateAdmin(Admin admin) {
        adminDAO.update(admin);
        audit.logAction("update_admin");
    }

    public void deleteAdmin(int id) {
        adminDAO.deleteById(id);
        audit.logAction("delete_admin");
    }

    public boolean login(String username, String password) {
        audit.logAction("admin_login_attempt");
        List<Admin> admins = adminDAO.readAll();
        return admins.stream().anyMatch(a ->
                a.getUsername().equals(username) && a.getPassword().equals(password));
    }

    public void handleAddAdmin(Scanner scanner) {
        System.out.print("Admin username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        createAdmin(new Admin(0, user, pass));
        System.out.println("Admin created.");
    }

    public void handleViewAccountRequests() {
        System.out.println("No account request system implemented.");
        audit.logAction("view_account_requests");
    }

    public void handleApproveAccountRequest(Scanner scanner) {
        System.out.println("No account request system implemented.");
        audit.logAction("approve_account_request");
    }

    public void handleUpdateAdmin(Scanner scanner) {
        System.out.print("Admin ID: ");
        int id = readInt(scanner);
        Admin a = getAdminById(id);
        if (a == null) {
            System.out.println("Admin not found.");
            return;
        }
        System.out.print("New username (blank to keep): ");
        String u = scanner.nextLine();
        System.out.print("New password (blank to keep): ");
        String p = scanner.nextLine();

        if (!u.isBlank()) a.setUsername(u);
        if (!p.isBlank()) a.setPassword(p);

        updateAdmin(a);
        System.out.println("Admin updated.");
    }

    public void handleUpdateClient(Scanner scanner) {
        System.out.print("Client ID: ");
        int id = readInt(scanner);

        var clientService = ClientService.getInstance();
        var client = clientService.getClientById(id);

        if (client == null) {
            System.out.println("Client not found.");
            return;
        }

        System.out.print("New name (blank to keep): ");
        String name = scanner.nextLine();
        System.out.print("New email (blank to keep): ");
        String email = scanner.nextLine();
        System.out.print("New phone (blank to keep): ");
        String phone = scanner.nextLine();

        if (!name.isBlank()) client.setName(name);
        if (!email.isBlank()) client.setEmail(email);
        if (!phone.isBlank()) client.setPhone(phone);

        clientService.updateClient(client);
        System.out.println("Client updated.");
        audit.logAction("admin_update_client");
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
}
