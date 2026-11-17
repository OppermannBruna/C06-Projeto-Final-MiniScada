package com.grupo8.scada.service;

import com.grupo8.scada.model.*;
import com.grupo8.scada.model.interfaces.Manutenivel;
import com.grupo8.scada.persistence.RegistradorCsv;
import com.grupo8.scada.threads.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Supervisorio {
    private final List<Equipamento> equipamentos;
    private final GerenciadorAlarmes gerenciadorAlarmes;
    private final List<Thread> threadsEquipamentos = new ArrayList<>();
    private Thread threadMonitor;
    private Thread threadHistoriador;
    private volatile boolean iniciado;

    public Supervisorio(List<Equipamento> equipamentos, GerenciadorAlarmes gerenciadorAlarmes) {
        this.equipamentos = new CopyOnWriteArrayList<>(equipamentos);
        this.gerenciadorAlarmes = gerenciadorAlarmes;
    }

    public synchronized void iniciar() {
        if (iniciado) return;
        iniciado = true;
        for (Equipamento e : equipamentos) {
            e.ligar();
            Thread t = new Thread(new TarefaEquipamento(e, 500), "Equipamento-" + e.getId());
            threadsEquipamentos.add(t);
            t.start();
        }
        threadMonitor = new Thread(new MonitorProcesso(equipamentos, gerenciadorAlarmes, 500), "MonitorProcesso");
        threadMonitor.start();

        RegistradorCsv registrador = new RegistradorCsv(Path.of("historico.csv"), Path.of("alarmes.csv"));
        threadHistoriador = new Thread(new Historiador(equipamentos, registrador, gerenciadorAlarmes, 2000),
                "Historiador");
        threadHistoriador.start();
    }

    public synchronized void pararEmergencia() {
        for (Equipamento e : equipamentos) e.pararEmergencia();
        interromperThreads();
        iniciado = false;
    }

    public synchronized void desligarSistema() {
        for (Equipamento e : equipamentos) e.desligar();
        interromperThreads();
        iniciado = false;
    }

    private void interromperThreads() {
        threadsEquipamentos.forEach(Thread::interrupt);
        if (threadMonitor != null) threadMonitor.interrupt();
        if (threadHistoriador != null) threadHistoriador.interrupt();
        threadsEquipamentos.clear();
    }

    public Equipamento buscar(String id) {
        return equipamentos.stream().filter(e -> e.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public List<Equipamento> getEquipamentos() { return List.copyOf(equipamentos); }
    public GerenciadorAlarmes getGerenciadorAlarmes() { return gerenciadorAlarmes; }
    public boolean isIniciado() { return iniciado; }

    /** Conclui a manutenção; se a planta está em operação, o equipamento volta a operar. */
    public boolean realizarManutencao(String id) {
        if (buscar(id) instanceof Manutenivel m) {
            m.realizarManutencao();
            religarSeIniciado((Equipamento) m);
            return true;
        }
        return false;
    }

    public boolean reabastecerEmpacotadora(String id, double quantidade) {
        if (buscar(id) instanceof Empacotadora e) {
            e.reabastecer(quantidade);
            religarSeIniciado(e);
            return true;
        }
        return false;
    }

    /** Rearma um equipamento em falha. Retorna false se a causa persiste (ex.: forno ainda quente). */
    public boolean rearmar(String id) {
        Equipamento e = buscar(id);
        if (e == null) throw new IllegalArgumentException("Equipamento não encontrado.");
        boolean ok = e.rearmar();
        if (ok) religarSeIniciado(e);
        return ok;
    }

    private void religarSeIniciado(Equipamento e) {
        if (iniciado) e.ligar();
    }
}
