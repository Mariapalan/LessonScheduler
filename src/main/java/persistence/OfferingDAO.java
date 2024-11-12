package persistence;

import offerings.Offering;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class OfferingDAO {

    private final Connection connection;

    public OfferingDAO(Connection connection) {
        this.connection = connection;
        createTable();
    }

    //todo: adjust table schema, placeholder for now
    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS offerings ("
                + "id INTEGER PRIMARY KEY, "
                + "location TEXT NOT NULL, "
                + "description TEXT NOT NULL, "
                + "is_available BOOLEAN DEFAULT TRUE"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean store(Offering offering) {
        String insertSQL = "INSERT INTO users (name, age) VALUES ('Alice', 30);";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertSQL);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
