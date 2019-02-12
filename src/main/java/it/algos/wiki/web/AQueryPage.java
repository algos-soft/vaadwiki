package it.algos.wiki.web;

import it.algos.wiki.Page;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:36
 * <p>
 * UrlRequest:
 * urlDomain = "&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles="
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 */
@Component("AQueryPage")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryPage extends AQueryGet {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryPage() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).pageResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
    public AQueryPage(String titoloWiki) {
        super(titoloWiki);
    }// end of constructor


    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        this.isUsaBot = false;
        this.isUploadCookies = false;
    }// end of method


    /**
     * Page della response
     *
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public Page pageResponse() {
        return pageResponse(urlDomain);
    }// end of method


    /**
     * Page della response
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     *
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public Page pageResponse(String titoloWiki) {
        Page page = null;
        String contenutoCompletoPaginaWebInFormatoJSON;
        HashMap<String, Object> mappa;

        contenutoCompletoPaginaWebInFormatoJSON = super.urlRequest(titoloWiki);
        mappa = wikiService.getMappaPagina(contenutoCompletoPaginaWebInFormatoJSON);
        page = new Page(mappa);

        return page;
    }// end of method


    /**
     * Controlla la stringa della request
     * <p>
     * Controlla che sia valida <br>
     * Inserisce un tag specifico iniziale <br>
     * In alcune query (AQueryWiki e sottoclassi) codifica i caratteri del wikiTitle <br>
     * Sovrascritto nelle sottoclassi specifiche <br>
     *
     * @param titoloWikiGrezzo della pagina (necessita di codifica per eliminare gli spazi vuoti) usato nella urlRequest
     *
     * @return stringa del titolo completo da inviare con la request
     */
    @Override
    public String fixUrlDomain(String titoloWikiGrezzo) {
        String titoloWikiCodificato = super.fixUrlDomain(titoloWikiGrezzo);
        return titoloWikiCodificato.startsWith(TAG_PAGE) ? titoloWikiCodificato : TAG_PAGE + titoloWikiCodificato;
    } // fine del metodo

}// end of class
