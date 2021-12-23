package it.algos.vaadflow14.ui.button;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.interfaces.*;
import org.springframework.beans.factory.annotation.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 20-mag-2020
 * Time: 20:42
 * Superclasse astratta per la gestione delle barre di comandi (sopra e sotto) <br>
 * Contenuto variabile in base ad una mappa di componenti inviata nel costruttore dalla Logic <br>
 * <p>
 * Se il costruttore arriva SENZA parametri, mostra solo quanto previsto nelle preferenze <br>
 */
public abstract class AButtonLayout extends VerticalLayout {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ArrayService array;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ALogService logger;

    //    /**
    //     * Flag di preferenza per selezionare la ricerca testuale:
    //     * 1) nessuna
    //     * 2) campo editText di selezione per una property specificata in searchProperty
    //     * 3) dialogo di selezione
    //     */
    //    protected AESearch searchType;

    /**
     * Flag di preferenza per specificare la property della entity su cui effettuare la ricerca <br>
     * Ha senso solo se searchType=EASearch.editField
     */
    protected String searchProperty;

    /**
     * Campo testo editabile per la ricerca <br>
     * Ha senso solo se searchType=EASearch.editField
     */
    protected TextField searchField;

    /**
     * Bottone standard per la pulizia del searchField (opzionale, un flag controlla se mostrarlo o meno) <br>
     */
    protected Button buttonClearFilter;


    /**
     * Property per la lista di bottoni finali (facoltativa)
     */
    protected List<Button> specifici;

    //    /**
    //     * Mappa dei bottoni standard (obbligatoria) <br>
    //     * La chiave è l'enumeration del bottone che contiene riferimenti per gli eventi <br>
    //     */
    //    protected Map<AIButton, Button> mappaBottoni;

    /**
     * Property per selezionare i bottoni standard in base al tipo di Form (usata solo in ABottomLayout)
     */
    protected AEOperation operationForm;


    /**
     * A - (obbligatorio) la AILogic con cui regolare i listener per l'evento/azione da eseguire
     */
    protected AILogic entityLogic;

    //    /**
    //     * B - (semi-obbligatorio) una serie di bottoni standard, sotto forma di List<AEButton>
    //     */
    //    protected List<AIButton> listaAEBottoni;
    //
    //    /**
    //     * C - (facoltativo) wrapper di dati per il dialogo/campo di ricerca
    //     */
    //    protected WrapSearch wrapSearch;
    //
    //    /**
    //     * D - (facoltativo) una mappa di combobox di selezione e filtro, LinkedHashMap<String, ComboBox>
    //     * La chiave generalmente è il nome della property della entity con cui costruire il comboBox <br>
    //     */
    //    protected Map<String, ComboBox> mappaComboBox;
    //
    //    /**
    //     * E - (facoltativo) una mappa di checkbox di selezione e filtro, Map<String, Checkbox>
    //     */
    //    protected Map<String, Checkbox> mappaCheckBox;

    /**
     * F - (facoltativo) una serie di bottoni non-standard, sotto forma di List<Button>
     */
    protected List<Button> listaBottoniSpecifici;

    /**
     * Wrap di informazioni usato da ALogic per la creazione di ATopLayout e ABottomLayout <br>
     * La ALogic mantiene lo stato ed elabora informazioni che verranno usate da ATopLayout e ABottomLayout <br>
     */
    protected WrapComponenti wrapper;


    /**
     * Layout grafico per la prima fila di bottoni sopra la Grid o il Form
     */
    protected HorizontalLayout primaRiga;

    /**
     * Layout grafico per la seconda (opzionale) fila di bottoni sopra la Grid
     */
    protected HorizontalLayout secondaRiga;

    /**
     * Layout grafico per l'unica fila (opzionale) di bottoni sotto la Grid o il Form
     */
    protected HorizontalLayout rigaUnica;

    /**
     * Mappa in entrata di componenti di selezione e filtro <br>
     */
    protected Map<String, Object> mappaComponenti;

    /**
     * Mappa corrente di componenti di selezione e filtro <br>
     * Serve per recuperare un componente e regolarlo durante la vita della List o Form <br>
     */
    protected Map<String, Object> mappaCorrente;

    /**
     * Costruttore base senza parametri <br>
     * Non utilizzato. Serve per automatismi di SpringBoot <br>
     */
    public AButtonLayout() {
    }


