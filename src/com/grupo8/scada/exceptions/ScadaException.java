package com.grupo8.scada.exceptions;

public class ScadaException extends Exception {
    public ScadaException(String mensagem) {
        super(mensagem);
    }

    public ScadaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
