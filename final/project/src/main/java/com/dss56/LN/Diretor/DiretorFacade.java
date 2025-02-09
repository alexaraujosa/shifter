package com.dss56.LN.Diretor;

import com.dss56.DL.DiretorDAO;

import java.sql.SQLException;

public class DiretorFacade implements IGesDiretor {
    private final DiretorDAO diretor;

    public DiretorFacade() throws SQLException {
        this.diretor = DiretorDAO.getInstance();
    }

    public boolean validarPalavraPasse(String palavraPasse) {
        try {
            return diretor.getPalavraPasse().equals(palavraPasse);
        } catch (Exception e) {
            return false;
        }
    }
}
