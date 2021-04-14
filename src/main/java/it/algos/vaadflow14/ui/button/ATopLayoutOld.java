package it.algos.vaadflow14.ui.button;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.logic.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 09-mag-2020
 * Time: 11:27
 * Placeholder SOPRA la Grid <br>
 * Contenuto variabile in base ad una lista di componenti inviata con un wrapper nel costruttore. Nell'ordine: <br>
 * 1) Prima zona:
 * . con o senza un bottone per cancellare tutta la collezione (default senza) <br>
 * . con o senza un bottone di reset per ripristinare (se previsto in automatico) la collezione (default senza) <br>
 * 2) Seconda zona:
 * . con o senza gruppo di ricerca: <br>
 * --    campo EditSearch predisposto su un unica property, oppure (in alternativa) <br>
 * --    bottone per aprire un DialogSearch con diverse property selezionabili <br>
 * --    bottone per annullare la ricerca e riselezionare tutta la collezione <br>
 * 3) Terza zona
 * . con o senza bottone New, con testo regolato da preferenza o da parametro (default con) <br>
 * . altri eventuali bottoni <br>
 * 4) Quarta zona
 * . con o senza comboBox di selezione e filtro (numero variabile) <br>
 * 5) Quinta zona
 * . con eventuali altri bottoni specifici <br>
 * <p>
 * I bottoni vengono passati nel wrapper come enumeration standard. <br>
 * Se si usano bottoni non standard vengono inseriti al termine del gruppo centrale di bottoni <br>
 * I listeners dei bottoni non standard devono essere regolati singolarmente dalla Logic chiamante <br>
 * Se il costruttore arriva SENZA parametri, mostra solo quanto previsto nelle preferenze <br>
 * La classe viene costruita con appContext.getBean(ATopLayout.class, wrapButtons) in AEntityService <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ATopLayoutOld  {


    /**
     * Costruttore base senza parametri <br>
     * La classe viene costruita con appContext.getBean(AxxxLayout.class) in AEntityService <br>
     */
    @Deprecated
//    public ATopLayoutOld() {
//    }


//    /**
//     * Costruttore base con parametro wrapper di passaggio dati <br>
//     * La classe viene costruita con appContext.getBean(xxxLayout.class, wrapButtons) in AEntityService <br>
//     *
//     * @param wrapButtons wrap di informazioni
//     */
//    public ATopLayoutOld(WrapButtons wrapButtons) {
////        super(wrapButtons);@//@todo PROVVISORIO
//    }


    /**
     * Costruisce il gruppo di ricerca testuale <br>
     * 1) nessuna <br>
     * 2) campo editText di selezione per una property specificata in searchProperty <br>
     * 3) bottone per aprire un dialogo di selezione <br>
     * DEVE essere sovrascritto. <br>
     */
    protected void fixSearch() {
//        Button button;
//        switch (searchType) {
//            case nonUsata:
//                break;
//            case editField:
//                this.fixSearchField();
//                break;
//            case dialog:
//                button = FactoryButton.get(AEButton.searchDialog);
//                this.mappaBottoni.put(AEButton.searchDialog, button);
//                break;
//            default:
//                logger.warn("Switch - caso non definito", this.getClass(), "creaSearch");
//                break;
//        }
    }


    /**
     * Campo EditSearch predisposto su un'unica property. <br>
     */
    private void fixSearchField() {
//        HorizontalLayout layout;
//        String placeHolder = text.isValid(searchProperty) ? text.primaMaiuscola(searchProperty) + "..." : "Cerca...";
//        String toolTip = "Caratteri iniziali della ricerca" + (text.isValid(searchProperty) ? " nel campo '" + searchProperty + "'" : VUOTA);
//        searchField = new TextField(VUOTA, placeHolder);
//        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
//        searchField.getElement().setAttribute("title", toolTip);
//        searchField.addClassName("view-toolbar__search-field");
//        searchField.setValueChangeMode(ValueChangeMode.EAGER);
//
//        layout = new HorizontalLayout(searchField);
//        layout.setSpacing(false);
//
//        //--bottone piccolo per pulire il campo testo di ricerca
//        if (AEPreferenza.usaSearchClearButton.is()) {
//            //        buttonClearFilter = new Button(new Icon("lumo", "cross"));
//            buttonClearFilter = new Button(VaadinIcon.CLOSE_SMALL.create());
//            buttonClearFilter.setEnabled(false);
//            buttonClearFilter.getElement().setAttribute("title", "Pulisce il campo di ricerca");
//            layout.add(buttonClearFilter);
//        }
//
//        this.add(layout);
    }


    /**
     * Aggiunta di tutti i listener <br>
     * Chiamato da AEntityService <br>
     * Recupera le istanze concrete dei bottoni dalla mappa <AEButton, Button> <br>
     * Aggiunge il listener al bottone, specificando l'azione di ritorno associata al singolo bottone <br>
     *
     * @param service a cui rinviare l'evento/azione da eseguire
     */
    public void setAllListener(AILogic service) {
//        super.setAllListener(service);
//
//        if (searchField != null) {
//            searchField.addValueChangeListener(event -> {
//                if (buttonClearFilter != null) {
//                    if (searchField.getValue().isEmpty()) {
//                        buttonClearFilter.setEnabled(false);
//                    } else {
//                        buttonClearFilter.setEnabled(true);
//                    }
//                }
//                performAction(AEAction.searchField, searchField.getValue());
//            });
//        }
//
//        if (buttonClearFilter != null) {
//            buttonClearFilter.addClickListener(event -> searchField.clear());
//        }
//
//        if (array.isAllValid(mappaComboBox)) {
//            for (Map.Entry<String, ComboBox> mappaEntry : mappaComboBox.entrySet()) {
//                mappaEntry.getValue().addValueChangeListener(event -> performAction(AEAction.valueChanged));
//            }
//        }
    }

}
