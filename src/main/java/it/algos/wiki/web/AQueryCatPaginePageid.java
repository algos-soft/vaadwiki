package it.algos.wiki.web;

import it.algos.vaadwiki.enumeration.EAQueryCat;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
 * La lista viene resa come un array di Long (pageid) nella variabile (pubblica) 'listaPageid' <br>
 */
@Component("AQueryCatPaginePageid")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryCatPaginePageid extends AQueryCatPagine {

    /**
     * Costruttore base senza parametri <br>
     * NON usato <br>
     * Viene usato solo il costruttore col titolo della categoria <br>
     * Questo costruttore (deprecato) rimane SOLO per non mandare in errore (in rosso) la injection di Spring
     * Se Spring trova un solo costruttore (quello col parametro) cerca di iniettare il parametro
     * che però è una stringa e va in errore (solo visivo, in realtà compila lo stesso)
     */
    @Deprecated
    public AQueryCatPaginePageid() {
    }// end of constructor

    /**
     * Costruttore con parametri. È OBBLIGATORIO titoloCat <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryCatPaginePageid.class, titoloCat) <br>
     * Usa: appContext.getBean(AQueryCatPaginePageid.class, titoloCat).listaPageid <br>
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public AQueryCatPaginePageid(String titoloCat) {
        super(titoloCat);
    }// end of constructor

    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.usaTitle = false;
    }// end of method

}// end of class
