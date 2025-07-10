package frontend;

import backend.CampoMinadoTabuleiro;
import backend.CampoMinadoHistorico;
import java.io.IOException;
import java.util.Scanner;
import java.util.NoSuchElementException;
import backend.CampoMinadoConfig;
import console.Console;
import cores.Cor;
import backend.JogoConfig;

public class Main {
    private static int linhasPadrao = 5;
    private static int colunasPadrao = 5;
    private static int bombasPadrao = 5;
    private static String simboloBomba = "*";
    private static String simboloBandeira = "F";
    private static Cor corFundo = Cor.RESET;
    private static Cor corNumero = Cor.RESET;
    private static final String[] OPCOES_BOMBA = {"*", "B", "@", "X", "#"};
    private static final String[] OPCOES_BANDEIRA = {"F", "!", "#", "B", "P"};
    private static final Cor[] OPCOES_COR_FUNDO = {
        Cor.RESET, Cor.FUNDO_VERMELHO, Cor.FUNDO_VERDE, Cor.FUNDO_AMARELO, Cor.FUNDO_AZUL, Cor.FUNDO_ROXO, Cor.FUNDO_CIANO, Cor.FUNDO_BRANCO
    };
    private static final Cor[] OPCOES_COR_NUMERO = {
        Cor.RESET, Cor.VERMELHO, Cor.VERDE, Cor.AMARELO, Cor.AZUL, Cor.ROXO, Cor.CIANO, Cor.BRANCO
    };

