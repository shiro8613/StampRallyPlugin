package dev.shiro8613.stamprallyplugin.database;

import java.sql.SQLException;

public interface DatabaseConnImpl {


    void CreateTable() throws SQLException;

    void Close() throws SQLException;

    boolean isClosed() throws SQLException;
}
