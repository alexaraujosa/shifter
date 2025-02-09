package com.dss56.LN.UC.Turno;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

abstract public class Turno {
    protected String id;
    protected DayOfWeek dia;
    protected LocalTime inicio;
    protected LocalTime fim;
    protected Tipo tipo;
    protected String codigoUC;
    protected int capacidadeMaxima;
    protected int ocupacao;
    protected List<String> inscritos;

    public Turno(String id, DayOfWeek dia, LocalTime inicio, LocalTime fim, Tipo tipo, String codigoUC, int capacidadeMaxima) {
        this.id = id;
        this.dia = dia;
        this.inicio = inicio;
        this.fim = fim;
        this.tipo = tipo;
        this.codigoUC = codigoUC;
        this.capacidadeMaxima = capacidadeMaxima;
        this.ocupacao = 0;
        this.inscritos = null; // TODO: Dielete?
    }

    public String getId() {
        return id;
    }

    public String getCodigoUC() {
        return codigoUC;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public int getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(int ocupacao) {
        this.ocupacao = ocupacao;
    }

    public boolean overlaps(Turno t) {
        if (this.dia != t.dia) return false;
        if (this.fim.equals(t.inicio) || this.inicio.equals(t.fim)) return false;
        if (this.fim.isBefore(t.inicio) || this.inicio.isAfter(t.fim)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "id='" + id + '\'' +
                ", dia=" + dia +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", tipo=" + tipo +
                ", uc=" + codigoUC +
                ", capacidadeMaxima=" + capacidadeMaxima +
                ", ocupacao=" + ocupacao +
                '}';
    }

    public static enum Tipo {
        TEORICO(1),
        PRATICO(2),
        LABORATORIAL(3);

        private final int value;

        private Tipo(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Tipo of(int value) {
            for (Tipo tipo : Tipo.values()) {
                if (tipo.getValue() == value) {
                    return tipo;
                }
            }
            return null;
        }
    }
}
