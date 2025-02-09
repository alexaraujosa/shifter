package com.dss56.LN.UC.Turno;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class TurnoTeorico extends Turno {
    public TurnoTeorico(String id, DayOfWeek dia, LocalTime inicio, LocalTime fim, Tipo tipo, String codigoUC, int capacidadeMaxima) {
        super(id, dia, inicio, fim, tipo, codigoUC, capacidadeMaxima);
    }
}
