package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import it.algos.vaadwiki.backend.enumeration.*;
import static it.algos.vaadwiki.backend.service.AWikiBotService.*;
import it.algos.vaadwiki.wiki.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 25-lug-2021
 * Time: 22:08
 * <p>
 * Query per recuperare una lista di pageid di una categoria wiki <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce una lista di pageIds <br>
 * <p>
 * Esegue dei cicli di dimensioni uguali a 'cmlimit' ammesso <br>
 * La query restituisce SOLO pageid <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryCat extends AQuery {

    private String catTitle;

    private String urlResponse = VUOTA;

    /**
     * Costruttore senza parametri <br>
     */
    public QueryCat() {
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     *
     * @param catTitle della categoria wiki
     */
    public QueryCat(final String catTitle) {
        this.catTitle = catTitle;
    } // end of SpringBoot constructor


    /**
     * Request principale <br>
     *
     * @param catTitle della categoria wiki (necessita di codifica) usato nella urlRequest
     */
    public AIResult urlRequest(final String catTitle) {
        String urlResponse = VUOTA;
        String continueParam = VUOTA;
        String urlDomain;
        URLConnection urlConn;

        try {
            urlDomain = fixUrlCat(catTitle, continueParam);
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, botLogin != null ? botLogin.getCookies() : null);
            urlResponse = sendRequest(urlConn);
        } catch (Exception unErrore) {
            logger.info(unErrore.toString());
        }

        return elaboraResponse(catTitle, urlResponse);
    }// end of method


    /**
     * Costruisce l'url come bot <br>
     * Come 'anonymous' tira 500 pagine
     * Come 'bot' tira 5.000 pagine
     *
     * @param catTitle      da cui estrarre le pagine
     * @param continueParam per la successiva query
     *
     * @return testo dell'url
     */
    private String fixUrlCat(final String catTitle, final String continueParam) {
        String query = TAG_REQUEST_CAT + wikiApi.fixWikiTitle(catTitle);
        String type = WIKI_QUERY_CAT_TYPE + AECatType.page.getTag();
        String prop = WIKI_QUERY_CAT_PROP + AECatProp.pageid.getTag();
        String limit = AETypeUser.bot.limit();
        String user = AETypeUser.bot.affermazione();
        String continua = WIKI_QUERY_CAT_CONTINUE + continueParam;

        return String.format("%s%s%s%s%s%s", query, type, prop, limit, user, continua);
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Recupera il token 'logintoken' dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    protected AIResult elaboraResponse(final String catTitle, final String rispostaDellaQuery) {
        AIResult result = AResult.errato();
        List<Long> lista = new ArrayList<>();
        boolean valida = false;
        JSONObject jsonAll;
        JSONObject jsonQuery;
        JSONArray jsonMembers = null;
        long pageid;

        result.setUrlRequest(catTitle);
        jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(KEY_JSON_VALID) != null) {
            valida = (boolean) jsonAll.get(KEY_JSON_VALID);
            urlResponse = KEY_JSON_VALID + UGUALE_SEMPLICE + valida;
            result = AResult.valido();
            result.setResponse(KEY_JSON_VALID + UGUALE_SEMPLICE + valida);
        }

        if (valida && jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            jsonQuery = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
            jsonMembers = (JSONArray) jsonQuery.get(KEY_JSON_MEMBERS);
        }

        if (jsonMembers != null) {
            for (Object obj : jsonMembers) {
                pageid = (long) ((JSONObject) obj).get(PAGE_ID);
                lista.add(pageid);
            }
            result.setLista(lista);
        }

        return result;
    }


    public String getUrlResponse() {
        return urlResponse;
    }

}
