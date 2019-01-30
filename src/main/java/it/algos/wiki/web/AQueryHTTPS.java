package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:39
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryHTTPS extends AQueryWeb {

    /**
     * Tag aggiunto prima del titolo (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    private final static String TAG_INIZIALE = "https://";


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Può essere usato anche per creare l'istanza come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryHTTPS() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).urlResponse() <br>
     *
     * @param urlDomain indirizzo web usato nella urlRequest
     */
    public AQueryHTTPS(String urlDomain) {
        super(urlDomain);
    }// end of constructor


    /**
     * Controlla la stringa della request
     * <p>
     * Controlla che sia valida <br>
     * Inserisce un tag specifico iniziale <br>
     * In alcune query (AQueryWiki e sottoclassi) codifica i caratteri del wikiTitle <br>
     *
     * @param urlDomain stringa della request originale
     *
     * @return stringa della request modificata
     */
    @Override
    public String fixUrlDomain(String urlDomain) {
        return text.isValid(urlDomain) ? urlDomain.startsWith(TAG_INIZIALE) ? urlDomain : TAG_INIZIALE + urlDomain : "";
    } // fine del metodo

}// end of class
