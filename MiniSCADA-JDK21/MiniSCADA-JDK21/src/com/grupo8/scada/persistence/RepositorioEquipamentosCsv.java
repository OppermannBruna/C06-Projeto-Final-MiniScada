package com.grupo8.scada.persistence;

import com.grupo8.scada.exceptions.ConfiguracaoInvalidaException;
import com.grupo8.scada.exceptions.ScadaException;
import com.grupo8.scada.model.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEquipamentosCsv implements Repositorio<Equipamento> {
    private final Path arquivo;

    public RepositorioEquipamentosCsv(Path arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public List<Equipamento> carregar() throws ScadaException {
        List<Equipamento> equipamentos = new ArrayList<>();
        if (!Files.exists(arquivo)) return equipamentos;

        try {
            List<String> linhas = Files.readAllLines(arquivo);
            for (int i = 0; i < linhas.size(); i++) {
                String linha = linhas.get(i).trim();
                if (linha.isEmpty() || linha.startsWith("#") || linha.toLowerCase().startsWith("id;")) continue;
                String[] c = linha.split(";");
                try {
                    if (c.length < 3) throw new IllegalArgumentException("Campos insuficientes.");
                    String id = c[0].trim();
                    String tipo = c[1].trim().toUpperCase();
                    String nome = c[2].trim();
                    Equipamento e = switch (tipo) {
                        case "ESTEIRA" -> new Esteira(id, nome, Double.parseDouble(c[3]), Double.parseDouble(c[4]));
                        case "FORNO" -> new Forno(id, nome, Double.parseDouble(c[3]), Double.parseDouble(c[4]),
                                Double.parseDouble(c[5]), Double.parseDouble(c[6]));
                        case "PALETIZADOR" -> new Paletizador(id, nome, Long.parseLong(c[3]));
                        case "EMPACOTADORA" -> new Empacotadora(id, nome, Double.parseDouble(c[3]),
                                Double.parseDouble(c[4]), Double.parseDouble(c[5]));
                        default -> throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
                    };
                    equipamentos.add(e);
                } catch (RuntimeException ex) {
                    throw new ConfiguracaoInvalidaException("Erro na linha " + (i + 1) + " de " + arquivo + ": " + ex.getMessage(), ex);
                }
            }
            return equipamentos;
        } catch (IOException e) {
            throw new ConfiguracaoInvalidaException("Não foi possível ler " + arquivo, e);
        }
    }

    @Override
    public void salvar(List<Equipamento> itens) throws ScadaException {
        List<String> linhas = new ArrayList<>();
        linhas.add("# A configuração é editada manualmente; formato: id;tipo;nome;parametros");
        for (Equipamento e : itens) {
            if (e instanceof Esteira x)
                linhas.add("%s;ESTEIRA;%s;%.2f;%.2f".formatted(x.getId(), x.getNome(), x.getSetpoint(), x.getVelocidadeMaxima()));
            else if (e instanceof Forno x)
                linhas.add("%s;FORNO;%s;%.2f;%.2f;2.0;250.0".formatted(x.getId(), x.getNome(), x.getTemperatura(), x.getSetpoint()));
            else if (e instanceof Paletizador x)
                linhas.add("%s;PALETIZADOR;%s;%d".formatted(x.getId(), x.getNome(), x.getLimiteCiclosManutencao()));
            else if (e instanceof Empacotadora x)
                linhas.add("%s;EMPACOTADORA;%s;%.2f;0.5;10.0".formatted(x.getId(), x.getNome(), x.getEstoqueFilme()));
        }
        try {
            Path parent = arquivo.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.write(arquivo, linhas);
        } catch (IOException e) {
            throw new ScadaException("Não foi possível salvar a configuração.", e);
        }
    }
}
