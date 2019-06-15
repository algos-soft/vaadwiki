package it.algos.vaadwiki.views;

import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_ANNO_MORTI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:15
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare anno <br>
 * Viene invocata da WikiAnnoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista dei Morti nell'anno <br>
 */
@Route(value = ROUTE_VIEW_ANNO_MORTI)
public class ViewAnnoMorto extends ViewAnni {


    /**
     * Costruisce una mappa di tutte le didascalie relative al giorno considerato <br>
     * Presenta le righe secondo uno dei possibili metodi di raggruppamento <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo deve invocare il metodo della superclasse <br>
     */
    public void inizia() {
        LinkedHashMap<String, ArrayList<String>> mappa;

        mappa = listaService.getMappaAnnoMorto(anno);
        super.testo = listaService.righeSemplici(mappa);

        super.numVoci = listaService.getMappaSize(mappa);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Sovrascritto <br>
     */
    protected void addTitolo() {
        String titolo = "Lista biografie di " + text.format(numVoci) + " persone morte nel " + anno.getTitolo() + A_CAPO + A_CAPO;
        testo = titolo + testo;
    }// end of method

}// end of class
