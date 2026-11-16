package com.grupo8.scada.model;

import com.grupo8.scada.model.interfaces.Manutenivel;

public class Paletizador extends Equipamento implements Manutenivel {
    private final long limiteCiclosManutencao;
    private long ciclosDesdeManutencao;
    private int caixasNoPalete;
    private long paletesConcluidos;

    public Paletizador(String id, String nome, long limiteCiclosManutencao) {
        super(id, nome);
        if (limiteCiclosManutencao <= 0) throw new IllegalArgumentException("Limite de manutenção inválido.");
        this.limiteCiclosManutencao = limiteCiclosManutencao;
    }

    @Override
    protected synchronized void executarCiclo() {
        caixasNoPalete++;
        ciclosDesdeManutencao++;
        if (caixasNoPalete >= 20) {
            caixasNoPalete = 0;
            paletesConcluidos++;
        }
        if (precisaManutencao()) iniciarManutencao();
    }

    @Override
    public String getTipo() { return "PALETIZADOR"; }

    @Override
    public synchronized boolean precisaManutencao() {
        return ciclosDesdeManutencao >= limiteCiclosManutencao;
    }

    @Override
    public synchronized void realizarManutencao() {
        ciclosDesdeManutencao = 0;
        concluirManutencao();
    }

    public synchronized int getCaixasNoPalete() { return caixasNoPalete; }
    public synchronized long getPaletesConcluidos() { return paletesConcluidos; }
    public long getLimiteCiclosManutencao() { return limiteCiclosManutencao; }
}
