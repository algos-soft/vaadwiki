package it.algos.vaadwiki.wiki.query;


import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.net.*;


/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 15:49
 * <p>
 * UrlRequest:
 * urlDomain = "&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles="
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 *
  * Legge una singola pagina individuate da wikiTitle <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryPage extends AQuery {


    /**
     * Request principale <br>
     *
     * @param wikiTitle della pagina wiki da recuperare
     */
    public AIResult request( final String wikiTitle) {
        AIResult result = AResult.valido();
        String urlDomain="";
//        urlDomain = WIKI_QUERY_PAGEIDS + pageIds + WIKI_QUERY_BOT;
        String urlResponse;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, botLogin != null ? botLogin.getCookies() : null);
            urlResponse = sendRequest(urlConn);
            result = elaboraResponse(result, urlResponse);
        } catch (Exception unErrore) {
        }

        return result;
    }
    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     *
     */
    protected AIResult elaboraResponse(AIResult result, final String rispostaDellaQuery) {
        AIResult assertResult;
        JSONObject jsonLogin = null;
        boolean loginValido = false;
        String jsonResult = VUOTA;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(LOGIN) != null) {
            jsonLogin = (JSONObject) jsonAll.get(LOGIN);
        }

        if (jsonLogin != null) {
            result.setResponse(jsonLogin.toString());
        }

        if (jsonLogin != null && jsonLogin.get(RESULT) != null) {
            if (jsonLogin.get(RESULT) != null) {
                jsonResult = (String) jsonLogin.get(RESULT);
                loginValido = text.isValid(jsonResult) && jsonResult.equals(JSON_SUCCESS);
            }
            if (loginValido) {
                result.setCodeMessage(JSON_SUCCESS);
            }
            else {
                result.setValido(false);
                result.setErrorCode(jsonResult.toString());
                result.setErrorMessage((String) jsonLogin.get(JSON_REASON));
                return result;
            }

        }



        return result;
    }

}
