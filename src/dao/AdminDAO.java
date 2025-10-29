package dao;

import db.DBConnection;
import model.Admin;

import java.sql.*;

public class AdminDAO extends BaseDAO<Admin> {

    // singleton instance
    private static AdminDAO instance;

    private AdminDAO() {}

    public static AdminDAO getInstance() {
        if (instance == null) {
            instance = new AdminDAO();
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return "admins";
    }

    // insert sql
    @Override
    protected String getInsertQuery() {
        return "INSERT INTO admins (username, password) VALUES (?, ?)";
    }

    // update sql
    @Override
    protected String getUpdateQuery() {
        return "UPDATE admins SET username = ?, password = ? WHERE id = ?";
    }

    // maps a result set row to an admin object
    @Override
    protected Admin mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        return new Admin(id, username, password);
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Admin admin) throws SQLException {
        stmt.setString(1, admin.getUsername());
        stmt.setString(2, admin.getPassword());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Admin admin) throws SQLException {
        stmt.setString(1, admin.getUsername());
        stmt.setString(2, admin.getPassword());
        stmt.setInt(3, admin.getId());
    }

    public Admin getById(int id) {
        String query = "SELECT * FROM admins WHERE id = ?";

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
