package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import it.algos.vaadwiki.backend.enumeration.*;
import static it.algos.vaadwiki.backend.service.WikiBotService.*;
import it.algos.vaadwiki.backend.wrapper.*;
import it.algos.vaadwiki.wiki.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.net.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 15:47
 * <p>
 * UrlRequest:
 * urlDomain = "&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles="
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryBio extends AQuery {

    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return testo del template Bio
     */
    public WResult urlRequest(String wikiTitleGrezzo) {
        WResult result = WResult.errato();
        String urlDomain = WIKI_QUERY_TITLES + wikiApi.fixWikiTitle(wikiTitleGrezzo);
        String urlResponse;
        URLConnection urlConn;

        result.setWikiTitle(wikiTitleGrezzo);
        result.setQueryType(TypeQuery.get.get());
        result.setUrlRequest(urlDomain);

        try {
            urlConn = this.creaGetConnection(urlDomain);
            urlResponse = sendRequest(urlConn);
            return elaboraResponse(result, urlResponse);
        } catch (Exception unErrore) {
            return result;
        }
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        JSONObject jsonQuery = null;
        JSONArray jsonPages = null;
        JSONObject jsonPageZero = null;
        JSONArray jsonRevisions = null;
        JSONObject jsonRevZero = null;
        String stringTimestamp = VUOTA;
        JSONObject jsonSlots = null;
        JSONObject jsonMain = null;
        long pageId=0;
        String content = VUOTA;
        String tmplBio;
        WrapBio wrap;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        //--controllo del batchcomplete
        if (jsonAll != null && jsonAll.get(KEY_JSON_VALID) != null) {
            if (!(boolean) jsonAll.get(KEY_JSON_VALID)) {
                result.setValido(false);
                result.setErrorCode("batchcomplete=false");
                result.setErrorMessage(String.format("Qualcosa non ha funzionato nella lettura delle pagina wiki '%s'", result.getWikiTitle()));
                return result;
            }
        }

        if (jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            jsonQuery = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
        }

        if (jsonQuery != null && jsonQuery.get(KEY_JSON_PAGES) != null) {
            jsonPages = (JSONArray) jsonQuery.get(KEY_JSON_PAGES);
        }

        if (jsonPages != null && jsonPages.size() > 0) {
            jsonPageZero = (JSONObject) jsonPages.get(0);
        }

        //--controllo del missing
        if (jsonPageZero != null && jsonPageZero.get(KEY_JSON_MISSING) != null) {
            if ((boolean) jsonPageZero.get(KEY_JSON_MISSING)) {
                result.setValido(false);
                result.setErrorCode("missing=true");
                result.setErrorMessage(String.format("La pagina wiki '%s' non esiste", result.getWikiTitle()));
                wrap = new WrapBio(result.getWikiTitle(), AETypePage.nonEsiste);
                result.setWrap(wrap);
                return result;
            }
        }

        //--pageId
        if (jsonPageZero != null && jsonPageZero.get(KEY_JSON_PAGE_ID) != null) {
            pageId = (long) jsonPageZero.get(KEY_JSON_PAGE_ID);
        }

        if (jsonPageZero != null && jsonPageZero.get(KEY_JSON_REVISIONS) != null) {
            jsonRevisions = (JSONArray) jsonPageZero.get(KEY_JSON_REVISIONS);
        }

        if (jsonRevisions != null && jsonRevisions.size() > 0) {
            jsonRevZero = (JSONObject) jsonRevisions.get(0);
        }

        if (jsonRevZero != null && jsonRevZero.get(KEY_JSON_TIMESTAMP) != null) {
            stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);
        }

        if (jsonRevZero != null && jsonRevZero.get(KEY_JSON_SLOTS) != null) {
            jsonSlots = (JSONObject) jsonRevZero.get(KEY_JSON_SLOTS);
        }

        if (jsonSlots != null && jsonSlots.get(KEY_JSON_MAIN) != null) {
            jsonMain = (JSONObject) jsonSlots.get(KEY_JSON_MAIN);
        }

        if (jsonMain != null && text.isValid(jsonMain.get(KEY_JSON_CONTENT))) {
            content = (String) jsonMain.get(KEY_JSON_CONTENT);
        }

        //--contenuto inizia col tag della disambigua
        if (content.startsWith(TAG_DISAMBIGUA_UNO) || content.startsWith(TAG_DISAMBIGUA_DUE)) {
            result.setValido(false);
            result.setErrorCode("disambigua");
            result.setErrorMessage(String.format("La pagina wiki '%s' è una disambigua", result.getWikiTitle()));
            wrap = new WrapBio(result.getWikiTitle(), AETypePage.disambigua);
            result.setWrap(wrap);
            return result;
        }

        //--contenuto inizia col tag del redirect
        if (content.startsWith(TAG_REDIRECT_UNO) || content.startsWith(TAG_REDIRECT_DUE) || content.startsWith(TAG_REDIRECT_TRE) || content.startsWith(TAG_REDIRECT_QUATTRO)) {
            result.setValido(false);
            result.setErrorCode("redirect");
            result.setErrorMessage(String.format("La pagina wiki '%s' è un redirect", result.getWikiTitle()));
            wrap = new WrapBio(result.getWikiTitle(), AETypePage.redirect);
            result.setWrap(wrap);
            return result;
        }

        //--controllo l'esistenza del template bio
        //--estrazione del template
        tmplBio = wikiApi.estraeTmpl(content, "Bio");
        if (text.isValid(tmplBio)) {
            result.setValido(true);
            result.setCodeMessage("valida");
            result.setValidMessage(String.format("La pagina wiki '%s' è una biografia", result.getWikiTitle()));
            result.setErrorCode(VUOTA);
            result.setErrorMessage(VUOTA);
            wrap = new WrapBio(pageId, result.getWikiTitle(), tmplBio, stringTimestamp, AETypePage.testoConTmpl);
            result.setWrap(wrap);
            return result;
        }
        else {
            result.setValido(false);
            result.setErrorCode("manca tmpl Bio");
            result.setErrorMessage(String.format("La pagina wiki '%s' non è una biografia", result.getWikiTitle()));
            wrap = new WrapBio(result.getWikiTitle(), AETypePage.testoSenzaTmpl);
            result.setWrap(wrap);
            return result;
        }
    }

}
