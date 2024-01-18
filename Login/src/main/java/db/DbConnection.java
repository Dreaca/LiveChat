package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public Connection connection;
    public static DbConnection dbConnection;
    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/livechat",
                    "root",
                        "Ijse@1234"
        );
    }

    public  Connection getConnection() {
        return connection;
    }
    public static DbConnection getDbConnection() throws SQLException {
        return dbConnection == null? new DbConnection() : dbConnection;
    }
}
