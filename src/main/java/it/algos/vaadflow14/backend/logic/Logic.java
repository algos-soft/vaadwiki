package it.algos.vaadflow14.backend.logic;

import com.vaadin.flow.component.*;
import com.vaadin.flow.router.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.ui.interfaces.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 01-mar-2021
 * Time: 21:29
 */
public abstract class Logic extends LogicProperty implements AILogic, HasUrlParameter<String>, BeforeEnterObserver {


    /**
     * Regola i parametri del browser per una view costruita da @Route <br>
     * Usato per costruire GenericLogicList e GenericLogicForm <br>
     * Se c'è solo il primo segmento, routeParameter NON è valido (non serve) <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l' interfaccia HasUrlParameter <br>
     * Chiamato DOPO @PostConstruct ma PRIMA di beforeEnter() <br>
     *
     * @param beforeEvent        con stringa del browser (bodyTextUTF8) da decodificare: primoSegmento e queryParameters
     * @param parametroOpzionale (poco usato) eventualmente presente DOPO il primoSegmento dell'URL e DOPO lo slash
     */
    @Override
    public void setParameter(final BeforeEvent beforeEvent, @OptionalParameter String parametroOpzionale) {
        //--routeParameter contiene sia il primoSegmento sia multiParametersMap sia singleParameter
        routeParameter = route.estraeParametri(beforeEvent, parametroOpzionale);

        //--Regola le property indispensabili per gestire questa view
        //--Se c'è solo il primo segmento, non serve regolare le property (probabilmente perché siamo in una sottoclasse specifica)
        if (routeParameter != null && routeParameter.getTypeParam() != AETypeParam.segmentOnly) {
            fixProperty();
        }
    }

    /**
     * Regola alcune property indispensabili per gestire questa view <br>
     * Possono provenire da una sottoclasse concreta oppure dai parametri del browser <br>
     * <p>
     * Se arriva qui da una sottoclasse, vuol dire:
     * - la @Route NON è ROUTE_NAME_GENERIC_VIEW
     * - che è stata chiamata una @Route di una classe tipo xxxList
     * - la stringa del browser NON ha parametri
     * - le property sono già regolate
     * <p>
     * Se arriva qui da una view generica, vuol dire:
     * - la @Route DEVE essere ROUTE_NAME_GENERIC_VIEW
     * - la stringa del browser DEVE avere dei parametri
     * - le property devono essere regolate
     * <p>
     * Questa classe, chiamata da @Route è praticamente un prototype <br>
     * le property vanno pertanto azzera ad ogni utilizzo <br>
     * <p>
     * The entityClazz obbligatorio di tipo AEntity, per la Grid <br>
     * The entityService obbligatorio, singleton di tipo xxxService <br>
     * The entityBean obbligatorio, istanza di entityClazz per il Form <br>
     */
    protected void fixProperty() {
        super.fixProperty();

        this.fixTypeView();
        this.fixEntityClazz();
        this.fixEntityService();
        this.fixEntityBean();
    }


    /**
     * Property per il tipo di view (List o Form) <br>
     * Property per il tipo di operazione (solo Form) <br>
     * Può essere sovrascritto, SENZA invocare prima il metodo della superclasse <br>
     */
    protected void fixTypeView() {
    }


    /**
     * Controlla che esista il riferimento alla entityClazz <br>
     * Se non esiste nella List, è un errore <br>
     * Se non esiste nel Form, lo crea dall'url del browser <br>
     * Deve essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    protected void fixEntityClazz() {
        String canonicalName;

        if (entityClazz == null) {
            canonicalName = routeParameter.get(KEY_BEAN_CLASS);
            try {
                entityClazz = (Class<AEntity>) Class.forName(canonicalName);
            } catch (Exception unErrore) {
                logger.error("Non sono riuscito a creare la entityClazz", Logic.class, "fixEntityClazz");
            }
        }
    }


    /**
     * Controlla che esista il riferimento alla classe entityService <br>
     * Se non esiste, lo crea <br>
     */
    protected void fixEntityService() {
        if (entityService == null) {
            entityService = classService.getServiceFromEntityClazz(entityClazz);
        }
    }


