package it.algos.wiki.mediawiki;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.web.Read;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.net.URLEncoder;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 18:09
 * <p>
 * Legge il contenuto web (grezzo) di una pagina wiki di tipo HTTPS. Non adatta per altre pagine web.
 * Solo lettura. Request di tipo GET. Niente Bot. Niente cookies.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ReadWiki extends Read {


    /**
     * Request di tipo GET
     * Accetta SOLO un domain (indirizzo) di pagine wiki
     *
     * @param titoloWiki
     *
     * @return contenuto visibile della voce sul server wiki
     */
    public String legge(String titoloWiki) {

        try { // prova ad eseguire il codice
            titoloWiki = URLEncoder.encode(titoloWiki, "UTF-8");
        } catch (Exception unErrore) { // intercetta l'errore
            String errore = unErrore.getMessage();
        }// fine del blocco try-catch

        return super.legge(titoloWiki);
    } // fine del metodo


    /**
     * Inserisce un tag specifico prima del titoloWiki della pagina per costruire il 'domain' completo
     */
    @Override
    public String getTagIniziale() {
        return TAG_WIKI;
    } // fine del metodo

}// end of class
