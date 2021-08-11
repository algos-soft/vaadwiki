package it.algos.vaadwiki.backend.packages.wiki;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.renderer.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.ui.enumeration.*;
import org.springframework.beans.factory.annotation.*;

import java.time.*;
import java.util.*;

/**
 * Project wikibio
 * Created by Algos
 * User: gac
 * First time: mar, 30-mar-2021
 * Last doc revision: mar, 18-mag-2021 alle 18:37 <br>
 */
public abstract class WikiLogicList extends LogicList {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected WebService web;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Viene iniettata nel costruttore della sottoclasse <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    protected WikiService wikiService;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ElaboraService elaboraService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioService bioService;


    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneUpdate;


    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneModulo;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneElabora;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneCheck;


    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneTest;


    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneStatistiche;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneStatisticheDue;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneUploadAll;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneUploadStatistiche;


    /**
     * Flag di preferenza per specificare il titolo del modulo wiki da mostrare in lettura <br>
     */
    protected String wikiModuloTitle;


    /**
     * Flag di preferenza per specificare il titolo della pagina wiki da mostrare in lettura <br>
     */
    protected String wikiStatisticheTitle;


    /**
     * Flag di preferenza per specificare il titolo della pagina wiki da mostrare in lettura <br>
     */
    protected String wikiStatisticheDueTitle;

    protected String parametri;

    protected String alfabetico;

    protected String singolare;

    protected String plurale;

    protected String minuscolo;

    protected String uno;

    protected String due;

    protected String anche;

    protected boolean usaColonnaWiki;

    protected boolean usaColonnaTest;

    protected boolean usaColonnaUpload;

