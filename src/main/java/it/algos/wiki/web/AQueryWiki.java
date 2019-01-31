package it.algos.wiki.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:36
 */
@Component("AQueryWiki")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

    protected String itwikiSession;

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Può essere usato anche per creare l'istanza come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryWiki() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).urlResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
    public AQueryWiki(String titoloWiki) {
        super(titoloWiki);
    }// end of constructor


    /**
     * Controlla la stringa della request
     * <p>
     * Controlla che sia valida <br>
     * Inserisce un tag specifico iniziale <br>
     * In alcune query (AQueryWiki e sottoclassi) codifica i caratteri del wikiTitle <br>
     * Sovrascritto nelle sottoclassi specifiche <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     *
     * @return stringa del titolo completo da inviare con la request
     */
    @Override
    public String fixUrlDomain(String titoloWiki) {
        try { // prova ad eseguire il codice
            return text.isValid(titoloWiki) ? URLEncoder.encode(titoloWiki, ENCODE) : VUOTA;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return titoloWiki;
    } // fine del metodo

}// end of class
