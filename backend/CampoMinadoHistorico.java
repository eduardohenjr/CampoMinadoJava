package backend;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

/**
 * Classe utilitária para registrar e consultar o histórico de partidas do Campo Minado.
 */
public class CampoMinadoHistorico {
    private static String arquivo = "historico.txt";

    public static void setArquivo(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            if (!nome.endsWith(".txt")) nome += ".txt";
            arquivo = nome;
        }
    }

    public static void registrar(String resultado, int linhas, int colunas, int bombas) {
        try (PrintWriter out = new PrintWriter(new FileWriter(arquivo, true))) {
            String data = new Date().toString();
            out.printf("%s | %s | %dx%d | %d bombas\n", data, resultado, linhas, colunas, bombas);
        } catch (IOException e) {
        }
    }
    
    public static String lerHistorico() {
        String historico = "";
        try (Scanner in = new Scanner(new File(arquivo))) {
            while (in.hasNextLine()) {
                historico = historico + in.nextLine() + "\n";
            }
        } catch (IOException e) {
            historico = historico + "Nenhum histórico encontrado.";
        }
        return historico;
    }
}
