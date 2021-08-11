package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 15:48
 * <p>
 * UrlRequest:
 * urlDomain = "&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles="
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 */
public abstract class AQueryVoce  {


    /**
     * Request principale <br>
     * <p>
     * La stringa del urlDomain per la request viene elaborata <br>
     * Si crea la connessione <br>
     * La request base usa solo il GET <br>
     * In alcune request (non tutte) si aggiunge anche il POST <br>
     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
     * Si invia la connessione <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
//    @Override
    public String urlRequest(String titoloWiki) {
        String contenutoTesto = "";
//        Page page = super.pageResponse(titoloWiki);
//
//        if (page != null) {
//            contenutoTesto = page.getText();
//        }// end of if cycle

        return contenutoTesto;
    }// end of method

}
