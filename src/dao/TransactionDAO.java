package dao;

import db.DBConnection;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO extends BaseDAO<Transaction> {

    // singleton instance
    private static TransactionDAO instance;

    private TransactionDAO() {}

    public static TransactionDAO getInstance() {
        if (instance == null) {
            instance = new TransactionDAO();
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return "transactions";
    }

    // insert sql
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO transactions (source_account_id, destination_account_id, amount, timestamp, type) VALUES (?, ?, ?, ?, ?)";
    }

    // update sql
    @Override
    protected String getUpdateQuery() {
        return "UPDATE transactions SET source_account_id = ?, destination_account_id = ?, amount = ?, timestamp = ?, type = ? WHERE id = ?";
    }

    @Override
    protected Transaction mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int sourceId = rs.getInt("source_account_id");
        int destId = rs.getInt("destination_account_id");
        double amount = rs.getDouble("amount");
        String type = rs.getString("type");
        Timestamp timestamp = rs.getTimestamp("timestamp");

        return new Transaction(id, sourceId, destId, amount, timestamp.toLocalDateTime(), type);
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Transaction t) throws SQLException {
        stmt.setInt(1, t.getSourceAccountId());
        stmt.setInt(2, t.getDestinationAccountId());
        stmt.setDouble(3, t.getAmount());
        stmt.setTimestamp(4, Timestamp.valueOf(t.getTimestamp()));
        stmt.setString(5, t.getTransactionType());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Transaction t) throws SQLException {
        stmt.setInt(1, t.getSourceAccountId());
        stmt.setInt(2, t.getDestinationAccountId());
        stmt.setDouble(3, t.getAmount());
        stmt.setTimestamp(4, Timestamp.valueOf(t.getTimestamp()));
        stmt.setString(5, t.getTransactionType());
        stmt.setInt(6, t.getId());
    }

    // returns all transactions for a given account id
    public List<Transaction> getByAccountId(int accountId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE source_account_id = ? OR destination_account_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database getByAccountId failed", e);
        }

        return list;
    }
}
