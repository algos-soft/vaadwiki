package it.algos.vaadwiki.backend.packages.genere;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.ui.*;
import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: mer, 14-apr-2021 <br>
 * Fix time: 9:14 <br>
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
@Route(value = "genere", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class GenereLogicList extends LogicList {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public GenereLogicList() {
        super.entityClazz = Genere.class;
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
//        super.usaBottoneStatistiche = false;
//        super.wikiModuloTitle = PATH_MODULO_GENERE;
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

        lista.add(html.getSpanBlu("Modulo:Bio/Plurale attività genere."));
        lista.add(html.getSpanVerde("Contiene la tabella di conversione delle attività passate via parametri <b>Attività/Attività2/Attività3</b>"));
        lista.add(html.getSpanVerde(" da singolare maschile e femminile (usati nell'incipit) al plurale maschile, per categorizzare la pagina"));
        lista.add(html.getSpanVerde("All'interno della tabella le attività sono in ordine alfabetico al fine di rendere più agevole la manutenzione delle stesse"));
        lista.add(html.getSpanVerde("Le attività sono elencate all'interno del modulo con la seguente sintassi:"));
        lista.add(html.getSpanVerde("[\"attività singolare maschile\"] = \"attività plurale maschile\","));
        lista.add(html.getSpanVerde("[\"attività singolare femminile\"] = \"attività plurale femminile\","));
        lista.add(html.getSpanRosso("Indipendentemente da come sono scritte nel modulo wiki, tutte le attività singolari e plurali sono convertite in maiuscolo"));

        return lista;
    }


}// end of Route class