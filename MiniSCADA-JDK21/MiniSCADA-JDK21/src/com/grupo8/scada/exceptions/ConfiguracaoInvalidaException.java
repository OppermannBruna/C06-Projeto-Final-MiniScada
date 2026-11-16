package com.grupo8.scada.exceptions;

public class ConfiguracaoInvalidaException extends ScadaException {
    public ConfiguracaoInvalidaException(String mensagem) {
        super(mensagem);
    }

    public ConfiguracaoInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
