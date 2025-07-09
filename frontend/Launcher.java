package frontend;

import javax.swing.JOptionPane;

public class Launcher {
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("console")) {
                Main.main(subArray(args, 1));
                return;
            } else if (args[0].equalsIgnoreCase("gui") || args[0].equalsIgnoreCase("swing")) {
                MainGUI.main(subArray(args, 1));
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