    /**
     * Costruttore base con parametro wrapper di passaggio dati <br>
     * La classe viene costruita con appContext.getBean(ATopLayout.class, wrapper) in LogicList <br>
     *
     * @param wrapper di informazioni tra 'logic' e 'view'
     */
    public AButtonLayout(WrapComponenti wrapper) {
        this.wrapper = wrapper;
        this.entityLogic = wrapper != null ? wrapper.getEntityLogic() : null;
        this.mappaComponenti = wrapper != null ? wrapper.getMappaComponenti() : null;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le (eventuali) istanze @Autowired <br>
     * Questo metodo viene chiamato subito dopo che il framework ha terminato l' init() implicito <br>
     * del costruttore e PRIMA di qualsiasi altro metodo <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        this.fixPreferenze();
        this.checkParametri();
        this.initView();
        this.creaAll();
        this.creaAllBottoni();
        this.addAllToView();
    }


    /**
     * Preferenze usate da questa 'view' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        mappaCorrente = new HashMap<>();
    }

    protected void checkParametri() {
        String message;

        if (AEPreferenza.usaDebug.is()) {
            if (wrapper == null) {
                logger.warn("Creazione di un xxxLayout senza WrapTop", this.getClass(), "checkParametri");
                return;
            }

            if (entityLogic == null) {
                logger.error("Manca la entityLogic nel WrapTop", this.getClass(), "checkParametri");
                return;
            }

            //            if (listaAEBottoni == null) {
            //                message = String.format("Non ci sono bottoni nella view (List) chiamata da %s", entityLogic.getClass().getSimpleName());
            //                logger.info(message, this.getClass(), "checkParametri");
            //            }
        }
    }


    /**
     * Qui va tutta la logica iniziale della view <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected void initView() {
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);
    }

    protected void creaAll() {
    }

    protected void creaAllBottoni() {
    }

    protected void addAllToView() {
    }

    @Deprecated
    protected Button getButton(final AIButton aeButton) {
        Button button = FactoryButton.get(aeButton);
        button.addClickListener(event -> performAction(aeButton.getAction()));

        return button;
    }

    /**
     * Aggiunta di tutti i listener <br>
     * Chiamato da AEntityService <br>
     * Recupera le istanze concrete dei bottoni dalla mappa <AEButton, Button> <br>
     * Aggiunge il listener al bottone, specificando l'azione di ritorno associata al singolo bottone <br>
     *
     * @param entityLogic a cui rinviare l'evento/azione da eseguire
     */
    public void setAllListener(AILogic entityLogic) {
    }


    /**
     * Esegue l'azione del bottone. <br>
     * <p>
     * Passa a AEntityService.performAction(azione) <br>
     *
     * @param azione selezionata da eseguire
     */
    public void performAction(AIAction azione) {
        if (!entityLogic.performAction(azione)) {
            logger.warn("Switch - caso non definito", this.getClass(), "performAction(azione)");
        }
    }


    /**
     * Esegue l'azione del bottone. <br>
     * <p>
     * Passa a AEntityService.performAction(azione) <br>
     *
     * @param azione           selezionata da eseguire
     * @param searchFieldValue valore corrente del campo editText (solo per List)
     */
    public void performAction(AIAction azione, String searchFieldValue) {
        entityLogic.performAction(azione, searchFieldValue);
    }


    /**
     * Esegue l'azione del bottone. <br>
     * <p>
     * Passa a AEntityService.performAction(azione) <br>
     *
     * @param azione     selezionata da eseguire
     * @param entityBean selezionata (solo per Form)
     */
    public void performAction(AIAction azione, AEntity entityBean) {
    }


    /**
     * Esegue l'azione del bottone. Azione che necessita di un field e di un valore. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param iAzione    interfaccia dell'azione selezionata da eseguire
     * @param fieldName  nome del field
     * @param fieldValue valore corrente del field
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    public void performAction(final AIAction iAzione, final String fieldName, final Object fieldValue) {
        entityLogic.performAction(iAzione, fieldName, fieldValue);
    }


    public TextField getSearchField() {
        return searchField;
    }

    /**
     * Recupera il bottone <br>
     *
     * @param key della mappa interna
     *
     * @return bottone richiesto
     */
    public Button getBottone(String key) {
        Object obj;
        Button button = null;

        if (array.isAllValid(mappaCorrente) && mappaCorrente.get(key) != null) {
            obj = mappaCorrente.get(key);
            if (obj != null && obj instanceof Button) {
                button = (Button) obj;
            }
        }

        return button;
    }

}
