package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaAttivita;
import it.algos.vaadwiki.liste.ListaNazionalita;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_ATTIVITA;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_NAZIONALITA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 23-dic-2019
 * Time: 06:48
 * Classe per la visualizzazione di una lista di prova di biografie di una particolare nazionalità <br>
 * Viene invocata da NazionalitaList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista delle biografie di una Nazionalita <br>
 */
@Route(value = ROUTE_VIEW_NAZIONALITA)
public class ViewNazionalita extends ViewListe {


    /**
     * Punto di ingresso dopo la chiamata navigate() effettuata da com.vaadin.flow.router.Router verso questa view <br>
     *
     * @param event     con la Location, segments, target, source, ecc
     * @param attivitaIdKey per recuperare l'istanza di Attivita
     */
    @Override
    public void setParameter(BeforeEvent event, String attivitaIdKey) {
        this.nazionalita = nazionalitaService.findById(attivitaIdKey);
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
        lista = appContext.getBean(ListaNazionalita.class, nazionalita);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    protected void addInfoTitolo() {
        this.add("Lista biografie di " + text.format(numVoci) + " persone di nazionalità " + nazionalita.getPlurale() + A_CAPO);
        super.addInfoTitolo();
    }// end of method

}// end of class
