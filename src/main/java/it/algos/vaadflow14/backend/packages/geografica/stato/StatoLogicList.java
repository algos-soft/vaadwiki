package it.algos.vaadflow14.backend.packages.geografica.stato;

import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.geografica.continente.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.button.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Project: vaadflow14 <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: ven, 12-mar-2021 <br>
 * Last doc revision: mer, 19-mag-2021 alle 18:38 <br>
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
@Route(value = "stato", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class StatoLogicList extends LogicList {


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
    public ContinenteService continenteService;

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
    public StatoLogicList(@Autowired @Qualifier("statoService") final AIService entityService) {
        super(entityService, Stato.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //        super.operationForm = AEOperation.showOnly;
        super.usaBottonePaginaWiki = true;
        super.usaBottoneSearch = true;
        super.wikiPageTitle = "ISO_3166-1";
        this.maxNumeroBottoniPrimaRiga = 3;
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixSpanList() {
        addSpanBlu("Stati del mondo. Codifica secondo ISO 3166-1");
        //        addSpanBlu("Recuperati dalla pagina wiki: " + wikiPageTitle));//@todo Funzionalità ancora da implementare
        addSpanBlu("Codici: numerico, alfa-due, alfa-tre e ISO locale");
        addSpanBlu("Ordinamento alfabetico: prima Italia, UE e poi gli altri");
    }

//    protected void fixTopLayout() {
//        topLayout = appContext.getBean(ATopLayout.class, this.getWrapButtonsTop());
//        if (topPlaceHolder != null && topLayout != null) {
//            topPlaceHolder.add(topLayout);
//        }
//    }

    /**
     * Costruisce una mappa di ComboBox di selezione e filtro <br>
     * DEVE essere sovrascritto nella sottoclasse <br>
     */
//    @Override
//    protected void fixMappaComboBox2() {
//        super.fixComboBox("continente", continenteService.findByKey("Europa"));
//    }

    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getFormPropertyNamesList() {
        String propertyStato = "stato";
        String tagRegioni = "regioni";
        boolean esistonoRegioni = false;
        List<String> lista = super.getFormPropertyNamesList();

        if (AEPreferenza.usaDebug.is()) {
            return lista;
        }

        esistonoRegioni = mongo.esistono(Regione.class, propertyStato, entityBean);
        if (!esistonoRegioni && lista.contains(tagRegioni)) {
            lista.remove(tagRegioni);
        }

        return lista;
    }


    /**
     * Azione proveniente dal click sul bottone Prima <br>
     * Recupera la lista FILTRATA e ORDINATA delle properties, ricevuta dalla Grid <br>@todo da realizzare
     * Si sposta alla precedente <br>
     * Carica il form relativo <br>
     */
    protected void prima(final AEntity currentEntityBean) {
        AEntity previousEntityBean = mongo.findPrevious(entityClazz, currentEntityBean.id);
        executeRoute(previousEntityBean.id);
    }

}// end of Route class