package com.grupo8.scada.model.interfaces;

import com.grupo8.scada.exceptions.SetpointInvalidoException;

public interface Ajustavel {
    void ajustarSetpoint(double valor) throws SetpointInvalidoException;
    double getSetpoint();
}
