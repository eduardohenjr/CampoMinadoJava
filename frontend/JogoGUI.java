package frontend;

import backend.CampoMinadoConfig;
import backend.CampoMinadoTabuleiro;
import mecanicas.Carta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Tela gráfica do jogo Campo Minado (Swing) - versão inicial.
 * Mostra o tabuleiro e permite abrir casas e marcar/desmarcar bandeiras.
 */
public class JogoGUI extends JFrame {
    private CampoMinadoTabuleiro tabuleiro;
    private JButton[][] botoes;
    private int linhas, colunas;
    private Color corFundo;
    private Color corNumero;
    private String simboloBomba;
    private String simboloBandeira;
    private static final String[] OPCOES_BOMBA = {"*", "B", "@", "X", "#", "\uD83D\uDCA3"};
    private static final String[] OPCOES_BANDEIRA = {"F", "!", "#", "B", "P"};
    private JLabel statusLabel;
    private JPanel grid;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private static final String UNICODE_BOMBA = "\uD83D\uDCA3"; // 💣
    private boolean jogoEncerrado = false;

    public JogoGUI(int linhas, int colunas, int bombas) {
        super("\uD83D\uDCA3 Campo Minado");
        this.linhas = linhas;
        this.colunas = colunas;
        this.tabuleiro = new CampoMinadoTabuleiro(linhas, colunas, bombas);
        this.botoes = new JButton[linhas][colunas];
        CampoMinadoConfig.Preferencias pref = CampoMinadoConfig.carregarPreferencias();
        this.corFundo = pref.getCorFundoColor();
        this.corNumero = pref.getCorNumeroColor();
        this.simboloBomba = pref.simboloBomba;
        this.simboloBandeira = pref.simboloBandeira;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(Math.max(400, 40 * colunas), Math.max(400, 40 * linhas + 120)));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        topPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("\uD83D\uDCA3 Campo Minado", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(titulo, BorderLayout.NORTH);
        statusLabel = new JLabel("Bombas: " + bombas + " | Casas abertas: 0", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(statusLabel, BorderLayout.SOUTH);
        // Botão de pause
        JButton pauseBtn = new JButton("\u23F8 Pause");
        pauseBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        pauseBtn.addActionListener(e -> mostrarMenuPause());
        topPanel.add(pauseBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        grid = new JPanel(new GridLayout(linhas, colunas, 2, 2));
        grid.setBackground(Color.DARK_GRAY);
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Monospaced", Font.BOLD, 22));
                btn.setFocusPainted(false);
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                final int li = i, co = j;
                btn.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            abrirCasa(li, co);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            alternarBandeira(li, co);
                        }
                    }
                });
                botoes[i][j] = btn;
                grid.add(btn);
            }
        }
        add(grid, BorderLayout.CENTER);

        bottomPanel = new JPanel(new BorderLayout());
        JLabel dica = new JLabel("Dica: Clique esquerdo para abrir, direito para marcar bandeira.", SwingConstants.CENTER);
        dica.setFont(new Font("Arial", Font.ITALIC, 13));
        bottomPanel.add(dica, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        atualizarTabuleiro();
    }

    public JogoGUI(CampoMinadoTabuleiro tabuleiro) {
        super("\uD83D\uDCA3 Campo Minado");
        this.tabuleiro = tabuleiro;
        this.linhas = tabuleiro.getTotalLinhas();
        this.colunas = tabuleiro.getTotalColunas();
        this.botoes = new JButton[linhas][colunas];

        CampoMinadoConfig.Preferencias pref = CampoMinadoConfig.carregarPreferencias();
        this.corFundo = pref.getCorFundoColor();
        this.corNumero = pref.getCorNumeroColor();
        this.simboloBomba = pref.simboloBomba;
        this.simboloBandeira = pref.simboloBandeira;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(Math.max(400, 40 * colunas), Math.max(400, 40 * linhas + 120)));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        topPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("\uD83D\uDCA3 Campo Minado", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(titulo, BorderLayout.NORTH);
        statusLabel = new JLabel("Bombas: " + tabuleiro.getBombas() + " | Casas abertas: 0", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(statusLabel, BorderLayout.SOUTH);
        JButton pauseBtn = new JButton("\u23F8 Pause");
        pauseBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        pauseBtn.addActionListener(e -> mostrarMenuPause());
        topPanel.add(pauseBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        // Painel do tabuleiro
        grid = new JPanel(new GridLayout(linhas, colunas, 2, 2));
        grid.setBackground(Color.DARK_GRAY);
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Monospaced", Font.BOLD, 22));
                btn.setFocusPainted(false);
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                final int li = i, co = j;
                btn.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            abrirCasa(li, co);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            alternarBandeira(li, co);
                        }
                    }
                });
                botoes[i][j] = btn;
                grid.add(btn);
            }
        }
        add(grid, BorderLayout.CENTER);
        // Painel inferior com dicas
        bottomPanel = new JPanel(new BorderLayout());
        JLabel dica = new JLabel("Dica: Clique esquerdo para abrir, direito para marcar bandeira.", SwingConstants.CENTER);
        dica.setFont(new Font("Arial", Font.ITALIC, 13));
        bottomPanel.add(dica, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        atualizarTabuleiro();
    }

    private void mostrarMenuPause() {
        String[] opcoes = {"Voltar para o jogo", "Iniciar novo jogo", "Voltar ao Menu Principal", "Salvar jogo", "Sair do jogo"};
        int escolha = JOptionPane.showOptionDialog(this,
                "=== MENU DE PAUSE ===",
                "Pause",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]);
        if (escolha == 1) { // Iniciar novo jogo
            dispose();
            MainGUI.abrirNovoJogo(linhas, colunas, tabuleiro.getBombas());
        } else if (escolha == 2) { // Voltar ao Menu Principal
            dispose();
            MainGUI.mostrarMenuPrincipal();
        } else if (escolha == 3) { // Salvar jogo
            String nome = JOptionPane.showInputDialog(this, "Digite o nome do save (sem extensão):");
            if (nome != null && !nome.trim().isEmpty()) {
                try {
                    backend.CampoMinadoPersistencia.salvarJogo(tabuleiro, nome.trim());
                    JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso!", "Salvar", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            mostrarMenuPause();
        } else if (escolha == 4) { // Sair do jogo
            System.exit(0);
        }
        // escolha == 0: Voltar para o jogo (não faz nada)
    }

    private void mostrarMenuPersonalizacao() {
        Color novaCorFundo = JColorChooser.showDialog(this, "Escolha a cor de fundo", corFundo);
        if (novaCorFundo != null) corFundo = novaCorFundo;
        Color novaCorNumero = JColorChooser.showDialog(this, "Escolha a cor dos números", corNumero);

        if (novaCorNumero != null) corNumero = novaCorNumero;
        String novoSimboloBomba = (String) JOptionPane.showInputDialog(this, "Escolha o símbolo da bomba:", "Símbolo da Bomba", JOptionPane.PLAIN_MESSAGE, null, OPCOES_BOMBA, simboloBomba);

        if (novoSimboloBomba != null) simboloBomba = novoSimboloBomba;
        String novoSimboloBandeira = (String) JOptionPane.showInputDialog(this, "Escolha o símbolo da bandeira:", "Símbolo da Bandeira", JOptionPane.PLAIN_MESSAGE, null, OPCOES_BANDEIRA, simboloBandeira);

        if (novoSimboloBandeira != null) simboloBandeira = novoSimboloBandeira;
        String corFundoHex = String.format("#%02x%02x%02x", corFundo.getRed(), corFundo.getGreen(), corFundo.getBlue());
        String corNumeroHex = String.format("#%02x%02x%02x", corNumero.getRed(), corNumero.getGreen(), corNumero.getBlue());
        CampoMinadoConfig.salvarPreferencias(simboloBomba, simboloBandeira, corFundoHex, corNumeroHex);
        atualizarTabuleiro();
    }

    private void abrirCasa(int l, int c) {
        if (jogoEncerrado) return;
        try {
            tabuleiro.abrirCasa(l, c);
            if (tabuleiro.venceu()) {
                jogoEncerrado = true;
                setEnabled(false); 
                desabilitarTabuleiro();
                atualizarTabuleiro();
                backend.CampoMinadoHistorico.registrar("Vitória", linhas, colunas, tabuleiro.getBombas());
                JOptionPane.showMessageDialog(this, "Você venceu!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }
            atualizarTabuleiro();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("bomba")) {
                if (!jogoEncerrado) {
                    jogoEncerrado = true;
                    setEnabled(false);
                    desabilitarTabuleiro();
                    tabuleiro.revelarBombas();
                    atualizarTabuleiro();
                    backend.CampoMinadoHistorico.registrar("Perdeu", linhas, colunas, tabuleiro.getBombas());
                    JOptionPane.showMessageDialog(this, "Bomba encontrada! Fim de jogo.", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void desabilitarTabuleiro() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                botoes[i][j].setEnabled(false);
                for (MouseListener ml : botoes[i][j].getMouseListeners()) {
                    botoes[i][j].removeMouseListener(ml);
                }
            }
        }
    }

    private void alternarBandeira(int l, int c) {
        if (jogoEncerrado) return;
        try {
            tabuleiro.alternarBandeira(l, c);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        atualizarTabuleiro();
    }

    private void atualizarTabuleiro() {
        CampoMinadoConfig.Preferencias pref = CampoMinadoConfig.carregarPreferencias();
        this.corFundo = pref.getCorFundoColor();
        this.corNumero = pref.getCorNumeroColor();
        this.simboloBomba = pref.simboloBomba;
        this.simboloBandeira = pref.simboloBandeira;
        int abertas = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                mecanicas.PilhaDeCartas pilha = tabuleiro.getPilha(i, j);
                Carta carta = pilha.verTopo();
                JButton btn = botoes[i][j];
                btn.setOpaque(true);
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                btn.setFont(new Font("Monospaced", Font.BOLD, 22));
                if (backend.CampoMinadoTabuleiro.isBandeira(carta)) {
                    btn.setText(simboloBandeira);
                    btn.setBackground(Color.YELLOW);
                    btn.setForeground(Color.BLACK);
                } else if (carta.estaViradaParaCima()) {
                    if (carta instanceof backend.CampoMinadoCarta) {
                        backend.CampoMinadoCarta cmc = (backend.CampoMinadoCarta) carta;
                        if (cmc.temBomba()) {
                            btn.setText(simboloBomba.equals("*") ? UNICODE_BOMBA : simboloBomba);
                            btn.setBackground(Color.RED);
                            btn.setForeground(Color.WHITE);
                        } else if (cmc.getNumero() > 0) {
                            btn.setText(String.valueOf(cmc.getNumero()));
                            btn.setBackground(corFundo);
                            btn.setForeground(corNumero);
                            abertas++;
                        } else {
                            btn.setText("");
                            btn.setBackground(corFundo);
                            btn.setForeground(Color.BLACK);
                            abertas++;
                        }
                    } else {
                        btn.setText(""); // Nunca mostra ?
                        btn.setBackground(corFundo);
                        btn.setForeground(Color.BLACK);
                    }
                } else {
                    btn.setText("");
                    btn.setBackground(Color.WHITE);
                }
            }
        }
        statusLabel.setText("Bombas: " + tabuleiro.getBombas() + " | Casas abertas: " + abertas);
    }
}