    public static void main(String[] args) {
        // Flags para cada modo GUI
        boolean modoGuiGeral = false;
        boolean modoGuiPrincipal = false;
        boolean modoGuiPause = false;
        boolean modoGuiArquivos = false;
        boolean modoGuiJogo = false;

        String[] argsFiltrados = new String[args != null ? args.length : 0];
        int n = 0;
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i].toLowerCase()) {
                    case "--gui":
                        modoGuiGeral = true;
                        break;
                    case "--gui-principal":
                        modoGuiPrincipal = true;
                        break;
                    case "--gui-pause":
                        modoGuiPause = true;
                        break;
                    case "--gui-arquivos":
                        modoGuiArquivos = true;
                        break;
                    case "--gui-jogo":
                        modoGuiJogo = true;
                        break;
                    case "--historico":
                        if (i + 1 < args.length) {
                            backend.CampoMinadoHistorico.setArquivo(args[i + 1]);
                            i++;
                        }
                        break;
                    default:
                        argsFiltrados[n++] = args[i];
                }
            }
        }
        if (modoGuiGeral) {
            modoGuiPrincipal = true;
            modoGuiPause = true;
            modoGuiArquivos = true;
            modoGuiJogo = true;
        }
        if (modoGuiJogo) {
            // Não chama MainGUI.main(args)!
            // Segue para o menu principal no console, mas ao iniciar/carregar jogo, abre o tabuleiro em GUI
            while (true) {
                Console.println("\n=== MENU PRINCIPAL ===");
                Console.println("1. Iniciar Jogo");
                Console.println("2. Carregar Jogo");
                Console.println("3. Histórico de Partidas");
                Console.println("4. Configurações");
                Console.println("5. Ver Regras");
                Console.println("6. Créditos");
                Console.println("7. Personalizar");
                Console.println("0. Sair");
                int op;
                try {
                    op = Integer.parseInt(Console.input("Escolha uma opção: "));
                } catch (NoSuchElementException e) {
                    Console.println("\nSaindo do jogo (Ctrl+C detectado)...");
                    break;
                } catch (Exception e) {
                    Console.println("Opção inválida!");
                    continue;
                }
                if (op == 0) {
                    Console.println("Saindo...");
                    break;
                }
                switch (op) {
                    case 1: // Iniciar Jogo
                        javax.swing.SwingUtilities.invokeLater(() -> new frontend.JogoGUI(linhasPadrao, colunasPadrao, bombasPadrao).setVisible(true));
                        break;
                    case 2: // Carregar Jogo
                        listarSaves();
                        Console.print("Digite o nome do arquivo para carregar (sem extensão): ");
                        String nome = Console.input().trim();
                        if (nome.isEmpty()) { Console.println("Nome inválido!"); break; }
                        try {
                            backend.CampoMinadoTabuleiro tabuleiro = backend.CampoMinadoPersistencia.carregarJogo(nome);
                            Console.println("Jogo carregado!");
                            javax.swing.SwingUtilities.invokeLater(() -> new frontend.JogoGUI(tabuleiro).setVisible(true));
                        } catch (IOException e) {
                            Console.println("Erro ao carregar: " + e.getMessage());
                        }
                        break;
                    case 3: Console.println("\n=== HISTÓRICO DE PARTIDAS ==="); Console.print(CampoMinadoHistorico.lerHistorico()); break;
                    case 4: configurarJogo(); break;
                    case 5: mostrarRegras(); break;
                    case 6: mostrarCreditos(); break;
                    case 7: personalizarConsole(); break;
                    default: Console.println("Opção inválida!");
                }
            }
            return;
        }
        // Se modoGuiPrincipal, exibe menu principal em GUI e segue no console
        if (modoGuiPrincipal) {
            int escolha = frontend.MainGUI.mostrarMenuPrincipalDialogSwing(); 
            switch (escolha) {
                case 0: Console.println("Saindo..."); return;
                case 1: iniciarJogo(modoGuiPause); break;
                case 2: carregarJogo(modoGuiArquivos); break;
                case 3: Console.println("\n=== HISTÓRICO DE PARTIDAS ==="); Console.print(CampoMinadoHistorico.lerHistorico()); break;
                case 4: configurarJogo(); break;
                case 5: mostrarRegras(); break;
                case 6: mostrarCreditos(); break;
                case 7: personalizarConsole(); break;
                default: Console.println("Opção inválida!"); return;
            }
            // Após a ação, segue normalmente para o menu de texto se desejar
            while (true) {
                Console.println("\n=== MENU PRINCIPAL ===");
                Console.println("1. Iniciar Jogo");
                Console.println("2. Carregar Jogo");
                Console.println("3. Histórico de Partidas");
                Console.println("4. Configurações");
                Console.println("5. Ver Regras");
                Console.println("6. Créditos");
                Console.println("7. Personalizar");
                Console.println("0. Sair");
                int op;
                try {
                    op = Integer.parseInt(Console.input("Escolha uma opção: "));
                } catch (NoSuchElementException e) {
                    Console.println("\nSaindo do jogo (Ctrl+C detectado)...");
                    break;
                } catch (Exception e) {
                    Console.println("Opção inválida!");
                    continue;
                }
                if (op == 0) {
                    Console.println("Saindo...");
                    break;
                }
                switch (op) {
                    case 1: iniciarJogo(modoGuiPause); break;
                    case 2: carregarJogo(modoGuiArquivos); break;
                    case 3: Console.println("\n=== HISTÓRICO DE PARTIDAS ==="); Console.print(CampoMinadoHistorico.lerHistorico()); break;
                    case 4: configurarJogo(); break;
                    case 5: mostrarRegras(); break;
                    case 6: mostrarCreditos(); break;
                    case 7: personalizarConsole(); break;
                    default: Console.println("Opção inválida!");
                }
            }
            return;
        }
        String[] argumentos = new String[n];
        System.arraycopy(argsFiltrados, 0, argumentos, 0, n);

        CampoMinadoConfig.Preferencias pref = CampoMinadoConfig.carregarPreferencias();
        simboloBomba = pref.simboloBomba;
        simboloBandeira = pref.simboloBandeira;
        corFundo = converteCorParaEnum(pref.corFundo);
        corNumero = converteCorParaEnum(pref.corNumero);
        linhasPadrao = JogoConfig.getLinhasPadrao();
        colunasPadrao = JogoConfig.getColunasPadrao();
        bombasPadrao = JogoConfig.getBombasPadrao();
        Scanner sc = new Scanner(System.in);
        try {
            if (argumentos != null && argumentos.length > 0) {
                if (argumentos[0].equals("novo")) {
                    if (argumentos.length == 4) {
                        try {
                            linhasPadrao = Integer.parseInt(argumentos[1]);
                            colunasPadrao = Integer.parseInt(argumentos[2]);
                            bombasPadrao = Integer.parseInt(argumentos[3]);
                        } catch (Exception e) {
                            Console.println("Argumentos inválidos para novo jogo. Usando padrão.");
                        }
                    }
                    iniciarJogo(modoGuiPause);
                    return;
                } else if (argumentos[0].equals("carregar")) {
                    if (argumentos.length >= 2) {
                        String nome = argumentos[1];
                        try {
                            CampoMinadoTabuleiro tabuleiro = backend.CampoMinadoPersistencia.carregarJogo(nome);
                            Console.println("Jogo carregado!");
                            jogarTabuleiro(tabuleiro, nome, modoGuiPause);
                        } catch (IOException e) {
                            Console.println("Erro ao carregar: " + e.getMessage());
                        }
                        return;
                    } else {
                        Console.println("Uso: carregar <nome_do_save>");
                        return;
                    }
                } else if (argumentos[0].equals("config")) {
                    if (argumentos.length == 4) {
                        try {
                            linhasPadrao = Integer.parseInt(argumentos[1]);
                            colunasPadrao = Integer.parseInt(argumentos[2]);
                            bombasPadrao = Integer.parseInt(argumentos[3]);
                            Console.println("Configuração definida: " + linhasPadrao + "x" + colunasPadrao + ", " + bombasPadrao + " bombas.");
                        } catch (Exception e) {
                            Console.println("Argumentos inválidos para configuração.");
                        }
                    } else {
                        Console.println("Uso: config <linhas> <colunas> <bombas>");
                    }
                    return;
                } else {
                    Console.println("Argumento não reconhecido. Use: novo | novo l c b | carregar nome | config l c b");
                    return;
                }
            }
            while (true) {
                if (modoGuiPrincipal) {
                    // Aqui futuramente chamar o menu principal em GUI
                    Console.println("[GUI] Menu principal gráfico não implementado ainda.");
                    break;
                }
                Console.println("\n=== MENU PRINCIPAL ===");
                Console.println("1. Iniciar Jogo");
                Console.println("2. Carregar Jogo");
                Console.println("3. Histórico de Partidas");
                Console.println("4. Configurações");
                Console.println("5. Ver Regras");
                Console.println("6. Créditos");
                Console.println("7. Personalizar");
                Console.println("0. Sair");
                int op;
                try {
                    op = Integer.parseInt(Console.input("Escolha uma opção: "));
                } catch (NoSuchElementException e) {
                    Console.println("\nSaindo do jogo (Ctrl+C detectado)...");
                    break;
                } catch (Exception e) {
                    Console.println("Opção inválida!");
                    continue;
                }
                if (op == 0) {
                    Console.println("Saindo...");
                    break;
                }
                switch (op) {
                    case 1: iniciarJogo(modoGuiPause); break;
                    case 2: carregarJogo(modoGuiArquivos); break;
                    case 3: Console.println("\n=== HISTÓRICO DE PARTIDAS ==="); Console.print(CampoMinadoHistorico.lerHistorico()); break;
                    case 4: configurarJogo(); break;
                    case 5: mostrarRegras(); break;
                    case 6: mostrarCreditos(); break;
                    case 7: personalizarConsole(); break;
                    default: Console.println("Opção inválida!");
                }
            }
        } catch (NoSuchElementException e) {
            Console.println("\nSaindo do jogo (Ctrl+C detectado)...");
        } catch (Exception e) {
            Console.println("Saindo do jogo...");
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    // --- MÉTODOS PRINCIPAIS (apenas uma versão de cada) ---

    private static void jogarTabuleiro(CampoMinadoTabuleiro tabuleiro, String nomeSaveAuto, boolean modoPauseGUI) {
        boolean fim = false;
        while (!fim) {
            mostrarTabuleiro(tabuleiro);
            Console.println("Digite: 1 para abrir, 2 para marcar/desmarcar bandeira, 9 para pause, 0 para sair para o menu principal");
            int op;
            try { op = Integer.parseInt(Console.input()); } catch (Exception e) { Console.println("Opção inválida!"); continue; }
            if (op == 0) break;
            if (op == 9) {
                PauseAction action = menuPauseAction(tabuleiro, modoPauseGUI);
                if (action == PauseAction.NOVO_JOGO) {
                    iniciarJogo(modoPauseGUI);
                    return;
                } else if (action == PauseAction.MENU_PRINCIPAL) {
                    return;
                } else if (action == PauseAction.SAIR) {
                    System.exit(0);
                } else {
                    continue;
                }
            }
            if (op != 1 && op != 2) { Console.println("Opção inválida! Digite 1, 2, 9 ou 0."); continue; }
            int l = -1, c = -1;
            while (true) {
                String linhaStr = Console.input("Linha: ");
                try { l = Integer.parseInt(linhaStr); if (l < 0 || l >= tabuleiro.getTotalLinhas()) { Console.println("Linha fora do intervalo!"); continue; } break; }
                catch (Exception e) { Console.println("Digite um número válido para a linha!"); }
            }
            while (true) {
                String colunaStr = Console.input("Coluna: ");
                try { c = Integer.parseInt(colunaStr); if (c < 0 || c >= tabuleiro.getTotalColunas()) { Console.println("Coluna fora do intervalo!"); continue; } break; }
                catch (Exception e) { Console.println("Digite um número válido para a coluna!"); }
            }
            try {
                if (op == 1) {
                    tabuleiro.abrirCasa(l, c);
                    if (nomeSaveAuto != null) { try { backend.CampoMinadoPersistencia.salvarJogo(tabuleiro, nomeSaveAuto); } catch (Exception ex) {} }
                    if (perdeu(tabuleiro)) {
                        revelarTodasBombas(tabuleiro);
                        mostrarTabuleiro(tabuleiro);
                        Console.println("Você perdeu!");
                        fim = true;
                    } else if (venceu(tabuleiro)) {
                        mostrarTabuleiro(tabuleiro);
                        Console.println("Você venceu!");
                        fim = true;
                    }
                } else if (op == 2) {
                    tabuleiro.alternarBandeira(l, c);
                    if (nomeSaveAuto != null) { try { backend.CampoMinadoPersistencia.salvarJogo(tabuleiro, nomeSaveAuto); } catch (Exception ex) {} }
                }
            } catch (Exception e) {
                Console.println(e.getMessage());
                if (e.getMessage() != null && e.getMessage().contains("Bomba encontrada!")) {
                    revelarTodasBombas(tabuleiro);
                    mostrarTabuleiro(tabuleiro);
                    fim = true;
                }
            }
        }
    }

    private static void iniciarJogo(boolean modoPauseGUI) {
        CampoMinadoTabuleiro tabuleiro = new CampoMinadoTabuleiro(linhasPadrao, colunasPadrao, bombasPadrao);
        jogarTabuleiro(tabuleiro, null, modoPauseGUI);
        if (tabuleiro.venceu() || tabuleiro.perdeu())
            backend.CampoMinadoHistorico.registrar(tabuleiro.venceu() ? "Venceu" : "Perdeu", tabuleiro.getTotalLinhas(), tabuleiro.getTotalColunas(), tabuleiro.getBombas());
    }

    private static void carregarJogo(boolean modoPauseGUI) {
        listarSaves();
        Console.print("Digite o nome do arquivo para carregar (sem extensão): ");
        String nome = Console.input().trim();
        if (nome.isEmpty()) { Console.println("Nome inválido!"); return; }
        try {
            CampoMinadoTabuleiro tabuleiro = backend.CampoMinadoPersistencia.carregarJogo(nome);
            Console.println("Jogo carregado!");
            jogarTabuleiro(tabuleiro, nome, modoPauseGUI);
        } catch (IOException e) {
            Console.println("Erro ao carregar: " + e.getMessage());
        }
    }

    public static void listarSaves() {
        String[] saves = backend.CampoMinadoPersistencia.listarSaves();
        if (saves.length == 0) Console.println("Nenhum save encontrado.");
        else {
            Console.println("Saves disponíveis:");
            for (String s : saves) {
                String nome = s.endsWith(".save") ? s.substring(0, s.length() - 5) : s;
                Console.println("- " + nome);
            }
        }
    }

    // Enum e método de pause corretos (apenas uma versão)
    private enum PauseAction { VOLTAR, NOVO_JOGO, MENU_PRINCIPAL, SALVAR, SAIR }

    private static PauseAction menuPauseAction(CampoMinadoTabuleiro tabuleiro, boolean modoPauseGUI) {
        if (modoPauseGUI) {
            final PauseAction[] resultado = {PauseAction.VOLTAR};
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    javax.swing.JFrame tempFrame = new javax.swing.JFrame();
                    tempFrame.setAlwaysOnTop(true);
                    tempFrame.setUndecorated(true);
                    tempFrame.setType(javax.swing.JFrame.Type.UTILITY);
                    tempFrame.setLocationRelativeTo(null);
                    tempFrame.setVisible(true);
                    String[] opcoes = {"Voltar para o jogo", "Iniciar novo jogo", "Voltar ao Menu Principal", "Salvar jogo", "Sair do jogo"};
                    int escolha = javax.swing.JOptionPane.showOptionDialog(tempFrame, "Menu de Pause", "Pause", javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
                    tempFrame.dispose();
                    switch (escolha) {
                        case 0: resultado[0] = PauseAction.VOLTAR; break;
                        case 1: resultado[0] = PauseAction.NOVO_JOGO; break;
                        case 2: resultado[0] = PauseAction.MENU_PRINCIPAL; break;
                        case 3:
                            listarSaves();
                            String nomeSave = javax.swing.JOptionPane.showInputDialog(null, "Digite o nome do arquivo para salvar (sem extensão):");
                            if (nomeSave == null || nomeSave.trim().isEmpty()) {
                                javax.swing.JOptionPane.showMessageDialog(null, "Nome inválido!");
                                resultado[0] = PauseAction.VOLTAR;
                                break;
                            }
                            String[] saves = backend.CampoMinadoPersistencia.listarSaves();
                            boolean existe = false;
                            for (String s : saves) if (s.equals(nomeSave + ".save")) existe = true;
                            if (existe) {
                                int resp = javax.swing.JOptionPane.showConfirmDialog(null, "Arquivo já existe. Deseja sobrescrever?", "Confirmação", javax.swing.JOptionPane.YES_NO_OPTION);
                                if (resp != javax.swing.JOptionPane.YES_OPTION) {
                                    javax.swing.JOptionPane.showMessageDialog(null, "Operação cancelada.");
                                    resultado[0] = PauseAction.VOLTAR;
                                    break;
                                }
                            }
                            try {
                                backend.CampoMinadoPersistencia.salvarJogo(tabuleiro, nomeSave);
                                javax.swing.JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!");
                            } catch (IOException e) { javax.swing.JOptionPane.showMessageDialog(null, "Erro ao salvar: " + e.getMessage()); }
                            resultado[0] = PauseAction.VOLTAR;
                            break;
                        case 4:
                            javax.swing.JOptionPane.showMessageDialog(null, "Saindo do jogo...");
                            resultado[0] = PauseAction.SAIR;
                            break;
                        default:
                            resultado[0] = PauseAction.VOLTAR;
                    }
                });
            } catch (Exception e) {
                Console.println("[ERRO] Exceção ao exibir menu de pause em GUI: " + e.getMessage());
                e.printStackTrace();
                return PauseAction.VOLTAR;
            }
            return resultado[0];
        }
        while (true) {
            Console.println("\n=== MENU DE PAUSE ===");
            Console.println("1. Voltar para o jogo");
            Console.println("2. Iniciar novo jogo");
            Console.println("3. Voltar ao Menu Principal");
            Console.println("4. Salvar jogo");
            Console.println("0. Sair do jogo");
            Console.print("Escolha uma opção: ");
            int op;
            try { op = Integer.parseInt(Console.input()); } catch (Exception e) { Console.println("Opção inválida!"); continue; }
            switch (op) {
                case 1: return PauseAction.VOLTAR;
                case 2: return PauseAction.NOVO_JOGO;
                case 3: return PauseAction.MENU_PRINCIPAL;
                case 4:
                    listarSaves();
                    String nomeSave = Console.input("Digite o nome do arquivo para salvar (sem extensão): ").trim();
                    if (nomeSave.isEmpty()) { Console.println("Nome inválido!"); continue; }
                    String[] saves = backend.CampoMinadoPersistencia.listarSaves();
                    boolean existe = false;
                    for (String s : saves) if (s.equals(nomeSave + ".save")) existe = true;
                    if (existe) {
                        String resp = Console.input("Arquivo já existe. Deseja sobrescrever? (s/n): ").trim().toLowerCase();
                        if (!resp.equals("s")) { Console.println("Operação cancelada."); continue; }
                    }
                    try {
                        backend.CampoMinadoPersistencia.salvarJogo(tabuleiro, nomeSave);
                        Console.println("Jogo salvo com sucesso!");
                    } catch (IOException e) { Console.println("Erro ao salvar: " + e.getMessage()); }
                    continue;
                case 0: Console.println("Saindo do jogo..."); return PauseAction.SAIR;
                default: Console.println("Opção inválida!");
            }
        }
    }

    private static void mostrarRegras() {
        Console.println("\n=== REGRAS DO CAMPO MINADO ===");
        Console.println("- Abra casas sem bombas para vencer.");
        Console.println("- Números indicam quantas bombas há nas casas vizinhas.");
        Console.println("- Marque bandeiras onde suspeitar de bombas.");
        Console.println("- Se abrir uma bomba, você perde.");
    }

    private static void mostrarCreditos() {
        Console.println("\n=== CRÉDITOS ===");
        Console.println("Desenvolvido por: Eduardo Marques");
        Console.println("Disciplina: Características das Linguagens de Programação - UERJ");
        Console.println("2025");
    }

    private static void personalizarConsole() {
        Console.println("\n=== PERSONALIZAÇÃO ===");
        Console.println("Escolha o símbolo da bomba:");
        for (int i = 0; i < OPCOES_BOMBA.length; i++) Console.println((i+1) + ". " + OPCOES_BOMBA[i]);
        Console.print("Opção (1-" + OPCOES_BOMBA.length + ") [atual: " + simboloBomba + "]: ");
        try { int op = Integer.parseInt(Console.input()); if (op >= 1 && op <= OPCOES_BOMBA.length) simboloBomba = OPCOES_BOMBA[op-1]; } catch (Exception e) {}
        Console.println("Escolha o símbolo da bandeira:");
        for (int i = 0; i < OPCOES_BANDEIRA.length; i++) Console.println((i+1) + ". " + OPCOES_BANDEIRA[i]);
        Console.print("Opção (1-" + OPCOES_BANDEIRA.length + ") [atual: " + simboloBandeira + "]: ");
        try { int op = Integer.parseInt(Console.input()); if (op >= 1 && op <= OPCOES_BANDEIRA.length) simboloBandeira = OPCOES_BANDEIRA[op-1]; } catch (Exception e) {}
        Console.println("Escolha a cor de fundo do tabuleiro:");
        for (int i = 0; i < OPCOES_COR_FUNDO.length; i++) Console.println((i+1) + ". " + nomeCor(OPCOES_COR_FUNDO[i]));
        Console.print("Opção (1-" + OPCOES_COR_FUNDO.length + ") [atual: " + nomeCor(corFundo) + "]: ");
        try { int op = Integer.parseInt(Console.input()); if (op >= 1 && op <= OPCOES_COR_FUNDO.length) corFundo = OPCOES_COR_FUNDO[op-1]; } catch (Exception e) {}
        Console.println("Escolha a cor dos números:");
        for (int i = 0; i < OPCOES_COR_NUMERO.length; i++) Console.println((i+1) + ". " + nomeCor(OPCOES_COR_NUMERO[i]));
        Console.print("Opção (1-" + OPCOES_COR_NUMERO.length + ") [atual: " + nomeCor(corNumero) + "]: ");
        try { int op = Integer.parseInt(Console.input()); if (op >= 1 && op <= OPCOES_COR_NUMERO.length) corNumero = OPCOES_COR_NUMERO[op-1]; } catch (Exception e) {}
        Console.println("Personalização salva!");
        salvarPreferencias();
    }
    private static String nomeCor(Cor cor) {
        if (cor == null) return "Padrão";
        String nome = cor.name().replace("FUNDO_", "Fundo ").replace("_CLARO", " Claro").replace("_CHOQUE", " Choque").replace("_BRANCO", " Branco");
        nome = nome.substring(0,1).toUpperCase() + nome.substring(1).toLowerCase().replace('_', ' ');
        if (cor == Cor.RESET) return "Padrão";
        return nome;
    }
    private static Cor converteCorParaEnum(String cor) {
        if (cor == null || cor.isEmpty()) return Cor.RESET;
        try { return Cor.valueOf(cor); } catch (Exception e) { return Cor.RESET; }
    }
    private static void mostrarTabuleiro(CampoMinadoTabuleiro tab) { Console.println(tab); }
    private static boolean venceu(CampoMinadoTabuleiro tab) { return tab.venceu(); }
    private static boolean perdeu(CampoMinadoTabuleiro tab) { return tab.perdeu(); }
    private static void revelarTodasBombas(CampoMinadoTabuleiro tab) { tab.revelarBombas(); }
    private static void salvarPreferencias() {
        CampoMinadoConfig.salvarPreferencias(simboloBomba, simboloBandeira, corFundo.name(), corNumero.name());
    }
    private static void configurarJogo() {
        Console.println("\n=== CONFIGURAÇÕES ===");
        int l = -1, c = -1, b = -1;
        try {
            String entrada;
            Console.print("Digite o número de linhas (mínimo 2): ");
            while (true) {
                entrada = Console.input();
                if (entrada.matches("\\d+")) { l = Integer.parseInt(entrada); if (l >= 2) break; }
                Console.println("Digite um número válido para linhas!");
                Console.print("Digite o número de linhas (mínimo 2): ");
            }
            Console.print("Digite o número de colunas (mínimo 2): ");
            while (true) {
                entrada = Console.input();
                if (entrada.matches("\\d+")) { c = Integer.parseInt(entrada); if (c >= 2) break; }
                Console.println("Digite um número válido para colunas!");
                Console.print("Digite o número de colunas (mínimo 2): ");
            }
            Console.print("Digite o número de bombas (mínimo 1, máximo " + (l * c - 1) + "): ");
            while (true) {
                entrada = Console.input();
                if (entrada.matches("\\d+")) { b = Integer.parseInt(entrada); if (b >= 1 && b < l * c) break; }
                Console.println("Digite um número válido para bombas!");
                Console.print("Digite o número de bombas (mínimo 1, máximo " + (l * c - 1) + "): ");
            }
            linhasPadrao = l;
            colunasPadrao = c;
            bombasPadrao = b;
            JogoConfig.setPadrao(l, c, b);
            Console.println("Configuração salva! Próximo jogo usará esses valores.");
        } catch (Exception e) {
            Console.println("Valores inválidos. Configuração não alterada.");
        }
    }
}
