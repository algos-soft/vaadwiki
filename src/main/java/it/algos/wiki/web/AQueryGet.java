package it.algos.wiki.web;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 30-gen-2019
 * Time: 12:01
 */
@Component("AQueryGet")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AQueryGet extends AQueryWiki {

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    protected final static String TAG_INFO = "&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles=";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    protected final static String TAG_SLOTS = "&rvslots=main";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    protected final static String TAG_PAGE = TAG_QUERY + TAG_SLOTS + TAG_INFO;

    /**
     * Tag completo 'urlDomain' per il controllo
     */
    protected final static String TAG_BOT = "&assert=bot";


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryGet() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, titoloWiki).urlResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
    public AQueryGet(String titoloWiki) {
        super(titoloWiki);
    }// end of constructor


}// end of class
