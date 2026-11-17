package com.grupo8.scada.persistence;

import com.grupo8.scada.exceptions.ScadaException;
import com.grupo8.scada.model.Alarme;
import com.grupo8.scada.model.Leitura;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Locale;

public class RegistradorCsv {
    private static final String CABECALHO_HISTORICO = "instante;origem;grandeza;valor;unidade";
    private static final String CABECALHO_ALARMES = "instante;origem;severidade;mensagem";

    private final Path historico;
    private final Path alarmes;

    public RegistradorCsv(Path historico, Path alarmes) {
        this.historico = historico;
        this.alarmes = alarmes;
    }

    public synchronized void registrarLeituras(List<Leitura> leituras) throws ScadaException {
        for (Leitura l : leituras) {
            // Locale.ROOT garante ponto decimal, independente do idioma do sistema.
            String linha = String.join(";",
                    l.getInstante().toString(), campo(l.getOrigem()), campo(l.getGrandeza()),
                    String.format(Locale.ROOT, "%.3f", l.getValor()), campo(l.getUnidade()));
            append(historico, CABECALHO_HISTORICO, linha);
        }
    }

    public synchronized void registrarAlarmes(List<Alarme> lista) throws ScadaException {
        for (Alarme a : lista) {
            String linha = String.join(";",
                    a.getInstante().toString(), campo(a.getOrigem()),
                    a.getSeveridade().toString(), campo(a.getMensagem()));
            append(alarmes, CABECALHO_ALARMES, linha);
        }
    }

    /** Remove o separador de dentro do texto, preservando o ';' entre as colunas. */
    private static String campo(String texto) {
        return texto == null ? "" : texto.replace(';', ',');
    }

    private void append(Path arquivo, String cabecalho, String linha) throws ScadaException {
        try {
            Path parent = arquivo.getParent();
            if (parent != null) Files.createDirectories(parent);
            if (!Files.exists(arquivo)) {
                Files.writeString(arquivo, cabecalho + System.lineSeparator(), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            Files.writeString(arquivo, linha + System.lineSeparator(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ScadaException("Erro ao gravar arquivo " + arquivo, e);
        }
    }
}
