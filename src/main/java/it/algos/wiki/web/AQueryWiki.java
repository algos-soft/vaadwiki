package it.algos.wiki.web;

import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
public abstract class AQueryWiki extends AQuery {

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    protected final static String TAG_WIKI = "https://it.wikipedia.org/wiki/";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    protected final static String TAG_API = "https://it.wikipedia.org/w/api.php?";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    protected final static String TAG_BASE = TAG_API + "format=json&formatversion=2&";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    protected final static String TAG_QUERY = TAG_BASE + "action=query&";

    private String titoloWiki;


    public AQueryBio() {
    }


    public AQueryBio(String titoloWiki) {
        this.titoloWiki = titoloWiki;
    }


}// end of class
