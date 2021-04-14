package it.algos.vaadflow14.backend.data;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;
import java.util.function.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 20-ott-2018
 * Time: 08:53
 * <p>
 * Poiché siamo in fase di boot, la sessione non esiste ancora <br>
 * Questo vuol dire che eventuali classi @VaadinSessionScope
 * NON possono essere iniettate automaticamente da Spring <br>
 * Vengono costruite con la BeanFactory <br>
 * <p>
 * Superclasse astratta per la costruzione iniziale delle Collections <br>
 * Viene invocata PRIMA della chiamata del browser, tramite il <br>
 * metodo FlowBoot.onContextRefreshEvent() <br>
 * Crea i dati di alcune collections sul DB mongo <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) <br>
 *
 * @since java 8
 */
@SpringComponent
@Qualifier(TAG_FLOW_DATA)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@AIScript(sovraScrivibile = false)
public class FlowData implements AIData {

    /**
     * Messaggio di errore <br>
     *
     * @since java 8
     */
    public Runnable mancaPrefLogic = () -> System.out.println("Non ho trovato la classe PreferenzaLogic");

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    protected AFileService file;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    protected ATextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    protected AClassService classService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    protected ALogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    protected AAnnotationService annotation;

    /**
     * Controlla che la classe sia una Entity <br>
     * Alcune entities possono non essere usate direttamente nel programma <br>
     * Nella costruzione del menu FlowBoot.fixMenuRoutes() due flags regolano <br>
     * quali entities facoltative mostrare nel menu <br>
     * <p>
     * Controlla il flag di ogni entity tra quelle facoltative <br>
     * Accetta solo quelle col flag positivo <br>
     * Le preferenze vengono gestite a parte in fixPreferenze() <br>
     */
    protected Predicate<String> checkEntity = canonicalName -> {
        String className = file.estraeClasseFinale(canonicalName);
        String simpleName = file.estraeClasseFinale(canonicalName).toLowerCase();

        if (simpleName.equals(Preferenza.class.getSimpleName().toLowerCase())) {
            return false;
        }

//        if (!FlowVar.usaCronoPackages && AECrono.getValue().contains(simpleName)) {
//            return false;
//        }
//
//        if (!FlowVar.usaGeografiaPackages && AEGeografia.getValue().contains(simpleName)) {
//            return false;
//        }

        return annotation.isEntityClass(canonicalName);
    };

    /**
     * Controllo la singola collezione <br>
     * <p>
     * Costruisco un' istanza della classe xxxService corrispondente alla entityClazz <br>
     * Controllo se l' istanza xxxService è creabile <br>
     * Un package standard contiene sia xxxService che xxxLogic <br>
     * Controllo se esiste un metodo resetEmptyOnly() nella classe xxxService specifica <br>
     * Invoco il metodo API resetEmptyOnly() della interfaccia AIService <br>
     */
    protected Consumer<Object> resetEmptyOnly = canonicalEntity -> {
        final AIResult result;
        final String canonicalEntityName = (String) canonicalEntity;
        final String canonicaName = canonicalEntityName.endsWith(SUFFIX_ENTITY) ? text.levaCoda(canonicalEntityName, SUFFIX_ENTITY) : canonicalEntityName;
        final String classeFinale = file.estraeClasseFinale(canonicaName);
        final String entityServicePrevista = classeFinale + SUFFIX_SERVICE;
        final AIService entityService = classService.getServiceFromEntityName(canonicalEntityName);
        final String packageName = file.estraeClasseFinale(canonicaName).toLowerCase();
        final String nameService;
        boolean methodExists = false;
        String message;

        if (entityService == null) {
            message = String.format("Nel package %s manca la entityService specifica e non sono neanche riuscito a creare la EntityService generica", packageName);
            logger.log(AETypeLog.checkData, message);
            return;
        }

        nameService = entityService.getClass().getSimpleName();
        if (nameService.equals(TAG_GENERIC_SERVICE)) {
            message = String.format("Nel package %s non esiste la classe %s e usa EntityService. Non esiste il metodo resetEmptyOnly()", packageName, entityServicePrevista);
            logger.log(AETypeLog.checkData, message);
            return;
        }

        try {
            methodExists = !packageName.equals(TAG_GENERIC_SERVICE) && entityService.getClass().getDeclaredMethod("resetEmptyOnly") != null;
        } catch (Exception unErrore) {
        }

        if (methodExists) {
            result = entityService.resetEmptyOnly();
            logger.log(AETypeLog.checkData, result.getMessage());
        }
        else {
            if (!nameService.equals(TAG_GENERIC_SERVICE)) {
                message = String.format("Nel package %s la classe %s non ha il metodo resetEmptyOnly() ", packageName, entityServicePrevista);
                logger.log(AETypeLog.checkData, message);
            }
        }
    };

