package it.algos.wiki.web;

import it.algos.vaadwiki.enumeration.EAQueryCat;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 11-May-2019
 * Time: 21:32
 * <p>
 * Recupera una lista di sole pagine (voci), senza le sub-categorie <br>
 * La query viene eseguita subito alla creazione dell'istanza della classe (visto che è SCOPE_PROTOTYPE) <br>
 * La lista viene resa come un array di String (wikiTitle) nella variabile (pubblica) 'listaTitle' <br>
 */
@Component("AQueryCatPagineTitle")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryCatPagineTitle extends AQueryCatPagine {

    /**
     * Costruttore base senza parametri <br>
     * NON usato <br>
     * Viene usato solo il costruttore col titolo della categoria <br>
     * Questo costruttore (deprecato) rimane SOLO per non mandare in errore (in rosso) la injection di Spring
     * Se Spring trova un solo costruttore (quello col parametro) cerca di iniettare il parametro
     * che però è una stringa e va in errore (solo visivo, in realtà compila lo stesso)
     */
    @Deprecated
    public AQueryCatPagineTitle() {
    }// end of constructor

    /**
     * Costruttore con parametri. È OBBLIGATORIO titoloCat <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryCatPagineTitle.class, titoloCat) <br>
     * Usa: appContext.getBean(AQueryCatPagineTitle.class, titoloCat).listaTitle <br>
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public AQueryCatPagineTitle(String titoloCat) {
        super(titoloCat);
    }// end of constructor

}// end of class
