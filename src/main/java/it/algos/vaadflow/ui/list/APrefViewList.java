package it.algos.vaadflow.ui.list;

import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.IADialog;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 07:19
 * <p>
 * Sottoclasse di servizio per regolare le property di AViewList in una classe 'dedicata'. <br>
 * Alleggerisce la 'lettura' della classe principale. <br>
 * Le property sono regolarmente disponibili in AViewList ed in tutte le sue sottoclassi. <br>
 * Qui vengono regolate le property 'standard'. <br>
 * Nelle sottoclassi concrete le property possono essere sovrascritte. <br>
 */
public abstract class APrefViewList extends AViewList {

    /**
     * Costruttore <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    public APrefViewList(IAPresenter presenter, IADialog dialog) {
        super(presenter, dialog);
    }// end of Spring constructor


    /**
     * Le preferenze standard
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    protected void fixPreferenze() {

        /**
         * Flag di preferenza per usare il campo-testo di ricerca e selezione nella barra dei menu.
         * Facoltativo ed alternativo a usaSearchTextDialog. Normalmente false.
         */
        usaSearchTextField = false;

        /**
         * Flag di preferenza per usare il campo-testo di ricerca e selezione nella barra dei menu.
         * Facoltativo ed alternativo a usaSearchTextField. Normalmente true.
         */
        usaSearchTextDialog = true;

        //--Flag di preferenza per usare il bottone all situato nella searchBar. Normalmente true
        usaAllButton = true;

        //--Flag di preferenza per usare il bottone new situato nella searchBar. Normalmente true.
        usaSearchBottoneNew = true;

        //--Flag di preferenza per usare il placeholder di informazioni specifiche sopra la Grid. Normalmente false.
        usaTopAlert = false;

        //--Flag di preferenza per la Label nell'header della Grid grid. Normalmente true.
        usaHaederGrid = true;

        //--Flag di preferenza per modificare la entity. Normalmente true.
        isEntityModificabile = true;

        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        usaBottoneEdit = true;

        //--Flag di preferenza per il testo del bottone Edit. Normalmente 'Edit'.
        testoBottoneEdit = EDIT_NAME;

        //--Flag di preferenza per usare il placeholder di botoni ggiuntivi sotto la Grid. Normalmente false.
        usaBottomLayout = false;

        //--Flag di preferenza per cancellare tutti gli elementi. Normalmente false.
        usaBottoneDeleteAll = false;

        //--Flag di preferenza per resettare le condizioni standard di partenza. Normalmente false.
        usaBottoneReset = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityDeveloper = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityAdmin = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityEmbedded = false;

        //--Flag di preferenza se si caricano dati demo alla creazione. Resettabili. Normalmente false.
        isEntityUsaDatiDemo = false;

        //--Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true.
        usaRefresh = true;

        //--Flag di preferenza per selezionare il numero di righe visibili della Grid. Normalmente limit = pref.getInt(FlowCost.MAX_RIGHE_GRID) .
        limit = pref.getInt(FlowCost.MAX_RIGHE_GRID);

        //--Flag di preferenza per usare una route view come detail della singola istanza. Normalmente true.
        //--In alternativa si può usare un Dialog.
        usaRouteFormView = false;

        //--Flag di preferenza per limitare le righe della Grid e mostrarle a gruppi (pagine). Normalmente true.
        usaPagination = true;

        //--Flag di preferenza per la soglia di elementi che fanno scattare la pagination.
        //--Specifico di ogni ViewList. Se non specificato è uguale alla preferenza. Default 50
        sogliaPagination = pref.getInt(FlowCost.SOGLIA_PAGINATION, 50);

        //--Flag per la larghezza della Grid. Default a 75. Espressa come numero per comodità; poi viene convertita in "em".
        gridWith = 75;
    }// end of method

}// end of class
