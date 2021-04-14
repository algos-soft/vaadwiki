package it.algos.vaadflow14.backend.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 12-set-2020
 * Time: 08:15
 */
public enum AEStatoEuropeo {
    italia("Italia", 1),

    austria("Austria", 2),

    belgio("Belgio", 3),

    bulgaria("Bulgaria", 4),

    cipro("Cipro", 5),

    croazia("Croazia", 6),

    danimarca("Danimarca", 7),

    estonia("Estonia", 8),

    finlandia("Finlandia", 9),

    francia("Francia", 10),

    germania("Germania", 11),

    grecia("Grecia", 12),

    irlanda("Irlanda", 13),

    lettonia("Lettonia", 14),

    lituania("Lituania", 15),

    lussemburgo("Lussemburgo", 16),

    malta("Malta", 17),

    olanda("Paesi Bassi", 18),

    polonia("Polonia", 19),

    portogallo("Portogallo", 20),

    cechia("Repubblica Ceca", 21),

    romania("Romania", 22),

    slovacchia("Slovacchia", 23),

    slovenia("Slovenia", 24),

    spagna("Spagna", 25),

    svezia("Svezia", 26),

    ungheria("Ungheria", 27),
    ;

    private String nome;

    private int pos;


    AEStatoEuropeo(String nome, int pos) {
        this.nome = nome;
        this.pos = pos;
    }


    public static int getPosizione(String nome) {
        int pos = 0;

        for (AEStatoEuropeo stato : AEStatoEuropeo.values()) {
            if (stato.nome.equals(nome)) {
                pos = stato.pos;
            }
        }

        return pos;
    }


    public int getPos() {
        return pos;
    }


    public String getNome() {
        return nome;
    }

}
