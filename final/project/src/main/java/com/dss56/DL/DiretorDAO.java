package com.dss56.DL;

import com.dss56.Util.DSSException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiretorDAO extends DAO {
    private static DiretorDAO instance;

    private final PreparedStatement GET_PASSWORD = this.connection.prepareStatement("SELECT palavraPasse FROM Diretor");

    public DiretorDAO() throws SQLException { super(); }

    public static DiretorDAO getInstance() throws SQLException {
        if (instance == null) instance = new DiretorDAO();
        return (DiretorDAO) instance;
    }

    public String getPalavraPasse() throws Exception {
        try {
            this.GET_PASSWORD.clearParameters();
            ResultSet rs = this.GET_PASSWORD.executeQuery();
            if (rs.next()) {
                return rs.getString("palavraPasse");
            }
            throw new Exception("No password entries on database. ");
        } catch (SQLException e) {
            throw new Exception("Error while getting diretor password:" + DSSException.getStackTrace(e));
        }
    }
}
