package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaAnnoMorto;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_ANNO_MORTI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:15
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare anno <br>
 * Viene invocata da WikiAnnoList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista dei Morti nell'anno <br>
 */
@Route(value = ROUTE_VIEW_ANNO_MORTI)
public class ViewAnnoMorto extends ViewListe {


    /**
     * Punto di ingresso dopo la chiamata navigate() effettuata da com.vaadin.flow.router.Router verso questa view <br>
     *
     * @param event     con la Location, segments, target, source, ecc
     * @param annoIdKey per recuperare l'istanza di Anno
     */
    @Override
    public void setParameter(BeforeEvent event, String annoIdKey) {
        this.anno = annoService.findById(annoIdKey);
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
        lista = appContext.getBean(ListaAnnoMorto.class, anno);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    protected void addInfoTitolo() {
        this.add("Lista biografie di " + text.format(numVoci) + " persone morte nel " + anno.getTitolo());
        super.addInfoTitolo();
    }// end of method

}// end of class
