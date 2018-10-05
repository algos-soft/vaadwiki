package it.algos.wiki.request;


import it.algos.wiki.*;

import java.net.URLConnection;
import java.util.HashMap;

/**
 * Superclasse per le Request al server MediaWiki
 * Fornisce le funzionalità di base
 * Necessita di Login per la sottoclasse QueryMultiId
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 */
public abstract class QueryWiki extends Query {

    // codifica dei caratteri
    protected static String ENCODE = "UTF-8";

    //--language selezionato (per adesso solo questo)
    protected static String LANGUAGE = "it";

    //--progetto selezionato (per adesso solo questo)
    protected static String PROJECT = "wikipedia";

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_QUERY + Cost.API_FORMAT;

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGESID = "&pageids=";
    protected static String TAG_EDIT = "&meta=tokens";

    protected String title;
    protected String pageid;
    protected String stringaPageIds;

//    // risultato della pagina
//    // risultato grezzo della query nel formato prescelto
//    protected String contenuto;

    //--token per la continuazione della query
    protected String continua = "";

    //--tipo di ricerca della pagina
    //--di default il titolo
    protected TipoRicerca tipoRicerca = TipoRicerca.title;

    // collegamento utilizzato
//    protected Login login = null;

    /**
     * Costruttore di default per il sistema (a volte serve)
     */
    public QueryWiki() {
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     */
    public QueryWiki(String titlepageid, TipoRicerca tipoRicerca, TipoRequest tipoRequest) {
        this.tipoRicerca = tipoRicerca;
        super.tipoRequest = tipoRequest;
        this.doInit(titlepageid);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     */
    public QueryWiki(int pageid, TipoRicerca tipoRicerca, TipoRequest tipoRequest) {
        this.tipoRicerca = tipoRicerca;
        super.tipoRequest = tipoRequest;
        this.doInit("" + pageid);
    }// fine del metodo costruttore


    protected void doInit(String titlepageid) {
        if (titlepageid != null) {
            title = titlepageid;
            pageid = titlepageid;
            stringaPageIds = titlepageid;
            domain = this.getDomain();
            super.doInit();
        }// fine del blocco if
    } // fine del metodo


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    protected String getDomain() {
        return "";
    } // fine del metodo


    /**
     * Crea la connessione
     * Regola i parametri della connessione
     * Recupera i cookies dal Login di registrazione
     */
    @Override
    protected URLConnection creaConnessione(String domain) throws Exception {
        URLConnection urlConn = super.creaConnessione(domain);
//        WikiLogin wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        String txtCookies;

        // regolo i cookies
        if (wikiLogin != null && wikiLogin.isValido()) {
            txtCookies = wikiLogin.getStringCookies();
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// fine del blocco if

        return urlConn;
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
        HashMap mappa = null;
        super.regolaRisultato(risultatoRequest);

        if (risultatoRequest != null) {
            mappa = LibWiki.creaMappaQuery(risultatoRequest);
        }// fine del blocco if

        if (mappa != null) {
            if (mappa.get(PagePar.missing.toString()) != null && (Boolean) mappa.get(PagePar.missing.toString())) {
                setRisultato(TipoRisultato.nonTrovata);
                valida = false;
            }// end of if cycle

            if (mappa.get(PagePar.missing.toString()) != null && !(Boolean) mappa.get(PagePar.missing.toString())) {
                if (mappa.get(PagePar.content.toString()) != null) {
                    setRisultato(TipoRisultato.letta);
                    valida = true;
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if
    } // fine del metodo


    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return post
     */
    protected String getSecondoPost() {
//        String testoPost;
//        String testo = this.getTestoNew();
//        String summary = this.getSummary();
////        String edittoken = this.getToken();
//
//        if (testo != null && !testo.equals("")) {
//            try { // prova ad eseguire il codice
//                testo = URLEncoder.encode(testo, "UTF-8");
//
//            } catch (Exception unErrore) { // intercetta l'errore
//            }// fine del blocco try-catch
//        }// fine del blocco if
//        if (summary != null && !summary.equals("")) {
//            try { // prova ad eseguire il codice
//                summary = URLEncoder.encode(summary, "UTF-8");
//            } catch (Exception unErrore) { // intercetta l'errore
//            }// fine del blocco try-catch
//        }// fine del blocco if
//
//        testoPost = "text=" + testo;
//        testoPost += "&bot=true";
//        testoPost += "&minor=true";
//        testoPost += "&summary=" + summary;
////        testoPost += "&token=" + edittoken;
//
        return "";
    } // fine della closure

//    /**
//     * Controlla di aver trovato la pagina e di aver letto un contenuto valido
//     * DEVE essere implementato nelle sottoclassi specifiche
//     */
//    @Override
//    public boolean isLetta() {
//        boolean valida = false;
//        String contenuto = testoPrimaRequest;
//
//
//        if (contenuto != null && contenuto.length() > 200 && !contenuto.contains("missing")) {
//            valida = true;
//        }// fine del blocco if
//
//        return valida;
//    } // fine del metodo

    /**
     * Controlla di aver scritto la pagina
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    @Override
    public boolean isScritta() {
        return false;
    } // fine del metodo

    public String getContinua() {
        return continua;
    } // fine del metodo

} // fine della classe
