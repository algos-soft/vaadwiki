package it.algos.vaadflow14.backend.enumeration;


import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Created by gac on 15/10/14.
 */
public enum AESecolo {

    XXac("XX", 2000, 1901, true),

    XIXac("XIX", 1900, 1801, true),

    XVIIIac("XVIII", 1800, 1701, true),

    XVIIac("XVII", 1700, 1601, true),

    XVIac("XVI", 1600, 1501, true),

    XVac("XV", 1500, 1401, true),

    XIVac("XIV", 1400, 1301, true),

    XIIIac("XIII", 1300, 1201, true),

    XIIac("XII", 1200, 1101, true),

    XIac("XI", 1100, 1001, true),

    Xac("X", 1000, 901, true),

    IXac("IX", 900, 801, true),

    VIIIac("VIII", 800, 701, true),

    VIIac("VII", 700, 601, true),

    VIac("VI", 600, 501, true),

    Vac("V", 500, 401, true),

    IVac("IV", 400, 301, true),

    IIIac("III", 300, 201, true),

    IIac("II", 200, 101, true),

    Iac("I", 100, 1, true),

    I("I", 1, 100, false),

    II("II", 101, 200, false),

    III("III", 201, 300, false),

    IV("IV", 301, 400, false),

    V("V", 401, 500, false),

    VI("VI", 501, 600, false),

    VII("VII", 601, 700, false),

    VIII("VIII", 701, 800, false),

    IX("IX", 801, 900, false),

    X("X", 901, 1000, false),

    XI("XI", 1001, 1100, false),

    XII("XII", 1101, 1200, false),

    XIII("XIII", 1201, 1300, false),

    XIV("XIV", 1301, 1400, false),

    XV("XV", 1401, 1500, false),

    XVI("XVI", 1501, 1600, false),

    XVII("XVII", 1601, 1700, false),

    XVIII("XVIII", 1701, 1800, false),

    XIX("XIX", 1801, 1900, false),

    XX("XX", 1901, 2000, false),

    XXI("XXI", 2001, 2100, false);

    public final static String TAG_AC = " a.C.";

    private final static String SECOLO_DC = " secolo";

    private final static String SECOLO_AC = SECOLO_DC + TAG_AC;

    private final String nome;

    private final int inizio;

    private final int fine;

    private final boolean anteCristo;


    /**
     * Costruttore completo con parametri.
     *
     * @param nome     del secolo
     * @param inizio     primo anno
     * @param fine       ultimo anno
     * @param anteCristo flag booleano
     */
    AESecolo(String nome, int inizio, int fine, boolean anteCristo) {
        if (anteCristo) {
            this.nome = nome + SECOLO_AC;
        } else {
            this.nome = nome + SECOLO_DC;
        }
        this.inizio = inizio;
        this.fine = fine;
        this.anteCristo = anteCristo;
    }


    /**
     * Seleziona un secolo dall'anno indicato <br>
     * SOLO per secoli AC <br>
     *
     * @param anno indicato per la selezione del secolo
     *
     * @return secolo Ante Cristo selezionato
     */
    public static AESecolo getSecoloAC(int anno) {
        AESecolo secolo = null;
        int inizio;
        int fine;

        for (AESecolo secoloTmp : values()) {
            if (secoloTmp.anteCristo) {
                inizio = secoloTmp.inizio;
                fine = secoloTmp.fine;
                if (anno >= fine && anno <= inizio) {
                    secolo = secoloTmp;
                }
            }
        }

        return secolo;
    }


    /**
     * Seleziona un secolo dall'anno indicato <br>
     * SOLO per secoli DC <br>
     *
     * @param anno indicato per la selezione del secolo
     *
     * @return secolo Dopo Cristo selezionato
     */
    public static AESecolo getSecoloDC(int anno) {
        AESecolo secolo = null;
        int inizio;
        int fine;

        for (AESecolo secoloTmp : values()) {
            if (!secoloTmp.anteCristo) {
                inizio = secoloTmp.inizio;
                fine = secoloTmp.fine;
                if (anno >= inizio && anno <= fine) {
                    secolo = secoloTmp;
                }
            }
        }

        return secolo;
    }


    /**
     * Seleziona un secolo dall' anno indicato <br>
     * Gli anni sono negativi per secoli AC e positivi per secoli DC <br>
     *
     * @param anno (positivo o negativo) indicato per la selezione del secolo
     *
     * @return secolo selezionato
     */
    public static AESecolo getSecolo(int anno) {
        AESecolo secolo = null;

        // l' anno zero NON esiste
        if (anno == 0) {
            return null;
        }

        if (anno < 0) {
            secolo = getSecoloAC(Math.abs(anno));
        } else {
            secolo = getSecoloDC(anno);
        }

        return secolo;
    }


    /**
     * Recupera l'enumeration dal titolo <br>
     *
     * @return enumeration trovata
     */
    public static AESecolo getSecolo(String titolo) {
        AESecolo secolo = null;

        for (AESecolo secoloTmp : values()) {
            if (secoloTmp.nome.equals(titolo)) {
                secolo = secoloTmp;
            }
        }

        return secolo;
    }


    /**
     * Recupera l' ordine della enumeration dal titolo <br>
     *
     * @return ordinamento
     */
    public static int getOrder(String titolo) {
        int ordine = 0;
        AESecolo secolo = getSecolo(titolo);

        if (secolo != null) {
            ordine = secolo.ordinal() + 1;
        }

        return ordine;
    }


    public String getTextSecoloAC(int anno) {
        String titolo = VUOTA;
        AESecolo secolo = getSecoloAC(anno);

        if (secolo != null) {
            titolo = secolo.getNome();
        }

        return titolo;
    }


    public String getTextSecoloDC(int anno) {
        String titolo = VUOTA;
        AESecolo secolo = getSecoloDC(anno);

        if (secolo != null) {
            titolo = secolo.getNome();
        }

        return titolo;
    }


    public int getOrd() {
        return this.ordinal() + 1;
    }


    public String getNome() {
        return nome;
    }


    public int getInizio() {
        return inizio;
    }


    public int getFine() {
        return fine;
    }


    public boolean isAnteCristo() {
        return anteCristo;
    }


}
