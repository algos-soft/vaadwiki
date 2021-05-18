package it.algos.vaadwiki.backend.packages.genere;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import static it.algos.vaadwiki.backend.packages.wiki.WikiService.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: mer, 14-apr-2021 <br>
 * Last doc revision: mar, 18-mag-2021 alle 19:18 <br>
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
@Route(value = "genere", layout = MainLayout.class)
@AIScript(sovraScrivibile = false, type = AETypeFile.list, doc = AEWizDoc.revisione)
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
        super.wikiModuloTitle =  PATH_MODULO_GENERE;
    }


//    /**
//     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
//     * DEVE essere sovrascritto <br>
//     *
//     * @return una liste di 'span'
//     */
//    @Override
//    protected List<Span> getSpanList() {
//        List<Span> lista = new ArrayList<>();
//
//        lista.add(super.fixInfoDownload(AEWikiPreferenza.lastDownloadGenere));
//        lista.add(html.getSpanBlu("Modulo:Bio/Plurale attività genere."));
//        lista.add(html.getSpanVerde("Contiene la tabella di conversione delle attività passate via parametri <b>Attività/Attività2/Attività3</b>"));
//        lista.add(html.getSpanVerde(" da singolare maschile e femminile (usati nell'incipit) al plurale maschile, per categorizzare la pagina"));
//        lista.add(html.getSpanVerde("All'interno della tabella le attività sono in ordine alfabetico al fine di rendere più agevole la manutenzione delle stesse"));
//        lista.add(html.getSpanVerde("Le attività sono elencate all'interno del modulo con la seguente sintassi:"));
//        lista.add(html.getSpanVerde("[\"attività singolare maschile\"] = \"attività plurale maschile\","));
//        lista.add(html.getSpanVerde("[\"attività singolare femminile\"] = \"attività plurale femminile\","));
//        lista.add(html.getSpanRosso("Indipendentemente da come sono scritte nel modulo wiki, tutte le attività singolari e plurali sono convertite in maiuscolo"));
//
//        return lista;
//    }


}// end of Route class