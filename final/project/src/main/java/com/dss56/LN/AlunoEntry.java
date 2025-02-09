package com.dss56.LN;

import java.util.Map;

public record AlunoEntry(int numMec, String nome, boolean estatuto, String codigoUC) {
    private static final String ALUNO_NUMMEC = "Nº Mecanográfico";
    private static final String ALUNO_NOME = "Nome";
    private static final String ALUNO_ESTATUTO = "Regimes especiais de frequência";
    private static final String UC_CODIGO = "Código da UC";

    public static AlunoEntry parseAlunoEntry(Map<String, String> parts) {
        if (!parts.containsKey(ALUNO_NUMMEC)) {
            throw new IllegalArgumentException("Invalid line: Missing field: " + ALUNO_NUMMEC + ".");
        }
        if (!parts.containsKey(ALUNO_NOME)) {
            throw new IllegalArgumentException("Invalid line: Missing field: " + ALUNO_NOME + ".");
        }
        if (!parts.containsKey(ALUNO_ESTATUTO)) {
            throw new IllegalArgumentException("Invalid line: Missing field: " + ALUNO_ESTATUTO + ".");
        }
        if (!parts.containsKey(UC_CODIGO)) {
            throw new IllegalArgumentException("Invalid line: Missing field: " + UC_CODIGO + ".");
        }

        return new AlunoEntry(
            Integer.parseInt(parts.get(ALUNO_NUMMEC)),
            parts.get(ALUNO_NOME),
            !parts.get(ALUNO_ESTATUTO).isEmpty(),
            parts.get(UC_CODIGO)
        );
    }
}
