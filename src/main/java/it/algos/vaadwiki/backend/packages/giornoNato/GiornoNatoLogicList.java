package it.algos.vaadwiki.backend.packages.giornoNato;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: dom, 1-ago-2021 alle 7:00 <br>
 * Last doc revision: dom, 1-ago-2021 alle 7:00 <br>
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
@Route(value = "GiornoNato", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class GiornoNatoLogicList extends WikiLogicList {


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
     * @param wikigiornoService (@Autowired) (@Qualifier) riferimento al service specifico correlato a questa istanza (prototype) di LogicList
     */
    public GiornoNatoLogicList(@Autowired @Qualifier("wikigiornoService") final AIService wikigiornoService) {
        super(wikigiornoService, Giorno.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        this.usaColonnaWiki = true;
        this.usaColonnaTest = true;
        this.usaColonnaUpload = true;
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto <br>
     *
     * @return una liste di 'span'
     */
    //    @Override
    protected List<Span> getSpanList() {
        return Collections.singletonList(html.getSpanVerde("Test"));
    }

    //    /**
    //     * Costruisce una lista ordinata di nomi delle properties della Grid. <br>
    //     * Nell' ordine: <br>
    //     * 1) Cerca nell' annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
    //     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
    //     * 3) Sovrascrive la lista nella sottoclasse specifica xxxLogicList <br>
    //     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
    //     *
    //     * @return lista di nomi di properties
    //     */
    //    @Override
    //    public List<String> getGridColumns() {
    //        return Arrays.asList("giorno","mese");
    //    }

    /**
     * Regolazioni finali della Grid <br>
     * <p>
     * Eventuali colonna 'ad-hoc' <br>
     * Eventuali 'listener' specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixGrid() {
        Grid realGrid;
        ComponentRenderer renderer;
        Grid.Column colonna;
        String lar = "5em";

        if (bodyPlaceHolder != null && grid != null) {
            realGrid = grid.getGrid();

            if (usaColonnaWiki) {
                renderer = new ComponentRenderer<>(this::createWikiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Wiki");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaTest) {
                renderer = new ComponentRenderer<>(this::createTestButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Test");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaUpload) {
                renderer = new ComponentRenderer<>(this::createUploadButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Upload");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
        }
    }

    @Override
    protected void wikiPage(AEntity entityBean) {
        String tag = "Nati il ";
        wikiApi.openWikiPage(tag + ((Giorno) entityBean).titolo);
    }

    @Override
    protected void testWiki(AEntity entityBean) {
        //        String link = "\"" + FlowCost.PATH_WIKI + wikiTitle + "\"";
        String link = "view-source:https://it.wikipedia.org/wiki/Nati_il_7_marzo";
//        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }

}// end of Route class