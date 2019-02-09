package it.algos.wiki.mediawiki;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import it.algos.wiki.WikiLoginOld;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.net.URLConnection;
import java.util.HashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 20:57
 * <p>
 * Esegue il collegamento al server wiki come Bot.
 * Doppia request.
 * Request di tipo POST.
 * Usa i cookies.
 * Costruisce una istanza di WikiLogin.
 *
 * @see https://www.mediawiki.org/wiki/OAuth/Owner-only_consumers
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ReadLogin extends ReadWiki {


    public static final String LOGIN_TOKEN = "logintoken";

    private final static String FIRST_REQUEST = TAG_QUERY + "meta=tokens&type=login";

    private String logintoken;

    private UrlLoginConnection urlLoginConnection;


    /**
     * Doppia request.
     *
     * @return istanza WikiLogin
     */
    public WikiLoginOld esegue() {
        WikiLoginOld login = null;

        //--recupera il logintoken necessario per la seconda request
        primaRequest();

        secondaRequest();

        return login;
    } // fine del metodo


    /**
     * Prima request.
     * Richiede un token, specializzato per il login
     * Request di tipo GET
     * Risposta in formato JSON
     * Recupera il logintoken necessario per la seconda request
     */
    public void primaRequest() {
        String textJSON = legge(FIRST_REQUEST);
        HashMap<String, Object> mappa = LibWiki.creaMappaLogin(textJSON);
        logintoken = LibWiki.getLoginToken(mappa);
    } // fine del metodo


    /**
     * Seconda request.
     * Request di tipo POST
     */
    public void secondaRequest() {
        String risposta;
        URLConnection urlConn;
        String tag = getTagIniziale();
        String indirizzoWebCompleto = "";

        try { // prova ad eseguire il codice
//            urlLoginConnection=
//            urlConn = urlLoginConnection.esegue();
//            risposta = urlRequest.esegue(urlConn);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    } // fine del metodo


    public String getTagIniziale() {
        return FIRST_REQUEST;
    } // fine del metodo

}// end of class
