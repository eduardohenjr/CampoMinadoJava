package backend;


import mecanicas.Carta;
import cores.StringColorida;

/**
 * Representa o tabuleiro do jogo Campo Minado.
 * Subclasse de Tabuleiro da engine CCGM.
 */
public class CampoMinadoCarta extends Carta {
    private final boolean temBomba;
    private final int numero;

    public CampoMinadoCarta(boolean temBomba, int numero) {
        super(
            temBomba ? new StringColorida("*") : (numero > 0 ? new StringColorida(Integer.toString(numero)) : new StringColorida(" ")),
            new StringColorida("#"),
            false // inicia virada para baixo
        );
        this.temBomba = temBomba;
        this.numero = numero;
    }

    public boolean temBomba() {
        return temBomba;
    }

    public int getNumero() {
        return numero;
    }
}
