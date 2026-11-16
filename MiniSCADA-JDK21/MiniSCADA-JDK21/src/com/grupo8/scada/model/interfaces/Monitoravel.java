package com.grupo8.scada.model.interfaces;

import com.grupo8.scada.model.Leitura;
import java.util.List;

public interface Monitoravel {
    List<Leitura> gerarLeituras();
}
