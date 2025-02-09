package com.dss56.LN.UC;

import com.dss56.DL.UCDAO;
import com.dss56.LN.UC.Turno.Turno;
import com.dss56.DL.TurnoDAO;
import com.dss56.Util.DSSException;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class UCFacade implements IGesUCs {
    private final UCDAO ucs = UCDAO.getInstance();
    private final TurnoDAO turnos = TurnoDAO.getInstance();

    public UCFacade() throws SQLException { }

    public boolean contemInscritoUC(String codigo, int numMec) {
        try {
            return ucs.existeAlunoInscrito(codigo, numMec);
        } catch (Exception e) {
            return false;
        }
    }

    public void adicionarInscritoUC(String codigo, int numMec) throws Exception {
        ucs.adicionarAlunoInscrito(codigo, numMec);
    }

    public List<Turno> obterHorario(int numMec) {
        try {
            return turnos.obterTurnosAluno(numMec);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Map<Integer, List<String>> obterInscricoes(List<Integer> numAlunos) {
        Map<Integer, List<String>> inscricoes = new HashMap<>();
        for (Integer num : numAlunos) {
            List<String> codigoUCs;
            try {
                codigoUCs = ucs.obterInscricoes(num);
            } catch (Exception e) {
                codigoUCs = null;
            }
            inscricoes.put(num, codigoUCs);
        }
        return inscricoes;
    }

    public List<Turno> obterTurnos(String codigo) {
        try {
            return turnos.obterTurnosUC(codigo);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void adicionarAlunoTurno(int num, String id, String codigo) {
        try {
            turnos.adicionarAlunoATurno(num, id, codigo);
        } catch (Exception e) {
            System.out.println(DSSException.getStackTrace(e));
        }
    }

    public void inscreverAlunoTurnos(int numMec, List<String> codigos) throws Exception {
        List<Turno> turnosAluno = turnos.obterTurnosAluno(numMec);

        for (String codigo: codigos) {
            if (turnosAluno.stream().anyMatch(turno -> codigo.equals(turno.getCodigoUC()))) continue;

            List<Turno> turnosUC = turnos.obterTurnosUC(codigo);

            List<Turno> availableTurnos = turnosUC.stream().filter(t ->
                    turnosAluno.stream().noneMatch(ta ->
                            (Objects.equals(ta.getId(), t.getId()) &&
                                    Objects.equals(ta.getCodigoUC(), t.getCodigoUC()) &&
                                    ta.getTipo() == t.getTipo()) ||
                                    ta.overlaps(t)
                    ) && t.getOcupacao() < t.getCapacidadeMaxima()
            ).collect(Collectors.toList());

            // Only get at most one turno of each type. If multiple candidates are available, the first one is chosen.
            HashSet<Turno.Tipo> types = new HashSet<>();
            availableTurnos.removeIf(t -> !types.add(t.getTipo()));

//            System.out.println("\n\n===== CODIGO UC: " + codigo + " =====");
//            System.out.println("Inscrito nos Turnos: ");
//            for (Turno t : turnosAluno) System.out.println("   " + t);
//            System.out.println("Turnos da UC: ");
//            for (Turno t : turnosUC) System.out.println("   " + t);
//            System.out.println("Turnos disponiveis: ");
//            for (Turno t : availableTurnos) System.out.println("   " + t);

            if (availableTurnos.isEmpty()) {
                // throw new Exception("No available turnos for aluno " + numMec + " in UC " + codigo);
                continue;
            }

            for (Turno t : availableTurnos) {
                turnos.adicionarAlunoATurno(numMec, t.getId(), t.getCodigoUC());
                turnosAluno.add(t);
            }

            if (availableTurnos.size() < 2) {
                // One turno type is missing, since a UC always have 1 T and 1 TP/PL.
//                System.out.println("NAO CONSEGUI ALOCAR O ALUNO: " + numMec);
//                System.out.println("UC: " + codigo + ". TURNOS DISPONIVEIS: "  + availableTurnos);
                throw new Exception("One turno enrollment is missing atleast for aluno " + numMec + " in UC " + codigo);
            }
        }

//        System.out.print("\n\n === INSCRITO NOS TURNOS [ FINAL ] ===\n");
//        turnosAluno.sort(Comparator.comparing(Turno::getCodigoUC));
//        for (Turno t : turnosAluno) System.out.println("   " + t);
    }
}
