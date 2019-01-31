package it.algos.wiki.web;

import it.algos.wiki.LibWiki;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:37
 */
@Component("AQueryBio")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryBio extends AQueryVoce {

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryBio() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).urlResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
    public AQueryBio(String titoloWiki) {
        super(titoloWiki);
    }// end of constructor


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
    @Override
    public String urlRequest(String titoloWiki) {
        String templateBio = "";
        String contenutoVoce = super.urlRequest(titoloWiki);

        if (text.isValid(contenutoVoce)) {
            templateBio = LibWiki.estraeTmplBioCompresi(contenutoVoce); //@@todo SVILUPPI - trasformare la Libreria in istanza
        }// end of if cycle

        return templateBio;
    }// end of method


}// end of class
