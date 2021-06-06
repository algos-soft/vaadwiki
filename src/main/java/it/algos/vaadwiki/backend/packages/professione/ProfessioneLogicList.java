package it.algos.vaadwiki.backend.packages.professione;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import static it.algos.vaadwiki.backend.packages.wiki.WikiService.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: gio, 15-apr-2021 <br>
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
@Route(value = "professione", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.list, doc = AEWizDoc.inizioRevisione)
public class ProfessioneLogicList extends WikiLogicList {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore con parametro <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Il framework SpringBoot/Vaadin con l'Annotation @Autowired inietta automaticamente un riferimento al singleton xxxService <br>
     * L'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Regola (nella superclasse) la entityClazz (final) associata a questa logicView <br>
     *
     * @param entityService (@Autowired) (@Qualifier) riferimento al service specifico correlato a questa istanza (prototype) di LogicList
     */
    public ProfessioneLogicList(@Autowired @Qualifier("professioneService") final AIService entityService) {
        super(entityService, Professione.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottoneUpload = false;
        super.usaBottoneStatistiche = false;
        super.usaBottoneUploadAll = false;
        super.usaBottoneUploadStatistiche = false;
        super.wikiModuloTitle = PATH_MODULO_PROFESSIONE;
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        super.fixAlertList();
        String riferimento = html.bold("pagina di riferimento");
        String maschili = html.bold("attività maschili");
        String non = html.bold("non");
        String ex = html.bold("ex-attività");
        String attivita = html.bold("Attività");
        String mantengono = html.bold("mantengono");
        String pagina = html.bold("pagina");
        String professione = html.bold("professione");

        super.fixInfoDownload(AEWikiPreferenza.lastDownloadProfessione);
        addWikiLink(PATH_MODULO_PROFESSIONE);
        addSpanVerde(String.format("Contiene la tabella di conversione delle attività passate via parametri %s", parametri));
        addSpanVerde(String.format(" dal nome dell'attività a quello della %s corrispondente, per creare dei piped wikiLink", pagina));
        addSpanVerde(String.format("Le attività sono elencate nel modulo con la sintassi: [\"attivita%s\"]=\"%s\", [\"attivita%s\"]=\"%s\",", uno, riferimento, due, riferimento));
        addSpanRossoFix(String.format("In mongoDB.%s vengono aggiunte %s le voci delle %s che corrispondono alla pagina (%s presenti nel Modulo su Wiki)", professione, anche, maschili, non));
        addSpanRossoFix(String.format("In mongoDB.%s vengono aggiunte %s le voci delle %s (%s presenti nel Modulo su Wiki) recuperate da mongoDB.%s", professione, anche, ex, non, attivita));
        addSpanRossoFix(String.format("Le attività e le pagine %s il maiuscolo/minuscolo previsto nel modulo",mantengono));
    }

    /**
     * Regolazioni finali della Grid <br>
     * <p>
     * Eventuali colonna 'ad-hoc' <br>
     * Eventuali 'listener' specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixGrid() {
        Grid realGrid;
        ComponentRenderer renderer;
        Grid.Column colonna;
        String lar = "5em";

        if (bodyPlaceHolder != null && grid != null) {
            realGrid = grid.getGrid();

            renderer = new ComponentRenderer<>(this::createWikiButton);
            colonna = realGrid.addColumn(renderer);
            colonna.setHeader("Wiki");
            colonna.setWidth(lar);
            colonna.setFlexGrow(0);
        }
    }

    protected Button createWikiButton(AEntity entityBean) {
        Button wikiButton = new Button(new Icon(VaadinIcon.LIST));
        wikiButton.getElement().setAttribute("theme", "secondary");
        wikiButton.addClickListener(e -> wikiPage(entityBean));

        return wikiButton;
    }

    @Override
    protected void wikiPage(AEntity entityBean) {
        wikiApi.openWikiPage(text.primaMaiuscola(((Professione) entityBean).pagina));
    }


}// end of Route class