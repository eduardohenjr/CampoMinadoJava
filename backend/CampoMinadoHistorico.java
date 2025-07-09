package backend;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

/**
 * Classe utilitária para registrar e consultar o histórico de partidas do Campo Minado.
 */
public class CampoMinadoHistorico {
    private static final String ARQUIVO = "historico.txt";

    public static void registrar(String resultado, int linhas, int colunas, int bombas) {
        try (PrintWriter out = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            String data = new Date().toString();
            out.printf("%s | %s | %dx%d | %d bombas\n", data, resultado, linhas, colunas, bombas);
        } catch (IOException e) {
        }
    }
    
    public static String lerHistorico() {
        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(new File(ARQUIVO))) {
            while (in.hasNextLine()) {
                sb.append(in.nextLine()).append("\n");
            }
        } catch (IOException e) {
            sb.append("Nenhum histórico encontrado.");
        }
        return sb.toString();
    }
}
