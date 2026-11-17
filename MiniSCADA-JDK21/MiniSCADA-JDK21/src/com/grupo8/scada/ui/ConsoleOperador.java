package com.grupo8.scada.ui;

import com.grupo8.scada.exceptions.SetpointInvalidoException;
import com.grupo8.scada.model.*;
import com.grupo8.scada.model.interfaces.Ajustavel;
import com.grupo8.scada.model.interfaces.Manutenivel;
import com.grupo8.scada.service.Supervisorio;
import java.util.Scanner;

public class ConsoleOperador {
    private final Supervisorio supervisorio;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleOperador(Supervisorio supervisorio) {
        this.supervisorio = supervisorio;
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcao = scanner.nextLine().trim();
            try {
                switch (opcao) {
                    case "1" -> supervisorio.iniciar();
                    case "2" -> supervisorio.desligarSistema();
                    case "3" -> listarEquipamentos();
                    case "4" -> ajustarSetpoint();
                    case "5" -> mostrarAlarmes();
                    case "6" -> manutencao();
                    case "7" -> reabastecerEmpacotadora();
                    case "8" -> {
                        supervisorio.pararEmergencia();
                        System.out.println("PARADA DE EMERGÊNCIA EXECUTADA.");
                    }
                    case "0" -> {
                        supervisorio.desligarSistema();
                        continuar = false;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            } catch (SetpointInvalidoException e) {
                System.out.println("Não foi possível ajustar: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Erro na operação: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n========== MINI-SCADA | FÁBRICA DE BISCOITOS ==========");
        System.out.println("1 - Iniciar planta");
        System.out.println("2 - Desligar planta");
        System.out.println("3 - Listar equipamentos");
        System.out.println("4 - Ajustar setpoint");
        System.out.println("5 - Consultar alarmes ativos");
        System.out.println("6 - Realizar manutenção do paletizador");
        System.out.println("7 - Reabastecer empacotadora");
        System.out.println("8 - PARADA DE EMERGÊNCIA");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private void listarEquipamentos() {
        System.out.println("\n--- EQUIPAMENTOS ---");
        for (Equipamento e : supervisorio.getEquipamentos()) {
            System.out.println(e);
            if (e instanceof Forno f)
                System.out.printf("  Temperatura: %.1f °C | Setpoint: %.1f °C | Resistência: %s%n",
                        f.getTemperatura(), f.getSetpoint(), f.isResistenciaLigada() ? "ligada" : "desligada");
            if (e instanceof Esteira x)
                System.out.printf("  Velocidade: %.2f m/s | Biscoitos transportados: %d%n",
                        x.getSetpoint(), x.getBiscoitosTransportados());
            if (e instanceof Empacotadora x)
                System.out.printf("  Filme: %.1f%% | Pacotes: %d%n", x.getEstoqueFilme(), x.getPacotesProduzidos());
            if (e instanceof Paletizador x)
                System.out.printf("  Paletes concluídos: %d | Caixas no palete: %d%n",
                        x.getPaletesConcluidos(), x.getCaixasNoPalete());
        }
    }

    private void ajustarSetpoint() throws SetpointInvalidoException {
        System.out.print("ID do equipamento: ");
        String id = scanner.nextLine().trim();
        Equipamento e = supervisorio.buscar(id);
        if (e == null) {
            System.out.println("Equipamento não encontrado.");
            return;
        }
        if (!(e instanceof Ajustavel ajustavel)) {
            System.out.println("Este equipamento não permite ajuste de setpoint.");
            return;
        }
        System.out.print("Novo setpoint: ");
        double valor = Double.parseDouble(scanner.nextLine().replace(',', '.'));
        ajustavel.ajustarSetpoint(valor);
        System.out.println("Setpoint atualizado.");
    }

    private void mostrarAlarmes() {
        var alarmes = supervisorio.getGerenciadorAlarmes().ativos();
        if (alarmes.isEmpty()) System.out.println("Nenhum alarme ativo.");
        else alarmes.forEach(System.out::println);
    }

    private void manutencao() {
        System.out.print("ID do paletizador: ");
        String id = scanner.nextLine().trim();
        Equipamento e = supervisorio.buscar(id);
        if (e instanceof Manutenivel m) {
            m.realizarManutencao();
            System.out.println("Manutenção concluída. Equipamento liberado para operação.");
        } else {
            System.out.println("Equipamento não encontrado ou não requer manutenção manual.");
        }
    }

    private void reabastecerEmpacotadora() {
        System.out.print("ID da empacotadora: ");
        Equipamento e = supervisorio.buscar(scanner.nextLine().trim());
        if (e instanceof Empacotadora x) {
            System.out.print("Quantidade de filme (%): ");
            x.reabastecer(Double.parseDouble(scanner.nextLine().replace(',', '.')));
            System.out.println("Estoque atualizado.");
        } else {
            System.out.println("Empacotadora não encontrada.");
        }
    }
}
