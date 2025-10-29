package service;

import dao.ClientDAO;
import model.Address;
import model.Client;

import java.util.List;
import java.util.Scanner;

// service for client operations + cli wrappers to keep main unchanged
public class ClientService {

    private static ClientService instance;
    private final ClientDAO clientDAO = ClientDAO.getInstance();
    private final AuditService audit = AuditService.getInstance();

    private ClientService() {}

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void createClient(Client client) {
        clientDAO.create(client);
        audit.logAction("create_client");
    }

    public List<Client> getAllClients() {
        audit.logAction("get_all_clients");
        return clientDAO.readAll();
    }

    public Client getClientById(int id) {
        audit.logAction("get_client_by_id");
        return clientDAO.getById(id);
    }

    public void updateClient(Client client) {
        clientDAO.update(client);
        audit.logAction("update_client");
    }

    public void deleteClient(int id) {
        clientDAO.deleteById(id);
        audit.logAction("delete_client");
    }

    // simple login by email+password
    public Client loginClient(String email, String password) {
        audit.logAction("client_login_attempt");
        return clientDAO.readAll().stream()
                .filter(c -> c.getEmail().equals(email) && c.getPassword().equals(password))
                .findFirst().orElse(null);
    }

    // reads client data and creates it
    public void handleAddClient(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Street: ");
        String street = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("Postal code: ");
        String postal = scanner.nextLine();

        Address address = new Address(street, city, postal);
        createClient(new Client(0, username, password, name, email, phone, address));
        System.out.println("Client created.");
    }

    public void handleShowAll() {
        List<Client> list = getAllClients();
        if (list.isEmpty()) System.out.println("No clients found.");
        else list.forEach(System.out::println);
    }

    public void handleDeleteClient(Scanner scanner) {
        System.out.print("Client ID: ");
        int id = readInt(scanner);
        deleteClient(id);
        System.out.println("Client deleted (if existed).");
    }

    public void handleUpdateClient(Scanner scanner) {
        System.out.print("Client ID: ");
        int id = readInt(scanner);
        Client c = getClientById(id);
        if (c == null) {
            System.out.println("Client not found.");
            return;
        }
        System.out.print("New name (blank to keep): ");
        String name = scanner.nextLine();
        System.out.print("New email (blank to keep): ");
        String email = scanner.nextLine();
        System.out.print("New phone (blank to keep): ");
        String phone = scanner.nextLine();

        if (!name.isBlank()) c.setName(name);
        if (!email.isBlank()) c.setEmail(email);
        if (!phone.isBlank()) c.setPhone(phone);

        updateClient(c);
        System.out.println("Client updated.");
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
