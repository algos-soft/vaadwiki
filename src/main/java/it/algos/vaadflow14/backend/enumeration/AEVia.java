package it.algos.vaadflow14.backend.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 10-set-2020
 * Time: 11:16
 */
public enum AEVia {

    via(1),
    largo(2),
    corso(3),
    piazza(4),
    viale(5),
    piazzale(6),
    vicolo(7),
    lungomare(8),
    circonvallazione(9),
    bastioni(10),
    quartiere(11),
    porta(12),
    rione(13),
    vico(14),
    stradone(15),
    galleria(16),
    fondamenta(17),
    campo(18),
    calle(19),
    campiello(20),
    giardino(21),
    sentiero(22),
    stazione(23),
    banchi(24),
    costa(25),
    corte(26),
    ;

    private int pos;


    AEVia(int pos) {
        this.pos = pos;
    }


    public int getPos() {
        return pos;
    }


    public String getNome() {
        return this.name();
    }

}
