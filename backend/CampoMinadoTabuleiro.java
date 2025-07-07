package backend;

import mecanicas.Tabuleiro;
import mecanicas.Carta;
import cores.StringColorida;

/**
 * Representa o tabuleiro do jogo Campo Minado.
 * Subclasse de Tabuleiro da engine CCGM.
 */
public class CampoMinadoTabuleiro extends Tabuleiro {
    private int bombas;

    public CampoMinadoTabuleiro(int linhas, int colunas, int bombas) {
        super(linhas, colunas, new CampoMinadoCarta(false, 0));
        this.bombas = bombas;
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        boolean[][] bombasArray = new boolean[getTotalLinhas()][getTotalColunas()];
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                this.colocaCarta(i, j, new CampoMinadoCarta(false, 0));
            }
        }
        // Distribui bombas e marca no array auxiliar
        int bombasColocadas = 0;
        java.util.Random rand = new java.util.Random();
        while (bombasColocadas < bombas) {
            int i = rand.nextInt(getTotalLinhas());
            int j = rand.nextInt(getTotalColunas());
            if (!bombasArray[i][j]) {
                this.colocaCarta(i, j, new CampoMinadoCarta(true, 0));
                bombasArray[i][j] = true;
                bombasColocadas++;
            }
        }
        // Calcula números usando apenas o array auxiliar
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                if (!bombasArray[i][j]) {
                    int count = 0;
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            if (x >= 0 && x < getTotalLinhas() && y >= 0 && y < getTotalColunas()) {
                                if (bombasArray[x][y]) count++;
                            }
                        }
                    }
                    this.colocaCarta(i, j, new CampoMinadoCarta(false, count));
                }
            }
        }
    }

    // Métodos de abrirCasa e alternarBandeira são específicos do jogo e permanecem
    public void abrirCasa(int linha, int coluna) throws Exception {
        if (linha < 0 || linha >= getTotalLinhas() || coluna < 0 || coluna >= getTotalColunas()) {
            throw new Exception("Posição inválida!");
        }
        Carta carta = pegaCarta(linha, coluna);
        if (carta instanceof CampoMinadoCarta) {
            CampoMinadoCarta cmc = (CampoMinadoCarta) carta;
            if (cmc.temBomba()) {
                carta.vira();
                this.colocaCarta(linha, coluna, carta);
                throw new Exception("Bomba encontrada! Fim de jogo.");
            }
            carta.vira();
            this.colocaCarta(linha, coluna, carta);
            if (cmc.getNumero() == 0) {
                for (int i = linha - 1; i <= linha + 1; i++) {
                    for (int j = coluna - 1; j <= coluna + 1; j++) {
                        if (i >= 0 && i < getTotalLinhas() && j >= 0 && j < getTotalColunas() && (i != linha || j != coluna)) {
                            Carta vizinha = pegaCarta(i, j);
                            if (vizinha instanceof CampoMinadoCarta && !vizinha.estaViradaParaCima()) {
                                this.colocaCarta(i, j, vizinha);
                                abrirCasa(i, j);
                            } else {
                                this.colocaCarta(i, j, vizinha);
                            }
                        }
                    }
                }
            }
        } else if (carta.getFrente().toString().equals("F")) {
            this.colocaCarta(linha, coluna, carta);
            return;
        } else {
            this.colocaCarta(linha, coluna, carta);
        }
    }

    public void alternarBandeira(int linha, int coluna) throws Exception {
        if (linha < 0 || linha >= getTotalLinhas() || coluna < 0 || coluna >= getTotalColunas()) {
            throw new Exception("Posição inválida!");
        }
        Carta carta = pegaCarta(linha, coluna);
        if (carta.getFrente().toString().equals("F")) {
            Carta debaixo = pegaCarta(linha, coluna);
            this.colocaCarta(linha, coluna, debaixo);
        } else if (!carta.estaViradaParaCima()) {
            this.colocaCarta(linha, coluna, carta);
            this.colocaCarta(linha, coluna, new Carta(new StringColorida("F"), new StringColorida("#")) {});
        } else {
            this.colocaCarta(linha, coluna, carta);
        }
    }

    public int getBombas() {
        return this.bombas;
    }
}
