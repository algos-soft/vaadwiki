package it.algos.vaadwiki.backend.packages.prenome;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.logic.*;
import static it.algos.vaadwiki.backend.logic.WikiService.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: dom, 18-apr-2021 <br>
 * Fix time: 7:42 <br>
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
@Route(value = "prenome", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class PrenomeLogicList extends WikiLogicList {


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
    public PrenomeLogicList(@Autowired @Qualifier("prenomeService") final AIService entityService) {
        super(entityService, Prenome.class);
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
        super.usaBottoneStatistiche = true;
        super.wikiModuloTitle = PATH_MODULO_PRENOME;
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

        lista.add(super.fixInfoDownload(AEWikiPreferenza.lastDownloadPrenome));
        lista.add(html.getSpanBlu("Progetto:Antroponimi/Nomi doppi."));
        lista.add(html.getSpanVerde("Sono elencati i " + html.bold("nomi doppi") + " (ad esempio 'Maria Teresa'), per i quali il BioBot deve fare una lista di biografati una volta superate le " + html.bold("50") + " biografie."));
        lista.add(html.getSpanVerde("Si veda anche la [[Categoria:Prenomi composti]]."));
        lista.add(html.getSpanRosso("La lista " + html.bold("nome") + " prevede " + html.bold("solo") + " nomi singoli a cui vengono aggiunti questi " + html.bold("nomi doppi") + " accettabili."));
        lista.add(html.getSpanRosso("Quando si crea la lista " + html.bold("nome") + ", i nomi doppi vengono scaricati ed aggiunti alla lista stessa."));
        return lista;
    }


}// end of Route class