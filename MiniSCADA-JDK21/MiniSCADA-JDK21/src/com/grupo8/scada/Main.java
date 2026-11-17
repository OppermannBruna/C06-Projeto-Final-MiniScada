package com.grupo8.scada;

import com.grupo8.scada.model.*;
import com.grupo8.scada.persistence.RepositorioEquipamentosCsv;
import com.grupo8.scada.service.GerenciadorAlarmes;
import com.grupo8.scada.service.Supervisorio;
import com.grupo8.scada.ui.ConsoleOperador;
import com.grupo8.scada.exceptions.ScadaException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            RepositorioEquipamentosCsv repositorio =
                    new RepositorioEquipamentosCsv(Path.of("config", "planta.csv"));
            List<Equipamento> equipamentos = repositorio.carregar();

            if (equipamentos.isEmpty()) {
                equipamentos = criarPlantaPadrao();
                repositorio.salvar(equipamentos);
                System.out.println("Configuração padrão criada em config/planta.csv.");
            }

            Supervisorio supervisorio = new Supervisorio(equipamentos, new GerenciadorAlarmes());
            new ConsoleOperador(supervisorio).executar();

        } catch (ScadaException e) {
            System.err.println("Falha ao carregar o Mini-SCADA: " + e.getMessage());
        }
    }

    private static List<Equipamento> criarPlantaPadrao() {
        List<Equipamento> lista = new ArrayList<>();
        lista.add(new Forno("F01", "Forno principal", 160, 180, 3, 230));
        lista.add(new Esteira("E01", "Esteira de transporte", 1.2, 3.0));
        lista.add(new Empacotadora("P01", "Empacotadora automática", 80, 0.8, 10));
        lista.add(new Paletizador("L01", "Paletizador", 100));
        return lista;
    }
}
