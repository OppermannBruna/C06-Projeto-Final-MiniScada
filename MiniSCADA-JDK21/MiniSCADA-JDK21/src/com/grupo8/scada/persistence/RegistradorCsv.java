package com.grupo8.scada.persistence;

import com.grupo8.scada.model.Alarme;
import com.grupo8.scada.model.Leitura;
import com.grupo8.scada.exceptions.ScadaException;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class RegistradorCsv {
    private final Path historico;
    private final Path alarmes;

    public RegistradorCsv(Path historico, Path alarmes) {
        this.historico = historico;
        this.alarmes = alarmes;
    }

    public synchronized void registrarLeituras(List<Leitura> leituras) throws ScadaException {
        for (Leitura l : leituras) {
            append(historico, "%s;%s;%s;%.3f;%s".formatted(
                    l.getInstante(), l.getOrigem(), l.getGrandeza(), l.getValor(), l.getUnidade()));
        }
    }

    public synchronized void registrarAlarmes(List<Alarme> lista) throws ScadaException {
        for (Alarme a : lista) {
            append(alarmes, "%s;%s;%s;%s".formatted(
                    a.getInstante(), a.getOrigem(), a.getSeveridade(), a.getMensagem()));
        }
    }

    private void append(Path arquivo, String linha) throws ScadaException {
        try {
            Path parent = arquivo.getParent();
            if (parent != null) Files.createDirectories(parent);
            boolean novo = !Files.exists(arquivo);
            if (novo) {
                String cabecalho = arquivo.equals(historico)
                        ? "instante;origem;grandeza;valor;unidade"
                        : "instante;origem;severidade;mensagem";
                Files.writeString(arquivo, cabecalho + System.lineSeparator(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            Files.writeString(arquivo, linha.replace(';', ',') + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ScadaException("Erro ao gravar arquivo " + arquivo, e);
        }
    }
}
