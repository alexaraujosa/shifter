package com.dss56.LN;

import com.dss56.LN.UC.Turno.Turno;

import java.util.List;

public interface IGeradorHAppLN {
    public boolean autenticarAluno(int numMec);

    public boolean autenticarDiretor(String password);

    public boolean importarAlunos(String filePath);

    public List<Turno> consultarHorario(int numMec);

    public void gerarHorario();

    public void alocarAluno(int num, String codigo, String id) throws Exception;
}
