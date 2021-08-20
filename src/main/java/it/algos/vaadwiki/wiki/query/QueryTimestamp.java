package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadwiki.backend.enumeration.*;
import static it.algos.vaadwiki.backend.service.WikiBotService.*;
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
     * Limite massimo (per il bot) = 500 pageIds per volta <br>
     *
     * @param listaPageids lista dei pageIds delle pagine wiki da controllare
     */
    public AIResult urlRequest(List<Long> listaPageids) {
        AIResult result = AResult.valido();
        String urlDomain = WIKI_QUERY_TIMESTAMP + listaPageids.subList(0, Math.min(10, listaPageids.size()));
        AIResult assertResult;
        String strisciaIds;
        int totPageIds = listaPageids.size();
        int limit = 500;
        int cicli = (totPageIds / limit) + 1;

        result.setQueryType(TypeQuery.getCookies.get());
        result.setUrlRequest(urlDomain);

        //--controlla l'esistenza e la validità del collegamento come bot
        queryAssert = queryAssert != null ? queryAssert : appContext.getBean(QueryAssert.class);
        assertResult = queryAssert.urlRequest();
        if (assertResult.isErrato()) {
            result.setValido(false);
            result.setErrorCode(assertResult.getErrorCode());
            result.setErrorMessage(assertResult.getErrorMessage());
            return result;
        }

        result.setCodeMessage(JSON_SUCCESS);
        for (int k = 0; k < cicli; k++) {
            strisciaIds = array.toStringaPipe(listaPageids.subList(k * limit, Math.min(k * limit + limit, listaPageids.size())));
            result = request(result, strisciaIds);
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
        urlDomain = WIKI_QUERY_TIMESTAMP + pageIds + WIKI_QUERY_BOT;
        String urlResponse;
        URLConnection urlConn;
        String ERROR_TOO_LONG = "java.io.IOException: Server returned HTTP response code: 414";

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, botLogin != null ? botLogin.getCookies() : null);
            urlResponse = sendRequest(urlConn);
            result = elaboraResponse(result, urlResponse);
        } catch (Exception unErrore) {
            if (unErrore.toString().startsWith(ERROR_TOO_LONG)) {
                result.setValido(false);
                result.setErrorCode("Troppi pageIds");
                result.setErrorMessage(ERROR_TOO_LONG);
                result.setCodeMessage(VUOTA);
            }
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
        List<MiniWrap> listaNew = new ArrayList<>();
        List<MiniWrap> listaOld;
        boolean valida = false;
        JSONObject jsonAll;
        JSONObject jsonError;
        JSONObject jsonQuery;
        JSONArray jsonPages = null;
        MiniWrap wrap;

        jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(JSON_ERROR) != null) {
            jsonError = (JSONObject) jsonAll.get(JSON_ERROR);
            result.setValido(false);
            result.setMessage(VUOTA);
            result.setErrorCode((String) jsonError.get(JSON_CODE));
            result.setErrorMessage((String) jsonError.get(JSON_INFO));
            return result;
        }

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
                    wrap = creaWrap((JSONObject) obj);
                    if (wrap != null) {
                        listaNew.add(wrap);
                    }
                }
                result.setCodeMessage(JSON_SUCCESS);
                listaOld = (List<MiniWrap>) result.getLista();
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

    public MiniWrap creaWrap(final JSONObject jsonPage) {
        long pageid;
        String stringTimestamp;

        if (jsonPage.get(KEY_JSON_MISSING) != null) {
            return null;
        }

        pageid = (long) jsonPage.get(KEY_JSON_PAGE_ID);
        JSONArray jsonRevisions = (JSONArray) jsonPage.get(KEY_JSON_REVISIONS);
        JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
        stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);

        return new MiniWrap(pageid, stringTimestamp);
    }

}
