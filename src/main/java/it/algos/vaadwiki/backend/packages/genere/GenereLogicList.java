package it.algos.vaadwiki.backend.packages.genere;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
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
@Route(value = "genere", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.list, doc = AEWizDoc.inizioRevisione)
public class GenereLogicList extends WikiLogicList {


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
    public GenereLogicList(@Autowired @Qualifier("genereService") final AIService entityService) {
        super(entityService, Genere.class);
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
        super.wikiModuloTitle = PATH_MODULO_GENERE;
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        super.fixAlertList();

        String maschile = html.bold("maschile");
        String femminile = html.bold("femminile");
        String nazionalitaTxt = "nazionalità, ";
        String nomeTxt = "nome, e ";
        String cognomeTxt = "cognome ";
        String listaNazionalita = "Categoria:Bio nazionalità";
        String listaNomi = "Progetto:Antroponimi/Liste nomi";
        String listaCognomi = "Progetto:Antroponimi/Liste cognomi";
        String progettoTxt = "progetto:Antroponimi";

        super.fixInfoDownload(AEWikiPreferenza.lastDownloadGenere);
        addWikiLink(PATH_MODULO_GENERE);
        addSpanVerde(String.format("Contiene la tabella di conversione delle attività passate via parametri %s", parametri));
        addSpanVerde(String.format(" da %s %s e %s (usati nell'incipit) al %s %s e %s, per le intestazioni dei paragrafi", singolare, maschile, femminile, plurale, maschile, femminile));
        //        addSpanVerde(String.format("nelle liste di nazionalità, nomi e cognomi previste nel Progetto:Antroponimi"));

        Span liste = html.getSpanVerde("nelle liste di ");
        Span nazionalità = html.getSpanVerde(nazionalitaTxt, AETypeWeight.bold);
        Span nomi = html.getSpanVerde(nomeTxt, AETypeWeight.bold);
        Span cognomi = html.getSpanVerde(cognomeTxt, AETypeWeight.bold);
        Span previste = html.getSpanVerde("previste nel ");
        Span progetto = html.getSpanVerde(progettoTxt, AETypeWeight.bold);
        Anchor anchorNaz = new Anchor(FlowCost.PATH_WIKI + listaNazionalita, nazionalità);
        Anchor anchorNomi = new Anchor(FlowCost.PATH_WIKI + listaNomi, nomi);
        Anchor anchorCog = new Anchor(FlowCost.PATH_WIKI + listaCognomi, cognomi);
        Anchor anchorAntr = new Anchor(FlowCost.PATH_WIKI + progettoTxt, progetto);
        Span riga = new Span(liste, anchorNaz, anchorNomi, anchorCog, previste, anchorAntr);
        if (alertList != null) {
            alertList.add(riga);
        }

        addSpanVerde(String.format("Le attività sono elencate nel modulo con la sintassi: [\"attivita %s %s\"]=\"attività %s %s\"", singolare, maschile, plurale, maschile));
        addSpanVerde(String.format("Le attività sono elencate nel modulo con la sintassi: [\"attivita %s %s\"]=\"attività %s %s\"", singolare, femminile, plurale, femminile));

        addSpanRossoFix(String.format("Indipendentemente da come sono scritte nel modulo wiki, tutte le attività singolari e plurali sono convertite in %s", minuscolo));
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
    }

}// end of Route class