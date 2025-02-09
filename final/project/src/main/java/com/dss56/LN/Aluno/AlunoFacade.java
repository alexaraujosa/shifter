package com.dss56.LN.Aluno;

import com.dss56.DL.AlunoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoFacade implements IGesAlunos {
    private final AlunoDAO alunos;

    public AlunoFacade() throws SQLException {
        this.alunos = AlunoDAO.getInstance();
    }

    public boolean contemAluno(int numMec) {
        try {
            return alunos.existeAluno(numMec);
        } catch (Exception e) {
            return false;
        }
    }

    public void importarAluno(int numMec, String nome, boolean estatuto) throws Exception {
        alunos.adicionarAluno(numMec, nome, estatuto);
    }

    public List<Integer> obterNumAlunosComEstatuto() {
        try {
            return alunos.obterNumAlunosEstatuto(1);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Integer> obterNumAlunosSemEstatuto() {
        try {
            return alunos.obterNumAlunosEstatuto(0);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
