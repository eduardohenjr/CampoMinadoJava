package frontend;

import backend.CampoMinadoConfig;
import javax.swing.*;
import java.awt.*;

/**
 * Tela de personalização de símbolos e cores compartilhada entre console e GUI.
 */
public class PersonalizacaoGUI {
    public static void mostrar(JFrame parent) {
        CampoMinadoConfig.Preferencias pref = CampoMinadoConfig.carregarPreferencias();
        Color corFundo = pref.getCorFundoColor();
        Color corNumero = pref.getCorNumeroColor();
        String simboloBomba = pref.simboloBomba;
        String simboloBandeira = pref.simboloBandeira;
        String[] opcoesBomba = {"*", "B", "@"};
        String[] opcoesBandeira = {"F", "!", "#"};


        Color novaCorFundo = JColorChooser.showDialog(parent, "Escolha a cor de fundo", corFundo);
        if (novaCorFundo != null) corFundo = novaCorFundo;

        Color novaCorNumero = JColorChooser.showDialog(parent, "Escolha a cor dos números", corNumero);
        if (novaCorNumero != null) corNumero = novaCorNumero;
   
        String novoSimboloBomba = (String) JOptionPane.showInputDialog(parent, "Escolha o símbolo da bomba:", "Símbolo da Bomba", JOptionPane.PLAIN_MESSAGE, null, opcoesBomba, simboloBomba);
        if (novoSimboloBomba != null) simboloBomba = novoSimboloBomba;
    
        String novoSimboloBandeira = (String) JOptionPane.showInputDialog(parent, "Escolha o símbolo da bandeira:", "Símbolo da Bandeira", JOptionPane.PLAIN_MESSAGE, null, opcoesBandeira, simboloBandeira);
        if (novoSimboloBandeira != null) simboloBandeira = novoSimboloBandeira;

        String corFundoHex = String.format("#%02x%02x%02x", corFundo.getRed(), corFundo.getGreen(), corFundo.getBlue());
        String corNumeroHex = String.format("#%02x%02x%02x", corNumero.getRed(), corNumero.getGreen(), corNumero.getBlue());
        CampoMinadoConfig.salvarPreferencias(simboloBomba, simboloBandeira, corFundoHex, corNumeroHex);
        JOptionPane.showMessageDialog(parent, "Personalização salva!", "Personalizar", JOptionPane.INFORMATION_MESSAGE);
    }
}
