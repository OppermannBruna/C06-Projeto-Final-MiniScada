package com.grupo8.scada.model;

import com.grupo8.scada.exceptions.SetpointInvalidoException;
import com.grupo8.scada.model.interfaces.Ajustavel;

public class Esteira extends Equipamento implements Ajustavel {
    private double velocidade;
    private final double velocidadeMaxima;
    private long biscoitosTransportados;

    public Esteira(String id, String nome, double velocidadeInicial, double velocidadeMaxima) {
        super(id, nome);
        if (velocidadeMaxima <= 0 || velocidadeInicial < 0 || velocidadeInicial > velocidadeMaxima) {
            throw new IllegalArgumentException("Parâmetros de velocidade inválidos.");
        }
        this.velocidade = velocidadeInicial;
        this.velocidadeMaxima = velocidadeMaxima;
    }

    @Override
    protected synchronized void executarCiclo() {
        biscoitosTransportados += Math.max(1, Math.round(velocidade * 10));
    }

    @Override
    public String getTipo() { return "ESTEIRA"; }

    @Override
    public synchronized void ajustarSetpoint(double valor) throws SetpointInvalidoException {
        if (!Double.isFinite(valor) || valor < 0 || valor > velocidadeMaxima) {
            throw new SetpointInvalidoException("Velocidade deve estar entre 0 e " + velocidadeMaxima + " m/s.");
        }
        velocidade = valor;
    }

    @Override
    public synchronized double getSetpoint() { return velocidade; }

    public synchronized long getBiscoitosTransportados() { return biscoitosTransportados; }
    public double getVelocidadeMaxima() { return velocidadeMaxima; }
}
