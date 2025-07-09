package backend;

import mecanicas.Tabuleiro;
import mecanicas.Carta;
import mecanicas.PilhaDeCartas;

public class CampoMinadoTabuleiro extends Tabuleiro {
    private int bombas;

    public CampoMinadoTabuleiro(int linhas, int colunas, int bombas) {
        super(linhas, colunas, new CampoMinadoCarta(false, 0));
        this.bombas = bombas;
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        boolean[][] bombasArray = distribuirBombas();
        calcularNumeros(bombasArray);
    }

    private boolean[][] distribuirBombas() {
        boolean[][] bombasArray = new boolean[getTotalLinhas()][getTotalColunas()];
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                this.colocaCarta(i, j, new CampoMinadoCarta(false, 0));
            }
        }
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
        return bombasArray;
    }

    private void calcularNumeros(boolean[][] bombasArray) {
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                if (!bombasArray[i][j]) {
                    int count = 0;
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            if (estaDentro(x, y) && bombasArray[x][y]) count++;
                        }
                    }
                    this.colocaCarta(i, j, new CampoMinadoCarta(false, count));
                }
            }
        }
    }

    private boolean estaDentro(int linha, int coluna) {
        return linha >= 0 && linha < getTotalLinhas() && coluna >= 0 && coluna < getTotalColunas();
    }

    public void abrirCasa(int linha, int coluna) throws Exception {
        if (!estaDentro(linha, coluna)) {
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
                        if (estaDentro(i, j) && (i != linha || j != coluna)) {
                            Carta vizinha = pegaCarta(i, j);
                            if (vizinha instanceof CampoMinadoCarta && !vizinha.estaViradaParaCima()) {
                                vizinha.vira();
                                this.colocaCarta(i, j, vizinha);
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
        if (!estaDentro(linha, coluna)) {
            throw new Exception("Posição inválida!");
        }
        PilhaDeCartas pilha = getPilha(linha, coluna);
        Carta topo = pilha.verTopo();
        if (isBandeira(topo)) {
            pilha.compra();
        } else if (!topo.estaViradaParaCima()) {
            String simboloBandeira = CampoMinadoConfig.carregarPreferencias().simboloBandeira;
            pilha.insereTopo(new CampoMinadoCarta(false, -1) {
                @Override
                public String toString() {
                    return simboloBandeira;
                }
                @Override
                public cores.StringColorida getFrente() {
                    return new cores.StringColorida(simboloBandeira);
                }
            });
        }
    }

    public int getBombas() {
        return this.bombas;
    }

    public static boolean isBandeira(Carta carta) {
        if (carta == null) return false;
        if (carta instanceof CampoMinadoCarta) {
            CampoMinadoCarta cmc = (CampoMinadoCarta) carta;
            if (cmc.getNumero() == -1) return true;
        }
        String simboloBandeira = CampoMinadoConfig.carregarPreferencias().simboloBandeira;
        return carta.getFrente().toString().equals(simboloBandeira);
    }

    public boolean venceu() {
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                Carta carta = pegaCarta(i, j);
                boolean ok = true;
                if (carta instanceof CampoMinadoCarta) {
                    CampoMinadoCarta c = (CampoMinadoCarta) carta;
                    if (!c.temBomba() && !c.estaViradaParaCima()) {
                        ok = false;
                    }
                }
                colocaCarta(i, j, carta);
                if (!ok) return false;
            }
        }
        return true;
    }

    public boolean perdeu() {
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                Carta carta = pegaCarta(i, j);
                boolean lost = false;
                if (carta instanceof CampoMinadoCarta) {
                    CampoMinadoCarta c = (CampoMinadoCarta) carta;
                    if (c.temBomba() && c.estaViradaParaCima()) {
                        lost = true;
                    }
                }
                colocaCarta(i, j, carta);
                if (lost) return true;
            }
        }
        return false;
    }

    public void revelarBombas() {
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                Carta carta = pegaCarta(i, j);
                if (carta instanceof CampoMinadoCarta) {
                    CampoMinadoCarta c = (CampoMinadoCarta) carta;
                    if (c.temBomba() && !c.estaViradaParaCima()) {
                        c.vira();
                    }
                }
                colocaCarta(i, j, carta);
            }
        }
    }

    @Override
    public String toString() {
        CampoMinadoConfig.Preferencias pref = CampoMinadoConfig.carregarPreferencias();
        String simboloBomba = pref.simboloBomba;
        String simboloBandeira = pref.simboloBandeira;
        String corFundo = pref.corFundo;
        String corNumero = pref.corNumero;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getTotalLinhas(); i++) {
            for (int j = 0; j < getTotalColunas(); j++) {
                Carta carta = pegaCarta(i, j);
                if (carta.estaViradaParaCima()) {
                    if (carta instanceof CampoMinadoCarta) {
                        CampoMinadoCarta cmc = (CampoMinadoCarta) carta;
                        if (cmc.temBomba()) {
                            sb.append(simboloBomba);
                        } else if (cmc.getNumero() > 0) {
                            cores.Cor corNumEnum;
                            try { corNumEnum = cores.Cor.valueOf(corNumero); } catch (Exception e) { corNumEnum = cores.Cor.RESET; }
                            sb.append(new cores.StringColorida(String.valueOf(cmc.getNumero()), corNumEnum));
                        } else {
                            cores.Cor corFundoEnum;
                            try { corFundoEnum = cores.Cor.valueOf(corFundo); } catch (Exception e) { corFundoEnum = cores.Cor.RESET; }
                            sb.append(new cores.StringColorida(" ", corFundoEnum));
                        }
                    } else {
                        sb.append("?");
                    }
                } else if (isBandeira(carta)) {
                    sb.append(simboloBandeira);
                } else {
                    cores.Cor corFundoEnum;
                    try { corFundoEnum = cores.Cor.valueOf(corFundo); } catch (Exception e) { corFundoEnum = cores.Cor.RESET; }
                    sb.append(new cores.StringColorida("#", corFundoEnum));
                }
                colocaCarta(i, j, carta);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
