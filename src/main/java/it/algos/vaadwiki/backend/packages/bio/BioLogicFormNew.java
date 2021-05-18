package it.algos.vaadwiki.backend.packages.bio;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.data.value.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.ui.form.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * First time: gio, 06-mag-2021
 * Last doc revision: mar, 18-mag-2021 alle 18:37 <br>
 * <p>
 */
@Route(value = "bioFormNew", layout = MainLayout.class)
@AIScript(sovraScrivibile = false, type = AETypeFile.form, doc = AEWizDoc.revisione)
public class BioLogicFormNew extends WikiLogicForm {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    protected AForm secondForm;

    /**
     * Costruttore con parametro <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Il framework SpringBoot/Vaadin con l'Annotation @Autowired inietta automaticamente un riferimento al singleton xxxService <br>
     * L'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Regola (nella superclasse) la entityClazz (final) associata a questa logicView <br>
     *
     * @param bioService (@Autowired) (@Qualifier) riferimento al service specifico correlato a questa istanza (prototype) di LogicForm
     */
    public BioLogicFormNew(@Autowired @Qualifier("bioService") final AIService bioService) {
        super(bioService, Bio.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
    }

    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Costruisce i bottoni come dai Flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected List<AIButton> getListaAEBottoniTop() {
        return Collections.singletonList(AEButton.download);
    }


    /**
     * Costruisce il corpo principale (obbligatorio) della Grid <br>
     */
    @Override
    protected void fixBodyLayout() {
        WrapForm wrapSimple = new WrapForm(entityBean, operationForm,Arrays.asList("wikiTitle", "tmpBioServer"));
        currentForm = appContext.getBean(AGenericForm.class, entityService, this, wrapSimple);

        WrapForm wrapSecond = new WrapForm(entityBean, operationForm, Collections.singletonList("tmpBioServer"));
        secondForm = appContext.getBean(AGenericForm.class, entityService, this, wrapSecond);

        Tab tab1 = new Tab("Simple");
        Div page1 = new Div();
        page1.add(currentForm);
        page1.setVisible(false);

        Tab tab2 = new Tab("Page");
        Div page2 = new Div();
        page2.add(secondForm);
        page2.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);
        Tabs tabs = new Tabs(tab1, tab2);
        tabs.setFlexGrowForEnclosedTabs(1);
        Div pages = new Div(page1, page2);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        bodyPlaceHolder.add(tabs, pages);
    }

    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
//    @Override
    public List<String> getFormPropertyNamesList2() {
        List<String> fieldsNameList = new ArrayList<>();

        fieldsNameList.add("wikiTitle");
        fieldsNameList.add("tmpBioServer");

        return fieldsNameList;
    }

    /**
     * Regolazioni finali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void regolazioniFinali() {
        super.regolazioniFinali();

        //--aggiunge un listener al field wikiTitle
        AField fieldWikiTitle = currentForm.getField("wikiTitle");
        if (fieldWikiTitle != null) {
            fieldWikiTitle.addValueChangeListener(e -> sincroDownload((String) e.getValue()));
            com.vaadin.flow.component.Component comp = fieldWikiTitle.get();
            ((ATextField) comp).textField.setValueChangeMode(ValueChangeMode.EAGER);
        }

        //--disabilita inizialmente il bottone download
        sincroDownload(VUOTA);
    }

    /**
     * Sincronizza lo stato del bottone download col contenuto del field wikiTitle <br>
     */
    private void sincroDownload(final String value) {
        Button bottoneDownload = topLayout.getMappaBottoni().get(AEButton.download);
        if (bottoneDownload != null) {
            bottoneDownload.setEnabled(text.isValid(value) && value.length() > 2);
        }
    }

    /**
     * Titolo della pagina wiki <br>
     */
    @Deprecated
    private String getWikiTitle() {
        String valueWikiTitle = VUOTA;
        AField fieldWikiTitle = currentForm.getField("wikiTitle");
        valueWikiTitle = fieldWikiTitle != null ? (String) fieldWikiTitle.getValue() : VUOTA;

        return valueWikiTitle;
    }


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        downloadBio();
        return true;
    }

    /**
     * Scarica una singola biografia <br>
     */
    private void downloadBio() {
        String textTmpl;
        AField fieldTmpBioServer = currentForm.getField("tmpBioServer");

        String wikiTitle = getWikiTitle();
        if (text.isValid(getWikiTitle())) {
            textTmpl = wikiBot.leggeTmpl(wikiTitle);
            if (text.isValid(textTmpl)) {
                fieldTmpBioServer.setValue(textTmpl);
            }
        }
    }

}// end of Route class



