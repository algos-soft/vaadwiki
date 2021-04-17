package it.algos.vaadflow14.backend.packages.preferenza;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project: vaadflow14 <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: ven, 12-mar-2021 <br>
 * Fix time: 8:03 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, si usa la classe GenericLogicList con @Route <br>
 * Gestione della 'business logic' e della 'grafica' di @Route <br>
 * Mantiene lo 'stato' <br>
 * L' istanza (PROTOTYPE) viene creata ad ogni chiamata del browser <br>
 * Eventuali parametri (opzionali) devono essere passati nell'URL <br>
 * <p>
 * Annotated with @Route (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Route(value = "preferenza", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class PreferenzaLogicList extends LogicList {


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
    public PreferenzaLogicList(@Autowired @Qualifier("preferenzaService") final AIService entityService) {
        super(entityService, Preferenza.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //        this.searchType = AESearch.editField;//@todo Funzionalità ancora da implementare
        //        this.searchProperty = annotation.getSearchPropertyName(entityClazz);//@todo Funzionalità ancora da implementare
        //        this.usaBottoneDelete = false;//@todo Funzionalità ancora da implementare
        this.usaBottoneResetList = true;
        this.usaBottoneNew = true;
        this.usaBottoneExport = false;
        this.usaBottonePaginaWiki = false;
        //        this.wikiPageTitle = VUOTA;//@todo Funzionalità ancora da implementare
        //        this.usaBottoneEdit = true;//@todo Funzionalità ancora da implementare
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     *
     * @return una lista di elementi html di tipo 'span'
     */
    @Override
    protected List<Span> getSpanList() {
        List<Span> lista = new ArrayList<>();

        lista.add(html.getSpanVerde("PreferenzaLogicList è @Scope " + html.bold("prototype") + " mentre PreferenzaService è " + html.bold("singleton")));
        lista.add(html.getSpanVerde("PreferenzaLogicList è usato come normale classe del package di preferenze e viene ricreato per ogni Grid e Form"));
        lista.add(html.getSpanVerde("PreferenzaService è usato per 'leggere' le preferenze da qualsiasi 'service' singleton"));
        lista.add(html.getSpanVerde("Alcune preferenze sono Enumeration e possono essere lette direttamente: AEPreferenza.usaDebug.is()"));
        lista.add(html.getSpanVerde("Altre preferenze sono inserite dall'utente e possono essere lette dal singleton APreferenzaService: pref.isBool(\"usaDebug\")"));
        lista.add(html.getSpanRosso("Bottoni 'DeleteAll', 'Reset' e 'New' (e anche questo avviso) solo in fase di debug. Sempre presente il searchField ed il comboBox 'Type'"));

        return lista;
    }

    /**
     * Costruisce una mappa di ComboBox di selezione e filtro <br>
     * DEVE essere sovrascritto nella sottoclasse <br>
     */
    //    @Override
    protected void fixMappaComboBox() {
        //        super.creaComboBox("type");
    }

    /**
     * Costruisce un wrapper di dati <br>
     * I dati sono gestiti da questa 'logic' (nella sottoclasse eventualmente) <br>
     * I dati vengono passati alla View che li usa <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     *
     * @param entityBean interessata
     *
     * @return wrapper di dati per il Form
     */
    //    @Override
    //    public WrapForm getWrapForm(AEntity entityBean) {
    //        WrapForm wrap = super.getWrapForm(entityBean);//@todo Funzionalità ancora da implementare
    //        wrap.setUsaBottomLayout(true);//@todo Funzionalità ancora da implementare
    //        return wrap;
    //    }

    /**
     * Costruisce un layout per il Form in bodyPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Costruisce un' istanza dedicata <br>
     * Passa all' istanza un wrapper di dati <br>
     * Inserisce l' istanza (grafica) in bodyPlacehorder della view <br>
     *
     * @param entityBean interessata
     *
     * @return componente grafico per il placeHolder
     */
    //    @Override
    //    public AForm getBodyFormLayout(AEntity entityBean) {
    //        currentForm = null;
    //
    //        //--entityBean dovrebbe SEMPRE esistere (anche vuoto), ma meglio controllare
    //        if (entityBean != null) {
    //            try {
    //                currentForm = appContext.getBean(PreferenzaForm.class, this, getWrapForm(entityBean));
    //            } catch (Exception unErrore) {
    //                logger.error(unErrore, this.getClass(), "getBodyFormLayout");
    //            }
    //        }
    //
    //        return currentForm;
    //    }


    /**
     * Costruisce una lista ordinata di nomi delle properties della Grid. <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    //    @Override
    public List<String> getGridPropertyNamesList() {
        String matrice;

        if (FlowVar.usaCompany) {
            matrice = "code,type,value,vaadFlow,usaCompany,needRiavvio,visibileAdmin,descrizione";
        }
        else {
            matrice = "code,type,value,vaadFlow,needRiavvio,descrizione";
        }

        return array.fromStringa(matrice);
    }

}// end of Route class