package com.dss56.DL;

import com.dss56.LN.UC.Turno.Turno;
import com.dss56.LN.UC.Turno.TurnoLaboratorial;
import com.dss56.LN.UC.Turno.TurnoPratico;
import com.dss56.LN.UC.Turno.TurnoTeorico;
import com.dss56.Util.DSSException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAO extends DAO {
    private static TurnoDAO instance;

    private final PreparedStatement GET_TURNOS;
    private final PreparedStatement GET_TURNOS_UC;
    private final PreparedStatement GET_TURNOS_ALUNO;
    private final PreparedStatement GET_TURNO_OCCUPATION;
    private final PreparedStatement ADD_ALUNO_TO_TURNO;

    public TurnoDAO() throws SQLException {
        this.GET_TURNOS = this.connection.prepareStatement("SELECT * FROM Turno");
        this.GET_TURNOS_ALUNO = this.connection.prepareStatement("SELECT id, codigo FROM InscritosTurnoUC WHERE numMec = ?");
        this.GET_TURNOS_UC = this.connection.prepareStatement("SELECT * FROM Turno WHERE codigo = ?");
        this.GET_TURNO_OCCUPATION = this.connection.prepareStatement("SELECT COUNT(*) FROM InscritosTurnoUC WHERE id = ? AND codigo = ?");
        this.ADD_ALUNO_TO_TURNO = this.connection.prepareStatement("INSERT INTO InscritosTurnoUC (numMec, id, codigo) VALUES (?, ?, ?)");
    }

    public static TurnoDAO getInstance() throws SQLException {
        if (instance == null) instance = new TurnoDAO();
        return (TurnoDAO)instance;
    }

    public List<Turno> obterTurnosAluno(int numMec) throws Exception {
        try {
            List<Turno> turnos = new ArrayList<>();
            this.GET_TURNOS.clearParameters();
            ResultSet rs = this.GET_TURNOS.executeQuery();
            while (rs.next()) {
                turnos.add(parseTurno(rs));
            }

            List<Turno> filtered = new ArrayList<>();
            this.GET_TURNOS_ALUNO.clearParameters();
            this.GET_TURNOS_ALUNO.setInt(1, numMec);
            ResultSet rs2 = this.GET_TURNOS_ALUNO.executeQuery();

            while (rs2.next()) {
                for (Turno t : turnos) {
                    if (t.getId().equals(rs2.getString("id")) && t.getCodigoUC().equals(rs2.getString("codigo"))) {
                        filtered.add(t);
                    }
                }
            }

            // Fetch occupation for each turno
            for (Turno t : filtered) {
                this.GET_TURNO_OCCUPATION.clearParameters();
                this.GET_TURNO_OCCUPATION.setString(1, t.getId());
                this.GET_TURNO_OCCUPATION.setString(2, t.getCodigoUC());
                ResultSet rs3 = this.GET_TURNO_OCCUPATION.executeQuery();
                if (rs3.next()) {
                    t.setOcupacao(rs3.getInt(1));
                }
            }

            return filtered;
        } catch (SQLException e) {
            throw new Exception("Error while getting list of turnos for aluno:" + DSSException.getStackTrace(e));
        }
    }

    public List<Turno> obterTurnosUC(String codigo) throws Exception {
        try {
            List<Turno> turnos = new ArrayList<>();
            this.GET_TURNOS_UC.clearParameters();
            this.GET_TURNOS_UC.setString(1, codigo);
            ResultSet rs = this.GET_TURNOS_UC.executeQuery();
            while (rs.next()) {
                Turno t = parseTurno(rs);

                this.GET_TURNO_OCCUPATION.clearParameters();
                this.GET_TURNO_OCCUPATION.setString(1, t.getId());
                this.GET_TURNO_OCCUPATION.setString(2, t.getCodigoUC());
                ResultSet rs2 = this.GET_TURNO_OCCUPATION.executeQuery();
                if (rs2.next()) {
                    t.setOcupacao(rs2.getInt(1));
                }

                turnos.add(t);
            }

            return turnos;
        } catch (SQLException e) {
            throw new Exception("Error while getting turnos of UC:" + DSSException.getStackTrace(e));
        }
    }

    public void adicionarAlunoATurno(int numMec, String id, String codigo) throws Exception {
        try {
            this.ADD_ALUNO_TO_TURNO.clearParameters();
            this.ADD_ALUNO_TO_TURNO.setInt(1, numMec);
            this.ADD_ALUNO_TO_TURNO.setString(2, id);
            this.ADD_ALUNO_TO_TURNO.setString(3, codigo);
            this.ADD_ALUNO_TO_TURNO.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error while adding aluno to turno:" + DSSException.getStackTrace(e));
        }
    }

    private Turno parseTurno(ResultSet rs) throws SQLException {
        int type = rs.getInt("tipo");
        switch (type) {
            case 1: {
                return new TurnoTeorico(
                        rs.getString("id"),
                        DayOfWeek.of(rs.getInt("dia")),
                        rs.getObject("inicio", LocalTime.class),
                        rs.getObject("fim", LocalTime.class),
                        Turno.Tipo.of(rs.getInt("tipo")),
                        rs.getString("codigo"),
                        rs.getInt("capacidadeMaxima")
                );
            }
            case 2: {
                return new TurnoPratico(
                        rs.getString("id"),
                        DayOfWeek.of(rs.getInt("dia")),
                        rs.getObject("inicio", LocalTime.class),
                        rs.getObject("fim", LocalTime.class),
                        Turno.Tipo.of(rs.getInt("tipo")),
                        rs.getString("codigo"),
                        rs.getInt("capacidadeMaxima")
                );
            }
            case 3: {
                return new TurnoLaboratorial(
                        rs.getString("id"),
                        DayOfWeek.of(rs.getInt("dia")),
                        rs.getObject("inicio", LocalTime.class),
                        rs.getObject("fim", LocalTime.class),
                        Turno.Tipo.of(rs.getInt("tipo")),
                        rs.getString("codigo"),
                        rs.getInt("capacidadeMaxima")
                );
            }
            default: {
                throw new SQLException("Invalid Turno type.");
            }
        }
    }
}