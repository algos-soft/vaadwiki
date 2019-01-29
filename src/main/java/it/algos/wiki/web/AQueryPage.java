package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.net.URLEncoder;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:36
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AQueryPage extends AQueryGet {

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    private final static String TAG_PAGE = TAG_QUERY + "prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles=";


    /**
     * Request di tipo GET
     * Accetta SOLO un titoloWiki (indirizzo) di pagine wiki
     *
     * @param titoloWiki della pagina
     *
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public Page crea(String titoloWiki) {
        Page page = null;
        String contenutoCompletoPaginaWebInFormatoJSON = "";

        if (text.isValid(titoloWiki)) {
            try { // prova ad eseguire il codice
                titoloWiki = URLEncoder.encode(titoloWiki, "UTF-8");
                contenutoCompletoPaginaWebInFormatoJSON = super.urlRequest(titoloWiki);
                page = new Page(contenutoCompletoPaginaWebInFormatoJSON);
            } catch (Exception unErrore) { // intercetta l'errore
                String errore = unErrore.getMessage();
            }// fine del blocco try-catch
        }// end of if cycle

        return page;
    }// end of method


    /**
     * Controlla la stringa della request
     * Inserisce un tag specifico
     * Codifica i caratteri
     *
     * @param titoloWiki della pagina
     *
     * @return stringa della request modificata
     */
    @Override
    public String fixUrlDomain(String titoloWiki) {
        return titoloWiki.startsWith(TAG_PAGE) ? titoloWiki : TAG_PAGE + titoloWiki;
    } // fine del metodo

}// end of class
