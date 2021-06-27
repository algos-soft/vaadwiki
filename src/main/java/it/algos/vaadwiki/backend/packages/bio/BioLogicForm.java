package it.algos.vaadwiki.backend.packages.bio;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.formlayout.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.wiki.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.ui.enumeration.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: lun, 26-apr-2021 <br>
 * Last doc revision: mar, 18-mag-2021 alle 19:35 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, usa la classe GenericLogicForm con @Route <br>
 * Gestione della 'view' di @Route e della 'business logic' <br>
 * Mantiene lo 'stato' <br>
 * L' istanza (PROTOTYPE) viene creata ad ogni chiamata del browser <br>
 * Eventuali parametri (opzionali) devono essere passati nell'URL <br>
 * <p>
 * Annotated with @Route (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */

//Vaadin flow
@Route(value = "bioForm", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.form, doc = AEWizDoc.inizioRevisione)
public class BioLogicForm extends WikiLogicForm {

    private BioService bioService;

    private BioUtility bioUtility;

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
    public BioLogicForm(@Autowired @Qualifier("bioService") final AIService bioService, final BioUtility bioUtility) {
        super(bioService, Bio.class);
        this.bioService = (BioService) bioService;
        this.bioUtility = bioUtility;
    }// end of Vaadin/@Route constructor


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        super.beforeEnter(beforeEnterEvent);
    }


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottoneDownload = true;
        //        super.usaBottonePaginaWiki = true;
        super.wikiPageTitle = ((Bio) entityBean).wikiTitle;
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertForm() {
        super.fixAlertForm();
        String download = "download";
        String down = html.bold(text.primaMaiuscola(download));
        String esatto = html.bold("esatto");
        String massiccia = html.bold("massiccia");
        String singola = html.bold("singola");

        addSpanBlu(String.format("%s di una singola scheda indicando l'%s wikiTitle", down, esatto));
        addSpanVerde(String.format("Normalmente il %s avviene in automatico in maniera %s", download, massiccia));
        addSpanVerde(String.format("Qui si può cercare la %s voce eventualmente sfuggita al ciclo di %s", singola, download));
    }

    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Bottoni standard AIButton di VaadinFlow14 e della applicazione corrente <br>
     * Costruisce i bottoni come dai flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAEBottoniTop() {
        super.creaAEBottoniTop();

        Button button;

        button = AEButton.wiki.get("View");
        button.addClickListener(event -> performAction(AEWikiAction.wikiPaginaView));
        putMappa(button);

        button = AEButton.wiki.get("Edit");
        button.addClickListener(event -> performAction(AEWikiAction.wikiPaginaEdit));
        putMappa(button);

        button = AEButton.wiki.get("Crono");
        button.addClickListener(event -> performAction(AEWikiAction.wikiPaginaHistory));
        putMappa(button);

        //        putMappa(((Button) AEButton.wiki.get("Edit").addClickListener(event -> performAction(AEWikiAction.wikiPaginaEdit))));
        //        putMappa((Button) AEButton.wiki.get("Crono").addClickListener(event -> performAction(AEWikiAction.wikiPaginaCrono)));
    }


    /**
     * Costruisce il corpo principale (obbligatorio) della Grid <br>
     */
    @Override
    protected void fixBodyLayout() {
        super.fixBodyLayout();

        String minWidthForm = "40em";

        FormLayout layoutTmpl = new FormLayout();
        layoutTmpl.setResponsiveSteps(new FormLayout.ResponsiveStep(minWidthForm, 1));

        Tab tab1 = new Tab("src");
        Div page1 = new Div();
        page1.add(currentForm);
        page1.setVisible(true);

        Tab tab2 = new Tab("maps");
        Div page2 = new Div();
        page2.add(appContext.getBean(BioFormMaps.class, entityBean));
        page2.setVisible(false);

        Tab tab3 = new Tab("tmpl");
        Div page3 = new Div();
        page3.add(appContext.getBean(BioFormTmpl.class, entityBean));
        page3.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);
        tabsToPages.put(tab3, page3);
        Tabs tabs = new Tabs(tab1, tab2, tab3);
        tabs.setFlexGrowForEnclosedTabs(1);
        Div pages = new Div(page1, page2, page3);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(pagina -> pagina.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        bodyPlaceHolder.add(tabs, pages);
        page1.setVisible(true);
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
            fieldWikiTitle.addValueChangeListener(e -> sincroDownload());
        }

        //--disabilita inizialmente il bottone download
        sincroDownload();
    }

    /**
     * Sincronizza lo stato del bottone download col contenuto del field wikiTitle <br>
     */
    private void sincroDownload() {
        Button bottoneDownload = null;//@todo sti cazzi
        if (bottoneDownload != null) {
            bottoneDownload.setEnabled(text.isValid(getWikiTitle()));
        }
    }

    /**
     * Titolo della pagina wiki <br>
     */
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
        String wikiTitle;
        WrapPage wrap = null;

        wikiTitle = getWikiTitle();
        if (text.isValid(wikiTitle)) {
            wrap = wikiBot.leggePage(wikiTitle);

            if (wrap != null && wrap.isValida()) {
                entityBean = bioService.newEntity(wrap);
                currentForm.getBinder().setBean(entityBean);
                logger.info(AETypeLog.download, String.format("Download della pagina %s", wikiTitle));
            }
            else {
                if (wrap != null) {
                    logger.warn(AETypeLog.download, "Qualcosa non ha funzionato");
                }
                else {
                    logger.info(AETypeLog.download, String.format("Su wiki non esiste la pagina %s", wikiTitle));
                }
            }
        }
    }


    private void printMappa(Map mappa) {
        for (Object key : mappa.keySet()) {
            System.out.println(key + SEP + mappa.get(key));
        }
    }

}