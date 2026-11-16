package com.grupo8.scada.model;

import com.grupo8.scada.exceptions.SetpointInvalidoException;
import com.grupo8.scada.model.interfaces.Ajustavel;
import com.grupo8.scada.model.interfaces.Monitoravel;
import java.util.List;

public class Forno extends Equipamento implements Ajustavel, Monitoravel {
    private double temperatura;
    private double setpoint;
    private final double histerese;
    private final double limiteSeguranca;
    private boolean resistenciaLigada;

    public Forno(String id, String nome, double temperaturaInicial,
                 double setpoint, double histerese, double limiteSeguranca) {
        super(id, nome);
        if (histerese < 0 || limiteSeguranca <= 0 || setpoint <= 0 || setpoint >= limiteSeguranca) {
            throw new IllegalArgumentException("Parâmetros do forno inválidos.");
        }
        this.temperatura = temperaturaInicial;
        this.setpoint = setpoint;
        this.histerese = histerese;
        this.limiteSeguranca = limiteSeguranca;
    }

    @Override
    protected synchronized void executarCiclo() {
        if (temperatura < setpoint - histerese) resistenciaLigada = true;
        if (temperatura > setpoint + histerese) resistenciaLigada = false;

        if (resistenciaLigada) temperatura += 2.0;
        else temperatura -= 0.7;

        temperatura = Math.max(20.0, temperatura);
        if (temperatura >= limiteSeguranca) definirFalha();
    }

    @Override
    public String getTipo() { return "FORNO"; }

    @Override
    public synchronized void ajustarSetpoint(double valor) throws SetpointInvalidoException {
        if (!Double.isFinite(valor) || valor <= 0 || valor >= limiteSeguranca) {
            throw new SetpointInvalidoException("Setpoint deve ser positivo e menor que " + limiteSeguranca + " °C.");
        }
        setpoint = valor;
    }

    @Override
    public synchronized double getSetpoint() { return setpoint; }

    @Override
    public synchronized List<Leitura> gerarLeituras() {
        return List.of(new Leitura(getId(), "Temperatura", temperatura, "°C", 20, limiteSeguranca));
    }

    public synchronized double getTemperatura() { return temperatura; }
    public double getLimiteSeguranca() { return limiteSeguranca; }
    public synchronized boolean isResistenciaLigada() { return resistenciaLigada; }
}
