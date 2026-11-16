package com.grupo8.scada.threads;

import com.grupo8.scada.model.*;
import com.grupo8.scada.model.interfaces.Monitoravel;
import com.grupo8.scada.service.GerenciadorAlarmes;
import java.util.List;

public class MonitorProcesso implements Runnable {
    private final List<Equipamento> equipamentos;
    private final GerenciadorAlarmes gerenciador;
    private final long intervaloMs;

    public MonitorProcesso(List<Equipamento> equipamentos, GerenciadorAlarmes gerenciador, long intervaloMs) {
        this.equipamentos = equipamentos;
        this.gerenciador = gerenciador;
        this.intervaloMs = intervaloMs;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (Equipamento equipamento : equipamentos) {
                    if (equipamento.getEstado() == EstadoEquipamento.FALHA) {
                        gerenciador.registrar(equipamento.getId(), Severidade.CRITICO,
                                "Equipamento em falha.");
                    }
                    if (equipamento instanceof Monitoravel monitoravel) {
                        for (Leitura leitura : monitoravel.gerarLeituras()) {
                            if (leitura.foraDoLimite()) {
                                Severidade severidade = equipamento instanceof Forno
                                        ? Severidade.CRITICO : Severidade.AVISO;
                                gerenciador.registrar(leitura.getOrigem(), severidade,
                                        leitura.getGrandeza() + " fora dos limites: " + leitura.getValor()
                                                + " " + leitura.getUnidade());
                            } else {
                                gerenciador.resolverAlarmesDaOrigem(leitura.getOrigem());
                            }
                        }
                    }
                }
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException e) {
                System.err.println("Erro no monitor de processo: " + e.getMessage());
            }
        }
    }
}
