package it.algos.vaadwiki.views;

import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaAnnoMorto;
import it.algos.vaadwiki.liste.ListaAnnoNato;

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
 * Viene invocata da WikiAnnoList <br>
 * Lista dei Morti nell'anno <br>
 */
@Route(value = ROUTE_VIEW_ANNO_MORTI)
public class ViewAnnoMorto extends ViewAnni {


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
