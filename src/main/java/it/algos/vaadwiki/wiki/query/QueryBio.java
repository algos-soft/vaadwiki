package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadwiki.backend.enumeration.*;
import static it.algos.vaadwiki.backend.service.AWikiBotService.*;
import it.algos.vaadwiki.backend.wrapper.*;
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
    public AIResult urlRequest(String wikiTitleGrezzo) {
        AIResult result = WResult.errato();
        String urlDomain = WIKI_QUERY_TITLES + wikiApi.fixWikiTitle(wikiTitleGrezzo);
        String urlResponse;
        URLConnection urlConn;

        result.setWikiTitle(wikiTitleGrezzo);
        result.setQueryType(TypeQuery.getCookies.get());
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
    protected AIResult elaboraResponse(AIResult result, final String rispostaDellaQuery) {
        JSONObject jsonQuery = null;
        JSONArray jsonNormalized = null;
        JSONObject jsonNormalizedZero = null;
        String jsonResult = VUOTA;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            jsonQuery = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
        }

        if (jsonQuery != null && jsonQuery.get(KEY_JSON_NORMALIZED) != null) {
            jsonNormalized = (JSONArray) jsonQuery.get(KEY_JSON_NORMALIZED);
        }

        if (jsonNormalized != null && jsonNormalized.get(0) != null) {
            jsonNormalizedZero = (JSONObject) jsonNormalized.get(0);
        }

        if (jsonNormalizedZero != null && jsonNormalizedZero.get(KEY_JSON_ENCODED) != null) {
            if ((boolean) jsonNormalizedZero.get(KEY_JSON_ENCODED)) {
            }
            else {
                result.setValido(false);
                result.setErrorCode("fromcoded=false");
                result.setErrorMessage(String.format("La pagina wiki '%s' non esiste",result.getWikiTitle()));
                return result;
            }
        }


        //        if (jsonAll != null && jsonAll.get(LOGIN) != null) {
        //            jsonLogin = (JSONObject) jsonAll.get(LOGIN);
        //        }
        //
        //        if (jsonLogin != null) {
        //            result.setResponse(jsonLogin.toString());
        //        }
        //
        //        if (jsonLogin != null && jsonLogin.get(RESULT) != null) {
        //            if (jsonLogin.get(RESULT) != null) {
        //                jsonResult = (String) jsonLogin.get(RESULT);
        //                loginValido = text.isValid(jsonResult) && jsonResult.equals(JSON_SUCCESS);
        //            }
        //            if (loginValido) {
        //                result.setCodeMessage(JSON_SUCCESS);
        //            }
        //            else {
        //                result.setValido(false);
        //                result.setErrorCode(jsonResult.toString());
        //                result.setErrorMessage((String) jsonLogin.get(JSON_REASON));
        //                return result;
        //            }
        //
        //        }

        return result;
    }

}
