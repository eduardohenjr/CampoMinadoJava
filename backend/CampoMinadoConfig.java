package backend;

import java.awt.Color;
import java.io.*;

/**
 * Utilitário para salvar e carregar preferências de personalização do Campo Minado.
 */
public class CampoMinadoConfig {
    private static String CONFIG_DIR = "savesconfig";
    private static String CONFIG_FILE = CONFIG_DIR + File.separator + "config.save";

    public static void salvarPreferencias(String simboloBomba, String simboloBandeira, String corFundo, String corNumero) {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) dir.mkdirs();
        try (PrintWriter out = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            out.println(simboloBomba);
            out.println(simboloBandeira);
            out.println(corFundo);
            out.println(corNumero);
        } catch (IOException ignored) {}
    }

    public static Preferencias carregarPreferencias() {
        Preferencias pref = new Preferencias();
        try (BufferedReader in = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String linha;
            if ((linha = in.readLine()) != null) pref.simboloBomba = linha;
            if ((linha = in.readLine()) != null) pref.simboloBandeira = linha;
            if ((linha = in.readLine()) != null) pref.corFundo = linha;
            if ((linha = in.readLine()) != null) pref.corNumero = linha;
        } catch (IOException ignored) {}
        return pref;
    }

    public static class Preferencias {
        public String simboloBomba = "*";
        public String simboloBandeira = "F";
        public String corFundo = "#D3D3D3";
        public String corNumero = "#0000FF";
        public Color getCorFundoColor() {
            try { return Color.decode(corFundo); } catch (Exception e) { return Color.LIGHT_GRAY; }
        }
        public Color getCorNumeroColor() {
            try { return Color.decode(corNumero); } catch (Exception e) { return Color.BLUE; }
        }
    }
}
