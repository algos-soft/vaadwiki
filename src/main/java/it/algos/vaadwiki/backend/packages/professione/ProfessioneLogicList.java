package it.algos.vaadwiki.backend.packages.professione;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadwiki.backend.logic.*;
import static it.algos.vaadwiki.backend.logic.WikiService.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: gio, 15-apr-2021 <br>
 * Fix time: 6:53 <br>
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
@Route(value = "professione", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class ProfessioneLogicList extends WikiLogicList {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public ProfessioneLogicList() {
        super.entityClazz = Professione.class;
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
        super.wikiModuloTitle =  PATH_MODULO_PROFESSIONE;
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

        lista.add(html.getSpanBlu("Modulo:Bio/Link attività."));
        lista.add(html.getSpanVerde("Contiene la tabella di conversione delle attività passate via parametri " + html.bold("Attività/Attività2/Attività3")));
        lista.add(html.getSpanVerde(" dal nome dell'attività a quello della pagina corrispondente, per creare dei piped wikiLink."));
        lista.add(html.getSpanVerde("All'interno della tabella le attività sono in ordine alfabetico al fine di rendere più agevole la manutenzione delle stesse"));
        lista.add(html.getSpanVerde("Le attività sono elencate all'interno del modulo con la seguente sintassi:"));
        lista.add(html.getSpanVerde("[\"attivitaForma1\"] = \"pagina di riferimento\","));
        lista.add(html.getSpanVerde("[\"attivitaForma2\"] = \"pagina di riferimento\","));
        lista.add(html.getSpanRosso("Nella collezione locale mongoDB vengono aggiunte " + html.bold("anche") + " le voci delle " + html.bold("attività maschili") + " che corrispondono alla pagina (non presenti nel Modulo su Wiki)"));
        lista.add(html.getSpanRosso("Nella collezione locale mongoDB vengono aggiunte " + html.bold("anche") + " le voci delle " + html.bold("ex-attività") + " (non presenti nel Modulo su Wiki) recuperate dalla collezione locale 'Attività' su mongoDB\n"));
        lista.add(html.getSpanRosso("Le attività e le pagine mantengono il maiuscolo/minuscolo previsto nel modulo"));

        return lista;
    }


}// end of Route class