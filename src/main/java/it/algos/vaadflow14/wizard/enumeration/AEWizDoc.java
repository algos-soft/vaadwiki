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
    nonModificabile(false, "Non modifica il file.", VUOTA),
    inizioFile(true, "Modifica l'header partendo dall'inizio del file e fino al tag " + TAG_END_DOC, TAG_INIZIO_DOC),
    inizioRevisione(true, "Modifica l'header partendo dalla data di revisione (compresa) e fino al tag " + TAG_END_DOC, TAG_INIZIO_REVISION),
    ;

    private boolean esegue;

    private String descrizione;

    private String tagIni;

    private String tagEnd;

    /**
     * Costruttore <br>
     */
    AEWizDoc(final boolean esegue, final String descrizione, final String tagIni) {
        this(esegue, descrizione, tagIni, TAG_END_DOC);
    }

    /**
     * Costruttore <br>
     */
    AEWizDoc(final boolean esegue, final String descrizione, final String tagIni, final String tagEnd) {
        this.esegue = esegue;
        this.descrizione = descrizione;
        this.tagIni = tagIni;
        this.tagEnd = tagEnd;
    }

    public boolean isEsegue() {
        return esegue;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTagIni() {
        return tagIni;
    }

    public String getTagEnd() {
        return tagEnd;
    }
}
