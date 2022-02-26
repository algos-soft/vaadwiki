package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.io.*;
import java.net.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 12-feb-2022
 * Time: 20:35
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryWrite extends AQuery {

    public static final AETypeQuery QUERY_TYPE = AETypeQuery.postCookies;

    public static final String WIKI_TITLE_DEBUG = "Utente:Biobot/2";

    // oggetto della modifica in scrittura
    protected String summary;

    // titolo della pagina
    private String wikiTitle;

    // nuovo testo da inserire nella pagina
    private String newText;

    // token di controllo recuperato dalla urlResponse della primaryRequestGet
    private String csrftoken;

    public AIResult urlRequest(final String wikiTitleGrezzo, final String newText) {
        return urlRequest(wikiTitleGrezzo, newText, VUOTA);
    }


    /**
     * Request al software mediawiki composta di tre passaggi:
     * 1. Log in, via one of the methods described in API:Login. Note that while this is required to correctly attribute the edit to its author, many wikis do allow users to edit without registering or logging into an account.
     * 2. GET a CSRF token.
     * 3. Send a POST request, with the CSRF token, to take action on a page.
     * <p>
     * Controllo del login già effettuato ad inizio programma <br>
     * I dati del login di collegamento (userid, username e cookies) sono nel singleton botLogin <br>
     * <p>
     * La prima request preliminare è di tipo GET, per recuperare token e session <br>
     * urlDomain = "&meta=tokens&type=csrf" <br>
     * Invia la request con i cookies di login e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'itwikisession') <br>
     * Recupera il logintoken dalla urlResponse <br>
     * <p>
     * La seconda request è di tipo POST <br>
     * urlDomain = "&action=xxxx" <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * La response viene elaborata per conferma <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     * @param newText         da inserire
     * @param summary         oggetto della modifica (facoltativo)
     *
     * @return wrapper di informazioni
     *
     * @see https://www.mediawiki.org/wiki/API:Edit#Example
     */
    public AIResult urlRequest(final String wikiTitleGrezzo, final String newText, final String summary) {
        WResult result = WResult.errato();
        this.newText = newText;
        this.summary = summary;

        //--Controllo del Login
        if (!botLogin.isBot()) {
            result.setWikiTitle(wikiTitleGrezzo);
            result.setErrorMessage("Login non valido come bot");
            return result;
        }

        result.setQueryType(QUERY_TYPE.get());
        result.setWikiTitle(wikiTitleGrezzo);
        result.setSummary(summary);
        result.setNewtext(newText);

        if (text.isEmpty(wikiTitleGrezzo)) {
            result.setErrorMessage("Manca il titolo della pagina wiki");
            return result;
        }

        if (text.isEmpty(newText)) {
            result.setErrorMessage("Manca il nuovo testo da inserire");
            return result;
        }

        //--codifica del titolo (spazi vuoti e simili)
        wikiTitle = wikiApi.fixWikiTitle(wikiTitleGrezzo);

        //--La prima request è di tipo GET
        //--Indispensabile aggiungere i cookies del botLogin
        result = this.primaryRequestGet(result);

        //--La seconda request è di tipo POST
        //--Indispensabile aggiungere i cookies
        //--Indispensabile aggiungere il testo POST
        return this.secondaryRequestPost(result);
    }

    /**
     * <br>
     * Request preliminare. Crea la connessione base di tipo GET <br>
     * La request preliminare è di tipo GET, per recuperare token e session <br>
     * <p>
     * Request 1
     * URL: https://it.wikipedia.org/w/api.php?action=query&format=json&meta=tokens
     * This will return a "token" parameter in JSON form, as documented at
     * Other output formats are available. It will also return HTTP cookies as described below.
     * <p>
     * urlDomain += "&meta=tokens" <br>
     * Invia la request con i cookies di login e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'itwikisession') <br>
     * Recupera il token dalla urlResponse <br>
     *
     * @return wrapper di informazioni
     */
    public WResult primaryRequestGet(WResult result) {
        String urlDomain = TAG_PRELIMINARY_REQUEST_GET;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        result.setUrlPreliminary(urlDomain);
        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, botLogin.getCookies());
            urlResponse = sendRequest(urlConn);
            cookies = downlodCookies(urlConn);
        } catch (Exception unErrore) {
            logger.error(AETypeLog.login, unErrore.getMessage());
        }

        return elaboraPreliminaryResponse(result, urlResponse);
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected WResult elaboraPreliminaryResponse(WResult result, final String rispostaDellaQuery) {
        result.setPreliminaryResponse(rispostaDellaQuery);
        csrftoken = getCsrfToken(rispostaDellaQuery);
        result.setToken(csrftoken);

        if (text.isValid(csrftoken)) {
            try {
                csrftoken = URLEncoder.encode(csrftoken, ENCODE);
            } catch (Exception unErrore) {
            }
            result.setValido(true);
        }
        else {
            result.setValido(true);
        }

        return result;
    }


    /**
     * Request principale. Crea la connessione base di tipo POST <br>
     * <p>
     * Request 2
     * URL: https://en.wikipedia.org/w/api.php?action=login&format=json
     * COOKIES parameters:
     * itwikiSession
     * POST parameters:
     * lgname=BOTUSERNAME
     * lgpassword=BOTPASSWORD
     * lgtoken=TOKEN
     * <p>
     * where TOKEN is the token from the previous result. The HTTP cookies from the previous request must also be passed with the second request.
     * <p>
     * A successful login attempt will result in the Wikimedia server setting several HTTP cookies. The bot must save these cookies and send them back every time it makes a request (this is particularly crucial for editing). On the English Wikipedia, the following cookies should be used: enwikiUserID, enwikiToken, and enwikiUserName. The enwikisession cookie is required to actually send an edit or commit some change, otherwise the MediaWiki:Session fail preview error message will be returned.
     * <p>
     * Crea la connessione base di tipo POST <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     * Recupera i cookies allegati alla risposta e li memorizza in WikiLogin per poterli usare in query successive <br>
     *
     * @return true se il collegamento come bot è confermato
     */
    public AIResult secondaryRequestPost(final AIResult result) {
        String urlDomain = TAG_SECONDARY_REQUEST_POST + wikiTitle;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        result.setUrlRequest(urlDomain);
        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, botLogin.getCookies());
            addPostConnection(urlConn, newText);
            urlResponse = sendRequest(urlConn);
            result.setMappa(downlodCookies(urlConn));
        } catch (Exception unErrore) {
        }

        return elaboraSecondaryResponse((WResult) result, urlResponse);
    }

    /**
     * Aggiunge il POST della request <br>
     *
     * @param urlConn connessione con la request
     */
    protected void addPostConnection(URLConnection urlConn, final String newText) throws Exception {
        if (urlConn != null) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPost(newText));
            out.close();
        }
    }

    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraPost(final String newText) {
        String testoPost = "";
        String testoSummary = "";
        String testoCodificato = "";

        if (text.isValid(csrftoken)) {
            testoPost += "token" + "=";
            testoPost += csrftoken;
        }

        testoPost += "&bot=true";
        testoPost += "&minor=true";

        // summary
        if (botLogin != null) {
            summary = text.isValid(summary) ? summary : text.setDoppieQuadre("Utente:" + botLogin.getUsername() + "|" + botLogin.getUsername());
        }

        if (text.isValid(summary)) {
            testoSummary = summary;
            //            try { // prova ad eseguire il codice
            //                testoSummary = URLEncoder.encode(summary, "UTF-8");
            //            } catch (Exception unErrore) { // intercetta l'errore
            //            }// fine del blocco try-catch
        }

        if (text.isValid(testoSummary)) {
            testoPost += "&summary=" + testoSummary;
        }

        try { // prova ad eseguire il codice
            testoCodificato = URLEncoder.encode(newText, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }

        if (text.isValid(testoCodificato)) {
            testoPost += "&text" + "=";
            testoPost += testoCodificato;
        }

        return testoPost;
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     *
     * @return true se il collegamento come bot è confermato
     */
    protected AIResult elaboraSecondaryResponse(WResult result, final String rispostaDellaQuery) {
        result.setResponse(rispostaDellaQuery);
        long pageid = getPageid(rispostaDellaQuery);
        long revid = getNewRevid(rispostaDellaQuery);
        String newtimestamp = getNewTimestamp(rispostaDellaQuery);
        boolean modificata = getModificata(rispostaDellaQuery);

        result.setLongValue(pageid);
        result.setNewrevid(revid);
        result.setNewtimestamp(newtimestamp);
        result.setModificata(modificata);
        return result;
    }

    /**
     * Restituisce il token dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return logintoken
     */
    public String getCsrfToken(String contenutoCompletoPaginaWebInFormatoJSON) {
        String csrfToken = VUOTA;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectQuery = null;
        JSONObject objectToken = null;

        if (objectAll != null && objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
        }

        if (objectQuery.get(AQuery.TOKENS) != null && objectQuery.get(AQuery.TOKENS) instanceof JSONObject) {
            objectToken = (JSONObject) objectQuery.get(TOKENS);
        }

        if (objectToken != null && objectToken.get(CSRF_TOKEN) != null && objectToken.get(CSRF_TOKEN) instanceof String tokenString) {
            csrfToken = tokenString;
        }

        return csrfToken;
    }

    /**
     * Restituisce il token dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return logintoken
     */
    public long getPageid(String contenutoCompletoPaginaWebInFormatoJSON) {
        long pageid = 0;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }
        if (objectEdit.get(PAGE_ID) != null && objectEdit.get(PAGE_ID) instanceof Long) {
            pageid = (Long) objectEdit.get(PAGE_ID);
        }

        return pageid;
    }

    /**
     * Restituisce il token dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return logintoken
     */
    public long getNewRevid(String contenutoCompletoPaginaWebInFormatoJSON) {
        long newrevid = 0;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }
        if (objectEdit.get(NEW_REV_ID) != null && objectEdit.get(NEW_REV_ID) instanceof Long) {
            newrevid = (Long) objectEdit.get(NEW_REV_ID);
        }

        return newrevid;
    }

    /**
     * Restituisce il timestamp della modifica <br>
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return timestamp della modifica
     */
    public String getNewTimestamp(String contenutoCompletoPaginaWebInFormatoJSON) {
        String newtimestamp = VUOTA;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }

        if (objectEdit.get(NEW_TIME_STAMP) != null && objectEdit.get(NEW_TIME_STAMP) instanceof String) {
            newtimestamp = (String) objectEdit.get(NEW_TIME_STAMP);
        }

        return newtimestamp;
    }

    /**
     * Restituisce lo stato del flag 'nochange' <br>
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return flag 'nochange'
     */
    public boolean getModificata(String contenutoCompletoPaginaWebInFormatoJSON) {
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }

        return objectEdit.get(NO_CHANGE)==null;
    }

}
