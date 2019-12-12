package it.algos.vaadwiki.liste;

import it.algos.wiki.LibWiki;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 12-dic-2019
 * Time: 17:58
 */
public class Titolo {

    public String chiave;

    public String pagina;

    public String visibile;

    private String linkato = VUOTA;

    private String conSize = VUOTA;

    private String definitivo = VUOTA;

    private int numVoci = 0;


    public Titolo(String chiave, String pagina, String visibile) {
        this.chiave = chiave;
        this.pagina = pagina;
        this.visibile = visibile;
        this.linkato = costruisceTitolo(pagina, visibile);
    }// end of constructor


    /**
     * Costruisce il titolo
     * Controlla se il titolo visibile (link) non esiste già
     * Se esiste, sostituisce la pagina (prima parte del titolo) con quella già esistente
     */
    protected String costruisceTitolo(String paginaWiki, String linkVisibile) {
        String titoloParagrafo = LibWiki.setLink(paginaWiki, linkVisibile);
        String link;

        if (linkVisibile.equals(paginaWiki)) {
            return LibWiki.setQuadre(paginaWiki);
        }// end of if cycle

        return titoloParagrafo;
    }// fine del metodo


    public String getLinkato() {
        return linkato.length() > 0 ? linkato : visibile;
    }// end of method


    public String getConSize() {
        return conSize.length() > 0 ? conSize : visibile;
    }// end of method


    public String getDefinitivo() {
        return definitivo.length() > 0 ? definitivo : visibile;
    }// end of method


    public int getNumVoci() {
        return numVoci;
    }// end of method

}// end of class