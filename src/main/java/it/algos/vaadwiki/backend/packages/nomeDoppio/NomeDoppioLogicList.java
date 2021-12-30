package it.algos.vaadwiki.backend.packages.nomeDoppio;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
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
 * First time: dom, 18-apr-2021 <br>
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
@Route(value = "nomeDoppio", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.list, doc = AEWizDoc.inizioRevisione)
public class NomeDoppioLogicList extends WikiLogicList {


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
    public NomeDoppioLogicList(@Autowired @Qualifier("nomeDoppioService") final AIService entityService) {
        super(entityService, NomeDoppio.class);
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
        super.usaBottoneUploadAll = false;
        super.usaBottoneUploadStatistiche = false;
        super.wikiModuloTitle = PATH_MODULO_PRENOME;
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        super.fixAlertList();

        String doppi = html.bold("nomi doppi");
        String biografie = html.bold("50");
        String nome = html.bold("nome");
        String solo = html.bold("solo");
        String lista = html.bold("persone per nome");
        String categoriaLink = "Categoria:Prenomi composti";

        super.fixInfoDownload(AEWikiPreferenza.lastDownloadNomeDoppio);
        addWikiLink(PATH_MODULO_PRENOME);
        addSpanVerde(String.format("Sono elencati i %s (ad esempio 'Maria Teresa'). BioBot crea una lista di %s quando le biografie superano i %s (tra nomi e %s)", doppi,lista, biografie,doppi));

        Span vedi = html.getSpanVerde(String.format("Vedi anche "));
        Span categoria = html.getSpanBlu(categoriaLink, AETypeWeight.bold);
        Anchor anchor = new Anchor(FlowCost.PATH_WIKI + categoriaLink, categoria);
        Span fine = html.getSpanVerde(".");
        Span riga = new Span(vedi, anchor, fine);
        if (alertList != null) {
            alertList.add(riga);
        }

        addSpanRossoFix(String.format("La lista %s prevede %s nomi singoli a cui vengono aggiunti questi %s accettabili. Quando si crea la lista  %s, i nomi doppi vengono scaricati ed aggiunti alla lista stessa", nome, solo, doppi,nome));
//        addSpanRossoFix(String.format("Quando si crea la lista  %s, i nomi doppi vengono scaricati ed aggiunti alla lista stessa", nome));
    }



}// end of Route class