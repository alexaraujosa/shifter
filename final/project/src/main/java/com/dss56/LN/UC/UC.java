package com.dss56.LN.UC;

import com.dss56.LN.UC.Turno.Turno;

import java.util.ArrayList;
import java.util.List;

public class UC {
    private String codigo;
    private String nome;
    private int ano;
    private List<String> inscritos;
    private List<Turno> turnos;

    public UC(String codigo, String nome, int ano, List<String> inscritos, List<Turno> turnos) {
        this.codigo = codigo;
        this.nome = nome;
        this.ano = ano;
        this.inscritos = inscritos;
    }

}