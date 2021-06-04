package it.algos.vaadwiki.backend.packages.nazionalita;

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
@Route(value = "nazionalita", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.list, doc = AEWizDoc.inizioRevisione)
public class NazionalitaLogicList extends WikiLogicList {


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
    public NazionalitaLogicList(@Autowired @Qualifier("nazionalitaService") final AIService entityService) {
        super(entityService, Nazionalita.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.wikiModuloTitle = PATH_MODULO_NAZIONALITA;
        super.wikiStatisticheTitle = PATH_STATISTICHE_NAZIONALITA;
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        String parametri = html.bold("Nazionalità/Cittadinanza/NazionalitàNaturalizzato");
        String plurale = html.bold("plurale maschile");
        String alfabetico = html.bold("alfabetico");
        String uno = html.bold("Forma1");
        String due = html.bold("Forma2");

        super.fixInfoDownload(AEWikiPreferenza.lastDownloadNazionalita);
        addSpanBlu(html.bold("Modulo:Bio/Plurale nazionalità."));
        addSpanBlu(html.bold("Progetto:Biografie/Nazionalità."));
        addSpanVerde(String.format("Contiene la tabella di conversione delle nazionalità passate via parametri %s", parametri));
        addSpanVerde(String.format(" da singolare maschile e femminile (usati nell'incipit) al %s, per categorizzare la pagina",plurale));
        addSpanVerde(String.format("All'interno della tabella le nazionalità sono in ordine %s al fine di rendere più agevole la manutenzione delle stesse", alfabetico));
        addSpanVerde(String.format("Le nazionalità sono elencate nel modulo con la sintassi: [\"nazionalita%s\"]=\"nazionalità al plurale\", [\"nazionalita%s\"]=\"nazionalità al plurale\",", uno, due));
    }


    @Override
    protected void wikiPage(AEntity entityBean) {
        wikiApi.openWikiPage("Progetto:Biografie/Nazionalità/" + text.primaMaiuscola(((Nazionalita)entityBean).plurale));
    }

}// end of Route class