    /**
     * Costruttore con parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nel costruttore della sottoclasse l'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Nel costruttore della sottoclasse usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Riceve e regola la entityClazz (final) associata a questa logicView <br>
     *
     * @param entityService (obbligatorio) riferimento al service specifico correlato a questa istanza (prototype) di LogicList
     * @param entityClazz   (obbligatorio)  the class of type AEntity
     */
    public WikiLogicList(final AIService entityService, final Class<? extends AEntity> entityClazz) {
        super(entityService, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottonePaginaWiki = false;
        super.usaBottoneDownload = true;
        super.usaBottoneUpload = false;
        this.usaBottoneModulo = true;
        this.usaBottoneStatistiche = true;
        this.usaBottoneUploadAll = true;
        this.usaBottoneUploadStatistiche = true;
        super.maxNumeroBottoniPrimaRiga = 3;

        this.usaColonnaWiki = false;
        this.usaColonnaTest = false;
        this.usaColonnaUpload = false;
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        super.fixAlertList();

        parametri = html.bold("Attività/Attività2/Attività3");
        alfabetico = html.bold("alfabetico");
        singolare = html.bold("singolare");
        plurale = html.bold("plurale");
        minuscolo = html.bold("minuscolo");
        uno = html.bold("Forma1");
        due = html.bold("Forma2");
        anche = html.bold("anche");
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

        if (usaBottoneUpdate) {
            putMappa(AEWikiButton.update);
        }
        if (usaBottoneModulo) {
            putMappa(AEWikiButton.modulo);
        }
        if (usaBottoneElabora) {
            putMappa(AEWikiButton.elabora);
        }
        if (usaBottoneCheck) {
            putMappa(AEWikiButton.check);
        }
        if (usaBottoneTest) {
            putMappa(AEWikiButton.test);
        }
        if (usaBottoneStatistiche) {
            putMappa(AEWikiButton.statistiche);
        }
        if (usaBottoneStatisticheDue) {
            putMappa(AEWikiButton.statisticheDue);
        }
        if (usaBottoneUploadAll) {
            putMappa(AEWikiButton.uploadAll);
        }
        if (usaBottoneUploadStatistiche) {
            putMappa(AEWikiButton.uploadStatistiche);
        }

    }

    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Costruisce i bottoni come dai flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected List<AIButton> getListaAEBottoniTop() {
        List<AIButton> listaBottoni = super.getListaAEBottoniTop();

        if (usaBottoneUpdate) {
            listaBottoni.add(AEWikiButton.update);
        }
        if (usaBottoneModulo) {
            listaBottoni.add(AEWikiButton.modulo);
        }
        if (usaBottoneElabora) {
            listaBottoni.add(AEWikiButton.elabora);
        }
        if (usaBottoneCheck) {
            listaBottoni.add(AEWikiButton.check);
        }
        if (usaBottoneTest) {
            listaBottoni.add(AEWikiButton.test);
        }
        if (usaBottoneStatistiche) {
            listaBottoni.add(AEWikiButton.statistiche);
        }
        if (usaBottoneStatisticheDue) {
            listaBottoni.add(AEWikiButton.statisticheDue);
        }

        return listaBottoni;
    }

    /**
     * Regolazioni finali della Grid <br>
     * <p>
     * Eventuali colonna 'ad-hoc' <br>
     * Eventuali 'listener' specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixGrid() {
        Grid realGrid;
        ComponentRenderer renderer;
        Grid.Column colonna;
        String lar = "5em";

        if (bodyPlaceHolder != null && grid != null) {
            realGrid = grid.getGrid();

            if (usaColonnaWiki) {
                renderer = new ComponentRenderer<>(this::createWikiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Wiki");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaTest) {
                renderer = new ComponentRenderer<>(this::createTestButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Test");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaUpload) {
                renderer = new ComponentRenderer<>(this::createUploadButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Upload");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
        }
    }

    protected Button createWikiButton(AEntity entityBean) {
        Button wikiButton = new Button(new Icon(VaadinIcon.LIST));
        wikiButton.getElement().setAttribute("theme", "secondary");
        wikiButton.addClickListener(e -> wikiPage(entityBean));

        return wikiButton;
    }

    protected Button createTestButton(AEntity entityBean) {
        Button viewButton = new Button(new Icon(VaadinIcon.SERVER));
        viewButton.getElement().setAttribute("theme", "secondary");
        //        viewButton.addClickListener(e -> testAttivita(entityBean));

        return viewButton;
    }

    protected Button createUploadButton(AEntity entityBean) {
        Button uploadButton = new Button(new Icon(VaadinIcon.UPLOAD));
        uploadButton.getElement().setAttribute("theme", "error");
        //        uploadButton.addClickListener(e -> uploadService.uploadAttivita(entityBean));

        return uploadButton;
    }


    protected void wikiPage(AEntity entityBean) {
    }

    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Interfaccia utilizzata come parametro per poter sovrascrivere il metodo <br>
     * Nella classe base eseguirà un casting a AEAction <br>
     * Nella (eventuale) sottoclasse specifica del progetto eseguirà un casting a AExxxAction <br>
     *
     * @param iAzione interfaccia dell'azione selezionata da eseguire
     *
     * @return false se il parametro non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(AIAction iAzione) {
        boolean status = super.performAction(iAzione);
        AEWikiAction azione = iAzione instanceof AEWikiAction ? (AEWikiAction) iAzione : null;

        if (azione == null) {
            return status;
        }

        status = true;
        switch (azione) {
            case elabora:
                elabora();
                break;
            case modulo:
                openWikiPage(wikiModuloTitle);
                break;
            case statistiche:
                openWikiPage(wikiStatisticheTitle);
                break;
            case statisticheDue:
                openWikiPage(wikiStatisticheDueTitle);
                break;
            default:
                status = false;
                break;
        }

        return status;
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean elabora() {
         elaboraService.esegue(bioService.fetch());
        super.reload();

        return true;
    }

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    @Override
    public boolean download() {
        entityService.download();
        super.reload();

        return true;
    }

    /**
     * Aggiunge informazioni sull'ultimo download effettuato <br>
     * Se non è mai stato fatto (lastDownload=ROOT_DATE_TIME), lo evidenzia <br>
     * Altrimenti formatta la data dell'ultimo download <br>
     *
     * @return un 'span' di colore rosso
     */
    protected void fixInfoDownload(AEWikiPreferenza aePreferenza) {
        String message;
        LocalDateTime last = aePreferenza.getDate();

        if (last.equals(ROOT_DATA_TIME)) {
            message = "Download non ancora effettuato";
        }
        else {
            message = "Ultimo download:" + SPAZIO + date.getDataOrarioCompleta(last);
        }
        addSpanRossoFix(message);
    }

    protected void addWikiLink(String wikiTitle) {
        this.addWikiLink(wikiTitle, wikiTitle);
    }

    protected void addWikiLink(String wikiTitle, String viewTitle) {
        super.addLink(FlowCost.PATH_WIKI + wikiTitle, html.bold(viewTitle));
    }


}
