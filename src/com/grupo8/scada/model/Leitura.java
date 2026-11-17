package com.grupo8.scada.model;

import java.time.LocalDateTime;

public class Leitura {
    private final String origem;
    private final String grandeza;
    private final double valor;
    private final String unidade;
    private final double limiteMinimo;
    private final double limiteMaximo;
    private final LocalDateTime instante;

    public Leitura(String origem, String grandeza, double valor, String unidade,
                   double limiteMinimo, double limiteMaximo) {
        if (origem == null || origem.isBlank() || grandeza == null || grandeza.isBlank()) {
            throw new IllegalArgumentException("Origem e grandeza são obrigatórias.");
        }
        if (!Double.isFinite(valor)) {
            throw new IllegalArgumentException("Valor da leitura inválido.");
        }
        this.origem = origem;
        this.grandeza = grandeza;
        this.valor = valor;
        this.unidade = unidade == null ? "" : unidade;
        this.limiteMinimo = limiteMinimo;
        this.limiteMaximo = limiteMaximo;
        this.instante = LocalDateTime.now();
    }

    public String getOrigem() { return origem; }
    public String getGrandeza() { return grandeza; }
    public double getValor() { return valor; }
    public String getUnidade() { return unidade; }
    public double getLimiteMinimo() { return limiteMinimo; }
    public double getLimiteMaximo() { return limiteMaximo; }
    public LocalDateTime getInstante() { return instante; }

    public boolean foraDoLimite() {
        return valor < limiteMinimo || valor > limiteMaximo;
    }

    @Override
    public String toString() {
        return "%s: %.2f %s".formatted(grandeza, valor, unidade);
    }
}
