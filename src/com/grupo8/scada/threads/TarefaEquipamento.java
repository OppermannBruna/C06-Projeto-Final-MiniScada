package com.grupo8.scada.threads;

import com.grupo8.scada.exceptions.EquipamentoEmFalhaException;
import com.grupo8.scada.model.Equipamento;

public class TarefaEquipamento implements Runnable {
    private final Equipamento equipamento;
    private final long intervaloMs;

    public TarefaEquipamento(Equipamento equipamento, long intervaloMs) {
        this.equipamento = equipamento;
        this.intervaloMs = intervaloMs;
    }

    /**
     * A thread continua viva mesmo com o equipamento em falha: assim, depois de rearmado
     * (ou de uma manutenção), ele volta a operar sem precisar reiniciar a planta.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                equipamento.ciclo();
            } catch (EquipamentoEmFalhaException e) {
                // equipamento já está em FALHA; apenas aguarda o rearme
            } catch (RuntimeException e) {
                System.err.println("Erro inesperado em " + equipamento.getId() + ": " + e.getMessage());
                equipamento.definirFalha();
            }
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
