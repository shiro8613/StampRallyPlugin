package dev.shiro8613.stamprallyplugin.database;

import dev.shiro8613.stamprallyplugin.StampRallyPlugin;
import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import org.bukkit.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConn implements DatabaseConnImpl{

    private final Connection connection;

    public DatabaseConn(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void CreateTable() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.execute("""
                CREATE TABLE IF NOT EXISTS config(
                    Radius INT
                );""");

        statement.execute("""
                CREATE TABLE IF NOT EXISTS positions(
                    StampId INT,
                    WorldName TEXT,
                    PosX INT,
                    PosY INT,
                    PosZ INT,
                    PRIMARY KEY (StampId)
                );""");

        statement.execute("""
                CREATE TABLE IF NOT EXISTS maps(
                    MapId INT,
                    StampId INT
                );""");
    }

    public List<StampLocation> getStampLocation() {
        String sql = "SELECT * FROM positions;";
        List<StampLocation> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new StampLocation(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5)));
            }

            return list;
        } catch (SQLException e) {
            StampRallyPlugin.getInstance().getLogger().warning(e.getMessage());
            return null;
        }
    }

    public boolean setStampLocation(int StampId, Location location) {
        String sql = """
                    INSERT INTO positions (StampId, WorldName, PosX, PosY, PosZ)
                    VALUES (?,?,?,?,?)
                    ON DUPLICATE KEY UPDATE
                        `WorldName`   = VALUES(`WorldName`)
                        ,`PosX`       = VALUES(`PosX`)
                        ,`PosY`       = VALUES(`PosY`)
                        ,`PosZ`       = VALUES(`PosZ`);
                    """;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1, StampId);
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setInt(3, location.getBlockX());
            preparedStatement.setInt(4, location.getBlockY());
            preparedStatement.setInt(5, location.getBlockZ());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            StampRallyPlugin.getInstance().getLogger().warning(e.getMessage());
            return false;
        }
    }

    @Override
    public void Close() throws SQLException {
        this.connection.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }
}
