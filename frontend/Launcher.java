package frontend;

import javax.swing.JOptionPane;

public class Launcher {
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("console")) {
                // Suporte ao menu de pause gráfico
                if (args.length > 1 && args[1].equalsIgnoreCase("--gui-pause")) {
                    String[] sub = subArray(args, 2);
                    String[] newArgs = new String[sub.length + 1];
                    newArgs[0] = "--gui-pause";
                    System.arraycopy(sub, 0, newArgs, 1, sub.length);
                    Main.main(newArgs);
                } else {
                    Main.main(subArray(args, 1));
                }
                return;
            } else if (
                args[0].equalsIgnoreCase("gui") ||
                args[0].equalsIgnoreCase("swing") ||
                args[0].equalsIgnoreCase("--gui")
            ) {
                MainGUI.main(subArray(args, 1));
                return;
            } else if (args[0].equalsIgnoreCase("--gui-principal")) {
                Main.main(args);
                return;
            } else if (args[0].equalsIgnoreCase("--gui-jogo")) {
                int l = backend.JogoConfig.getLinhasPadrao();
                int c = backend.JogoConfig.getColunasPadrao();
                int b = backend.JogoConfig.getBombasPadrao();
                MainGUI.abrirNovoJogo(l, c, b);
                return;
            } else if (args[0].equalsIgnoreCase("--gui-arquivos")) {
                // Abre seletor gráfico de arquivos para carregar um save, joga pelo console
                javax.swing.SwingUtilities.invokeLater(() -> {
                    String[] saves = backend.CampoMinadoPersistencia.listarSaves();
                    if (saves.length == 0) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Nenhum save encontrado.", "Carregar Jogo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    String[] nomes = new String[saves.length];
                    for (int i = 0; i < saves.length; i++) {
                        String s = saves[i];
                        nomes[i] = s.endsWith(".save") ? s.substring(0, s.length() - 5) : s;
                    }
                    String escolha = (String) javax.swing.JOptionPane.showInputDialog(null, "Selecione o save para carregar:", "Carregar Jogo", javax.swing.JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);
                    if (escolha != null && !escolha.trim().isEmpty()) {
                        Main.main(new String[]{"carregar", escolha.trim()});
                    }
                });
                return;
            }
        }
        Object[] opcoes = {"Console", "Interface Gráfica (Swing)", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(null,
                "Escolha a interface para jogar Campo Minado:",
                "Campo Minado - Launcher",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);
        if (escolha == 0) {
            Main.main(new String[0]);
        } else if (escolha == 1) {
            MainGUI.main(new String[0]);
        } else {
            System.exit(0);
        }
    }
    private static String[] subArray(String[] arr, int start) {
        if (start >= arr.length) return new String[0];
        String[] out = new String[arr.length - start];
        System.arraycopy(arr, start, out, 0, out.length);
        return out;
    }
}
