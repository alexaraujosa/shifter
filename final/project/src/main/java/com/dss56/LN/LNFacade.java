package com.dss56.LN;

import com.dss56.LN.Aluno.Aluno;
import com.dss56.LN.Aluno.AlunoFacade;
import com.dss56.LN.Diretor.DiretorFacade;
import com.dss56.LN.UC.Turno.Turno;
import com.dss56.LN.UC.UC;
import com.dss56.LN.UC.UCFacade;
import com.dss56.Util.DSSException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class LNFacade implements IGeradorHAppLN {
    private final AlunoFacade alunoFacade = new AlunoFacade();
    private final DiretorFacade diretorFacade = new DiretorFacade();
    private final UCFacade ucFacade = new UCFacade();

    public LNFacade() throws SQLException { }

    //region Autenticação
    public boolean autenticarAluno(int numMec) {
        return alunoFacade.contemAluno(numMec);
    }

    public boolean autenticarDiretor(String palavraPasse) {
        return diretorFacade.validarPalavraPasse(palavraPasse);
    }
    //endregion

    //region Importar Alunos
    public boolean importarAlunos(String filePath) {
        try (
                Scanner sc = new Scanner(new FileReader(filePath))
        ) {
            String[] header = new String[0];

            int i = 0;
            int alunoCounter = 0, inscritoCounter = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(";", -1);

                i++;
                if (header.length == 0) {
                    header = parts;
                    continue;
                }

                if (parts.length != header.length) {
                    throw new IllegalArgumentException("Invalid line: Number of fields does not match header.");
                }

                Map<String, String> map = matchHeaderAndPayload(header, parts);

                try {
                    AlunoEntry alunoEntry = AlunoEntry.parseAlunoEntry(map);
                    if (this.alunoFacade.contemAluno(alunoEntry.numMec())) {
//                        System.out.println("Skipped import of aluno " + alunoEntry.numMec() + ", since it already exists.");
                    } else {
//                        System.out.println("Importing aluno: " + alunoEntry.numMec() + " | " + alunoEntry.nome() + " | " + alunoEntry.estatuto());
                        this.adicionarAluno(alunoEntry.numMec(), alunoEntry.nome(), alunoEntry.estatuto());
                        alunoCounter++;
                    }

                    if (this.ucFacade.contemInscritoUC(alunoEntry.codigoUC(), alunoEntry.numMec())) {
//                        System.out.println("Skipped add aluno " + alunoEntry.numMec() + " to UC, since it already exists in UC " + alunoEntry.codigoUC() + ".");
                    } else {
//                        System.out.println("Adding aluno to UC: " + alunoEntry.codigoUC() + " | " + alunoEntry.numMec());
                        this.adicionarInscritoUC(alunoEntry.codigoUC(), alunoEntry.numMec());
                        inscritoCounter++;
                    }
                } catch (Exception e) {
                    throw new Exception("Error importing student at line " + i + ": " + DSSException.getStackTrace(e));
                }
            }

            System.out.println("Imported a total of " + alunoCounter + " students with success.");
            System.out.println("Imported a total of " + inscritoCounter + " enrolled students on UC with success.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private Map<String, String> matchHeaderAndPayload(String[] header, String[] parts) {
        if (parts.length != header.length) {
            throw new IllegalArgumentException("Invalid line: Number of fields does not match header.");
        }

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            map.put(header[i], parts[i]);
        }

        return map;
    }

    private void adicionarAluno(int num, String nome, boolean estatuto) throws Exception {
        alunoFacade.importarAluno(num, nome, estatuto);
    }

    private void adicionarInscritoUC(String codigo, int num) throws Exception {
        ucFacade.adicionarInscritoUC(codigo, num);
    }
    //endregion Importar Alunos

    //region Consultar Horario
    public List<Turno> consultarHorario(int numMec) {
        return ucFacade.obterHorario(numMec);
    }
    //endregion Consultar Horario

    //region Gerar Horario Automatico
    public void gerarHorario() {
        Map<Integer, Exception> unalocatedAlunos = new HashMap<>();
        int counter = 0;
        int total = 0;
        //region Alunos com Estatuto
        List<Integer> numAlunosComEstatuto = alunoFacade.obterNumAlunosComEstatuto();
        Map<Integer, List<String>> inscricoesAlunosComEstatuto = ucFacade.obterInscricoes(numAlunosComEstatuto);

        for (Map.Entry<Integer, List<String>> entry : inscricoesAlunosComEstatuto.entrySet()) {
            if (entry.getValue() == null) {
                unalocatedAlunos.put(entry.getKey(), new Exception("Could not retrieve enrollments for student " + entry.getKey()));
                counter++;
                continue;
            }

            if (entry.getValue().isEmpty()) {
                System.out.println("Student " + entry.getKey() + " does not have enrollments.");
            }
            total += entry.getValue().size();

            try {
                ucFacade.inscreverAlunoTurnos(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                unalocatedAlunos.put(entry.getKey(), e);
                counter++;
            }
        }
        //endregion Alunos com Estatuto

        //region Alunos sem Estatuto
        List<Integer> numAlunosSemEstatuto = alunoFacade.obterNumAlunosSemEstatuto();
        Map<Integer, List<String>> inscricoesAlunosSemEstatuto = ucFacade.obterInscricoes(numAlunosSemEstatuto);

        for (Map.Entry<Integer, List<String>> entry : inscricoesAlunosSemEstatuto.entrySet()) {
            if (entry.getValue() == null) {
                unalocatedAlunos.put(entry.getKey(), new Exception("Could not retrieve enrollments for student " + entry.getKey()));
                counter++;
                continue;
            }

            if (entry.getValue().isEmpty()) {
                System.out.println("Student " + entry.getKey() + " does not have enrollments.");
            }
            total += entry.getValue().size();

            try {
                ucFacade.inscreverAlunoTurnos(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                unalocatedAlunos.put(entry.getKey(), e);
                counter++;
            }
        }
        //endregion

//        if (!unalocatedAlunos.isEmpty()) {
//            System.out.println("The following students could not be allocated to any turno:");
//            for (Map.Entry<Integer, Exception> entry : unalocatedAlunos.entrySet()) {
//                System.out.println("  - " + entry.getKey() + ": " + DSSException.getStackTrace(entry.getValue()));
//            }
//        }

        System.out.println("Unallocated students: " + counter + "/" + total);
    }
    //endregion

    //region Alocar Aluno Manual
    public void alocarAluno(int num, String codigo, String id) throws Exception {
        if (!alunoFacade.contemAluno(num)) throw new Exception("Student " + num + " not found.");

        List<String> inscricoesAluno = ucFacade.obterInscricoes(List.of(num)).get(num);
        if (!inscricoesAluno.contains(codigo)) throw new Exception("Student " + num + " not enrolled on UC with code: " + codigo + ".");

        List<Turno> turnosUC = ucFacade.obterTurnos(codigo);
        if ((turnosUC.stream().noneMatch(t -> t.getId().equals(id)))) throw new Exception("UC " + codigo + " doesn't have a shift with id " + id + ".");

        Stream<Turno> turnosAluno = ucFacade.obterHorario(num).stream().filter(t -> t.getCodigoUC().equals(codigo));
        Turno turno = turnosUC.stream().filter(t -> Objects.equals(t.getId(), id)).findFirst().orElse(null);
        if (turno != null && turno.getOcupacao() >= turno.getCapacidadeMaxima()) throw new Exception("Shift " + id + " is already full.");
        if (turno != null && turnosAluno.anyMatch(t -> t.overlaps(turno))) throw new Exception("Shift " + id + " overlaps with another shift.");

        ucFacade.adicionarAlunoTurno(num, id, codigo);
    }
    //endregion
}
