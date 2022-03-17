package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaCognomi;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_COGNOMI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 14-Jun-2019
 * Time: 17:19
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare cognome <br>
 * Viene invocata da CognomeList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista delle biografie di un Cognome <br>
 */
@Route(value = ROUTE_VIEW_COGNOMI)
public class ViewCognome extends ViewListe {


    /**
     * Punto di ingresso dopo la chiamata navigate() effettuata da com.vaadin.flow.router.Router verso questa view <br>
     *
     * @param event        con la Location, segments, target, source, ecc
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
        this.add("Lista biografie di " + text.format(numVoci) + " persone di cognome " + cognome.cognome);
        super.addInfoTitolo();
    }// end of method

}// end of class
