package it.algos.vaadwiki.liste;

import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.bio.Bio;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 04-dic-2019
 * Time: 18:10
 * Lista delle persone per attività <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Creata con appContext.getBean(ListaAttivita.class, nome) <br>
 * Punto di inzio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {

    //--property
    protected Attivita attivita;

    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaAttivita() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaNomi.class, nome) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param attivita di cui costruire la pagina sul server wiki
     */
    public ListaAttivita(Attivita attivita) {
        this.attivita = attivita;
        super.typeDidascalia = EADidascalia.listaAttivita;
    }// end of constructor

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.paragrafoVuotoInCoda = pref.isBool(IS_PARAGRAFO_VUOTO_ATTIVITA_IN_CODA);
        super.usaParagrafoSize = pref.isBool(USA_PARAGRAFO_SIZE_ATTIVITA);
    }// end of method

    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Nome nella property nome
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    @Override
    public List<Bio> listaBio() {
        if (true) {
            return bioService.findAllByAttivita23(attivita);
        } else {
            return bioService.findAllByAttivita(attivita);
        }// end of if/else cycle
    }// fine del metodo


    /**
     * Ordina la lista di didascalie specifiche <br>
     */
    @SuppressWarnings("all")
    public ArrayList<WrapDidascalia> ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        return listaService.ordinaListaDidascalieNomi(listaDisordinata);
    }// fine del metodo


//    public ListaSottopagina getSottopagina() {
//        return listaService.sottopagina(mappa, pref.getInt(SOGLIA_SOTTOPAGINA_NOMI_COGNOMI), "Persone di nome " + nome.nome, titoloParagrafoVuoto, titoloSottoPaginaVuota);
//    }// fine del metodo

}// end of class