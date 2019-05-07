package it.algos.wiki.web;

import it.algos.vaadwiki.enumeration.EAQueryCat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 04-May-2019
 * Time: 11:08
 * <p>
 * Recupera una lista di sole sub-categorie (categorie), senza le voci <br>
 * La query viene eseguita subito alla creazione dell'istanza della classe (visto che è SCOPE_PROTOTYPE) <br>
 * La lista viene resa come un array di String (wikiTitle) nella variabile (pubblica) 'lista' <br>
 */
@Component("AQueryCatCategorie")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryCatCategorie extends AQueryCat {

    /**
     * Costruttore base senza parametri <br>
     * NON usato <br>
     * Viene usato solo il costruttore col titolo della categoria <br>
     * Questo costruttore (deprecato) rimane SOLO per non mandare in errore (in rosso) la injection di Spring
     * Se Spring trova un solo costruttore (quello col parametro) cerca di iniettare il parametro
     * che però è una stringa e va in errore (solo visivo, in realtà compila lo stesso)
     */
    @Deprecated
    public AQueryCatCategorie() {
    }// end of constructor


    /**
     * Costruttore con parametri. È OBBLIGATORIO titoloCat <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryCatCategorie.class, titoloCat) <br>
     * Usa: appContext.getBean(AQueryCatCategorie.class, titoloCat).lista <br>
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public AQueryCatCategorie(String titoloCat) {
        super(titoloCat);
        super.type = EAQueryCat.categorie;
    }// end of constructor


}// end of class
