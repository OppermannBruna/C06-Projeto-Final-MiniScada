package com.grupo8.scada.persistence;

import com.grupo8.scada.exceptions.ScadaException;
import java.util.List;

public interface Repositorio<T> {
    List<T> carregar() throws ScadaException;
    void salvar(List<T> itens) throws ScadaException;
}
