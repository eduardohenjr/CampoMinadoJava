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

