package com.grupo8.scada.threads;

import com.grupo8.scada.model.*;
import com.grupo8.scada.model.interfaces.Monitoravel;
import com.grupo8.scada.persistence.RegistradorCsv;
import com.grupo8.scada.service.GerenciadorAlarmes;
import com.grupo8.scada.exceptions.ScadaException;
import java.util.ArrayList;
import java.util.List;

public class Historiador implements Runnable {
    private final List<Equipamento> equipamentos;
    private final RegistradorCsv registrador;
    private final GerenciadorAlarmes gerenciador;
    private final long intervaloMs;

    public Historiador(List<Equipamento> equipamentos, RegistradorCsv registrador,
                       GerenciadorAlarmes gerenciador, long intervaloMs) {
        this.equipamentos = equipamentos;
        this.registrador = registrador;
        this.gerenciador = gerenciador;
        this.intervaloMs = intervaloMs;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<Leitura> leituras = new ArrayList<>();
                for (Equipamento e : equipamentos) {
                    if (e instanceof Monitoravel monitoravel) leituras.addAll(monitoravel.gerarLeituras());
                }
                registrador.registrarLeituras(leituras);
                registrador.registrarAlarmes(gerenciador.todos());
                Thread.sleep(intervaloMs);
            } catch (ScadaException e) {
                System.err.println("Erro no historiador: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException e) {
                System.err.println("Erro inesperado no historiador: " + e.getMessage());
            }
        }
    }
}
