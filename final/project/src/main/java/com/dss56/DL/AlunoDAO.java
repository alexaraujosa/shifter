package com.dss56.DL;

import com.dss56.Util.DSSException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO extends DAO {
    private static AlunoDAO instance;

    private final PreparedStatement ADD_ALUNO;
    private final PreparedStatement HAS_ALUNO;
    private final PreparedStatement GET_ALUNO_ESTATUTO;

    public AlunoDAO() throws SQLException {
        super();
        this.HAS_ALUNO = this.connection.prepareStatement("SELECT numMec FROM Aluno WHERE numMec = ?");
        this.ADD_ALUNO = this.connection.prepareStatement("INSERT INTO Aluno (numMec, nome, estatuto) VALUES (?, ?, ?)");
        this.GET_ALUNO_ESTATUTO = this.connection.prepareStatement("SELECT numMec FROM Aluno WHERE estatuto = ?");
    }

    public static AlunoDAO getInstance() throws SQLException {
        if (instance == null) instance = new AlunoDAO(); // TODO: Validar esta porra
        return (AlunoDAO)instance;
    }

    public boolean existeAluno(int numMec) throws Exception {
        try {
            this.HAS_ALUNO.clearParameters();
            this.HAS_ALUNO.setInt(1, numMec);
            ResultSet rs = this.HAS_ALUNO.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new Exception("Error while verifying if aluno exists:" + DSSException.getStackTrace(e));
        }
    }

    public void adicionarAluno(int numMec, String nome, boolean estatuto) throws Exception {
        try {
            this.ADD_ALUNO.clearParameters();
            this.ADD_ALUNO.setInt(1, numMec);
            this.ADD_ALUNO.setString(2, nome);
            this.ADD_ALUNO.setBoolean(3, estatuto);
            this.ADD_ALUNO.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error while adding aluno to Aluno table:" + DSSException.getStackTrace(e));
        }
    }

    public List<Integer> obterNumAlunosEstatuto(int estatuto) throws Exception {
        try {
            this.GET_ALUNO_ESTATUTO.clearParameters();
            this.GET_ALUNO_ESTATUTO.setInt(1, estatuto);
            ResultSet rs = this.GET_ALUNO_ESTATUTO.executeQuery();
            List<Integer> numAlunos = new ArrayList<>();
            while (rs.next()) {
                numAlunos.add(rs.getInt("numMec"));
            }
            return numAlunos;
        } catch (SQLException e) {
            throw new Exception("Error while getting alunos number with estatuto: " + DSSException.getStackTrace(e));
        }
    }

    
}
