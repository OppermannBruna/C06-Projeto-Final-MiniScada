package com.grupo8.scada.model;

import com.grupo8.scada.exceptions.SetpointInvalidoException;
import com.grupo8.scada.model.interfaces.Ajustavel;
import com.grupo8.scada.model.interfaces.Monitoravel;
import java.util.List;

public class Esteira extends Equipamento implements Ajustavel, Monitoravel {
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
        // Velocidade zero = esteira parada = nada transportado.
        biscoitosTransportados += Math.round(velocidade * 10);
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

    @Override
    public synchronized List<Leitura> gerarLeituras() {
        return List.of(new Leitura(getId(), "Velocidade", velocidade, "m/s", 0, velocidadeMaxima));
    }

    public synchronized long getBiscoitosTransportados() { return biscoitosTransportados; }
    public double getVelocidadeMaxima() { return velocidadeMaxima; }
}
