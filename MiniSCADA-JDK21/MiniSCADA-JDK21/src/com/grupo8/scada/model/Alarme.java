package com.grupo8.scada.model;

import java.time.LocalDateTime;

public class Alarme {
    private final String origem;
    private final Severidade severidade;
    private final String mensagem;
    private final LocalDateTime instante;
    private boolean ativo;

    public Alarme(String origem, Severidade severidade, String mensagem) {
        this.origem = origem;
        this.severidade = severidade;
        this.mensagem = mensagem;
        this.instante = LocalDateTime.now();
        this.ativo = true;
    }

    public String getOrigem() { return origem; }
    public Severidade getSeveridade() { return severidade; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getInstante() { return instante; }
    public synchronized boolean isAtivo() { return ativo; }
    public synchronized void resolver() { ativo = false; }

    @Override
    public String toString() {
        return "[%s] %s - %s (%s)".formatted(severidade, origem, mensagem, instante);
    }
}
