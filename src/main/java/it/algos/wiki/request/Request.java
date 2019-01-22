package it.algos.wiki.request;


import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.wiki.TipoRisultato;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Superclasse astratta per le Request sul Web
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 * Le request più semplici usano il GET
 * In alcune request (non tutte) è obbligatorio anche il POST
 * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
 */
public abstract class Request {

    //--codifica dei caratteri
    protected static String INPUT = "UTF8";

    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    public AArrayService array = AArrayService.getInstance();

    //--validità specifica della request
    protected TipoRisultato risultato = TipoRisultato.erroreGenerico;

    //--validità generale della request (webUrl esistente e letto)
    protected boolean valida;

    //--indirizzo internet da leggere
    protected String webUrl;
    protected boolean needContinua;
    protected boolean needPost;
    protected boolean needCookies;

    //--token per la continuazione della query
    protected String tokenContinua = "";

    //--contenuto testuale completo della risposta (la seconda, se ci sono due request)
    protected String testoResponse;


    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse (questa classe astratta)
     */
    public Request() {
    }// fine del metodo costruttore


    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doInit() {
        doRequest();
    } // fine del metodo

    /**
     * Metodo iniziale
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doRequest() {
        if (needContinua) {
            try { // prova ad eseguire il codice
                urlRequest();
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
            while (!tokenContinua.equals("")) {
                try { // prova ad eseguire il codice
                    urlRequest();
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            } // fine del blocco while
        } else {
            try { // prova ad eseguire il codice
                urlRequest();
            } catch (Exception unErrore) { // intercetta l'errore
                String errore = unErrore.getClass().getSimpleName();
                valida = false;
            }// fine del blocco try-catch
        }// end of if/else cycle
    } // fine del metodo

    /**
     * Request
     * Quella base usa il GET
     * In alcune request (non tutte) è obbligatorio anche il POST
     */
    protected void urlRequest() throws Exception {
        URLConnection urlConn;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;
        String risposta;

        //--Connessione
        urlConn = creaConnessione();

        //--rimanda i cookies arrivati con la prima richiesta
        if (needCookies) {
            this.uploadCookies(urlConn);
        }// end of if cycle

        //--POST
        if (needPost) {
            this.creaPost(urlConn);
        }// end of if cycle

        //--GET
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        // read the request
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        // controlla il valore di ritorno della request e regola il risultato
        risposta = textBuffer.toString();
        elaboraRisposta(risposta);
    } // fine del metodo


    /**
     * Stringa del browser per la request
     * <p>
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String getDomain() {
        return getWebUrl();
    } // end of getter method

    /**
     * Crea la connessione
     * <p>
     * Regola i parametri della connessione
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected URLConnection creaConnessione() throws Exception {
        URLConnection urlConn = null;
        String domain = this.getDomain();

        if (domain != null && !domain.equals("")) {
            urlConn = new URL(domain).openConnection();
            urlConn.setDoOutput(true);
//            urlConn.setRequestProperty("Accept-Encoding", "GZIP");
//            urlConn.setRequestProperty("Content-Encoding", "GZIP");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");

        }// end of if cycle
        return urlConn;
    } // fine del metodo

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(URLConnection urlConn) {
    } // fine del metodo

    /**
     * Crea il POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void creaPost(URLConnection urlConn) throws Exception {
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRisposta(String rispostaRequest) {

        if (rispostaRequest != null && !rispostaRequest.equals("")) {
            this.testoResponse = rispostaRequest;
            valida = true;
            tokenContinua = "";
        }// end of if cycle

    } // end of getter method


    private String getWebUrl() {
        return webUrl;
    }// end of getter method

    public boolean isValida() {
        return valida;
    }// end of getter method

    public TipoRisultato getRisultato() {
        return risultato;
    }// end of getter method

    public String getTestoResponse() {
        return testoResponse;
    }// end of getter method

} // fine della classe
