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
import java.time.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 28-lug-2021
 * Time: 21:29
 * Query per recuperare i timestamp da una serie di pageIds <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce una lista di MiniWrap con 'pageid' e 'lastModifica' <br>
 * <p>
 * Riceve una lista di pageIds ed esegue una serie di request ognuna col valore massimo di elementi ammissibile per le API di MediWiki <br>
 * Accumula i risultati <br>
 * La query restituisce SOLO MiniWrap <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryTimestamp extends AQuery {


    /**
     * Request principale <br>
     *
     * @param listaPageids lista dei pageIds delle pagine wiki da controllare
     */
    public AIResult urlRequest(final List<Long> listaPageids) {
        AIResult result = AResult.valido();
        List<MiniWrap> wraps = new ArrayList<>();
        String urlResponse = VUOTA;
        String tokenContinue = VUOTA;
        String urlDomain = WIKI_QUERY_TIMESTAMP + listaPageids.subList(0, Math.min(10, listaPageids.size()));
        URLConnection urlConn;
        AIResult assertResult;
        String strisciaIds = VUOTA;

        result.setCodeMessage(JSON_SUCCESS);
        result.setQueryType(TypeQuery.getCookies.get());
        result.setUrlRequest(urlDomain);
        strisciaIds = array.toStringaPipe(listaPageids.subList(0, 2));
        result = request(result, strisciaIds);

        //        result.setWikiTitle(catTitle);
        //        result.setUrlRequest(fixUrlCat(catTitle, VUOTA));
        //        result.setQueryType(TypeQuery.getCookies.get());

        //--controlla l'esistenza e la validità del collegamento come bot
        queryAssert = queryAssert != null ? queryAssert : appContext.getBean(QueryAssert.class);
        assertResult = queryAssert.urlRequest();
        if (assertResult.isErrato()) {
            result.setValido(false);
            result.setErrorCode(assertResult.getErrorCode());
            result.setErrorMessage(assertResult.getErrorMessage());
            return result;
        }

        return result;
    }

    /**
     * Request singolo blocco <br>
     *
     * @param pageIds stringa dei pageIds delle pagine wiki da controllare
     */
    public AIResult request(AIResult result, final String pageIds) {
        String urlDomain;
        urlDomain = WIKI_QUERY_TIMESTAMP + pageIds;
        urlDomain = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query&prop=revisions&rvprop=timestamp&pageids=876876|793444";

        String urlResponse = VUOTA;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, botLogin != null ? botLogin.getCookies() : null);
            urlResponse = sendRequest(urlConn);
            result = elaboraResponse(result, urlResponse);
        } catch (Exception unErrore) {
            logger.info(unErrore.toString());
        }

        return result;
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Recupera il token 'logintoken' dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    protected AIResult elaboraResponse(AIResult result, final String rispostaDellaQuery) {
        List<Long> listaNew = new ArrayList<>();
        List<Long> listaOld;
        boolean valida = false;
        JSONObject jsonAll;
        JSONObject jsonQuery;
        JSONArray jsonPages = null;
        JSONArray jsonRevisions = null;
        JSONObject jsonUltimaRevisione;
        long pageid = 0;
        String lastRevisionTxt;
        LocalDateTime lastRevision;

        jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);
        if (jsonAll != null && jsonAll.get(KEY_JSON_VALID) != null) {
            valida = (boolean) jsonAll.get(KEY_JSON_VALID);
            result.setResponse(KEY_JSON_VALID + UGUALE_SEMPLICE + valida);
        }

        if (valida && jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            jsonQuery = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
            jsonPages = (JSONArray) jsonQuery.get(KEY_JSON_PAGES);
        }

        if (jsonPages != null) {
            if (jsonPages.size() > 0) {
                for (Object obj : jsonPages) {
                    pageid = (long) ((JSONObject) obj).get(PAGE_ID);
                    jsonRevisions = (JSONArray) ((JSONObject) obj).get(KEY_JSON_REVISIONS);
                    jsonUltimaRevisione = (JSONObject) jsonRevisions.get(0);
                    lastRevisionTxt=   (String)jsonUltimaRevisione.get(KEY_JSON_TIMESTAMP);
                    listaNew.add(pageid);
                }
                result.setCodeMessage(JSON_SUCCESS);
                listaOld = (List<Long>) result.getLista();
                if (listaOld != null) {
                    listaOld.addAll(listaNew);
                }
                else {
                    listaOld = listaNew;
                }
                result.setLista(listaOld);
                result.setValue(listaOld.size());
                return result;
            }
            else {
                result = AResult.errato();
                result.setErrorMessage("Non ci sono pagine nella categoria");
            }
        }
        return result;
    }

}
