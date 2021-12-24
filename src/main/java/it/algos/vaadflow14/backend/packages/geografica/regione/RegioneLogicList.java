package it.algos.vaadflow14.backend.packages.geografica.regione;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;

import javax.management.*;
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
@Route(value = "regione", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class RegioneLogicList extends LogicList {


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
    public StatoService statoService;


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
    public RegioneLogicList(@Autowired @Qualifier("regioneService") final AIService entityService) {
        super(entityService, Regione.class);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottonePaginaWiki = true;
        super.usaBottoneSearch = true;
        super.wikiPageTitle = "ISO_3166-2";
        this.maxNumeroBottoniPrimaRiga = 3;
    }


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertList() {
        addSpanBlu("Suddivisioni geografica di secondo livello. Codifica secondo ISO 3166-2");
        addSpanBlu("Codice ISO, sigla abituale e 'status' normativo");
        addSpanBlu("Ordinamento alfabetico: prima Italia poi altri stati europei");
    }


    /**
     * Costruisce una mappa di ComboBox da usare nel wrapper WrapTop <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected Map<String, ComboBox> getMappaComboBox() {
        Map<String, ComboBox> mappa = super.getMappaComboBox();
        ComboBox combo = null;

        if (AEPreferenza.usaBandiereStati.is()) {
            try {
                combo = getComboBox("stato", statoService.creaComboStati());
            } catch (AlgosException unErrore) {
                logger.warn(unErrore, this.getClass(), "getMappaComboBox");
            }

            mappa.put("stato", combo);
        }
        else {
            try {
                combo = getComboBox("stato", AEStato.italia.getStato());
            } catch (AlgosException unErrore) {
                logger.warn(unErrore, this.getClass(), "getMappaComboBox");
            }
            mappa.put("stato", combo);
        }

        try {
            combo = getComboBox("status");
        } catch (AlgosException unErrore) {
            logger.warn(unErrore, this.getClass(), "getMappaComboBox");
        }
        mappa.put("status", combo);

        return mappa;
    }

}// end of Route class