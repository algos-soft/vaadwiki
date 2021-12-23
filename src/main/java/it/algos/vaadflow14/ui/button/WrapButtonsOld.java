package it.algos.vaadflow14.ui.button;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.enumeration.*;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 09-mag-2020
 * Time: 14:20
 * Wrap di informazioni passato dalla Logic alla creazione del AButtonLayout <br>
 * La Logic mantiene lo stato ed elabora informazioni che verranno usate dal AButtonLayout <br>
 * <p>
 * - Un gruppo di bottoni iniziali
 * - Un gruppo di ricerca
 * - Un gruppo di bottoni centrali
 * - Un gruppo di combobox di selezione e filtro
 * - Un gruppo di bottoni finali
 */
public class WrapButtonsOld {

    /**
     * Property per l'enumeration di bottoni iniziali (facoltativa, può anche essere sostituita da listaBottoniIniziali)
     */
    private List<AEButton> iniziali;

    /**
     * Property per le informazioni per la ricerca (facoltativa)
     */
    private WrapSearch wrapSearch;

    /**
     * Property per l'enumeration di bottoni centrali (facoltativa, può anche essere sostituita da listaBottoniCentrali)
     */
    private List<AEButton> centrali;

    /**
     * Property per la lista di bottoni non standard (facoltativa)
     */
    private List<Button> specifici;

    /**
     * Property con la mappa di combobox di selezione e filtro (facoltativa)
     */
    private LinkedHashMap<String, ComboBox> mappaComboBox;


    /**
     * Property per l'enumeration di bottoni finali (facoltativa)
     */
    private List<AEButton> finali;


    /**
     * Property per selezionare i bottoni standard in base al tipo di Form (usata solo in ABottomLayout)
     */
    private AEOperation operationForm;


    public WrapButtonsOld(List<AEButton> iniziali) {
        this(iniziali, null, null, null, null, null, null);
    }


    public WrapButtonsOld(List<AEButton> iniziali, List<Button> specifici, List<AEButton> finali, AEOperation operationForm) {
        this(iniziali, null, null, specifici, null, finali, operationForm);
    }


    public WrapButtonsOld(List<AEButton> iniziali, WrapSearch wrapSearch, List<AEButton> centrali, List<Button> specifici, LinkedHashMap<String, ComboBox> mappaComboBox, List<AEButton> finali, AEOperation operationForm) {
        this.iniziali = iniziali;
        this.wrapSearch = wrapSearch != null ? wrapSearch : new WrapSearch();
        this.centrali = centrali;
        this.specifici = specifici;
        this.mappaComboBox = mappaComboBox;
        this.finali = finali;
        this.operationForm = operationForm != null ? operationForm : AEOperation.edit;
    }


    public List<AEButton> getIniziali() {
        return iniziali;
    }


    public List<AEButton> getCentrali() {
        return centrali;
    }


    public List<AEButton> getFinali() {
        return finali;
    }


    public WrapSearch getWrapSearch() {
        return wrapSearch;
    }


    public List<Button> getSpecifici() {
        return specifici;
    }


    public LinkedHashMap<String, ComboBox> getMappaComboBox() {
        return mappaComboBox;
    }


    public AEOperation getOperationForm() {
        return operationForm;
    }

}
