package backend;

import mecanicas.Carta;
import cores.StringColorida;
import java.io.*;

/**
 * Classe utilitária para persistência do estado do jogo Campo Minado.
 * Permite salvar e carregar o tabuleiro em arquivo texto simples.
 */
public class CampoMinadoPersistencia {

    public static void salvarJogo(CampoMinadoTabuleiro tabuleiro, String caminho) throws IOException {
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
                    // devolve carta ao topo do tabuleiro
                    tabuleiro.colocaCarta(i, j, carta);
                }
            }
        }
    }

    /**
     * Carrega o estado do tabuleiro de um arquivo texto simples.
     * @param caminho Caminho do arquivo origem
     * @return Tabuleiro restaurado
     * @throws IOException Se ocorrer erro de leitura ou formato inválido
     */
    public static CampoMinadoTabuleiro carregarJogo(String caminho) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(caminho))) {
            String[] meta = in.readLine().split(",");
            int linhas = Integer.parseInt(meta[0]);
            int colunas = Integer.parseInt(meta[1]);
            int bombas = Integer.parseInt(meta[2]);
            CampoMinadoTabuleiro tabuleiro = new CampoMinadoTabuleiro(linhas, colunas, bombas);
            String linha;
            while ((linha = in.readLine()) != null) {
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
}
