package com.dss56.LN.Aluno;

import java.util.List;

public interface IGesAlunos {

    public boolean contemAluno(int numMec);

    public void importarAluno(int numMec, String nome, boolean estatuto) throws Exception;

    public List<Integer> obterNumAlunosComEstatuto();

    public List<Integer> obterNumAlunosSemEstatuto();
}
