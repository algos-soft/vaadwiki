package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaGiornoNato;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_GIORNO_NATI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 28-May-2019
 * Time: 22:38
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da WikiGiornoList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista dei Nati nel giorno <br>
 */
@Route(value = ROUTE_VIEW_GIORNO_NATI)
public class ViewGiornoNato extends ViewListe {


    /**
     * Punto di ingresso dopo la chiamata navigate() effettuata da com.vaadin.flow.router.Router verso questa view <br>
     *
     * @param event       con la Location, segments, target, source, ecc
     * @param giornoIdKey per recuperare l'istanza di Giorno
     */
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        this.giorno = giornoService.findById(giornoIdKey);
        this.inizia();
    }// end of method


    /**
     * Costruisce il testo con tutte le didascalie relative al giorno considerato <br>
     * Presenta le righe secondo uno dei possibili metodi di raggruppamento <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    public void inizia() {
        lista = appContext.getBean(ListaGiornoNato.class, giorno);
        super.inizia();
    }// end of method



    /**
     * Costruisce il titolo della pagina <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    protected void addInfoTitolo() {
        this.add("Lista biografie di " + text.format(numVoci) + " persone nate il " + giorno.getTitolo());
        super.addInfoTitolo();
    }// end of method

}// end of class
