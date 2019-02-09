package it.algos.wiki.request;


import it.algos.wiki.*;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
 * Necessita di Login per scrivere
 */
public abstract class QueryWrite extends QueryWiki {


    private boolean scritta = false;
    // mappa dati di passaggio tra la prima e la seconda request
    private HashMap mappa;

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(String titlepageid, String testoNew) {
        this(titlepageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(long pageid, String testoNew) {
        this(pageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWrite(String titlepageid, String testoNew, String summary) {
        this(titlepageid, testoNew, summary, null);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryWrite(String titlepageid, String testoNew, String summary, WikiLoginOld login) {
        super.tipoRicerca = TipoRicerca.title;
        this.doInit(titlepageid, testoNew, summary, login);
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flag
     */
    public QueryWrite(long pageid, String testoNew, String summary) {
        super.tipoRicerca = TipoRicerca.pageid;
        this.doInit("" + pageid, testoNew, summary, null);
    }// fine del metodo costruttore

    protected void doInit(String titlepageid, String testoNew, String summary, WikiLoginOld login) {
        super.testoNew = testoNew;
        super.summary = summary;
        super.tipoRequest = TipoRequest.write;
        super.serveLogin = true;

        if (testoNew == null || testoNew.equals("")) {
            risultato = TipoRisultato.erroreGenerico;
            valida = false;
            return;
        }// end of if cycle

        if (login == null) {
//            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        } else {
            wikiLoginOld = login;
        }// end of if/else cycle

        if (serveLogin && wikiLoginOld == null) {
            risultato = TipoRisultato.noLogin;
            valida = false;
            return;
        }// end of if cycle

        if (titlepageid != null) {
            title = titlepageid;
            pageid = titlepageid;
            stringaPageIds = titlepageid;
            domain = this.getDomain();
        }// fine del blocco if

        super.doInit();
    } // fine del metodo


    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        super.regolaRisultato(risultatoRequest);
        this.elaboraPrimaRequest(risultatoRequest);
    } // fine del metodo


    /**
     * Costruisce la mappa dei dati dalla risposta alla prima Request
     * I dati servono per costruire la seconda request
     *
     * @param testoRisposta alla prima Request
     */
    protected void elaboraPrimaRequest(String testoRisposta) {
        HashMap mappa = null;
        int pageid;

        // controllo di congruità
        if (testoRisposta != null) {
            mappa = LibWiki.creaMappaQuery(testoRisposta);
        }// fine del blocco if

        if (mappa != null) {
            this.mappa = mappa;
        }// fine del blocco if
    } // fine del metodo

    //--Costruisce il domain per l'URL dal pageid della pagina
    //--@return domain
    protected String getSecondoDomain() {
        String domain = "";
        String titolo = "";
        String tag = "https://it.wikipedia.org/w/api.php?format=json&action=edit";

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(title, "UTF-8");
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        domain = tag + "&title=" + titolo;

        return domain;
    } // fine del metodo

    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return post
     */
    @Override
    protected String getSecondoPost() {
        String testoPost;
        String testo = this.getTestoNew();
        String summary = this.getSummary();
        String edittoken = (String) this.mappa.get(LibWiki.TOKEN);

        if (testo != null && !testo.equals("")) {
            try { // prova ad eseguire il codice
                testo = URLEncoder.encode(testo, "UTF-8");
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        if (summary != null && !summary.equals("")) {
            try { // prova ad eseguire il codice
                summary = URLEncoder.encode(summary, "UTF-8");
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        testoPost = "text=" + testo;
        testoPost += "&bot=true";
        testoPost += "&minor=true";
        testoPost += "&summary=" + summary;
        testoPost += "&token=" + edittoken;

        return testoPost;
    } // fine della closure

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void regolaRisultatoSecondo(String risultatoRequest) {
        super.regolaRisultatoSecondo(risultatoRequest);
        this.elaboraSecondaRequest(risultatoRequest);
    } // end of getter method


    /**
     * Elabora la risposta alla seconda Request
     *
     * @param testoRisposta alla prima Request
     */
    protected void elaboraSecondaRequest(String testoRisposta) {
        boolean noChange;
        HashMap mappa = LibWiki.creaMappaEdit(testoRisposta);

        if (testoRisposta.equals("")) {
            risultato = TipoRisultato.erroreGenerico;
            return;
        }// end of if cycle

        valida = true;
        if (mappa != null && mappa.get(LibWiki.NOCHANGE) != null) {
            noChange = (Boolean) mappa.get(LibWiki.NOCHANGE);
            if (noChange) {
                risultato = TipoRisultato.nonRegistrata;
            } else {
                risultato = TipoRisultato.registrata;
            }// end of if/else cycle
        }// end of if cycle

    } // fine del metodo

    /**
     * Controlla di aver scritto la pagina
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    @Override
    public boolean isScritta() {
        return scritta;
    } // fine del metodo

}// end of class
