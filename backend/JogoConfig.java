package backend;

import java.io.*;

/**
 * Utilitário para armazenar e persistir as configurações padrão do jogo (linhas, colunas, bombas).
 * Usado pela GUI e pode ser acessado pelo console se desejado.
 */
public class JogoConfig {
    private static int linhasPadrao = 5;
    private static int colunasPadrao = 5;
    private static int bombasPadrao = 5;
    private static final String CONFIG_DIR = "savesconfig";
    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config_jogo.save";

    static {
        carregar();
    }

    public static int getLinhasPadrao() { return linhasPadrao; }
    public static int getColunasPadrao() { return colunasPadrao; }
    public static int getBombasPadrao() { return bombasPadrao; }

    public static void setPadrao(int l, int c, int b) {
        linhasPadrao = l;
        colunasPadrao = c;
        bombasPadrao = b;
        salvar();
    }

    private static void carregar() {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) dir.mkdirs();
        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
            linhasPadrao = Integer.parseInt(br.readLine());
            colunasPadrao = Integer.parseInt(br.readLine());
            bombasPadrao = Integer.parseInt(br.readLine());
        } catch (Exception e) {
        }
    }

    private static void salvar() {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) dir.mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            pw.println(linhasPadrao);
            pw.println(colunasPadrao);
            pw.println(bombasPadrao);
        } catch (Exception e) {
        }
    }
}
