package it.algos.vaadwiki.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.liste.ListaGiornoMorto;
import it.algos.vaadwiki.liste.ListaGiornoNato;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 15:46
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da WikiGiornoViewList <br>
 * Lista dei Morti nel giorno <br>
 */
@Route(value = ROUTE_VIEW_GIORNO_MORTI)
public class ViewGiornoMorto extends ViewGiorni {



    /**
     * Costruisce il testo con tutte le didascalie relative al giorno considerato <br>
     * Presenta le righe secondo uno dei possibili metodi di raggruppamento <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    @Override
    public void inizia() {
        lista = appContext.getBean(ListaGiornoMorto.class, giorno);
        super.inizia();
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Sovrascritto <br>
     */
    @Override
    protected String addTitolo() {
        return "Lista biografie di " + text.format(numVoci) + " persone morte il " + giorno.getTitolo();
    }// end of method


}// end of class