    /**
     *
     */
    protected void fixEntityBean() {
        String keyID = routeParameter.get(KEY_BEAN_ENTITY) != null ? routeParameter.get(KEY_BEAN_ENTITY) : VUOTA;

        if (text.isEmpty(keyID) || keyID.equals(KEY_NULL)) {
            entityBean = entityService.newEntity();
        }
        else {
            entityBean = entityService.findById(keyID);
        }
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.fixEntityClazz();
        this.fixEntityService();
        //        this.fixLogicForm();
        this.fixPreferenze();
        this.initView();
    }


    /**
     * Qui va tutta la logica iniziale della view <br>
     */
    protected void initView() {
        //--Costruisce gli oggetti base (placeholder) di questa view
        this.fixLayout();

        //--Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
        this.fixAlertLayout();

        //--Costruisce un layout (obbligatorio per la List) per i bottoni di comando della view al Top <br>
        //--Eventualmente i bottoni potrebbero andare su due righe <br>
        this.fixTopLayout();

        //--Corpo principale della Grid/Form (obbligatorio) <br>
        this.fixBodyLayout();

        //--Costruisce un layout (obbligatorio per il Form) per i bottoni di comando della view al Bottom <br>
        this.fixBottomLayout();

        //--Scritte in basso al video (eventuale) nel Footer <br>
        this.fixFooterLayout();

        //--Aggiunge i 5 oggetti base (placeholder) alla view, se sono utilizzati <br>
        this.addToLayout();

        //--Regolazioni finali di alcuni oggetti <br>
        this.regolazioniFinali();
    }



    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Interfaccia utilizzata come parametro per poter sovrascrivere il metodo <br>
     * Nella classe base eseguirà un casting a AEAction <br>
     * Nella (eventuale) sottoclasse specifica del progetto eseguirà un casting a AExxxAction <br>
     *
     * @param iAzione interfaccia dell'azione selezionata da eseguire
     *
     * @return false se il parametro non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(final AIAction iAzione) {
        return false;
    }

    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Interfaccia utilizzata come parametro per poter sovrascrivere il metodo <br>
     * Nella classe base eseguirà un casting a AEAction <br>
     * Nella (eventuale) sottoclasse specifica del progetto eseguirà un casting a AExxxAction <br>
     *
     * @param iAzione    interfaccia dell'azione selezionata da eseguire
     * @param entityBean selezionata
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(final AIAction iAzione, final AEntity entityBean) {
        return false;
    }

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        return false;
    }

    /**
     *
     */
    @Override
    public List<String> getGridColumns() {
        return null;
    }

    protected void executeRoute() {
        executeRoute(VUOTA);
    }

    /**
     * Lancia una @route con la visualizzazione di una singola scheda. <br>
     * Se il package usaSpostamentoTraSchede=true, costruisce una query
     * con le keyIDs della scheda precedente e di quella successiva
     * (calcolate secondo l'ordinamento previsto) <br>
     */
    protected void executeRoute(final String entityBeanID) {
    }


    /**
     * Lancia una @route con la visualizzazione di una singola scheda. <br>
     * Se il package usaSpostamentoTraSchede=true, costruisce una query
     * con le keyIDs della scheda precedente e di quella successiva
     * (calcolate secondo l'ordinamento previsto) <br>
     */
    protected void executeRoute(final String entityBeanID, final String entityBeanPrevID, final String entityBeanNextID) {
        final QueryParameters query;

        if (annotation.usaSpostamentoTraSchede(entityClazz)) {
            query = route.getQueryForm(entityClazz, operationForm, entityBeanID, entityBeanPrevID, entityBeanNextID);
        }
        else {
            query = route.getQueryForm(entityClazz, operationForm, entityBeanID);
        }

        UI.getCurrent().navigate(routeNameForm, query);
    }


    /**
     * Apre una pagina di wikipedia. <br>
     */
    protected final void openWikiPage(String wikiTitle) {
        String link = "\"" + PATH_WIKI + wikiTitle + "\"";
        if (text.isValid(wikiTitle)) {
            UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
        }
        else {
            logger.info("Manca il valore di wikiPageTitle", this.getClass(), "openWikiPage");
        }
    }

}
