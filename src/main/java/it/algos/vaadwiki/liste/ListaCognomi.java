package it.algos.vaadwiki.liste;

import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.nome.Nome;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 14-Jun-2019
 * Time: 17:23
 *
 * Crea la lista delle persone col cognome <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaCognomi extends Lista{


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

    /**
     * Costruisce una lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     * La lista NON è ordinata <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @param listaGrezzaBio di persone che hanno una valore valido per la pagina specifica
     *
     * @return lista NON ORDINATA di didascalie (Wrap)
     */
    @Override
    public ArrayList<WrapDidascalia> creaListaDidascalie(ArrayList<Bio> listaGrezzaBio) {
        ArrayList<WrapDidascalia> lista = new ArrayList<WrapDidascalia>();
        WrapDidascalia wrap;

        for (Bio bio : listaGrezzaBio) {
            wrap = appContext.getBean(WrapDidascalia.class, bio, EADidascalia.liste);
            lista.add(wrap);
        }// end of for cycle

        return lista;
    }// fine del metodo

}// end of class
