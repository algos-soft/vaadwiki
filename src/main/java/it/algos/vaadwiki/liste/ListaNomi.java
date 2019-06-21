package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.nome.Nome;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 07-Jun-2019
 * Time: 20:32
 * <p>
 * Crea la lista delle persone col nome <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaNomi extends ListaNomiCognomi {


    //--property
    protected Nome nome;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaNomi() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaNomi.class, nome) <br>
     *
     * @param nome di cui costruire la pagina sul server wiki
     */
    public ListaNomi(Nome nome) {
        this.nome = nome;
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
        return bioService.findAllByNome(nome.nome);
    }// fine del metodo

    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public void creaMappa() {
        ArrayList<WrapDidascalia> listaDidascalie = null;

        //--Crea la lista grezza delle voci biografiche
        this.listaGrezzaBio = listaBio();

        //--Crea una lista di didascalie specifiche
        listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, typeDidascalia);

//        //--Ordina la lista di didascalie specifiche
//        listaService.ordinaListaDidascalie(listaDidascalie);//@todo la query è già ordinata  MA FORSE NO

        //--Costruisce la mappa completa
        this.mappa = listaService.creaMappa(listaDidascalie);


        //--Costruisce la mappa completa @todo TEST
//        listaService.pippo(listaDidascalie, typeDidascalia,tagParagrafoNullo);
    }// fine del metodo

}// end of class
