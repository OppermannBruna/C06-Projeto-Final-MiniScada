package com.grupo8.scada.service;

import com.grupo8.scada.model.Alarme;
import com.grupo8.scada.model.Severidade;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorAlarmes {
    private final List<Alarme> alarmes = new ArrayList<>();

    public synchronized void registrar(Alarme novo) {
        boolean jaExiste = alarmes.stream().anyMatch(a ->
                a.isAtivo() && a.getOrigem().equals(novo.getOrigem())
                        && a.getMensagem().equals(novo.getMensagem()));
        if (!jaExiste) alarmes.add(novo);
    }

    public synchronized List<Alarme> ativos() {
        return alarmes.stream().filter(Alarme::isAtivo).toList();
    }

    public synchronized List<Alarme> todos() {
        return List.copyOf(alarmes);
    }

    /** Resolve só os alarmes da origem cuja mensagem começa com o prefixo (ex.: a grandeza). */
    public synchronized void resolver(String origem, String prefixoMensagem) {
        alarmes.stream()
                .filter(a -> a.getOrigem().equals(origem) && a.getMensagem().startsWith(prefixoMensagem))
                .forEach(Alarme::resolver);
    }

    public synchronized void registrar(String origem, Severidade severidade, String mensagem) {
        registrar(new Alarme(origem, severidade, mensagem));
    }
}
