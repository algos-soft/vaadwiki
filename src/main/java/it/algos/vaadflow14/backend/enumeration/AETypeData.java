package it.algos.vaadflow14.backend.enumeration;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 27-set-2018
 * Time: 16:23
 */
public enum AETypeData {

    /**
     * Usata di default <br>
     * Pattern: d MMM yyyy <br>
     * Esempio: 20 gen 2019 <br>
     */
    standard("data standard", "d MMM yyyy", "20 gen 2019", 8),


    /**
     * Pattern: d-M-yy <br>
     * Esempio: 5-4-17 <br>
     */
    dateShort("data corta", "d-M-yy", "5-4-17", 6),

    /**
     * Pattern: d-MMM-yy <br>
     * Esempio: 5-ott-14 <br>
     */
    dateNormal("data normale", "d-MMM-yy", "5-ott-14", 7),

    /**
     * Pattern: d-MMMM-yyy <br>
     * Esempio: 5-ottobre-2014 <br>
     */
    dateLong("data lunga", "d-MMMM-yyy", "5-ottobre-2014", 12),

    /**
     * Pattern: d-M <br>
     * Esempio: 5-10 <br>
     */
    meseShort("mese corto", "d-M", "5-10", 5),

    /**
     * Pattern: d-MMM <br>
     * Esempio: 5-ott <br>
     */
    meseNormal("mese normale", "d-MMM", "5-ott", 5),

    /**
     * Pattern: d MMMM <br>
     * Esempio: 5 ottobre <br>
     */
    meseLong("mese lungo", "d MMMM", "5 ottobre", 8),


    /**
     * Pattern: EEE d <br>
     * Esempio: dom 5 <br>
     */
    weekShort("settimana corta", "EEE d", "dom 5", 5),

    /**
     * Pattern: EEE, d MMM <br>
     * Esempio: dom, 5 apr <br>
     */
    weekShortMese("settimana corta mese", "EEE, d MMM", "dom, 5 apr", 7),


    /**
     * Pattern: EEEE d <br>
     * Esempio: domenica 5 <br>
     */
    weekLong("settimana lunga", "EEEE d", "domenica 5", 8),


    /**
     * Pattern: MMMM yyy <br>
     * Esempio: ottobre 2014 <br>
     */
    meseCorrente("meseCorrente", "MMMM yyy", "ottobre 2014", 10),


    /**
     * Pattern: EEEE, d-MMMM-yyy <br>
     * Esempio: domenica, 5-ottobre-2014 <br>
     */
    dataCompleta("data completa", "EEEE, d-MMMM-yyy", "domenica, 5-ottobre-2014", 16),

    /**
     * Pattern: EEEE, d-MMMM-yyy <br>
     * Esempio: dom, 5-ott-2014 <br>
     */
    dataCompletaShort("data completa short", "EEE, d-MMM-yyy", "dom, 5-ott-2014", 14),

    /**
     * Pattern: d-MMM-yy 'alle' H:mm <br>
     * Esempio: 5-ott-14 alle 13:45 <br>
     */
    breveOrario("data e orario brevi", "d-MMM-yy 'alle' H:mm", "5-ott-14 alle 13:45", 20, false),

    /**
     * Pattern: EEEE, d-MMMM-yyy 'alle' H:mm <br>
     * Esempio: domenica, 5-ottobre-2014 alle 13:45 <br>
     */
    completaOrario("data e orario completi", "EEEE, d-MMMM-yyy 'alle' H:mm", "domenica, 5-ottobre-2014 alle 13:45", 20, false),

    /**
     * Pattern: d-MMM-yy H:mm <br>
     * Esempio: 18-nov-17 13:45 <br>
     */
    normaleOrario("data e orario", "d-MMM-yy H:mm", "18-nov-17 13:45", 10, false),


    /**
     * ISO8601: yyyy-MM-dd'T'HH:mm:ss.SSSXXX <br>
     * Pattern: yyyy-MM-dd'T'HH:mm:ss <br>
     * Esempio: 2017-02-16T21:00:00.000+01:00 <br>
     */
    iso8601("data e orario iso8601", "yyyy-MM-dd'T'HH:mm:ss", "2017-02-16T21:00:00", 13, false),


    /**
     * Pattern: H:mm <br>
     * Esempio:  13:45 <br>
     */
    orario("solo orario", "H:mm", "13:45", 5, false),

    /**
     * Pattern: H:mm:ss <br>
     * Esempio:  13:45:08 <br>
     */
    orarioLungo("orario completo", "H:mm:ss", "13:45:08", 6, false),
    ;

    private String tag;

    private String pattern;

    private String esempio;

    private boolean senzaTime = true;

    private int width;


    AETypeData(String tag, String pattern, String esempio, int width) {
        this(tag, pattern, esempio, width, true);
    }


    AETypeData(String tag, String pattern, String esempio, int width, boolean senzaTime) {
        this.tag = tag;
        this.pattern = pattern;
        this.esempio = esempio;
        this.width = width;
        this.senzaTime = senzaTime;
    }


    public String getTag() {
        return tag;
    }


    public String getPattern() {
        return pattern;
    }


    public String getEsempio() {
        return esempio;
    }


    public boolean isSenzaTime() {
        return senzaTime;
    }


    public int getWidth() {
        return width;
    }


    public String getWidthEM() {
        return width > 0 ? width + TAG_EM : VUOTA;
    }
}
