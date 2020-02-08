package it.algos.wiki.web;

import it.algos.vaadflow.enumeration.EASchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 13-feb-2019
 * Time: 12:06
 * <p>
 * Recupera il numero totale  di pagine (voci) e di sub-categorie <br>
 * La query viene eseguita subito alla creazione dell'istanza della classe (visto che è SCOPE_PROTOTYPE) <br>
 * La lista viene resa come un array di String (wikiTitle) nella variabile (pubblica) 'lista' <br>
 */
@Component("AQueryCatInfo")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryCatInfo extends AQueryGet {

    /**
     * Tag completo 'urlDomain' per la richiesta di info sulla categoria
     */
    private static String TAG_INFO_CAT = TAG_QUERY + "&prop=categoryinfo&titles=Category:";

    public int numVoci;


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
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizia() {
        numVoci();
    }// end of method


    /**
     * Numero di pagine della categoria
     *
     * @return numero di pagine presenti nelloa categoria
     */
    public int numVoci() {
        return numVoci(urlDomain);
    }// end of method


    /**
     * Numero di pagine della categoria
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     *
     * @return numero di pagine presenti nelloa categoria
     */
    public int numVoci(String titoloCat) {
        String urlResponse = "";

        if (text.isValid(titoloCat)) {
            urlResponse = super.urlRequest(titoloCat);
            numVoci = wikiService.getNumVociCategory(urlResponse);
        }// end of if cycle

        return numVoci;
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
