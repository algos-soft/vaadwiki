package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 14-Jun-2019
 * Time: 17:23
 * <p>
 * Crea la lista delle persone col cognome <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaCognomi extends ListaNomiCognomi {


    //--property
    protected Cognome cognome;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaCognomi() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaCognomi.class, cognome) <br>
     *
     * @param cognome di cui costruire la pagina sul server wiki
     */
    public ListaCognomi(Cognome cognome) {
        this.cognome = cognome;
        super.typeDidascalia = EADidascalia.liste;
    }// end of constructor


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Nome nella property nome
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    @Override
    public ArrayList<Bio> listaBio() {
        return bioService.findAllByCognome(cognome.cognome);
    }// fine del metodo


}// end of class
