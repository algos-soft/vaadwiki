package it.algos.vaadwiki.backend.packages.nazionalita;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadwiki.backend.logic.*;
import static it.algos.vaadwiki.backend.logic.WikiService.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: mer, 14-apr-2021 <br>
 * Fix time: 18:44 <br>
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
@Route(value = "nazionalita", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class NazionalitaLogicList extends WikiLogicList {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public NazionalitaLogicList() {
        super.entityClazz = Nazionalita.class;
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottoneUpload = true;
        super.usaBottoneStatistiche = true;
        super.wikiModuloTitle = PATH_MODULO_NAZIONALITA;
        super.wikiStatisticheTitle = PATH_STATISTICHE_NAZIONALITA;
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto <br>
     *
     * @return una liste di 'span'
     */
    @Override
    protected List<Span> getSpanList() {
        List<Span> lista = new ArrayList<>();

        lista.add(html.getSpanBlu("Modulo:Bio/Plurale nazionalità."));
        lista.add(html.getSpanVerde("Contiene la tabella di conversione delle nazionalità passate via parametri " + html.bold("Nazionalità/Cittadinanza/NazionalitàNaturalizzato")));
        lista.add(html.getSpanVerde(" da singolare maschile e femminile (usati nell'incipit) al plurale maschile, per categorizzare la pagina"));
        lista.add(html.getSpanVerde("All'interno della tabella le nazionalità sono in ordine alfabetico al fine di rendere più agevole la manutenzione delle stesse"));
        lista.add(html.getSpanVerde("Le nazionalità sono elencate all'interno del modulo con la seguente sintassi:"));
        lista.add(html.getSpanVerde("[\"nazionalitaforma1\"] = \"nazionalità al plurale\","));
        lista.add(html.getSpanVerde("[\"nazionalitaforma2\"] = \"nazionalità al plurale\","));
        lista.add(html.getSpanRosso("Progetto:Biografie/Nazionalità."));

        return lista;
    }


}// end of Route class