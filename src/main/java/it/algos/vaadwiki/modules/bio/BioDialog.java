package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadwiki.download.DeleteService;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.upload.UploadService;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwiki.application.WikiCost.*;


/**
 * Project vaadbio2 <br>
 * Created by Algos
 * User: Gac
 * Date: 11-ago-2018 17.19.29
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope (obbligatorio = 'prototype') <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_BIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class BioDialog extends AViewDialog<Bio> {

    private final Button downloadButton = new Button("DownloadOnly");

    private final Button elaboraOnlyButton = new Button("ElaboraOnly");

    private final Button wikiShowButton = new Button("WikiShow");

    private final Button wikiEditButton = new Button("WikiEdit");

    private final Button uploadButton = new Button("Upload");

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PageService pageService;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected DeleteService deleteService;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected UploadService uploadService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;

    @Autowired
    private LibBio libBio;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ElaboraService elaboraService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected Api api;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected BioService bioService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ATextService text;


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public BioDialog() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(BioDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public BioDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.usaSaveButton = false;
    }// end of method


    @PostConstruct
    protected void addBottoniSpecifici() {
        saveButton.getElement().setAttribute("theme", "secondary");

        downloadButton.setIcon(new Icon(VaadinIcon.ARROW_DOWN));
        downloadButton.getElement().setAttribute("theme", "primary");
        downloadButton.addClickListener(event -> downloadOnly());
        bottomLayout.add(downloadButton);

        elaboraOnlyButton.setIcon(new Icon(VaadinIcon.ARROW_RIGHT));
        elaboraOnlyButton.addClickListener(event -> elaboraOnly());
        bottomLayout.add(elaboraOnlyButton);

        saveButton.setIcon(new Icon(VaadinIcon.DATABASE));
        saveButton.getElement().setAttribute("theme", "error");
        bottomLayout.add(saveButton);

        wikiShowButton.setIcon(new Icon(VaadinIcon.SEARCH));
        wikiShowButton.addClickListener(event -> libBio.showWikiPage(getWikiTitle()));
        bottomLayout.add(wikiShowButton);

        wikiEditButton.setIcon(new Icon(VaadinIcon.SEARCH));
        wikiEditButton.addClickListener(event -> libBio.editWikiPage(getWikiTitle()));
        bottomLayout.add(wikiEditButton);

        uploadButton.setIcon(new Icon(VaadinIcon.ARROW_UP));
        uploadButton.getElement().setAttribute("theme", "error");
        uploadButton.addClickListener(event -> upload());
        bottomLayout.add(uploadButton);
    }// end of method


    protected void downloadOnly() {
        long pageId = this.getPageId();
        String wikiTitle = this.getWikiTitle();
        Bio bio = null;
        AbstractField field;
        Object obj = null;

        if (pageId > 0) {
            bio = api.leggeBio(pageId);
        }// end of if cycle

        if (bio == null) {
            if (text.isValid(wikiTitle)) {
                bio = api.leggeBio(wikiTitle);
            }// end of if cycle
        }// end of if cycle

        if (bio == null) {
            field = this.getField("wikiTitle");
            if (field != null) {
                obj = field.getValue();
            }// end of if cycle
            if (obj != null && obj instanceof String) {
                wikiTitle = (String) obj;
                bio = api.leggeBio(wikiTitle);
            }// end of if cycle
        }// end of if cycle

        if (bio != null) {
            binder.setBean(bio);
            currentItem.lastLettura = bio.lastLettura;
            currentItem.lastModifica = bio.lastModifica;
        } else {
            Notification.show("Non ho trovato la pagina " + (text.isValid(wikiTitle) ? wikiTitle : ""), 2000, Notification.Position.MIDDLE);
        }// end of if/else cycle
    }// end of method


    /**
     * Non registra <br>
     * Non chiude il Form <br>
     */
    protected Bio elaboraOnly() {
        Bio bio = null;

        if (binder != null) {
            bio = binder.getBean();
        }// end of if cycle

        if (bio == null && currentItem != null) {
            bio = currentItem;
        }// end of if cycle

        if (bio != null && text.isValid(bio.tmplBioServer)) {
            bio = elaboraService.esegueNoSave(bio);
            binder.setBean(bio);
        }// end of if cycle

        return bio;
    }// end of method


//    protected void showWikiPage() {
//        String wikiTitle = this.getWikiTitle();
//        String link = "\"" + PATH_WIKI + wikiTitle + "\"";
//        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
//    }// end of method
//
//
//    protected void editWikiPage() {
//        String wikiTitle = this.getWikiTitle();
//        String link = "\"" + PATH_WIKI_EDIT_ANTE + wikiTitle + PATH_WIKI_EDIT_POST + "&section=0\"";
//        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
//    }// end of method


    protected void upload() {
        String wikiTitle = this.getWikiTitle();

        if (text.isValid(wikiTitle)) {
            saveClicked(EAOperation.edit);
            uploadService.uploadBio(wikiTitle);
        }// end of if cycle

        super.close();
    }// end of method


    protected long getPageId() {
        long pageid = 0;

        if (currentItem != null) {
            pageid = currentItem.getPageid();
        }// end of if cycle

        return pageid;
    }// end of method


    protected String getWikiTitle() {
        String title = "";

        if (currentItem != null) {
            title = currentItem.getWikiTitle();
        }// end of if cycle

        return title;
    }// end of method

}// end of class