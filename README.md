# Campo Minado - Java (Console)

Este projeto é uma implementação do jogo Campo Minado em Java, com separação entre backend (lógica) e frontend (interface console), utilizando a engine CCGM.

## Como executar

1. Compile os arquivos Java do backend e frontend:
   - Os arquivos fonte estão em `backend/` e `frontend/`.
   - O diretório `bin/` é usado para os arquivos compilados.
   - Exemplo de compilação (Windows, usando prompt na raiz do projeto):
     ```
     javac -cp lib/cardgamemaker-0.3.jar -d bin backend/*.java frontend/*.java
     ```
2. Execute a classe principal:
   - Exemplo:
     ```
     java -cp bin;lib/cardgamemaker-0.3.jar frontend.Main
     ```

## Funcionalidades
- Menu principal com opções de iniciar, carregar, salvar, histórico, configurações, regras e créditos.
- Controle de dificuldade (linhas, colunas, bombas).
- Salvar e carregar o estado do jogo (nome do arquivo à escolha do usuário).
- Histórico de partidas: registra data/hora, resultado, configuração do tabuleiro e bombas.
- Marcação de bandeiras e lógica fiel ao Campo Minado tradicional.
- Separação entre backend (lógica) e frontend (interface console).

## Engine utilizada
O projeto utiliza a engine CCGM para manipulação de cartas e tabuleiros:
- [ConsoleCardGameMaker (CCGM) - Releases](https://github.com/Clique33/ConsoleCardGameMaker/releases)

Inclua o arquivo JAR da engine em `lib/` para compilar e rodar o projeto.

## Observações
- O projeto foi desenvolvido para fins acadêmicos na disciplina "Características das Linguagens de Programação - UERJ".
- O histórico de partidas é salvo em `historico.txt` na raiz do projeto.
- Os saves podem ser feitos em qualquer nome de arquivo (exemplo sugerido: `partida1.txt`).
- Não é necessário interface gráfica.
- O código foi mantido simples, usando apenas recursos básicos de Java (Scanner, PrintWriter, Date, etc).

## Argumentos de Console

O programa aceita argumentos de linha de comando para facilitar a execução direta de ações, sem passar pelo menu interativo. Veja as opções:

- `novo`  
  Inicia um novo jogo com a configuração padrão (5x5, 5 bombas ou a última configuração definida).
  
  Exemplo:
  ```
  java -cp bin;lib/cardgamemaker-0.3.jar frontend.Main novo
  ```

- `novo <linhas> <colunas> <bombas>`  
  Inicia um novo jogo com a configuração informada.
  
  Exemplo:
  ```
  java -cp bin;lib/cardgamemaker-0.3.jar frontend.Main novo 6 6 8
  ```

- `carregar <nome_do_save>`  
  Carrega diretamente um jogo salvo (sem extensão, ex: `partida1`).
  
  Exemplo:
  ```
  java -cp bin;lib/cardgamemaker-0.3.jar frontend.Main carregar partida1
  ```

- `config <linhas> <colunas> <bombas>`  
  Define a configuração padrão para os próximos jogos.
  
  Exemplo:
  ```
  java -cp bin;lib/cardgamemaker-0.3.jar frontend.Main config 8 8 10
  ```

Se nenhum argumento for passado, o menu interativo será exibido normalmente.

