package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.PagePar;
import it.algos.wiki.TipoRisultato;

import java.util.HashMap;

/**
 * Classe concreta per le Request sul server di Wikipedia per leggere una o più pagine
 * Usa solo il GET
 * La ricerca può essere fatta col pageid, col title oppure con una lista di pageids
 */
public abstract class RequestWikiRead extends RequestWiki {


    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse (questa classe astratta)
     */
    public RequestWikiRead() {
    }// fine del metodo costruttore

    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    public void doInit() {
        super.needPost = false;
        super.needLogin = false;
        super.needToken = false;
        super.needContinua = false;
        super.doInit();
    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return super.getDomain()+API_QUERY + TAG_PROP;
    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, Object> mappa;
        super.elaboraRisposta(rispostaRequest);

        mappa = LibWiki.creaMappaQuery(rispostaRequest);

        if (mappa != null) {
            if (mappa.get(PagePar.missing.toString()) != null && (Boolean) mappa.get(PagePar.missing.toString())) {
                risultato = TipoRisultato.nonTrovata;
                valida = false;
            }// end of if cycle

            if (mappa.get(PagePar.missing.toString()) != null && !(Boolean) mappa.get(PagePar.missing.toString())) {
                if (mappa.get(PagePar.content.toString()) != null) {
                    risultato = TipoRisultato.letta;
                    valida = true;
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if

    } // fine del metodo

} // fine della classe
