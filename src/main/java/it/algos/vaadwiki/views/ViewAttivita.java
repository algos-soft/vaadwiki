package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaAttivita;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_ATTIVITA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 04-dic-2019
 * Time: 17:21
 * Classe per la visualizzazione di una lista di prova di biografie di una particolare attivita <br>
 * Viene invocata da AttivitaList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista delle biografie di una Attivita <br>
 */
@Route(value = ROUTE_VIEW_ATTIVITA)
public class ViewAttivita extends ViewListe {



    /**
     * Punto di ingresso dopo la chiamata navigate() effettuata da com.vaadin.flow.router.Router verso questa view <br>
     *
     * @param event     con la Location, segments, target, source, ecc
     * @param attivitaIdKey per recuperare l'istanza di Attivita
     */
    @Override
    public void setParameter(BeforeEvent event, String attivitaIdKey) {
        this.attivita = attivitaService.findById(attivitaIdKey);
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
        lista = appContext.getBean(ListaAttivita.class, attivita);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    protected void addInfoTitolo() {
        this.add("Lista biografie di " + text.format(numVoci) + " persone con attività " + attivita.getPlurale() + A_CAPO);
        super.addInfoTitolo();
    }// end of method

}// end of class
