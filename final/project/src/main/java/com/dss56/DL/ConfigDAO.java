package com.dss56.DL;

import com.dss56.Util.DSSException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigDAO {
    public static final String HOST = "localhost";
    public static final String PORT = "3306";
    public static final String USERNAME = "batman";
    public static final String PASSWORD = "88k%NryV&C2zkmQ8%8B^m8aHE!6pxG3r";
    public static final String DATABASE = "DSS";
    public static final String DRIVER = "jdbc:mysql";
    public static final String URL = DRIVER + "://" + HOST + ":" + PORT + "/" + DATABASE + "?allowPublicKeyRetrival=true&useSSL=false" + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetime=false&serverTimezone=UTC";

    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Establishing connection to the database..." + URL);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Error while connecting to the database: " + DSSException.getStackTrace(e));
        }
    }
}
