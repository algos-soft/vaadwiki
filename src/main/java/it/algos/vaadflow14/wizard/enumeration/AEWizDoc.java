package it.algos.vaadflow14.wizard.enumeration;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 16-mag-2021
 * Time: 15:34
 */
public enum AEWizDoc {
    nullo(false, "Non modifica il file.", VUOTA),
    inizio(true, "Modifica l'header partendo dall'inizio del file e fino al tag " + TAG_END_DOC, TAG_INIZIO_DOC),
    revisione(true, "Modifica l'header partendo dalla data di revisione (compresa) e fino al tag " + TAG_END_DOC, TAG_INIZIO_REVISION),
    ;

    private boolean esegue;

    private String descrizione;

    private String iniTag;

    private String endTag;

    /**
     * Costruttore <br>
     */
    AEWizDoc(boolean esegue, String descrizione, String iniTag) {
        this(esegue, descrizione, iniTag, TAG_END_DOC);
    }

    /**
     * Costruttore <br>
     */
    AEWizDoc(boolean esegue, String descrizione, String iniTag, String endTag) {
        this.esegue = esegue;
        this.descrizione = descrizione;
        this.iniTag = iniTag;
    }

    public boolean isEsegue() {
        return esegue;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getIniTag() {
        return iniTag;
    }

    public String getEndTag() {
        return endTag;
    }
}
