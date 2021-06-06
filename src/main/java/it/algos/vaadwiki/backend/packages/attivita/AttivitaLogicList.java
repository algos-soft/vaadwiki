package it.algos.vaadwiki.backend.packages.attivita;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
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
 * First time: mer, 14-apr-2021 <br>
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
@Route(value = "attivita", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.list, doc = AEWizDoc.inizioRevisione)
public class AttivitaLogicList extends WikiLogicList {


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
    public AttivitaLogicList(@Autowired @Qualifier("attivitaService") final AIService entityService) {
        super(entityService, Attivita.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.wikiModuloTitle = PATH_MODULO_ATTIVITA;
        super.wikiStatisticheTitle = PATH_STATISTICHE_ATTIVITA;
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        super.fixAlertList();

        String ex = html.bold("ex");
        String moduloTxt = "Modulo:Bio/Plurale attività genere";

        super.fixInfoDownload(AEWikiPreferenza.lastDownloadAttivita);
        addWikiLink(PATH_MODULO_ATTIVITA);
        addWikiLink(PATH_STATISTICHE_ATTIVITA);
        addSpanVerde(String.format("Contiene la tabella di conversione delle attività passate via parametri %s", parametri));
        addSpanVerde(String.format(" da %s maschile e femminile (usati nell'incipit) al %s maschile, per categorizzare la pagina", singolare, plurale));
        addSpanVerde(String.format("Le attività sono elencate nel modulo con la sintassi: [\"attivita%s\"]=\"attività al plurale\", [\"attivita%s\"]=\"attività al plurale\",", uno, due));
        addSpanRossoFix(String.format("Nella collezione locale mongoDB vengono aggiunte %s le voci delle %s-attività (non presenti nel Modulo su Wiki)", anche, ex));

        Span voci = html.getSpanRosso("Le voci aggiunte vengono recuperate dal ");
        Span modulo = html.getSpanRosso(moduloTxt, AETypeWeight.bold);
        Anchor anchor = new Anchor(FlowCost.PATH_WIKI + moduloTxt, modulo);
        Span riga = new Span(voci, anchor);
        if (alertList != null) {
            alertList.add(riga);
        }

        addSpanRossoFix(String.format("Indipendentemente da come sono scritte nel modulo wiki, tutte le attività singolari e plurali sono convertite in %s", minuscolo));
    }


    @Override
    protected void wikiPage(AEntity entityBean) {
        wikiApi.openWikiPage("Progetto:Biografie/Attività/" + text.primaMaiuscola(((Attivita) entityBean).plurale));
    }


}// end of Route class