package com.grupo8.scada.threads;

import com.grupo8.scada.model.*;
import com.grupo8.scada.model.interfaces.Monitoravel;
import com.grupo8.scada.service.GerenciadorAlarmes;
import java.util.List;

public class MonitorProcesso implements Runnable {
    static final String MSG_FALHA = "Equipamento em falha";

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
                    verificarEstado(equipamento);
                    if (equipamento instanceof Monitoravel monitoravel) {
                        for (Leitura leitura : monitoravel.gerarLeituras()) verificarLeitura(equipamento, leitura);
                    }
                }
            } catch (RuntimeException e) {
                System.err.println("Erro no monitor de processo: " + e.getMessage());
            }
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void verificarEstado(Equipamento equipamento) {
        if (equipamento.getEstado() == EstadoEquipamento.FALHA) {
            gerenciador.registrar(equipamento.getId(), Severidade.CRITICO, MSG_FALHA);
        } else {
            gerenciador.resolver(equipamento.getId(), MSG_FALHA);
        }
    }

    private void verificarLeitura(Equipamento equipamento, Leitura leitura) {
        if (!leitura.foraDoLimite()) {
            gerenciador.resolver(leitura.getOrigem(), leitura.getGrandeza());
            return;
        }
        // A mensagem não leva o valor medido: assim ela é igual a cada ciclo e o alarme
        // não se repete enquanto a condição persistir.
        String mensagem = leitura.getValor() < leitura.getLimiteMinimo()
                ? "%s abaixo do mínimo (%.1f %s)".formatted(leitura.getGrandeza(), leitura.getLimiteMinimo(), leitura.getUnidade())
                : "%s acima do máximo (%.1f %s)".formatted(leitura.getGrandeza(), leitura.getLimiteMaximo(), leitura.getUnidade());
        Severidade severidade = equipamento instanceof Forno ? Severidade.CRITICO : Severidade.AVISO;
        gerenciador.registrar(leitura.getOrigem(), severidade, mensagem);
    }
}
