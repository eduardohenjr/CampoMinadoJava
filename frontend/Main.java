package frontend;

import backend.CampoMinadoTabuleiro;
import backend.CampoMinadoCarta;
import mecanicas.Carta;
import backend.CampoMinadoPersistencia;
import java.io.IOException;
import java.util.Scanner;

/**
 * Classe principal do Campo Minado (modo texto).
 * Responsável pelo menu, interação e fluxo do jogo.
 */
public class Main {
    private static int linhasPadrao = 5;
    private static int colunasPadrao = 5;
    private static int bombasPadrao = 5;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1. Iniciar Jogo");
                System.out.println("2. Carregar Jogo");
                System.out.println("3. Histórico de Partidas");
                System.out.println("4. Configurações");
                System.out.println("5. Ver Regras");
                System.out.println("6. Créditos");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                int op;
                try {
                    op = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("Opção inválida!");
                    continue;
                }
                if (op == 0) {
                    System.out.println("Saindo...");
                    break;
                }
                switch (op) {
                    case 1:
                        iniciarJogo(sc);
                        break;
                    case 2:
                        carregarJogo(sc);
                        break;
                    case 3:
                        System.out.println("Histórico de Partidas: Em desenvolvimento.");
                        break;
                    case 4:
                        configurarJogo(sc);
                        break;
                    case 5:
                        mostrarRegras();
                        break;
                    case 6:
                        mostrarCreditos();
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            System.out.println("Saindo do jogo...");
        } finally {
            sc.close();
        }
    }

    // Inicia um novo jogo
    private static void iniciarJogo(Scanner sc) {
        CampoMinadoTabuleiro tabuleiro = new CampoMinadoTabuleiro(linhasPadrao, colunasPadrao, bombasPadrao);
        boolean fim = false;
        while (!fim) {
            mostrarTabuleiro(tabuleiro);
            System.out.println("Digite: 1 para abrir, 2 para marcar/desmarcar bandeira, 3 para salvar, 9 para pause, 0 para sair para o menu principal");
            int op;
            try {
                op = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Opção inválida!");
                continue;
            }
            if (op == 0) break;
            if (op == 9) {
                if (menuPause(sc)) break;
                else continue;
            }
            if (op == 3) {
                System.out.print("Digite o nome do arquivo para salvar: ");
                String nome = sc.nextLine();
                try {
                    CampoMinadoPersistencia.salvarJogo(tabuleiro, nome);
                    System.out.println("Jogo salvo com sucesso!");
                } catch (IOException e) {
                    System.out.println("Erro ao salvar: " + e.getMessage());
                }
                continue;
            }
            if (op != 1 && op != 2) {
                System.out.println("Opção inválida! Digite 1, 2, 3, 9 ou 0.");
                continue;
            }
            int l = -1, c = -1;
            while (true) {
                System.out.print("Linha: ");
                String linhaStr = sc.nextLine();
                try {
                    l = Integer.parseInt(linhaStr);
                    if (l < 0 || l >= tabuleiro.getTotalLinhas()) {
                        System.out.println("Linha fora do intervalo!");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Digite um número válido para a linha!");
                }
            }
            while (true) {
                System.out.print("Coluna: ");
                String colunaStr = sc.nextLine();
                try {
                    c = Integer.parseInt(colunaStr);
                    if (c < 0 || c >= tabuleiro.getTotalColunas()) {
                        System.out.println("Coluna fora do intervalo!");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Digite um número válido para a coluna!");
                }
            }
            try {
                if (op == 1) {
                    tabuleiro.abrirCasa(l, c);
                    if (perdeu(tabuleiro)) {
                        revelarTodasBombas(tabuleiro);
                        mostrarTabuleiro(tabuleiro);
                        System.out.println("Você perdeu!");
                        fim = true;
                    } else if (venceu(tabuleiro)) {
                        mostrarTabuleiro(tabuleiro);
                        System.out.println("Você venceu!");
                        fim = true;
                    }
                } else if (op == 2) {
                    tabuleiro.alternarBandeira(l, c);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("Bomba encontrada!")) {
                    revelarTodasBombas(tabuleiro);
                    mostrarTabuleiro(tabuleiro);
                    fim = true;
                }
            }
        }
    }

    // Carrega um jogo salvo
    private static void carregarJogo(Scanner sc) {
        System.out.print("Digite o nome do arquivo para carregar: ");
        String nome = sc.nextLine();
        try {
            CampoMinadoTabuleiro tabuleiro = CampoMinadoPersistencia.carregarJogo(nome);
            System.out.println("Jogo carregado!");
            boolean fim = false;
            while (!fim) {
                mostrarTabuleiro(tabuleiro);
                System.out.println("Digite: 1 para abrir, 2 para marcar/desmarcar bandeira, 3 para salvar, 9 para pause, 0 para sair para o menu principal");
                int op;
                try {
                    op = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("Opção inválida!");
                    continue;
                }
                if (op == 0) break;
                if (op == 9) {
                    if (menuPause(sc)) break;
                    else continue;
                }
                if (op == 3) {
                    System.out.print("Digite o nome do arquivo para salvar: ");
                    String nomeSave = sc.nextLine();
                    try {
                        CampoMinadoPersistencia.salvarJogo(tabuleiro, nomeSave);
                        System.out.println("Jogo salvo com sucesso!");
                    } catch (IOException e) {
                        System.out.println("Erro ao salvar: " + e.getMessage());
                    }
                    continue;
                }
                if (op != 1 && op != 2) {
                    System.out.println("Opção inválida! Digite 1, 2, 3, 9 ou 0.");
                    continue;
                }
                int l = -1, c = -1;
                while (true) {
                    System.out.print("Linha: ");
                    String linhaStr = sc.nextLine();
                    try {
                        l = Integer.parseInt(linhaStr);
                        if (l < 0 || l >= tabuleiro.getTotalLinhas()) {
                            System.out.println("Linha fora do intervalo!");
                            continue;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Digite um número válido para a linha!");
                    }
                }
                while (true) {
                    System.out.print("Coluna: ");
                    String colunaStr = sc.nextLine();
                    try {
                        c = Integer.parseInt(colunaStr);
                        if (c < 0 || c >= tabuleiro.getTotalColunas()) {
                            System.out.println("Coluna fora do intervalo!");
                            continue;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Digite um número válido para a coluna!");
                    }
                }
                try {
                    if (op == 1) {
                        tabuleiro.abrirCasa(l, c);
                        if (perdeu(tabuleiro)) {
                            revelarTodasBombas(tabuleiro);
                            mostrarTabuleiro(tabuleiro);
                            System.out.println("Você perdeu!");
                            fim = true;
                        } else if (venceu(tabuleiro)) {
                            mostrarTabuleiro(tabuleiro);
                            System.out.println("Você venceu!");
                            fim = true;
                        }
                    } else if (op == 2) {
                        tabuleiro.alternarBandeira(l, c);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    if (e.getMessage() != null && e.getMessage().contains("Bomba encontrada!")) {
                        revelarTodasBombas(tabuleiro);
                        mostrarTabuleiro(tabuleiro);
                        fim = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
    }

    // Menu de pause
    private static boolean menuPause(Scanner sc) {
        while (true) {
            System.out.println("\n=== MENU DE PAUSE ===");
            System.out.println("1. Voltar para o jogo");
            System.out.println("2. Iniciar novo jogo");
            System.out.println("3. Voltar ao Menu Principal");
            System.out.println("0. Sair do jogo");
            System.out.print("Escolha uma opção: ");
            int op;
            try {
                op = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Opção inválida!");
                continue;
            }
            switch (op) {
                case 1:
                    return false;
                case 2:
                    iniciarJogo(sc);
                    return true;
                case 3:
                    return true;
                case 0:
                    System.out.println("Saindo do jogo...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // Exibe as regras do jogo
    private static void mostrarRegras() {
        System.out.println("\n=== REGRAS DO CAMPO MINADO ===");
        System.out.println("- Abra casas sem bombas para vencer.");
        System.out.println("- Números indicam quantas bombas há nas casas vizinhas.");
        System.out.println("- Marque bandeiras onde suspeitar de bombas.");
        System.out.println("- Se abrir uma bomba, você perde.");
    }

    // Exibe os créditos
    private static void mostrarCreditos() {
        System.out.println("\n=== CRÉDITOS ===");
        System.out.println("Desenvolvido por: Eduardo Marques");
        System.out.println("Disciplina: Características das Linguagens de Programação - UERJ");
        System.out.println("2025");
    }

    // Exibe o tabuleiro no console
    private static void mostrarTabuleiro(CampoMinadoTabuleiro tab) {
        for (int i = 0; i < tab.getTotalLinhas(); i++) {
            for (int j = 0; j < tab.getTotalColunas(); j++) {
                Carta carta = tab.pegaCarta(i, j);
                if (carta.estaViradaParaCima()) {
                    if (carta instanceof CampoMinadoCarta) {
                        CampoMinadoCarta cmc = (CampoMinadoCarta) carta;
                        if (cmc.temBomba()) {
                            System.out.print(" * ");
                        } else if (cmc.getNumero() > 0) {
                            System.out.print(" " + cmc.getNumero() + " ");
                        } else {
                            System.out.print("   ");
                        }
                    } else {
                        System.out.print(" ? ");
                    }
                } else if (carta.getFrente().toString().equals("F")) {
                    System.out.print(" F ");
                } else {
                    System.out.print(" # ");
                }
                tab.colocaCarta(i, j, carta); // devolve carta ao topo
            }
            System.out.println();
        }
    }

    // Verifica se o jogador venceu
    private static boolean venceu(CampoMinadoTabuleiro tab) {
        for (int i = 0; i < tab.getTotalLinhas(); i++) {
            for (int j = 0; j < tab.getTotalColunas(); j++) {
                Carta carta = tab.pegaCarta(i, j);
                boolean ok = true;
                if (carta instanceof CampoMinadoCarta) {
                    CampoMinadoCarta c = (CampoMinadoCarta) carta;
                    if (!c.temBomba() && !c.estaViradaParaCima()) {
                        ok = false;
                    }
                }
                tab.colocaCarta(i, j, carta);
                if (!ok) return false;
            }
        }
        return true;
    }

    // Verifica se o jogador perdeu
    private static boolean perdeu(CampoMinadoTabuleiro tab) {
        for (int i = 0; i < tab.getTotalLinhas(); i++) {
            for (int j = 0; j < tab.getTotalColunas(); j++) {
                Carta carta = tab.pegaCarta(i, j);
                boolean lost = false;
                if (carta instanceof CampoMinadoCarta) {
                    CampoMinadoCarta c = (CampoMinadoCarta) carta;
                    if (c.temBomba() && c.estaViradaParaCima()) {
                        lost = true;
                    }
                }
                tab.colocaCarta(i, j, carta);
                if (lost) return true;
            }
        }
        return false;
    }

    // Revela todas as bombas ao perder
    private static void revelarTodasBombas(CampoMinadoTabuleiro tab) {
        for (int i = 0; i < tab.getTotalLinhas(); i++) {
            for (int j = 0; j < tab.getTotalColunas(); j++) {
                Carta carta = tab.pegaCarta(i, j);
                if (carta instanceof CampoMinadoCarta) {
                    CampoMinadoCarta c = (CampoMinadoCarta) carta;
                    if (c.temBomba() && !c.estaViradaParaCima()) {
                        c.vira();
                    }
                }
                tab.colocaCarta(i, j, carta);
            }
        }
    }

    // Configura o jogo (linhas, colunas, bombas)
    private static void configurarJogo(Scanner sc) {
        System.out.println("\n=== CONFIGURAÇÕES ===");
        int l = -1, c = -1, b = -1;
        try {
            System.out.print("Digite o número de linhas (mínimo 2): ");
            while (!sc.hasNextInt()) {
                System.out.println("Digite um número válido para linhas!");
                sc.nextLine();
                System.out.print("Digite o número de linhas (mínimo 2): ");
            }
            l = sc.nextInt();
            sc.nextLine(); // consumir quebra de linha
            if (l < 2) throw new Exception();

            System.out.print("Digite o número de colunas (mínimo 2): ");
            while (!sc.hasNextInt()) {
                System.out.println("Digite um número válido para colunas!");
                sc.nextLine();
                System.out.print("Digite o número de colunas (mínimo 2): ");
            }
            c = sc.nextInt();
            sc.nextLine();
            if (c < 2) throw new Exception();

            System.out.print("Digite o número de bombas (mínimo 1, máximo " + (l * c - 1) + "):");
            while (!sc.hasNextInt()) {
                System.out.println("Digite um número válido para bombas!");
                sc.nextLine();
                System.out.print("Digite o número de bombas (mínimo 1, máximo " + (l * c - 1) + "):");
            }
            b = sc.nextInt();
            sc.nextLine();
            if (b < 1 || b >= l * c) throw new Exception();

            linhasPadrao = l;
            colunasPadrao = c;
            bombasPadrao = b;
            System.out.println("Configuração salva! Próximo jogo usará esses valores.");
        } catch (Exception e) {
            System.out.println("Valores inválidos. Configuração não alterada.");
        }
    }
}
