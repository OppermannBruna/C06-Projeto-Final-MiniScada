package com.grupo8.scada.threads;

import com.grupo8.scada.exceptions.ScadaException;
import com.grupo8.scada.model.*;
import com.grupo8.scada.model.interfaces.Monitoravel;
import com.grupo8.scada.persistence.RegistradorCsv;
import com.grupo8.scada.service.GerenciadorAlarmes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class Historiador implements Runnable {
    private final List<Equipamento> equipamentos;
    private final RegistradorCsv registrador;
    private final GerenciadorAlarmes gerenciador;
    private final long intervaloMs;
    /** Alarmes que já foram gravados, para não repeti-los a cada ciclo. */
    private final Set<Alarme> jaRegistrados = Collections.newSetFromMap(new IdentityHashMap<>());

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

                List<Alarme> novos = gerenciador.todos().stream()
                        .filter(a -> !jaRegistrados.contains(a)).toList();
                registrador.registrarAlarmes(novos);
                jaRegistrados.addAll(novos);
            } catch (ScadaException e) {
                System.err.println("Erro no historiador: " + e.getMessage());
            } catch (RuntimeException e) {
                System.err.println("Erro inesperado no historiador: " + e.getMessage());
            }
            // O sleep fica fora do try acima: um erro persistente de disco não vira loop sem pausa.
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
