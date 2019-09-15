package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaCognomi;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.cognome.CognomeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_COGNOMI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 14-Jun-2019
 * Time: 17:19
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare cognome <br>
 * Viene invocata da CognomeViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista delle biografie di un Cognome <br>
 */
@Route(value = ROUTE_VIEW_COGNOMI)
public class ViewCognome extends ViewListe{




    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected CognomeService cognomeService;


    //--property
    protected Cognome cognome;


    /**
     * Recupera il nome arrivato come parametro nella chiamata del browser effettuata da @Route <br>
     *
     * @param cognomeIdKey per recuperare l'istanza di Cognome
     */
    @Override
    public void setParameter(BeforeEvent event, String cognomeIdKey) {
        this.cognome = cognomeService.findById(cognomeIdKey);
        this.inizia();
    }// end of method




    /**
     * Costruisce una mappa di tutte le didascalie relative al cognome considerato <br>
     * Presenta le righe secondo uno dei possibili metodi di raggruppamento <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo deve invocare il metodo della superclasse <br>
     */
    public void inizia() {
        lista = appContext.getBean(ListaCognomi.class, cognome);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    protected void addInfoTitolo() {
        this.add("Lista biografie di " + text.format(numVoci) + " persone di cognome " + cognome.getCognome());
        super.addInfoTitolo();
    }// end of method

}// end of class
