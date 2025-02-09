package com.dss56.LN.UC;

import com.dss56.LN.UC.Turno.Turno;

import java.util.List;
import java.util.Map;

public interface IGesUCs {

    public boolean contemInscritoUC(String codigo, int numMec);

    public void adicionarInscritoUC(String codigo, int numMec) throws Exception;

    public List<Turno> obterHorario(int numMec);

    public Map<Integer, List<String>> obterInscricoes(List<Integer> numAlunos);

    public List<Turno> obterTurnos(String codigo);

    public void adicionarAlunoTurno(int num, String id, String codigo);

    public void inscreverAlunoTurnos(int numMec, List<String> codigos) throws Exception;
}