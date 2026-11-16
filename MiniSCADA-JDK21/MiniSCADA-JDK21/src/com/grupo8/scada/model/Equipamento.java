package com.grupo8.scada.model;

import com.grupo8.scada.exceptions.EquipamentoEmFalhaException;

public abstract class Equipamento {
    private final String id;
    private final String nome;
    private EstadoEquipamento estado;
    private long ciclosExecutados;

    protected Equipamento(String id, String nome) {
        if (id == null || id.isBlank() || nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("ID e nome do equipamento são obrigatórios.");
        }
        this.id = id;
        this.nome = nome;
        this.estado = EstadoEquipamento.PARADO;
        this.ciclosExecutados = 0;
    }

    public synchronized void ligar() {
        if (estado == EstadoEquipamento.PARADO) {
            estado = EstadoEquipamento.OPERANDO;
        }
    }

    public synchronized void desligar() {
        if (estado == EstadoEquipamento.OPERANDO) {
            estado = EstadoEquipamento.PARADO;
        }
    }

    public synchronized void pararEmergencia() {
        if (estado == EstadoEquipamento.OPERANDO) {
            estado = EstadoEquipamento.PARADO;
        }
    }

    public synchronized void definirFalha() {
        estado = EstadoEquipamento.FALHA;
    }

    public synchronized void rearmar() {
        if (estado == EstadoEquipamento.FALHA) {
            estado = EstadoEquipamento.PARADO;
        }
    }

    public synchronized void iniciarManutencao() {
        estado = EstadoEquipamento.MANUTENCAO;
    }

    public synchronized void concluirManutencao() {
        if (estado == EstadoEquipamento.MANUTENCAO) {
            estado = EstadoEquipamento.PARADO;
        }
    }

    public final synchronized void ciclo() throws EquipamentoEmFalhaException {
        if (estado == EstadoEquipamento.FALHA) {
            throw new EquipamentoEmFalhaException("Equipamento " + id + " está em falha.");
        }
        if (estado != EstadoEquipamento.OPERANDO) {
            return;
        }
        executarCiclo();
        ciclosExecutados++;
    }

    protected abstract void executarCiclo();
    public abstract String getTipo();

    public String getId() { return id; }
    public String getNome() { return nome; }
    public synchronized EstadoEquipamento getEstado() { return estado; }
    public synchronized long getCiclosExecutados() { return ciclosExecutados; }

    @Override
    public String toString() {
        return "%s | %s | %s | estado=%s | ciclos=%d"
                .formatted(id, nome, getTipo(), getEstado(), getCiclosExecutados());
    }
}
