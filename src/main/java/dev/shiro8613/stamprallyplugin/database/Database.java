package dev.shiro8613.stamprallyplugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static DatabaseConn databaseConn;
    private final String host, user, database, password;
    private final int port;
    private final DriverType driverType;

    public Database(DriverType driverType,
                    String host, String user,
                    String database, String password,
                    int port) throws SQLException, ClassNotFoundException {
        this.host = host;
        this.user = user;
        this.database = database;
        this.password = password;
        this.port = port;
        this.driverType = driverType;

    }

    public static DatabaseConn getInstance() {
        return databaseConn;
    }

    public DatabaseConn getConn() {
        return databaseConn;
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (databaseConn != null && databaseConn.isClosed()) return;

        synchronized (this) {
            if (databaseConn != null && databaseConn.isClosed()) return;

            Class.forName("com.mysql.jdbc.Driver");
            databaseConn = new DatabaseConn(driverSetup());
        }
    }

    public void closeConnection() throws SQLException {
        if (databaseConn != null && databaseConn.isClosed()) return;
        if (databaseConn != null) {
            databaseConn.Close();
        }
        databaseConn = null;
    }

    private Connection driverSetup() throws SQLException {
        switch (this.driverType) {
            case MYSQL -> {
                return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
            }
            case SQLITE -> {
                return DriverManager.getConnection("jdbc:sqlite://" + database);
            }
        }
        return null;
    }
}
