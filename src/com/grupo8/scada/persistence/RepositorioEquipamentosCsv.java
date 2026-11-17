package com.grupo8.scada.persistence;

import com.grupo8.scada.exceptions.ConfiguracaoInvalidaException;
import com.grupo8.scada.exceptions.ScadaException;
import com.grupo8.scada.model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
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
                        case "ESTEIRA" -> new Esteira(id, nome, num(c, 3), num(c, 4));
                        case "FORNO" -> new Forno(id, nome, num(c, 3), num(c, 4), num(c, 5), num(c, 6));
                        case "PALETIZADOR" -> new Paletizador(id, nome, (long) num(c, 3));
                        case "EMPACOTADORA" -> new Empacotadora(id, nome, num(c, 3), num(c, 4), num(c, 5));
                        default -> throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
                    };
                    equipamentos.add(e);
                } catch (RuntimeException ex) {
                    throw new ConfiguracaoInvalidaException(
                            "Erro na linha " + (i + 1) + " de " + arquivo + ": " + ex.getMessage(), ex);
                }
            }
            return equipamentos;
        } catch (IOException e) {
            throw new ConfiguracaoInvalidaException("Não foi possível ler " + arquivo, e);
        }
    }

    /** Lê o parâmetro na posição i; aceita vírgula ou ponto como separador decimal. */
    private static double num(String[] campos, int i) {
        if (i >= campos.length) {
            throw new IllegalArgumentException("Parâmetro ausente na coluna " + (i + 1) + ".");
        }
        return Double.parseDouble(campos[i].trim().replace(',', '.'));
    }

    @Override
    public void salvar(List<Equipamento> itens) throws ScadaException {
        List<String> linhas = new ArrayList<>();
        linhas.add("# id;tipo;nome;parametros");
        for (Equipamento e : itens) {
            if (e instanceof Esteira x)
                linhas.add(f("%s;ESTEIRA;%s;%.2f;%.2f", x.getId(), x.getNome(), x.getSetpoint(), x.getVelocidadeMaxima()));
            else if (e instanceof Forno x)
                linhas.add(f("%s;FORNO;%s;%.2f;%.2f;%.2f;%.2f", x.getId(), x.getNome(), x.getTemperatura(),
                        x.getSetpoint(), x.getHisterese(), x.getLimiteSeguranca()));
            else if (e instanceof Paletizador x)
                linhas.add(f("%s;PALETIZADOR;%s;%d", x.getId(), x.getNome(), x.getLimiteCiclosManutencao()));
            else if (e instanceof Empacotadora x)
                linhas.add(f("%s;EMPACOTADORA;%s;%.2f;%.2f;%.2f", x.getId(), x.getNome(), x.getEstoqueFilme(),
                        x.getConsumoPorCiclo(), x.getEstoqueMinimo()));
        }
        try {
            Path parent = arquivo.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.write(arquivo, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ScadaException("Não foi possível salvar a configuração.", e);
        }
    }

    // Locale.ROOT: sempre ponto decimal, para o arquivo poder ser relido em qualquer sistema.
    private static String f(String formato, Object... args) {
        return String.format(Locale.ROOT, formato, args);
    }
}
