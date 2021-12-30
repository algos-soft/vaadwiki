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
        String antroponimi = "Progetto:Antroponimi";
        String listaNomi = "/Liste nomi";
        String listaCognomi = "/Liste cognomi";

        super.fixInfoDownload(AEWikiPreferenza.lastDownloadGenere);
        addWikiLink(PATH_MODULO_GENERE);
        addSpanVerde(String.format("Contiene la tabella di conversione delle attività passate via parametri %s da %s %s e %s (usati nell'incipit) al %s %s e %s, per le intestazioni dei paragrafi.", parametri, singolare, maschile, femminile, plurale, maschile, femminile));

        Span liste = html.getSpanVerde("Usata nelle liste di ");
        Span naz = html.getSpanBlu(nazionalitaTxt, AETypeWeight.bold);
        Anchor anchor = new Anchor(FlowCost.PATH_WIKI + listaNazionalita, naz);
        Span nome = html.getSpanBlu(nomeTxt, AETypeWeight.bold);
        Anchor anchor2 = new Anchor(FlowCost.PATH_WIKI + antroponimi + listaNomi, nome);
        Span cognome = html.getSpanBlu(cognomeTxt, AETypeWeight.bold);
        Anchor anchor3 = new Anchor(FlowCost.PATH_WIKI + antroponimi + listaCognomi, cognome);
        Span previste = html.getSpanVerde("previste nel ");
        Span progetto = html.getSpanBlu(antroponimi + ".", AETypeWeight.bold);
        Anchor anchor4 = new Anchor(FlowCost.PATH_WIKI + antroponimi, progetto);
        Span riga = new Span(liste, anchor, anchor2, anchor3, previste, anchor4);
        if (alertList != null) {
            alertList.add(riga);
        }

        addSpanVerde(String.format("Le attività sono elencate nel modulo con la sintassi: [\"attivita %s %s\"]=\"attività %s %s\".", singolare, maschile, plurale, maschile));
        addSpanVerde(String.format("Le attività sono elencate nel modulo con la sintassi: [\"attivita %s %s\"]=\"attività %s %s\".", singolare, femminile, plurale, femminile));

        Span scritte = html.getSpanRosso(String.format(" Indipendentemente da come sono scritte nel "));
        Span modulo = html.getSpanBlu("modulo", AETypeWeight.bold);
        Anchor ancho5 = new Anchor(FlowCost.PATH_WIKI + PATH_MODULO_GENERE, modulo);
        Span minuscole = html.getSpanRosso(String.format(", tutte le attività singolari ed i generi plurali sono convertiti in %s.", minuscolo));
        Span riga5 = new Span(scritte, ancho5, minuscole);
        if (alertList != null) {
            alertList.add(riga5);
        }

//        addSpanRossoFix(String.format("Indipendentemente da come sono scritte nel modulo wiki, tutte le attività singolari e plurali sono convertite in %s.", minuscolo));
    }


}// end of Route class