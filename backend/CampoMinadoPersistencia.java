package backend;

import mecanicas.Carta;
import cores.StringColorida;
import java.io.*;
import java.util.Scanner;

/**
 * Classe utilitária para persistência do estado do jogo Campo Minado.
 * Permite salvar e carregar o tabuleiro em arquivo texto simples.
 */
public class CampoMinadoPersistencia {

    private static String SAVE_DIR = "saves";
    private static String EXTENSAO = ".save";

    private static String caminhoCompleto(String nome) {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) dir.mkdirs();
        if (!nome.endsWith(EXTENSAO)) nome += EXTENSAO;
        return SAVE_DIR + File.separator + nome;
    }

    public static void salvarJogo(CampoMinadoTabuleiro tabuleiro, String nome) throws IOException {
        String caminho = caminhoCompleto(nome);
        try (PrintWriter out = new PrintWriter(new FileWriter(caminho))) {
            out.println(tabuleiro.getTotalLinhas() + "," + tabuleiro.getTotalColunas() + "," + tabuleiro.getBombas());
            for (int i = 0; i < tabuleiro.getTotalLinhas(); i++) {
                for (int j = 0; j < tabuleiro.getTotalColunas(); j++) {
                    Carta carta = tabuleiro.pegaCarta(i, j);
                    String tipo = (carta instanceof CampoMinadoCarta) ? "C" : "F";
                    int bomba = (carta instanceof CampoMinadoCarta && ((CampoMinadoCarta)carta).temBomba()) ? 1 : 0;
                    int numero = (carta instanceof CampoMinadoCarta) ? ((CampoMinadoCarta)carta).getNumero() : 0;
                    int virada = carta.estaViradaParaCima() ? 1 : 0;
                    String frente = carta.getFrente().toString();
                    out.printf("%s,%d,%d,%d,%d,%d,%s\n", tipo, bomba, numero, virada, i, j, frente);
                    tabuleiro.colocaCarta(i, j, carta);
                }
            }
        }
    }

    public static CampoMinadoTabuleiro carregarJogo(String nome) throws IOException {
        String caminho = caminhoCompleto(nome);
        try (Scanner in = new Scanner(new File(caminho))) {
            String[] meta = in.nextLine().split(",");
            int linhas = Integer.parseInt(meta[0]);
            int colunas = Integer.parseInt(meta[1]);
            int bombas = Integer.parseInt(meta[2]);
            CampoMinadoTabuleiro tabuleiro = new CampoMinadoTabuleiro(linhas, colunas, bombas);
            while (in.hasNextLine()) {
                String linha = in.nextLine();
                String[] dados = linha.split(",");
                String tipo = dados[0];
                int bomba = Integer.parseInt(dados[1]);
                int numero = Integer.parseInt(dados[2]);
                int virada = Integer.parseInt(dados[3]);
                int i = Integer.parseInt(dados[4]);
                int j = Integer.parseInt(dados[5]);
                String frente = dados.length > 6 ? dados[6] : "#";
                Carta carta;
                if (tipo.equals("C")) {
                    carta = new CampoMinadoCarta(bomba == 1, numero);
                } else {
                    carta = new Carta(new StringColorida(frente), new StringColorida("#")) {};
                }
                if (virada == 1) carta.vira();
                tabuleiro.colocaCarta(i, j, carta);
            }
            return tabuleiro;
        } catch (Exception e) {
            throw new IOException("Erro ao carregar o jogo: " + e.getMessage());
        }
    }

    public static String[] listarSaves() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) return new String[0];
        return dir.list(new FilenameFilter() {
            public boolean accept(File d, String name) {
                return name.endsWith(EXTENSAO);
            }
        });
    }
}
