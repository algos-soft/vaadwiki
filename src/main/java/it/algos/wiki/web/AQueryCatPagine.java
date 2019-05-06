package it.algos.wiki.web;

import it.algos.vaadwiki.enumeration.EAQueryCat;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 04-May-2019
 * Time: 11:07
 * <p>
 * Recupera una lista di sole pagine (voci), senza le sub-categorie <br>
 * La query viene eseguita subito alla creazione dell'istanza della classe (visto che è SCOPE_PROTOTYPE) <br>
 * La lista viene resa come un array di String (wikiTitle) nella variabile (pubblica) 'lista' <br>
 */
@Component("AQueryCatPagine")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryCatPagine extends AQueryCat {

    /**
     * Costruttore base senza parametri <br>
     * NON usato <br>
     * Viene usato solo il costruttore col titolo della categoria <br>
     * Questo costruttore (deprecato) rimane SOLO per non mandare in errore (in rosso) la injection di Spring
     * Se Spring trova un solo costruttore (quello col parametro) cerca di iniettare il parametro
     * che però è una stringa e va in errore (solo visivo, in realtà compila lo stesso)
     */
    @Deprecated
    public AQueryCatPagine() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryCatPagine.class, titoloCat) <br>
     * Usa: appContext.getBean(AQueryCatPagine.class, titoloCat).lista <br>
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public AQueryCatPagine(String titoloCat) {
        super(titoloCat);
        super.type = EAQueryCat.pagine;
    }// end of constructor


}// end of class
