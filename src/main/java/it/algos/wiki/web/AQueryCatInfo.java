package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 13-feb-2019
 * Time: 12:06
 */
@Component("AQueryCatInfo")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryCatInfo extends AQueryGet {

    /**
     * Tag completo 'urlDomain' per la richiesta di info sulla categoria
     */
    private static String TAG_INFO_CAT = TAG_QUERY + "&prop=categoryinfo&titles=Category:";


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryCatInfo() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).urlResponse() <br>
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public AQueryCatInfo(String titoloCat) {
        super(titoloCat);
    }// end of constructor

    /**
     * Numero di voci della categoria
     *
     * @return numero di pagine presenti nelloa categoria
     */
    public int numVoci() {
        return numVoci(urlDomain);
    }// end of method


    /**
     * Numero di voci della categoria
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     *
     * @return numero di pagine presenti nelloa categoria
     */
    public int numVoci(String titoloCat) {
        int numeroVoci = 0;
        String urlResponse = "";

        if (text.isValid(titoloCat)) {
            urlResponse = super.urlRequest(titoloCat);
            numeroVoci= wikiService.getNumVociCategory(urlResponse);
        }// end of if cycle

        return numeroVoci;
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
        return titoloWikiCodificato.startsWith(TAG_INFO_CAT) ? titoloWikiCodificato : TAG_INFO_CAT + titoloWikiCodificato;
    } // fine del metodo



}// end of class
