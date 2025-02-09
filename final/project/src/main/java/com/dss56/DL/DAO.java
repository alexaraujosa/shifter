package com.dss56.DL;

import java.sql.Connection;
import java.sql.SQLException;

public class DAO {
    protected static DAO instance;
    protected final Connection connection;

    public DAO() throws SQLException {
        this.connection = ConfigDAO.getConnection();
    }

    public static DAO getInstance() throws SQLException {
        return instance;
    }
}
