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

import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 30-lug-2021
 * Time: 19:52
 * <p>
 * Legge una serie di pagine individuate da listaPageids <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryPages extends AQuery {

    /**
     * Request principale <br>
     * Limite massimo (per il bot) = 500 pageIds per volta <br>
     *
     * @param listaPageids lista dei pageIds delle pagine wiki da controllare
     */
    public AIResult urlRequest(List<Long> listaPageids) {
        AIResult result = AResult.valido();
        String urlDomain = WIKI_QUERY_PAGEIDS + listaPageids.subList(0, Math.min(10, listaPageids.size()));
        AIResult assertResult;
        String strisciaIds;
        int totPageIds = listaPageids.size();
        int valide = 0;
        int errate = 0;
        int limit = 500;
        int cicli = (totPageIds / limit) + 1;

        result.setWikiTitle("lista di pageIds");
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

        result.setMessage("Recupera WrapBio");
        valide = result.getValue();
        errate = totPageIds - valide;
        result.setErrorMessage(String.format("%s pageIds non erano voci biografiche valide", errate));
        result.setErrorCode(errate+"");
        result.setMessage(String.format("Recuperati %s WrapBio (con tmplBio) da una lista di %s pageIds'", text.format(valide), totPageIds));

        return result;
    }

    /**
     * Request singolo blocco <br>
     *
     * @param pageIds stringa dei pageIds delle pagine wiki da controllare
     */
    public AIResult request(AIResult result, final String pageIds) {
        String urlDomain;
        urlDomain = WIKI_QUERY_PAGEIDS + pageIds + WIKI_QUERY_BOT;
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
        List<WrapBio> listaNew = new ArrayList<>();
        List<WrapBio> listaOld;
        boolean valida = false;
        JSONObject jsonAll;
        JSONObject jsonError;
        JSONObject jsonQuery;
        JSONArray jsonPages = null;
        WrapBio wrap = null;
        int pagesNonValide = 0;

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
                    wrap = creaWrapBio((JSONObject) obj);
                    if (wrap != null) {
                        if (wrap != null && wrap.isValido()) {
                            listaNew.add(wrap);
                        }
                        else {
                            pagesNonValide++;
                        }
                    }
                }
                result.setValue(result.getValue() + pagesNonValide);
                result.setCodeMessage(JSON_SUCCESS);
                listaOld = (List<WrapBio>) result.getLista();
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


    private WrapBio creaWrapBio(final JSONObject jsonPage) {
        long pageid;
        String title;
        String stringTimestamp;
        String content;
        String tmplBio;

        title = (String) jsonPage.get(KEY_JSON_TITLE);

        pageid = (long) jsonPage.get(KEY_JSON_PAGE_ID);
        if (jsonPage.get(KEY_JSON_MISSING) != null && (boolean) jsonPage.get(KEY_JSON_MISSING)) {
            return new WrapBio(pageid, title, AETypePage.nonEsiste);
        }

        JSONArray jsonRevisions = (JSONArray) jsonPage.get(KEY_JSON_REVISIONS);
        JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
        stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);
        JSONObject jsonSlots = (JSONObject) jsonRevZero.get(KEY_JSON_SLOTS);
        JSONObject jsonMain = (JSONObject) jsonSlots.get(KEY_JSON_MAIN);
        content = (String) jsonMain.get(KEY_JSON_CONTENT);

        //--la pagina esiste ma il content no
        if (text.isEmpty(content)) {
            return new WrapBio(pageid, title, VUOTA, stringTimestamp, AETypePage.testoVuoto);
        }

        //--contenuto inizia col tag della disambigua
        if (content.startsWith(TAG_DISAMBIGUA_UNO) || content.startsWith(TAG_DISAMBIGUA_DUE)) {
            return new WrapBio(title, AETypePage.disambigua);
        }

        //--contenuto inizia col tag del redirect
        if (content.startsWith(TAG_REDIRECT_UNO) || content.startsWith(TAG_REDIRECT_DUE) || content.startsWith(TAG_REDIRECT_TRE) || content.startsWith(TAG_REDIRECT_QUATTRO)) {
            return new WrapBio(title, AETypePage.redirect);
        }

        //--estrazione del template
        tmplBio = wikiApi.estraeTmpl(content, "Bio");
        if (text.isValid(tmplBio)) {
            return new WrapBio(pageid, title, tmplBio, stringTimestamp, AETypePage.testoConTmpl);
        }
        else {
            return new WrapBio(pageid, title, VUOTA, stringTimestamp, AETypePage.testoSenzaTmpl);
        }
    }

}
