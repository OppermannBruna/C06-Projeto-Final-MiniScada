package com.grupo8.scada.threads;

import com.grupo8.scada.model.Equipamento;
import com.grupo8.scada.exceptions.EquipamentoEmFalhaException;

public class TarefaEquipamento implements Runnable {
    private final Equipamento equipamento;
    private final long intervaloMs;

    public TarefaEquipamento(Equipamento equipamento, long intervaloMs) {
        this.equipamento = equipamento;
        this.intervaloMs = intervaloMs;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                equipamento.ciclo();
                Thread.sleep(intervaloMs);
            } catch (EquipamentoEmFalhaException e) {
                equipamento.definirFalha();
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException e) {
                equipamento.definirFalha();
                break;
            }
        }
    }
}
