package dao;

import db.DBConnection;
import model.Account;
import model.SavingsAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends BaseDAO<Account> {

    // singleton instance
    private static AccountDAO instance;

    private AccountDAO() {}

    public static AccountDAO getInstance() {
        if (instance == null) {
            instance = new AccountDAO();
        }
        return instance;
    }

    // returns all accounts for a given client id
    public List<Account> getByClientId(int clientId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE client_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database getByClientId failed", e);
        }

        return list;
    }

    public void updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database updateBalance failed", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM accounts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database deleteById failed", e);
        }
    }

    @Override
    protected String getTableName() {
        return "accounts";
    }

    // insert sql
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO accounts (client_id, iban, balance, currency, type, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";
    }

    // update sql
    @Override
    protected String getUpdateQuery() {
        return "UPDATE accounts SET client_id = ?, iban = ?, balance = ?, currency = ?, type = ?, interest_rate = ? WHERE id = ?";
    }

    // maps a result set row to an account object
    @Override
    protected Account mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int clientId = rs.getInt("client_id");
        String iban = rs.getString("iban");
        double balance = rs.getDouble("balance");
        String currency = rs.getString("currency");
        String type = rs.getString("type");
        double interestRate = rs.getDouble("interest_rate");

        if ("savings".equalsIgnoreCase(type)) {
            return new SavingsAccount(id, clientId, iban, balance, currency, interestRate);
        } else {
            return new Account(id, clientId, iban, balance, currency, type);
        }
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Account acc) throws SQLException {
        stmt.setInt(1, acc.getClientId());
        stmt.setString(2, acc.getIban());
        stmt.setDouble(3, acc.getBalance());
        stmt.setString(4, acc.getCurrency());
        stmt.setString(5, acc.getType());

        if (acc instanceof SavingsAccount) {
            stmt.setDouble(6, ((SavingsAccount) acc).getInterestRate());
        } else {
            stmt.setNull(6, Types.DOUBLE);
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Account acc) throws SQLException {
        stmt.setInt(1, acc.getClientId());
        stmt.setString(2, acc.getIban());
        stmt.setDouble(3, acc.getBalance());
        stmt.setString(4, acc.getCurrency());
        stmt.setString(5, acc.getType());

        if (acc instanceof SavingsAccount) {
            stmt.setDouble(6, ((SavingsAccount) acc).getInterestRate());
        } else {
            stmt.setNull(6, Types.DOUBLE);
        }

        stmt.setInt(7, acc.getId());
    }
}
