# Mini-SCADA – Fábrica de Biscoitos

Projeto final da disciplina **C06 – Programação Orientada a Objetos**, Prof. Christopher Lima.

> 🚧 Em desenvolvimento. A proposta de arquitetura está em [`docs/arquitetura.md`](docs/arquitetura.md).

## Sobre o projeto

Sistema supervisório (**SCADA**) simulado para uma linha de produção de biscoitos: forno, esteira, empacotadora e paletizador operam em paralelo, enquanto o supervisório monitora sensores, gera alarmes, recebe comandos do operador e registra o histórico do processo.

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

No IntelliJ: abra a pasta do projeto (é importado como Maven) e rode `com.grupo8.scada.Main`.

## Requisitos de POO atendidos

| Critério | Onde está no código |
|----------|---------------------|
| Classes (membros, métodos, construtores) | `Equipamento`, `Forno`, `Esteira`, `Paletizador`, `Empacotadora`, `Alarme` e `Leitura`, com atributos, construtores e métodos próprios. |
| Herança | `Forno`, `Esteira`, `Paletizador` e `Empacotadora` utilizam `extends Equipamento`. |
| Polimorfismo | `Supervisorio` utiliza `List<Equipamento>` para gerenciar diferentes equipamentos. Cada classe implementa seu próprio comportamento no método `executarCiclo()`. |
| Interfaces | `Ajustavel`, `Monitoravel` e `Manutenivel`, implementadas por `Esteira`, `Forno`, `Empacotadora` e `Paletizador`. |
| Classes abstratas | `Equipamento` é uma classe abstrata que define características comuns aos equipamentos e o método abstrato `executarCiclo()`. |
| Tratamento de erros | Pacote `exceptions`, contendo `ScadaException`, `EquipamentoEmFalhaException`, `SetpointInvalidoException` e `ConfiguracaoInvalidaException`, além de estruturas `try/catch`. |
| Arquivos / Banco de dados | `Repositorio<T>` e `RepositorioEquipamentosCsv` realizam a leitura da configuração em `config/planta.csv`. `RegistradorCsv` registra os dados em `historico.csv` e `alarmes.csv`. |
| Threads | Pacote `threads`, com `TarefaEquipamento`, `MonitorProcesso` e `Historiador`, que implementam `Runnable`. O `Supervisorio` cria e gerencia as threads utilizando `Thread`. |

## Diagrama UML

O diagrama UML completo da solução fica em [`docs/uml/`](docs/uml/).

## Uso de Inteligência Artificial

_Declaração obrigatória: o grupo utilizou IA? Como? Qual ferramenta?_

- **Utilizou IA?** Sim.
- **Ferramenta(s):** Claude Code (Anthropic), Gemini (Google) e ChatGPT (OpenAI).
- **Como:**
  - **Claude Code:** criação e configuração inicial do repositório (estrutura de pastas, `.gitignore`, `pom.xml` e este README) e proposta inicial de arquitetura/UML em `docs/arquitetura.md`, revisada e discutida pelo grupo.
  - **Gemini:** utilizado para levantar ideias de tema para o projeto.
  - **ChatGPT:** utilizado como apoio na elaboração da implementação inicial do código Java 21, incluindo classes, interfaces, herança, polimorfismo, tratamento de exceções, threads e persistência em arquivos CSV.

O código e as sugestões geradas por IA devem ser revisados e compreendidos pelos integrantes do grupo.# Mini-SCADA – Fábrica de Biscoitos

Projeto final da disciplina **C06 – Programação Orientada a Objetos**, Prof. Christopher Lima.

> 🚧 Em desenvolvimento. A proposta de arquitetura está em [`docs/arquitetura.md`](docs/arquitetura.md).

## Sobre o projeto

Sistema supervisório (**SCADA**) simulado para uma linha de produção de biscoitos: forno, esteira, empacotadora e paletizador operam em paralelo, enquanto o supervisório monitora sensores, gera alarmes, recebe comandos do operador e registra o histórico do processo.

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

No IntelliJ: abra a pasta do projeto (é importado como Maven) e rode `com.grupo8.scada.Main`.

## Requisitos de POO atendidos

| Critério | Onde está no código |
|----------|---------------------|
| Classes (membros, métodos, construtores) | `Equipamento`, `Forno`, `Esteira`, `Paletizador`, `Empacotadora`, `Alarme` e `Leitura`, com atributos, construtores e métodos próprios. |
| Herança | `Forno`, `Esteira`, `Paletizador` e `Empacotadora` utilizam `extends Equipamento`. |
| Polimorfismo | `Supervisorio` utiliza `List<Equipamento>` para gerenciar diferentes equipamentos. Cada classe implementa seu próprio comportamento no método `executarCiclo()`. |
| Interfaces | `Ajustavel`, `Monitoravel` e `Manutenivel`, implementadas por `Esteira`, `Forno`, `Empacotadora` e `Paletizador`. |
| Classes abstratas | `Equipamento` é uma classe abstrata que define características comuns aos equipamentos e o método abstrato `executarCiclo()`. |
| Tratamento de erros | Pacote `exceptions`, contendo `ScadaException`, `EquipamentoEmFalhaException`, `SetpointInvalidoException` e `ConfiguracaoInvalidaException`, além de estruturas `try/catch`. |
| Arquivos / Banco de dados | `Repositorio<T>` e `RepositorioEquipamentosCsv` realizam a leitura da configuração em `config/planta.csv`. `RegistradorCsv` registra os dados em `historico.csv` e `alarmes.csv`. |
| Threads | Pacote `threads`, com `TarefaEquipamento`, `MonitorProcesso` e `Historiador`, que implementam `Runnable`. O `Supervisorio` cria e gerencia as threads utilizando `Thread`. |

## Diagrama UML

O diagrama UML completo da solução fica em [`docs/uml/`](docs/uml/).

## Uso de Inteligência Artificial

_Declaração obrigatória: o grupo utilizou IA? Como? Qual ferramenta?_

- **Utilizou IA?** Sim.
- **Ferramenta(s):** Claude Code (Anthropic) e ChatGPT (OpenAI).
- **Como:**
  - **Claude Code:** criação e configuração inicial do repositório (estrutura de pastas, `.gitignore`, `pom.xml` e este README) e proposta inicial de arquitetura/UML em `docs/arquitetura.md`, revisada e discutida pelo grupo.
  - **ChatGPT:** utilizado como apoio na elaboração da implementação inicial do código Java 21, incluindo classes, interfaces, herança, polimorfismo, tratamento de exceções, threads e persistência em arquivos CSV.
