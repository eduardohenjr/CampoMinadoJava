package frontend;

import javax.swing.*;
import java.awt.*;
import backend.JogoConfig;

/**
 * Interface gráfica principal do Campo Minado (Swing).
 * Estrutura enxuta para integração com o backend e alternância via argumentos.
 */
public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::mostrarMenuPrincipal);
    }

    public static void mostrarMenuPrincipal() {
        JFrame frame = new JFrame("Campo Minado - Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnNovo = new JButton("Iniciar Jogo");
        JButton btnCarregar = new JButton("Carregar Jogo");
        JButton btnHistorico = new JButton("Histórico de Partidas");
        JButton btnConfig = new JButton("Configurações");
        JButton btnRegras = new JButton("Ver Regras");
        JButton btnCreditos = new JButton("Créditos");
        JButton btnSair = new JButton("Sair");
        JButton btnPersonalizar = new JButton("Personalizar");

        panel.add(btnNovo);
        panel.add(btnCarregar);
        panel.add(btnHistorico);
        panel.add(btnConfig);
        panel.add(btnRegras);
        panel.add(btnCreditos);
        panel.add(btnSair);
        panel.add(btnPersonalizar);

        btnSair.addActionListener(e -> System.exit(0));
        btnNovo.addActionListener(e -> new JogoGUI(JogoConfig.getLinhasPadrao(), JogoConfig.getColunasPadrao(), JogoConfig.getBombasPadrao()).setVisible(true));
        btnCarregar.addActionListener(e -> {
            String[] saves = backend.CampoMinadoPersistencia.listarSaves();
            if (saves.length == 0) {
                JOptionPane.showMessageDialog(frame, "Nenhum save encontrado.", "Carregar Jogo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String[] nomes = new String[saves.length];
            for (int i = 0; i < saves.length; i++) {
                String s = saves[i];
                nomes[i] = s.endsWith(".save") ? s.substring(0, s.length() - 5) : s;
            }
            String escolha = (String) JOptionPane.showInputDialog(frame, "Selecione o save para carregar:", "Carregar Jogo", JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);
            if (escolha != null && !escolha.trim().isEmpty()) {
                try {
                    backend.CampoMinadoTabuleiro tab = backend.CampoMinadoPersistencia.carregarJogo(escolha.trim());
                    new JogoGUI(tab).setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao carregar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnHistorico.addActionListener(e -> {
            String historico = backend.CampoMinadoHistorico.lerHistorico();
            JTextArea area = new JTextArea(historico);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(380, 250));
            JOptionPane.showMessageDialog(frame, scroll, "Histórico de Partidas", JOptionPane.INFORMATION_MESSAGE);
        });
        btnConfig.addActionListener(e -> {
            JPanel configPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField tfLinhas = new JTextField(String.valueOf(JogoConfig.getLinhasPadrao()));
            JTextField tfColunas = new JTextField(String.valueOf(JogoConfig.getColunasPadrao()));
            JTextField tfBombas = new JTextField(String.valueOf(JogoConfig.getBombasPadrao()));
            configPanel.add(new JLabel("Linhas (mínimo 2):"));
            configPanel.add(tfLinhas);
            configPanel.add(new JLabel("Colunas (mínimo 2):"));
            configPanel.add(tfColunas);
            configPanel.add(new JLabel("Bombas (mínimo 1):"));
            configPanel.add(tfBombas);
            int result = JOptionPane.showConfirmDialog(frame, configPanel, "Configurações do Jogo", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int l = Integer.parseInt(tfLinhas.getText().trim());
                    int c = Integer.parseInt(tfColunas.getText().trim());
                    int b = Integer.parseInt(tfBombas.getText().trim());
                    if (l < 2 || c < 2 || b < 1 || b >= l * c) throw new Exception();
                    JogoConfig.setPadrao(l, c, b);
                    JOptionPane.showMessageDialog(frame, "Configuração salva! Próximo jogo usará esses valores.", "Configurações", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Valores inválidos. Configuração não alterada.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnRegras.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "=== REGRAS DO CAMPO MINADO ===\n" +
                "- Abra casas sem bombas para vencer.\n" +
                "- Números indicam quantas bombas há nas casas vizinhas.\n" +
                "- Marque bandeiras onde suspeitar de bombas.\n" +
                "- Se abrir uma bomba, você perde.",
                "Regras", JOptionPane.INFORMATION_MESSAGE));
        btnCreditos.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "=== CRÉDITOS ===\n" +
                "Desenvolvido por: Eduardo Marques\n" +
                "Disciplina: Características das Linguagens de Programação - UERJ\n" +
                "2025",
                "Créditos", JOptionPane.INFORMATION_MESSAGE));
        btnPersonalizar.addActionListener(e -> PersonalizacaoGUI.mostrar(frame));

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
