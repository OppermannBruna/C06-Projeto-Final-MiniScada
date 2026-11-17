# Mini-SCADA – Fábrica de Biscoitos

Projeto final da disciplina **C06 – Programação Orientada a Objetos**, Prof. Christopher Lima.

> 🚧 Em desenvolvimento. A proposta de arquitetura está em [`docs/arquitetura.md`](docs/arquitetura.md).

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
| Classes (membros, métodos, construtores) | |
| Herança | |
| Polimorfismo | |
| Interfaces | |
| Classes abstratas | |
| Tratamento de erros | |
| Arquivos / Banco de dados | |
| Threads | |

## Diagrama UML

O diagrama UML completo da solução fica em [`docs/uml/`](docs/uml/): imagem [`MiniSCADA.png`](docs/uml/MiniSCADA.png), versão Mermaid [`diagrama-classes.md`](docs/uml/diagrama-classes.md) (renderizada pelo GitHub) e fonte PlantUML [`MiniSCADA.puml`](docs/uml/MiniSCADA.puml).

## Uso de Inteligência Artificial

_Declaração obrigatória: o grupo utilizou IA? Como? Qual ferramenta?_

- **Utilizou IA?** Sim.
- **Ferramenta(s):** Claude Code (Anthropic).
- **Como:** criação e configuração inicial do repositório (estrutura de pastas, `.gitignore`, `pom.xml` e este README) e proposta inicial de arquitetura/UML em `docs/arquitetura.md`, revisada e discutida pelo grupo. O Gemini (Google) foi usado para levantar ideias de tema. _Atualizar conforme o uso ao longo do projeto._
