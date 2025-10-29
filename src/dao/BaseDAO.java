package dao;

import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// generic base dao providing common crud operations

public abstract class BaseDAO<T> {

    protected abstract String getTableName();

    protected abstract T mapResultSet(ResultSet rs) throws SQLException;

    protected abstract void setInsertParameters(PreparedStatement stmt, T obj) throws SQLException;

    protected abstract void setUpdateParameters(PreparedStatement stmt, T obj) throws SQLException;

    // inserts a new record into the database
    public void create(T obj) {
        String sql = getInsertQuery();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setInsertParameters(stmt, obj);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database insert failed", e);
        }
    }

    // returns all records from the table
    public List<T> readAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database readAll failed", e);
        }

        return list;
    }

    // returns one record by id
    public T readById(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        T result = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database readById failed", e);
        }

        return result;
    }

    // updates an existing record
    public void update(T obj) {
        String sql = getUpdateQuery();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setUpdateParameters(stmt, obj);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database update failed", e);
        }
    }

    // deletes a record by id
    public boolean deleteById(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database delete failed", e);
        }
    }


    // returns the insert sql string (must be overridden by subclasses)
    protected String getInsertQuery() {
        throw new UnsupportedOperationException("Insert query not implemented.");
    }

    // returns the update sql string (must be overridden by subclasses)
    protected String getUpdateQuery() {
        throw new UnsupportedOperationException("Update query not implemented.");
    }
}
