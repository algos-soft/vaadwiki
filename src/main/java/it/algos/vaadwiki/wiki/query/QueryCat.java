package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import it.algos.vaadwiki.backend.enumeration.*;
import static it.algos.vaadwiki.backend.service.WikiBotService.*;
import it.algos.vaadwiki.wiki.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 25-lug-2021
 * Time: 22:08
 * <p>
 * Query per recuperare una lista di pageIds di una categoria wiki <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce una lista di pageIds <br>
 * <p></p>
 * Ripete la request finché riceve un valore valido di cmcontinue <br>
 * La query restituisce SOLO pageIds <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryCat extends AQuery {


    /**
     * Request principale <br>
     *
     * @param catTitle della categoria wiki (necessita di codifica) usato nella urlRequest
     */
    public AIResult urlRequest(final String catTitle) {
        AIResult result = AResult.valido();
        String urlResponse = VUOTA;
        String tokenContinue = VUOTA;
        String urlDomain;
        URLConnection urlConn;
        AIResult assertResult;
        int pageIdsRecuperati = 0;

        result.setWikiTitle(catTitle);
        result.setQueryType(TypeQuery.getCookies.get());
        result.setUrlRequest(fixUrlCat(catTitle, VUOTA));

        //--controlla l'esistenza della categoria
        if (!wikiBot.isEsisteCat(catTitle)) {
            result.setValido(false);
            result.setErrorCode("Inesistente");
            result.setErrorMessage(String.format("Non esiste la categoria %s", catTitle));
            return result;
        }

        //--controlla l'esistenza e la validità del collegamento come bot
        queryAssert = queryAssert != null ? queryAssert : appContext.getBean(QueryAssert.class);
        assertResult = queryAssert.urlRequest();
        if (assertResult.isErrato()) {
            result.setValido(false);
            result.setErrorCode(assertResult.getErrorCode());
            result.setErrorMessage(assertResult.getErrorMessage());
            return result;
        }

        try {
            do {
                urlDomain = fixUrlCat(catTitle, tokenContinue);
                urlConn = this.creaGetConnection(urlDomain);
                uploadCookies(urlConn, botLogin != null ? botLogin.getCookies() : null);
                urlResponse = sendRequest(urlConn);
                result = elaboraResponse(result, urlResponse);
                tokenContinue = result.getToken();
            }
            while (text.isValid(tokenContinue));
        } catch (Exception unErrore) {
            logger.info(unErrore.toString());
        }

        pageIdsRecuperati = result.getIntValue();
        result.setMessage(String.format("Recuperati %s pageIds dalla categoria '%s'", text.format(pageIdsRecuperati), catTitle));

        return result;
    }


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
    protected AIResult elaboraResponse(AIResult result, final String rispostaDellaQuery) {
        List<Long> listaNew = new ArrayList<>();
        List<Long> listaOld;
        boolean valida = false;
        JSONObject jsonAll;
        JSONObject jsonQuery;
        JSONObject jsonContinue;
        JSONArray jsonMembers = null;
        long pageid = 0;

        jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);
        result.setToken(VUOTA);
        if (jsonAll != null && jsonAll.get(KEY_JSON_VALID) != null) {
            valida = (boolean) jsonAll.get(KEY_JSON_VALID);
            result.setResponse(KEY_JSON_VALID + UGUALE_SEMPLICE + valida);
        }

        if (valida && jsonAll != null && jsonAll.get(KEY_JSON_CONTINUE) != null) {
            jsonContinue = (JSONObject) jsonAll.get(KEY_JSON_CONTINUE);
            result.setToken((String) jsonContinue.get(KEY_JSON_CONTINUE_CM));
        }

        if (valida && jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            jsonQuery = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
            jsonMembers = (JSONArray) jsonQuery.get(KEY_JSON_MEMBERS);
        }

        if (jsonMembers != null) {
            if (jsonMembers.size() > 0) {
                for (Object obj : jsonMembers) {
                    pageid = (long) ((JSONObject) obj).get(PAGE_ID);
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
                result.setIntValue(listaOld.size());
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
