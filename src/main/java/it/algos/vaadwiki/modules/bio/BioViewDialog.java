package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadwiki.service.DeleteService;
import it.algos.vaadwiki.service.ElaboraService;
import it.algos.vaadwiki.service.PageService;
import it.algos.vaadwiki.service.UploadService;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwiki.application.VaadwikiCost.*;


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
public class BioViewDialog extends AViewDialog<Bio> {

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
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere dall'interfaccia la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     */
    @Autowired
    public BioViewDialog(@Qualifier(TAG_BIO) IAPresenter presenter) {
        super(presenter);
    }// end of constructor

    @PostConstruct
    protected void addBottoniSpecifici() {
        downloadButton.addClickListener(event -> downloadOnly());
        elaboraOnlyButton.addClickListener(event -> elaboraOnly());
        wikiShowButton.addClickListener(event -> showWikiPage());
        wikiEditButton.addClickListener(event -> editWikiPage());
        uploadButton.addClickListener(event -> upload());

        buttonBar.add(downloadButton);
        buttonBar.add(elaboraOnlyButton);
        buttonBar.add(wikiShowButton);
        buttonBar.add(wikiEditButton);
        buttonBar.add(uploadButton);
    }// end of method


    protected void downloadOnly() {
        long pageId = this.getPageId();
        String wikiTitle = this.getWikiTitle();
        Bio bio;

        if (pageId > 0) {
            bio = api.leggeBio(pageId);
            binder.setBean(bio);
        } else {
            if (text.isValid(wikiTitle)) {
                bio = api.leggeBio(wikiTitle);
                binder.setBean(bio);
            }// end of if cycle
        }// end of if/else cycle

    }// end of method

    /**
     * Non registra <br>
     * Non chiude il Form <br>
     */
    protected Bio elaboraOnly() {
        Bio bio = elaboraService.esegueNoSave(currentItem);
        binder.setBean(bio);
        return bio;
    }// end of method


    protected void showWikiPage() {
        String wikiTitle = this.getWikiTitle();
        String link = "\"" + PATH_WIKI + wikiTitle + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method

    protected void editWikiPage() {
        String wikiTitle = this.getWikiTitle();
        String link = "\"" + PATH_WIKI_EDIT_ANTE + wikiTitle + PATH_WIKI_EDIT_POST + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void upload() {
        long pageid = this.getPageId();

        if (pageid > 0) {
            bioService.save(currentItem);
            uploadService.esegue(pageid);
        }// end of if cycle

        super.close();
    }// end of method


    protected long getPageId() {
        long pageid = 0;

        if (currentItem != null) {
            pageid = currentItem.getPageId();
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