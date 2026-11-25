# Mini-SCADA – Fábrica de Biscoitos

Projeto final da disciplina **C06 – Programação Orientada a Objetos**, Prof. Christopher Lima.

A arquitetura do projeto está em [`docs/arquitetura.md`](docs/arquitetura.md).

## Sobre o projeto

Sistema supervisório (**SCADA**) simulado para uma linha de produção de biscoitos: forno, esteira, empacotadora e paletizador
operam em paralelo, enquanto o supervisório monitora sensores, gera alarmes, recebe comandos do operador e registra o histórico do processo.

**Relevância ao curso:** supervisão, controle de processos e alarmes industriais são temas centrais de Engenharia de Controle e Automação.

## Integrantes

| Nome | GitHub |
|------|--------|
| Bruna Oppermann | [@OppermannBruna](https://github.com/OppermannBruna) |
| Enzo Rodrigues Ribeiro Lemos | [@rodrigris1](https://github.com/rodrigris1) |
| Samuel Silva Oliveira | [@SamuelOliveira1606](https://github.com/SamuelOliveira1606) |

## Tecnologias

- Java 21 (versão alvo fixada no `pom.xml`)
- Maven

## Como executar

Pré-requisito: JDK 21.

No IntelliJ: abra a pasta do projeto (é importado como Maven) e rode `com.grupo8.scada.Main`. A pasta de trabalho da execução deve ser a raiz do projeto (onde está `config/`).

Pelo terminal, na raiz do projeto:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out com.grupo8.scada.Main
```

Formato do `config/planta.csv`, menu e demais detalhes: [`docs/guia-tecnico.md`](docs/guia-tecnico.md).

## Requisitos de POO atendidos

| Critério | Onde está no código |
|----------|---------------------|
| Classes (membros, métodos, construtores) | Todas as classes de `src/com/grupo8/scada/`. Atributos privados com getters e construtores que validam os dados (`IllegalArgumentException`) em `Equipamento`, `Forno`, `Esteira`, `Empacotadora`, `Paletizador` e `Leitura`. Exemplos de métodos: `Forno.executarCiclo()`, `Alarme.resolver()`, `GerenciadorAlarmes.registrar()`. |
| Herança | `Forno`, `Esteira`, `Empacotadora` e `Paletizador` estendem `Equipamento` (`model/`). `ConfiguracaoInvalidaException`, `EquipamentoEmFalhaException` e `SetpointInvalidoException` estendem `ScadaException`, que estende `Exception` (`exceptions/`). |
| Polimorfismo | `Supervisorio` guarda uma `List<Equipamento>`, e `TarefaEquipamento` chama `equipamento.ciclo()` sem saber o tipo: cada subclasse sobrescreve `executarCiclo()` e `getTipo()` (`Forno` também sobrescreve `aoManterFalha()` e `podeRearmar()`). `MonitorProcesso` e `Historiador` tratam qualquer `Monitoravel` via `gerarLeituras()`. `ConsoleOperador` usa `Ajustavel` e `Manutenivel` sem conhecer a classe concreta. |
| Interfaces | `Ajustavel` (`Forno`, `Esteira`), `Monitoravel` (os 4 equipamentos) e `Manutenivel` (`Paletizador`), em `model/interfaces/`. `Repositorio<T>` (genérica), implementada por `RepositorioEquipamentosCsv` (`persistence/`). As threads implementam `Runnable`. |
| Classes abstratas | `Equipamento` (`model/Equipamento.java`): `executarCiclo()` e `getTipo()` são abstratos, e `ciclo()` é o método-modelo `final` que chama `executarCiclo()`. Não existe "equipamento genérico". |
| Tratamento de erros | Exceções próprias em `exceptions/` (`ScadaException` e subclasses, verificadas). `try/catch` em `TarefaEquipamento`, `MonitorProcesso` e `Historiador` (a thread não morre por erro), em `RepositorioEquipamentosCsv.carregar()` (informa a linha inválida do CSV), em `ConsoleOperador.executar()` (`NumberFormatException`, `SetpointInvalidoException`) e em `Main`. |
| Arquivos / Banco de dados | Sem banco de dados; usa arquivos CSV. `config/planta.csv` é lido e salvo por `RepositorioEquipamentosCsv` (se não existir, `Main` cria a planta padrão). `historico.csv` (leituras) e `alarmes.csv` são gravados por `RegistradorCsv`. |
| Threads | Criadas em `Supervisorio.iniciar()`: uma `TarefaEquipamento` por equipamento, mais `MonitorProcesso` (alarmes) e `Historiador` (CSV). Encerradas com `interrupt()` em `pararEmergencia()` e `desligarSistema()`. Dados compartilhados protegidos com `synchronized` (`Equipamento` e subclasses, `GerenciadorAlarmes`, `RegistradorCsv`) e `CopyOnWriteArrayList` (`Supervisorio`). |

## Diagrama UML

O diagrama UML completo da solução fica em [`docs/uml/`](docs/uml/): imagem [`MiniSCADA.png`](docs/uml/MiniSCADA.png), versão Mermaid [`diagrama-classes.md`](docs/uml/diagrama-classes.md) (renderizada pelo GitHub) e fonte PlantUML [`MiniSCADA.puml`](docs/uml/MiniSCADA.puml).

## Uso de Inteligência Artificial

- **Utilizou IA?** Sim.
- **Ferramenta(s):** Claude Code (Anthropic), ChatGPT (OpenAI) e Gemini (Google).
- **Gemini:** levantar ideias de tema e relacionar cada critério do enunciado a uma parte do projeto.
- **ChatGPT:** gerar uma versão do código a partir da arquitetura combinada; o grupo comparou as versões, escolheu a base e estudou o código.
- **Claude Code** foi usado para:
  - Criar a estrutura inicial do repositório (pastas, `.gitignore`, `pom.xml`, README) e a proposta de arquitetura em `docs/arquitetura.md`, que o grupo revisou e discutiu.
  - Analisar o código existente, apontar bugs e corrigi-los (formato dos CSVs, retomada dos equipamentos após falha ou manutenção, duplicação de alarmes, validação do setpoint do forno, entre outros).
  - Reorganizar a estrutura de pastas do projeto.
  - Gerar o diagrama UML atualizado (`docs/uml/`) e preencher a tabela de requisitos de POO deste README.
  - Tutoria durante a parte de equipamentos (revisão de código e explicação de conceitos) e revisão de conceitos de POO para a apresentação.
