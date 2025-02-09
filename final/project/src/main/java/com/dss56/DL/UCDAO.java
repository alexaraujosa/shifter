package com.dss56.DL;

import com.dss56.Util.DSSException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UCDAO extends DAO {
    private static UCDAO instance;

    private final PreparedStatement ADD_ALUNO_INSCRITO;
    private final PreparedStatement HAS_ALUNO_INSCRITO;
    private final PreparedStatement GET_INSCRICOES;

    public UCDAO() throws SQLException {
        super();
        this.HAS_ALUNO_INSCRITO = this.connection.prepareStatement("SELECT numMec FROM InscritosUC WHERE codigo = ? AND numMec = ?");
        this.ADD_ALUNO_INSCRITO = this.connection.prepareStatement("INSERT INTO InscritosUC (codigo, numMec) VALUES (?, ?)");
        this.GET_INSCRICOES = this.connection.prepareStatement("SELECT codigo FROM InscritosUC WHERE numMec = ?");
    }

    public static UCDAO getInstance() throws SQLException {
        if (instance == null) instance = new UCDAO();
        return (UCDAO) instance;
    }

    public boolean existeAlunoInscrito(String codigo, int numMec) throws Exception {
        try {
            this.HAS_ALUNO_INSCRITO.clearParameters();
            this.HAS_ALUNO_INSCRITO.setString(1, codigo);
            this.HAS_ALUNO_INSCRITO.setInt(2, numMec);
            ResultSet rs = this.HAS_ALUNO_INSCRITO.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new Exception("Error while veryfing if aluno is enrolled on UC: " + DSSException.getStackTrace(e));
        }
    }

    public void adicionarAlunoInscrito(String codigo, int numMec) throws Exception {
        try {
            this.ADD_ALUNO_INSCRITO.clearParameters();
            this.ADD_ALUNO_INSCRITO.setString(1, codigo);
            this.ADD_ALUNO_INSCRITO.setInt(2, numMec);
            this.ADD_ALUNO_INSCRITO.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error while adding aluno to enrolled list on UC:" + DSSException.getStackTrace(e));
        }
    }

    public List<String> obterInscricoes(int numMec) throws Exception {
        try {
            List<String> inscricoes = new ArrayList<>();
            this.GET_INSCRICOES.clearParameters();
            this.GET_INSCRICOES.setInt(1, numMec);
            ResultSet rs = this.GET_INSCRICOES.executeQuery();
            while (rs.next()) {
                inscricoes.add(rs.getString("codigo"));
            }
            return inscricoes;
        } catch (SQLException e) {
            throw new Exception("Error while fetching Inscricoes: " + DSSException.getStackTrace(e));
        }
    }
}