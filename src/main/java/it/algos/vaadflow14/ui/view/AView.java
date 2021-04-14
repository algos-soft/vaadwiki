package it.algos.vaadflow14.ui.view;

import com.vaadin.flow.router.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.footer.*;
import it.algos.vaadflow14.ui.service.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: ven, 01-mag-2020
 * Time: 09:45
 * <p>
 * Superclasse astratta di tutte le classi che hanno un'annotation @Route. <br>
 * Funziona come contenitore con un drawer, un menu, un 'container' ed un footer <br>
 * Il 'container' può ospitare: <br>
 * AViewList <br>
 * AViewForm <br>
 * CRUD View <br>
 * AViewGeneric <br>
 * <p>
 * Contiene la 'ui logic'. <br>
 * Nella superclasse AViewProperty vengono riportate, per comodità, le properties ed i link ai services <br>
 */
//@Route(value = ROUTE_NAME_GENERIC_VIEW, layout = MainLayout.class)
public  class AView extends AViewProperty implements  HasUrlParameter<String>, BeforeEnterObserver {

    protected Parametro routeParameter;


    /**
     * Regola i parametri del browser per una view costruita da @Route <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l' interfaccia HasUrlParameter <br>
     * Chiamato DOPO @PostConstruct ma PRIMA di beforeEnter() <br>
     *
     * @param beforeEvent  con la location, ui, navigationTarget, source, ecc
     * @param bodyTextUTF8 stringa del browser da decodificare
     */
    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String bodyTextUTF8) {
        routeParameter = route.estraeParametri(beforeEvent, bodyTextUTF8);

        //--Regola le property indispensabili per gestire questa view
//        if (routeParameter != null && routeParameter.isValido()) {
//            fixProperty();
//        }
    }


    /**
     * Regola le property indispensabili per gestire questa view <br>
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
     * Questa classe, chiamata da @Route è praticamente un Singleton <br>
     * le property vanno pertanto azzera ad ogni utilizzo <br>
     * <p>
     * The entityClazz obbligatorio di tipo AEntity, per la Grid <br>
     * The entityService obbligatorio, singleton di tipo xxxService <br>
     * The entityLogic obbligatorio, prototype di tipo xxxLogic <br>
     * The entityBean obbligatorio, istanza di entityClazz per il Form <br>
     */
    public void fixProperty() {
        super.fixProperty();

        if (routeParameter == null && annotation.getRouteName(this.getClass()).equals(ROUTE_NAME_GENERIC_VIEW)) {
            logger.error("Qualcosa non quadra", AView.class, "fixProperty");
        }

        this.fixTypeView();
        this.fixEntityClazz();
        this.fixEntityService();
        this.fixEntityLogic();
        this.fixEntityBean();

        //        if (annotation.getRouteName(this.getClass()).equals(ROUTE_NAME_GENERIC_VIEW)) {
        //            this.fixPropertyGenericView(parametro);
        //            //        } else {
        //            //            fixPropertySpecificView();
        //        }
    }


    /**
     * La @Route proviene da una classe qualsiasi che NON ha le properties corrette. <br>
     * Occorre ricostruirle partendo dalla AEntity di base <br>
     */
    @Deprecated
    protected void fixPropertyGenericView() {
    }


    /**
     *
     */
    protected void fixTypeView() {
        String typeVistaTxt;
        String operationTxt;

        if (routeParameter != null && text.isValid(routeParameter.getPrimoSegmento())) {
            typeVistaTxt = routeParameter.getPrimoSegmento();
            if (text.isValid(typeVistaTxt)) {
                typeVista = AEVista.valueOf(typeVistaTxt);
            }
        }

        //--usata solo in AViewForm
//        if (routeParameter != null && text.isValid(routeParameter.get(KEY_FORM_TYPE))) {
//            operationTxt = routeParameter.get(KEY_FORM_TYPE);
//            if (text.isValid(operationTxt)) {
//                operationForm = AEOperation.valueOf(operationTxt);
//            }
//        }

    }


    /**
     * Controlla che esista il riferimento alla entityClazz <br>
     * Se non esiste, lo crea <br>
     */
    protected void fixEntityClazz() {
        String canonicalName;

        if (entityClazz == null) {
            canonicalName = routeParameter.get(KEY_BEAN_CLASS);
            try {
                entityClazz = (Class<AEntity>) Class.forName(canonicalName);
            } catch (Exception unErrore) {
                logger.error("Non sono riuscito a creare la entityClazz", AView.class, "fixEntityClazz");
            }
        }
        else {
            logger.info("Esisteva già la entityClazz", AView.class, "fixEntityClazz");
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
        else {
            logger.info("Esisteva già la entityService", AView.class, "fixEntityService");
        }
    }

    /**
     * Controlla che esista il riferimento alla classe entityLogic <br>
     * Se non esiste, lo crea <br>
     */
    protected void fixEntityLogic() {
        if (entityLogic == null) {
            entityLogic = classService.getLogicListFromEntityClazz(entityClazz, entityService, operationForm);
        }
        else {
            logger.info("Esisteva già la entityLogic", AView.class, "fixEntityLogic");
        }
    }


    /**
     *
     */
    protected void fixEntityBean() {
    }


    /**
     * Creazione iniziale (business logic) della view DOPO costruttore, init(), postConstruct() e setParameter() <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l' interfaccia BeforeEnterObserver <br>
     * Chiamato DOPO @PostConstruct e DOPO setParameter() <br>
     * Le property necessarie sono già state regolate <br>
     *
     * @param beforeEnterEvent con la location, ui, navigationTarget, source, ecc
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (entityClazz == null || entityService == null || entityLogic == null) {
            if (entityClazz == null) {
                logger.error("Manca la entityClazz", AView.class, "beforeEnter");
            }
            if (entityService == null) {
                logger.error("Manca la entityService", AView.class, "beforeEnter");
            }
            if (entityLogic == null) {
                logger.error("Manca la entityLogic", AView.class, "beforeEnter");
            }
            return;
        }

        this.initView();
    }


    /**
     * Qui va tutta la logica iniziale della view <br>
     */
    protected void initView() {
        //--Login and context della sessione
        //        this.mongo = appContext.getBean(AMongoService.class);
        //        context = vaadinService.getSessionContext();
        //        login = context != null ? context.getLogin() : null;

        //--se il login è obbligatorio e manca, la View non funziona
        //        if (vaadinService.mancaLoginSeObbligatorio()) {
        //            return;
        //        }// end of if cycle

        //--Costruisce gli oggetti base (placeholder) di questa view
        super.fixLayout();

        //--Costruisce un (eventuale) layout per informazioni aggiuntive come header della view
        this.fixAlertLayout();

        //--Costruisce un layout (obbligatorio) per i menu ed i bottoni di comando della view
        this.fixTopLayout();

        //--body con la Grid oppure il Form o qualsiasi altro componente grafico
        this.fixBodyLayout();

        //--eventuale barra di bottoni in basso
        this.fixBottomLayout();

        //--aggiunge il footer standard
        this.fixFooterLayout();
    }// end of method


    /**
     * Costruisce un (eventuale) layout per avvisi aggiuntivi in alertPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Normalmente ad uso esclusivo del developer <br>
     * Nell' implementazione standard di default NON presenta nessun avviso <br>
     * Recupera dal service specifico gli (eventuali) avvisi <br>
     * Costruisce un' istanza dedicata con le liste di avvisi <br>
     * Gli avvisi sono realizzati con label differenziate per colore in base all'utente collegato <br>
     * Se l' applicazione non usa security, il colore è unico <br<
     * Se esiste, inserisce l' istanza (grafica) in alertPlacehorder della view <br>
     * alertPlacehorder viene sempre aggiunto, per poter (eventualmente) essere utilizzato dalle sottoclassi <br>
     */
    protected void fixAlertLayout() {
//        AHeader header = entityLogic != null ? entityLogic.getAlertHeaderLayout(typeVista) : null;//@todo implementare
//        AIHeader header2 = entityLogic != null ? entityLogic.getAlertLayout(typeVista) : null;//@todo implementare
//
//        if (alertPlaceholder != null && header != null) {
//            alertPlaceholder.add(header);
//            this.add(alertPlaceholder);
//        }
//
//        if ( header2 == null) {
//            header2= null;
//        }
//
//        if (alertPlaceholder != null && header2 != null) {
//            alertPlaceholder.add(header2.get());
//            this.add(alertPlaceholder);
//        }
    }


    /**
     * Costruisce i bottoni di comando della view (List) <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * L'istanza di ATopLayout viene costruita dal service 'entityLogic' <br>
     * L'istanza (grafica) viene inserita in topPlacehorder della view (List) <br>
     * Nell' implementazione standard di default presenta solo il bottone 'New' <br>
     */
    protected void fixTopLayout() {
//        AButtonLayout topLayout = entityLogic != null ? entityLogic.getTopLayout() : null;
//
//        if (topPlaceholder != null && topLayout != null) {
//            topPlaceholder.add(topLayout);
//            this.add(topPlaceholder);
//        }
    }


    /**
     * Costruisce il 'corpo' centrale della view <br>
     * <p>
     * Differenziato tra AViewList e AViewForm <br>
     * Costruisce un' istanza dedicata <br>
     * Inserisce l' istanza (grafica) in bodyPlacehorder della view <br>
     */
    public void fixBodyLayout() {
    }


    /**
     * Costruisce un layout per i bottoni di comando in bottomPlaceholder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Tipicamente usato SOLO nel Form <br>
     * Nell'implementazione standard di default presenta solo il bottone 'New' <br>
     * Recupera dalla logica specifica i menu/bottoni previsti <br>
     * Costruisce un'istanza dedicata con i bottoni <br>
     * Inserisce l'istanza (grafica) in bottomPlacehorder della view <br>
     */
    protected void fixBottomLayout() {
//        ABottomLayout bottomLayout = entityLogic != null ? entityLogic.getBottomLayout(operationForm):null;
//
//        if (bottomPlaceholder != null && bottomLayout != null) {
//            bottomPlaceholder.add(bottomLayout);
//            this.add(bottomPlaceholder);
//        }
    }


    protected void fixFooterLayout() {
        footerPlaceholder.add(appContext.getBean(AFooter.class));
        this.add(footerPlaceholder);
    }

}
