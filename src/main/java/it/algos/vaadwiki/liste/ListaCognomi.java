package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 14-Jun-2019
 * Time: 17:23
 * <p>
 * Crea la lista delle persone per cognome <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Creata con appContext.getBean(ListaCognomi.class, nome) <br>
 * Punto di inzio @PostConstruct inizia() nella superclasse <br>
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
        super.typeDidascalia = EADidascalia.listaCognomi;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.titoloParagrafoVuoto = pref.getStr(TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI);
        super.paragrafoVuotoInCoda = pref.isBool(IS_PARAGRAFO_VUOTO_COGNOMI_IN_CODA);
        super.usaParagrafoSize = pref.isBool(USA_PARAGRAFO_SIZE_COGNOMI);
    }// end of method


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
     * Ordina la lista di didascalie specifiche <br>
     */
    @SuppressWarnings("all")
    public ArrayList<WrapDidascalia> ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        return listaService.ordinaListaDidascalieCognomi(listaDisordinata);
    }// fine del metodo

}// end of class
