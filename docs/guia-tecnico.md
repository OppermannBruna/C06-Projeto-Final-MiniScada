# Mini-SCADA Industrial — JDK 21

Projeto acadêmico de Programação Orientada a Objetos: simulação de uma fábrica de biscoitos supervisionada por um sistema Mini-SCADA.

## Requisitos
- JDK 21
- Terminal ou IDE (IntelliJ IDEA, Eclipse, NetBeans ou VS Code)

## Executar pelo terminal

Na pasta raiz do projeto:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out com.grupo8.scada.Main
```

No Windows PowerShell, uma alternativa é compilar os arquivos pelo IntelliJ/IDE ou usar:

```powershell
$files = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $files
java -cp out com.grupo8.scada.Main
```

## Funcionalidades
- Quatro equipamentos: forno, esteira, empacotadora e paletizador.
- Controle on/off com histerese no forno.
- Ajuste de setpoint do forno e da velocidade da esteira.
- Monitoramento de temperatura e estoque de filme.
- Alarmes ativos, rearme de equipamentos em falha e parada de emergência.
- Threads independentes para os equipamentos, monitoramento e histórico.
- Persistência em `historico.csv` e `alarmes.csv`.
- Configuração inicial em `config/planta.csv`.

## Formato da configuração

Os campos são separados por ponto e vírgula:

```text
id;tipo;nome;parametros
F01;FORNO;Forno principal;160;180;3;230
E01;ESTEIRA;Esteira de transporte;1.2;3.0
P01;EMPACOTADORA;Empacotadora automática;80;0.8;10
L01;PALETIZADOR;Paletizador;100
```

Parâmetros:
- FORNO: temperatura inicial, setpoint, histerese, limite de segurança.
- ESTEIRA: velocidade inicial e velocidade máxima.
- EMPACOTADORA: estoque inicial (%), consumo por ciclo e estoque mínimo (%).
- PALETIZADOR: ciclos permitidos até manutenção.

## Observações técnicas
- As threads de equipamento são interrompidas na parada de emergência.
- Os métodos que acessam estados compartilhados usam sincronização ou coleções concorrentes.
- A simulação é didática; não representa um sistema industrial certificado ou adequado para controlar máquinas reais.
- O grupo deve revisar e compreender o código antes da apresentação.
- A IA foi utilizada como apoio para estruturar e gerar uma primeira versão do código; a ferramenta utilizada foi ChatGPT (OpenAI). O grupo deve declarar no README o uso real que fez da IA, conforme as regras da disciplina.

## Diagrama UML

Diagrama de classes atualizado conforme o código ([`docs/uml/MiniSCADA.png`](docs/uml/MiniSCADA.png)): [`docs/uml/diagrama-classes.md`](docs/uml/diagrama-classes.md) (Mermaid, renderizado pelo GitHub) e [`docs/uml/MiniSCADA.puml`](docs/uml/MiniSCADA.puml) (PlantUML).
