package com.grupo8.scada.model;

import com.grupo8.scada.exceptions.SetpointInvalidoException;
import com.grupo8.scada.model.interfaces.Ajustavel;
import com.grupo8.scada.model.interfaces.Monitoravel;
import java.util.List;

public class Forno extends Equipamento implements Ajustavel, Monitoravel {
    private static final double AQUECIMENTO_POR_CICLO = 2.0;
    private static final double RESFRIAMENTO_POR_CICLO = 0.7;
    private static final double TEMPERATURA_AMBIENTE = 20.0;

    private double temperatura;
    private double setpoint;
    private final double histerese;
    private final double limiteSeguranca;
    private boolean resistenciaLigada;

    public Forno(String id, String nome, double temperaturaInicial,
                 double setpoint, double histerese, double limiteSeguranca) {
        super(id, nome);
        // O setpoint mais a histerese precisa caber abaixo do limite de segurança,
        // senão o próprio controle on/off levaria o forno à falha.
        if (histerese < 0 || limiteSeguranca <= 0 || setpoint <= 0 || setpoint + histerese >= limiteSeguranca) {
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

        if (resistenciaLigada) temperatura += AQUECIMENTO_POR_CICLO;
        else temperatura -= RESFRIAMENTO_POR_CICLO;

        temperatura = Math.max(TEMPERATURA_AMBIENTE, temperatura);
        if (temperatura >= limiteSeguranca) definirFalha();
    }

    /** Em falha a resistência é desligada e o forno esfria até poder ser rearmado. */
    @Override
    protected synchronized void aoManterFalha() {
        resistenciaLigada = false;
        temperatura = Math.max(TEMPERATURA_AMBIENTE, temperatura - RESFRIAMENTO_POR_CICLO);
    }

    @Override
    protected synchronized boolean podeRearmar() {
        return temperatura < limiteSeguranca;
    }

    @Override
    public String getTipo() { return "FORNO"; }

    @Override
    public synchronized void ajustarSetpoint(double valor) throws SetpointInvalidoException {
        if (!Double.isFinite(valor) || valor <= 0 || valor + histerese >= limiteSeguranca) {
            throw new SetpointInvalidoException("Setpoint deve ser positivo e menor que "
                    + (limiteSeguranca - histerese) + " °C (limite de segurança "
                    + limiteSeguranca + " °C menos a histerese).");
        }
        setpoint = valor;
    }

    @Override
    public synchronized double getSetpoint() { return setpoint; }

    @Override
    public synchronized List<Leitura> gerarLeituras() {
        return List.of(new Leitura(getId(), "Temperatura", temperatura, "°C",
                TEMPERATURA_AMBIENTE, limiteSeguranca));
    }

    public synchronized double getTemperatura() { return temperatura; }
    public double getHisterese() { return histerese; }
    public double getLimiteSeguranca() { return limiteSeguranca; }
    public synchronized boolean isResistenciaLigada() { return resistenciaLigada; }
}
