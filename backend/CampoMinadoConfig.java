package backend;

import java.awt.Color;
import java.io.*;
import java.util.Properties;

/**
 * Utilitário para salvar e carregar preferências de personalização do Campo Minado.
 */
public class CampoMinadoConfig {
    private static final String CONFIG_DIR = "savesconfig";
    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config.save";

    public static void salvarPreferencias(String simboloBomba, String simboloBandeira, String corFundo, String corNumero) {
        Properties props = new Properties();
        props.setProperty("simboloBomba", simboloBomba);
        props.setProperty("simboloBandeira", simboloBandeira);
        props.setProperty("corFundo", corFundo);
        props.setProperty("corNumero", corNumero);
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) dir.mkdirs();
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, null);
        } catch (IOException ignored) {}
    }

    public static Preferencias carregarPreferencias() {
        Properties props = new Properties();
        Preferencias pref = new Preferencias();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            props.load(in);
            pref.simboloBomba = props.getProperty("simboloBomba", "*");
            pref.simboloBandeira = props.getProperty("simboloBandeira", "F");
            pref.corFundo = props.getProperty("corFundo", "#D3D3D3");
            pref.corNumero = props.getProperty("corNumero", "#0000FF");
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
