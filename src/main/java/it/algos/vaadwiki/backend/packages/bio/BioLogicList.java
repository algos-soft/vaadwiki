package it.algos.vaadwiki.backend.packages.bio;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.wiki.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.service.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: lun, 26-apr-2021 <br>
 * Last doc revision: mar, 18-mag-2021 alle 19:35 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, usa la classe GenericLogicList con @Route <br>
 * Gestione della 'view' di @Route e della 'business logic' <br>
 * Mantiene lo 'stato' <br>
 * L' istanza (PROTOTYPE) viene creata ad ogni chiamata del browser <br>
 * Eventuali parametri (opzionali) devono essere passati nell'URL <br>
 * <p>
 * Annotated with @Route (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
//Vaadin flow
@Route(value = "bio", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.list, doc = AEWizDoc.inizioRevisione)
public class BioLogicList extends LogicList {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWikiBotService wikiBot;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioService bioService;


    /**
     * Costruttore con parametro <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Il framework SpringBoot/Vaadin con l'Annotation @Autowired inietta automaticamente un riferimento al singleton xxxService <br>
     * L'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Regola (nella superclasse) la entityClazz (final) associata a questa logicView <br>
     *
     * @param bioService (@Autowired) (@Qualifier) riferimento al service specifico correlato a questa istanza (prototype) di LogicList
     */
    public BioLogicList(@Autowired @Qualifier("bioService") final AIService bioService) {
        super(bioService, Bio.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
    }

    /**
     * Costruisce una nuova @route in modalità new <br>
     * Seleziona (eventualmente) il Form da usare <br>
     * Può essere sovrascritto, senza invocare il metodo della superclasse <br>
     * <p>
     * Intercetta il bottone 'new' <br>
     * Costruisce un dialogo di input per il titolo wiki della pagina <br>
     * Scarica (download) dal server la pagina <br>
     * Costruisce e NON salva la entity <br>
     * Apre la scheda per controllo <br>
     * Salva la entity (probabilmente) <br>
     */
    @Override
    protected void newForm() {
        this.getInput();
    }

    protected void getInput() {
        //        EnhancedDialog dialog = new EnhancedDialog();
        //        dialog.setHeader("Example Header");
        //        dialog.setContent(new Span("Content"));
        //        dialog.setFooter(new Button("Close", evt -> dialog.close()));
        //        dialog.open();

        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        TextField field = new TextField("Titolo della pagina sul server wiki");
        field.setWidth("14em");
        field.addValueChangeListener(event -> newFormEsegue(field.getValue()));
        layout.add(field);
        field.setAutofocus(true);

        dialog.setWidth("400px");
        dialog.setHeight("200px");
        layout.add(new Button("Download", evt -> dialog.close()));
        dialog.add(layout);
        dialog.open();
    }

    protected void newFormEsegue(String wikiTitle) {
        Bio bio = downloadBio(wikiTitle);

        if (bio != null) {
            this.executeRoute(bio);
        }
    }

    /**
     * Scarica una singola biografia <br>
     */
    private Bio downloadBio(String wikiTitle) {
        WrapPage wrap = null;
        Bio bio = null;

        if (text.isValid(wikiTitle)) {
            wrap = wikiBot.leggePage(wikiTitle);
            bio = bioService.newEntity(wrap);
            bioService.save(bio);
        }

        return bio;
    }

}// end of Route class