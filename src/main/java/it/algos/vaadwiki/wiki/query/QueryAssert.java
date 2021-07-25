package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 24-lug-2021
 * Time: 18:52
 * <p>
 * Query di controllo per 'testare' il collegamento come bot <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce true or false <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryAssert extends AQuery {


    private boolean requestNonAncoraEffettuata = true;

    private boolean valida = false;


    /**
     * Costruttore con parametri <br>
     * I cookies sono indispensabili <br>
     * Possono arrivare nel costruttore, oppure direttamente nel metodo urlRequest <br>
     */
    public QueryAssert() {
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * I cookies sono indispensabili <br>
     * Possono arrivare nel costruttore, oppure direttamente nel metodo urlRequest <br>
     *
     * @param cookies coi parametri di controllo indispensabili
     */
    public QueryAssert(final Map<String, Object> cookies) {
        this.urlRequest(cookies);
    } // end of SpringBoot constructor


    /**
     * La request effettiva viene eseguita una volta sola <br>
     * o dal metodo postConstruct()
     * o da una chiamata diretta al metodo urlRequest()
     *
     * @return true se il collegamento come bot è confermato
     */
    public boolean urlRequest(final Map<String, Object> cookies) {
        String urlDomain = TAG_REQUEST_ASSERT;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        //--se mancano i cookies
        if (cookies == null || cookies.size() < 1) {
            return false;
        }

        //--se il metodo non è ancora stato chiamato
        if (requestNonAncoraEffettuata) {
            try {
                urlConn = this.creaGetConnection(urlDomain);
                uploadCookies(urlConn, cookies);
                urlResponse = sendRequest(urlConn);
            } catch (Exception unErrore) {
            }
            requestNonAncoraEffettuata = false;
        }

        return elaboraResponse(urlResponse);
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Recupera il token 'logintoken' dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    protected boolean elaboraResponse(final String rispostaDellaQuery) {
        valida = false;
        JSONObject jsonAll;

        jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(KEY_JSON_VALID) != null) {
            valida = (boolean) jsonAll.get(KEY_JSON_VALID);
            return valida;
        }

        return valida;
    }

    public boolean isValida() {
        return valida;
    }

}
