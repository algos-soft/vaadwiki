package it.algos.vaadflow14.ui.button;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.interfaces.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 09-mag-2020
 * Time: 14:20
 * Wrap di informazioni passato da ALogic alla creazione di AButtonLayout <br>
 * La ALogic mantiene lo stato ed elabora informazioni che verranno usate dal AButtonLayout <br>
 * Questo wrapper viene costruito da in ALogic e contiene:
 * A - (obbligatorio) la AILogic con cui regolare i listener per l'evento/azione da eseguire
 * B - (semi-obbligatorio) una serie di bottoni standard, sotto forma di List<AEButton>
 * C - (facoltativo) wrapper di dati per il dialogo/campo di ricerca
 * D - (facoltativo) una mappa di combobox di selezione e filtro, LinkedHashMap<String, ComboBox>
 * E - (facoltativo) una serie di bottoni non-standard, sotto forma di List<Button>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WrapButtons {

    private static int NUMBER_BUTTONS_STANDARD = 4;

    /**
     * A - (obbligatorio) la AILogic con cui regolare i listener per l'evento/azione da eseguire
     */
    private AILogic entityLogic;

    /**
     * B - (semi-obbligatorio) una serie di bottoni standard, sotto forma di List<AIButton>
     */
    private List<AIButton> listaABottoni;

    /**
     * C - (facoltativo) wrapper di dati per il dialogo/campo di ricerca
     */
    private WrapSearch wrapSearch;

    /**
     * D - (facoltativo) una mappa di combobox di selezione e filtro, LinkedHashMap<String, ComboBox>
     */
    private Map<String, ComboBox> mappaComboBox;

    /**
     * E - (facoltativo) una serie di bottoni non-standard, sotto forma di List<Button>
     */
    private List<Button> listaBottoniSpecifici;

    /**
     * Property per il numero di bottoni nella prima riga sopra la Grid (facoltativa)
     */
    private int maxNumeroBottoniPrimaRiga;

    //    /**
    //     * Property per selezionare i bottoni standard in base al tipo di Form (usata solo in ABottomLayout)
    //     */
    //    private AEOperation operationForm;


    /**
     * Costruttore senza bottoni <br>
     * Costruito con appContext.getBean(WrapButtons.class, entityLogic) <br>
     * Se l'istanza viene creata SENZA una lista di bottoni, presenta solo il bottone 'New' <br>
     *
     * @param entityLogic a cui rinviare l'evento/azione da eseguire
     */
    public WrapButtons(final AILogic entityLogic) {
        this(entityLogic, (List<AIButton>) Collections.singletonList((AIButton)AEButton.nuovo));
    }


    /**
     * Costruttore con una serie di bottoni standard, sotto forma di List<AEButton> <br>
     * Costruito con appContext.getBean(WrapButtons.class, entityLogic, listaAEBottoni) <br>
     *
     * @param entityLogic    a cui rinviare l'evento/azione da eseguire
     * @param listaABottoni una serie di bottoni standard
     */
    public WrapButtons(final AILogic entityLogic, final List<AIButton> listaABottoni) {
        this(entityLogic, listaABottoni, (WrapSearch) null);
    }

    /**
     * Costruttore con una serie di bottoni standard, sotto forma di List<AEButton> <br>
     * Costruito con appContext.getBean(WrapButtons.class, entityLogic, listaAEBottoni, wrapSearch) <br>
     *
     * @param entityLogic               a cui rinviare l'evento/azione da eseguire
     * @param listaABottoni            una serie di bottoni standard
     * @param wrapSearch                wrapper di dati per la ricerca
     */
    public WrapButtons(final AILogic entityLogic, final List<AIButton> listaABottoni, final WrapSearch wrapSearch) {
        this(entityLogic, listaABottoni, wrapSearch, (Map<String, ComboBox>) null, (List<Button>) null, NUMBER_BUTTONS_STANDARD);
    }

    /**
     * Costruttore con una serie di bottoni standard, sotto forma di List<AEButton> <br>
     * Costruito con appContext.getBean(WrapButtons.class, entityLogic, listaAEBottoni, wrapSearch, numBottoni) <br>
     *
     * @param entityLogic               a cui rinviare l'evento/azione da eseguire
     * @param listaABottoni            una serie di bottoni standard
     * @param wrapSearch                wrapper di dati per la ricerca
     * @param mappaComboBox             di selezione e filtro
     */
    public WrapButtons(final AILogic entityLogic, final List<AIButton> listaABottoni, final WrapSearch wrapSearch, final Map<String, ComboBox> mappaComboBox) {
        this(entityLogic, listaABottoni, wrapSearch, mappaComboBox, (List<Button>) null, NUMBER_BUTTONS_STANDARD);
    }

    /**
     * Costruttore con una serie di bottoni standard, sotto forma di List<AEButton> <br>
     * Costruito con appContext.getBean(WrapButtons.class, entityLogic, listaAEBottoni, wrapSearch, numBottoni) <br>
     *
     * @param entityLogic               a cui rinviare l'evento/azione da eseguire
     * @param listaABottoni            una serie di bottoni standard
     * @param wrapSearch                wrapper di dati per la ricerca
     * @param listaBottoniSpecifici              non-standard
     */
    public WrapButtons(final AILogic entityLogic, final List<AIButton> listaABottoni, final WrapSearch wrapSearch, final List<Button> listaBottoniSpecifici) {
        this(entityLogic, listaABottoni, wrapSearch, (Map<String, ComboBox>) null, listaBottoniSpecifici, NUMBER_BUTTONS_STANDARD);
    }

    /**
     * Costruttore con una serie di bottoni standard, sotto forma di List<AEButton> <br>
     * Costruito con appContext.getBean(WrapButtons.class, entityLogic, listaAEBottoni, wrapSearch, numBottoni) <br>
     *
     * @param entityLogic               a cui rinviare l'evento/azione da eseguire
     * @param listaABottoni            una serie di bottoni standard
     * @param wrapSearch                wrapper di dati per la ricerca
     * @param mappaComboBox             di selezione e filtro
     * @param listaBottoniSpecifici              non-standard
     * @param maxNumeroBottoniPrimaRiga nella prima riga sopra la Grid
     */
    public WrapButtons(final AILogic entityLogic, final List<AIButton> listaABottoni, final WrapSearch wrapSearch, final Map<String, ComboBox> mappaComboBox, final List<Button> listaBottoniSpecifici, final int maxNumeroBottoniPrimaRiga) {
        this.entityLogic = entityLogic;
        this.listaABottoni = listaABottoni;
        this.wrapSearch = wrapSearch;
        this.mappaComboBox = mappaComboBox;
        this.listaBottoniSpecifici = listaBottoniSpecifici;
        this.maxNumeroBottoniPrimaRiga = maxNumeroBottoniPrimaRiga;
    }


    public AILogic getEntityLogic() {
        return entityLogic;
    }

    public List<AIButton> getListaABottoni() {
        return listaABottoni;
    }

    public Map<String, ComboBox> getMappaComboBox() {
        return mappaComboBox;
    }

    public List<Button> getListaBottoniSpecifici() {
        return listaBottoniSpecifici;
    }

    public WrapSearch getWrapSearch() {
        return wrapSearch;
    }

    public int getMaxNumeroBottoniPrimaRiga() {
        return maxNumeroBottoniPrimaRiga;
    }

}
