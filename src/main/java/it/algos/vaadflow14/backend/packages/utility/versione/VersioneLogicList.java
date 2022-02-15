package it.algos.vaadflow14.backend.packages.utility.versione;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

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
@Route(value = "versione", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class VersioneLogicList extends LogicList {


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
    public VersioneLogicList(@Autowired @Qualifier("versioneService") final AIService entityService) {
        super(entityService, Versione.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        String base = html.bold(FlowCost.NAME_VAADFLOW);
        String progetto = html.bold(FlowVar.projectNameUpper);
        String type = html.bold("Type");
        String filtro = html.bold("filtro");
        String numero = html.bold("#");
        String release = html.bold("release");
        String company = html.bold("company");
        Span casetta;
        Icon casettaIcon;
        Span casettaSpan;
        Span casettaText;
        Span fabbrica=null;
        Icon fabbricaIcon;
        Span fabbricaSpan;
        Span fabbricaText;

        addSpanVerde("Cronologia delle modifiche/cambiamenti/versioni/patch/aggiunte al programma");
        addSpanVerde(String.format("%s è una categorizzazione semplice di riferimento (enumeration). Con %s selezionabile da menu.", type, filtro));
        addSpanVerde(String.format("%s è il numero della %s del programma (FlowVar.flowVersion o FlowVar.projectVersion).", numero, release));

        casettaIcon = new Icon(VaadinIcon.HOME);
        casettaIcon.setColor("green");
        casettaSpan = new Span(casettaIcon);
        casettaText = html.getSpanVerde(String.format(" è il flag booleano per separare le versioni di %s da quelle del progetto specifico %s", base, progetto));
        casetta = new Span(casettaSpan, casettaText);

        if (FlowVar.usaCompany) {
            fabbricaIcon = new Icon((VaadinIcon.FACTORY));
            fabbricaIcon.setColor("green");
            fabbricaSpan = new Span(fabbricaIcon);
            fabbricaText = html.getSpanVerde(String.format(" è il flag booleano per identificare le versioni specifiche di una sola %s", company));
            fabbrica = new Span(fabbricaSpan, fabbricaText);
        }

        if (alertList != null) {
            alertList.add(casetta);
            if (FlowVar.usaCompany) {
                alertList.add(fabbrica);
            }
        }
    }

    /**
     * Costruisce una lista ordinata di nomi delle properties della Grid. <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica xxxLogicList <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridColumns() {
        if (FlowVar.usaCompany) {
            return array.fromStringa("type,release,giorno,titolo,company,descrizione,vaadFlow,usaCompany");
        }
        else {
            return array.fromStringa("type,release,giorno,titolo,company,descrizione,vaadFlow");
        }
    }

}// end of Route class