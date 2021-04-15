package it.algos.vaadwiki.backend.packages.attivita;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadwiki.backend.logic.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: mer, 14-apr-2021 <br>
 * Fix time: 17:58 <br>
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
@Route(value = "attivita", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class AttivitaLogicList extends WikiLogicList {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public AttivitaLogicList() {
        super.entityClazz = Attivita.class;
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
        super.wikiModuloTitle = PATH_MODULO_ATTIVITA;
        super.wikiStatisticheTitle = PATH_STATISTICHE_ATTIVITA;
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

        lista.add(html.getSpanBlu("Modulo:Bio/Plurale attività."));
        lista.add(html.getSpanVerde("Contiene la tabella di conversione delle attività passate via parametri " + html.bold("Attività/Attività2/Attività3")));
        lista.add(html.getSpanVerde(" da singolare maschile e femminile (usati nell'incipit) al plurale maschile, per categorizzare la pagina"));
        lista.add(html.getSpanVerde("All'interno della tabella le attività sono in ordine alfabetico al fine di rendere più agevole la manutenzione delle stesse"));
        lista.add(html.getSpanVerde("Le attività sono elencate all'interno del modulo con la seguente sintassi:"));
        lista.add(html.getSpanVerde("[\"attivitaForma1\"] = \"attività al plurale\","));
        lista.add(html.getSpanVerde("[\"attivitaForma2\"] = \"attività al plurale\","));
        lista.add(html.getSpanRosso("Nella collezione locale mongoDB vengono aggiunte " + html.bold("anche") + " le voci delle " + html.bold("ex") + "-attività (non presenti nel Modulo su Wiki) recuperate dal modulo Modulo:Bio/Plurale attività genere"));
        lista.add(html.getSpanRosso("Progetto:Biografie/Attività."));

        return lista;
    }


}// end of Route class