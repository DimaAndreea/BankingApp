package dao;

import db.DBConnection;
import model.Address;
import model.Client;

import java.sql.*;

public class ClientDAO extends BaseDAO<Client> {

    // singleton instance
    private static ClientDAO instance;

    private ClientDAO() {}

    public static ClientDAO getInstance() {
        if (instance == null) {
            instance = new ClientDAO();
        }
        return instance;
    }

    // returns the table name
    @Override
    protected String getTableName() {
        return "clients";
    }

    // insert sql
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO clients (username, password, name, email, phone, street, city, postal_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    // update sql
    @Override
    protected String getUpdateQuery() {
        return "UPDATE clients SET username = ?, password = ?, name = ?, email = ?, phone = ?, " +
                "street = ?, city = ?, postal_code = ? WHERE id = ?";
    }

    // maps a result set row to a client object
    @Override
    protected Client mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String street = rs.getString("street");
        String city = rs.getString("city");
        String postalCode = rs.getString("postal_code");

        Address address = new Address(street, city, postalCode);
        return new Client(id, username, password, name, email, phone, address);
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Client client) throws SQLException {
        stmt.setString(1, client.getUsername());
        stmt.setString(2, client.getPassword());
        stmt.setString(3, client.getName());
        stmt.setString(4, client.getEmail());
        stmt.setString(5, client.getPhone());
        stmt.setString(6, client.getAddress().getStreet());
        stmt.setString(7, client.getAddress().getCity());
        stmt.setString(8, client.getAddress().getPostalCode());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Client client) throws SQLException {
        stmt.setString(1, client.getUsername());
        stmt.setString(2, client.getPassword());
        stmt.setString(3, client.getName());
        stmt.setString(4, client.getEmail());
        stmt.setString(5, client.getPhone());
        stmt.setString(6, client.getAddress().getStreet());
        stmt.setString(7, client.getAddress().getCity());
        stmt.setString(8, client.getAddress().getPostalCode());
        stmt.setInt(9, client.getId());
    }

    public Client getById(int id) {
        String query = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database getById failed", e);
        }

        return null;
    }
}
