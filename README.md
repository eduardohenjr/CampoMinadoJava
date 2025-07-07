# Campo Minado - Java (Console)

Este projeto é uma implementação do jogo Campo Minado em Java com manipulação de cartas e tabuleiro.

## Como executar

1. Compile os arquivos Java do backend e frontend:
   - Os arquivos fonte estão em `backend/` e `frontend/`.
   - O diretório `bin/` é usado para os arquivos compilados.
2. Execute a classe principal:
   - `frontend/Main.java` é o ponto de entrada do jogo.

## Funcionalidades
- Menu principal com opções de iniciar, carregar, salvar, histórico, configurações, regras e créditos.
- Controle de dificuldade (linhas, colunas, bombas).
- Salvar e carregar o estado do jogo.
- Marcação de bandeiras e lógica fiel ao Campo Minado tradicional.
- Separação entre backend (lógica) e frontend (interface console).

## Engine utilizada
O projeto utiliza a engine CCGM para manipulação de cartas e tabuleiros:
- [ConsoleCardGameMaker (CCGM) - Releases](https://github.com/Clique33/ConsoleCardGameMaker/releases)

Inclua o arquivo JAR da engine em `lib/` para compilar e rodar o projeto.

## Observações
- O projeto foi desenvolvido para fins acadêmicos na disciplina "Características das Linguagens de Programação - UERJ".