    /**
     * Alcune entities possono non essere usate direttamente nel programma <br>
     * Nella costruzione del menu FlowBoot.fixMenuRoutes() due flags regolano <br>
     * quali entities facoltative mostrare nel menu <br>
     * <p>
     * Controlla i flags di tutte le entities <br>
     * Accetta solo quelle col flag positivo <br>
     *
     * @since java 8
     */
    protected Function<List<String>, Long> checkFiles = listaCanonicalNamesAllEntity -> {
        return listaCanonicalNamesAllEntity.stream()
                .filter(checkEntity)
                .count();
    };

    /**
     * Constructor with @Autowired on setter. Usato quando ci sono sottoclassi. <br>
     * Per evitare di avere nel costruttore tutte le property che devono essere iniettate e per poterle aumentare <br>
     * senza dover modificare i costruttori delle sottoclassi, l'iniezione tramite @Autowired <br>
     * viene delegata ad alcuni metodi setter() che vengono qui invocati con valore (ancora) nullo. <br>
     * Al termine del ciclo init() del costruttore il framework SpringBoot/Vaadin, inietterà la relativa istanza <br>
     */
    public FlowData() {
        this.setFile(file);
        this.setText(text);
        this.setClassService(classService);
        this.setLogger(logger);
        this.setAnnotation(annotation);
    }// end of constructor with @Autowired on setter


    /**
     * Check iniziale di alcune collections <br>
     * Controlla se le collections sono vuote e, nel caso, le ricrea <br>
     * Vengono create se mancano e se esiste un metodo resetEmptyOnly() nella classe xxxLogic specifica <br>
     * Crea un elenco di entities/collections che implementano il metodo resetEmptyOnly() <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * L' ordine con cui vengono create le collections è significativo <br>
     *
     * @since java 8
     */
    public void fixData() {
        this.fixData("vaadflow14");
    }


    /**
     * Check iniziale di alcune collections <br>
     * Controlla se le collections sono vuote e, nel caso, le ricrea <br>
     * Vengono create se mancano e se esiste un metodo resetEmptyOnly() nella classe xxxLogic specifica <br>
     * Crea un elenco di entities/collections che implementano il metodo resetEmptyOnly() <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * L' ordine con cui vengono create le collections è significativo <br>
     *
     * @param moduleName da controllare
     *
     * @since java 8
     */
    protected void fixData(final String moduleName) {
        List<Object> allEntities;
        List<String> allEntitiesGrezze;
        long entities;
        String message;
        Object[] matrice = null;

        //--spazzola tutta la directory packages
        allEntitiesGrezze = file.getModuleSubFilesEntity(moduleName);

        //--conta le collections valide
        //--inutile. Superato dopo aver diviso in due passaggi la successiva elaborazione
        //--prima checkEntity (filter=selezione) e poi resetEmptyOnly (forEach=elaborazione)
        //--potrebbero anche andare insieme
        //        entities = checkFiles.apply(allEntitiesGrezze);
        //        message = String.format("In %s sono stati trovati %d packages con classi di tipo AEntity da controllare", moduleName, entities);
        //        logger.log(AETypeLog.checkData, message);

        //--seleziona le collections valide
        //--prima checkEntity (filter=selezione)
        logger.log(AETypeLog.checkData, VUOTA);
        allEntities = Arrays.asList(allEntitiesGrezze.stream().filter(checkEntity).sorted().toArray());
        message = String.format("In %s sono stati trovati %d packages con classi di tipo AEntity da controllare", moduleName, allEntities.size());
        logger.log(AETypeLog.checkData, message);

        //--elabora le collections valide
        //--poi resetEmptyOnly (forEach=elaborazione)
        allEntities.stream().forEach(resetEmptyOnly);
        message = String.format("Controllati i dati iniziali di %s", moduleName);
        logger.log(AETypeLog.checkData, message);
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    void setFile(final AFileService file) {
        this.file = file;
    }


    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    void setText(final ATextService text) {
        this.text = text;
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    void setClassService(final AClassService classService) {
        this.classService = classService;
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    void setLogger(final ALogService logger) {
        this.logger = logger;
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    void setAnnotation(final AAnnotationService annotation) {
        this.annotation = annotation;
    }

}
