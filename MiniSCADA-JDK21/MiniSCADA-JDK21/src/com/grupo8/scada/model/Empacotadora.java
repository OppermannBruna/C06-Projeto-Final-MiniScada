package com.grupo8.scada.model;

import com.grupo8.scada.model.interfaces.Monitoravel;
import java.util.List;

public class Empacotadora extends Equipamento implements Monitoravel {
    private double estoqueFilme;
    private final double consumoPorCiclo;
    private final double estoqueMinimo;
    private long pacotesProduzidos;

    public Empacotadora(String id, String nome, double estoqueInicial,
                        double consumoPorCiclo, double estoqueMinimo) {
        super(id, nome);
        if (estoqueInicial < 0 || consumoPorCiclo <= 0 || estoqueMinimo < 0) {
            throw new IllegalArgumentException("Parâmetros da empacotadora inválidos.");
        }
        this.estoqueFilme = estoqueInicial;
        this.consumoPorCiclo = consumoPorCiclo;
        this.estoqueMinimo = estoqueMinimo;
    }

    @Override
    protected synchronized void executarCiclo() {
        if (estoqueFilme < consumoPorCiclo) {
            definirFalha();
            return;
        }
        estoqueFilme -= consumoPorCiclo;
        pacotesProduzidos++;
    }

    @Override
    public String getTipo() { return "EMPACOTADORA"; }

    @Override
    public synchronized List<Leitura> gerarLeituras() {
        return List.of(new Leitura(getId(), "Estoque de filme", estoqueFilme, "%",
                estoqueMinimo, 100));
    }

    public synchronized void reabastecer(double quantidade) {
        if (quantidade > 0 && Double.isFinite(quantidade)) {
            estoqueFilme = Math.min(100, estoqueFilme + quantidade);
            if (getEstado() == EstadoEquipamento.FALHA && estoqueFilme >= consumoPorCiclo) {
                rearmar();
            }
        }
    }

    public synchronized double getEstoqueFilme() { return estoqueFilme; }
    public synchronized long getPacotesProduzidos() { return pacotesProduzidos; }
    public double getEstoqueMinimo() { return estoqueMinimo; }
}
