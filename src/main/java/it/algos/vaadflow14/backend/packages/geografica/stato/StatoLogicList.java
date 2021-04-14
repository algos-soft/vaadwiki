package it.algos.vaadflow14.backend.packages.geografica.stato;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.ui.*;

import java.util.*;

/**
 * Project: vaadflow14 <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: ven, 12-mar-2021 <br>
 * Fix time: 7:36 <br>
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
@Route(value = "stato", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class StatoLogicList extends LogicList {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public StatoLogicList() {
        super.entityClazz = Stato.class;
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.operationForm =   AEOperation.showOnly;
        super.usaBottonePaginaWiki = true;
//        super.searchType = AESearch.editField;//@todo Funzionalità ancora da implementare
        super.wikiPageTitle = "ISO_3166-1";
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

        lista.add(html.getSpanBlu("Stati del mondo. Codifica secondo ISO 3166-1"));
//        lista.add(html.getSpanBlu("Recuperati dalla pagina wiki: " + wikiPageTitle));//@todo Funzionalità ancora da implementare
        lista.add(html.getSpanBlu("Codici: numerico, alfa-due, alfa-tre e ISO locale"));
        lista.add(html.getSpanBlu("Ordinamento alfabetico: prima Italia, UE e poi gli altri"));
        if (AEPreferenza.usaDebug.is()) {
            lista.add(html.getSpanRosso("Bottoni 'DeleteAll', 'Reset' e 'New' (e anche questo avviso) solo in fase di debug. Sempre presente il searchField"));
        }

        return lista;
    }

    /**
     * Costruisce una mappa di ComboBox di selezione e filtro <br>
     * DEVE essere sovrascritto nella sottoclasse <br>
     */
//    @Override
    protected void fixMappaComboBox() {
//        super.creaComboBox("continente", "europa");
    }


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