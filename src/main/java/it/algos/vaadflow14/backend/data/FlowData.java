package it.algos.vaadflow14.backend.data;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.*;

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
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AMongoService mongo;

    /**
     * Controlla che la classe sia una Entity <br>
     */
    protected Predicate<String> checkEntity = canonicalName -> classService.isEntity(canonicalName);


    /**
     * Controlla che la Entity estenda AREntity <br>
     */
    protected Predicate<Object> checkResetEntity = clazzName -> classService.isResetEntity(clazzName.toString());


    /**
     * Controlla che la classe abbia usaBoot=true <br>
     */
    protected Predicate<Object> checkUsaBoot = clazzName -> annotation.usaBoot(clazzName.toString());


    /**
     * Controllo la singola collezione <br>
     * <p>
     * Costruisco un' istanza della classe xxxService corrispondente alla entityClazz <br>
     * Controllo se l' istanza xxxService è creabile <br>
     * Un package standard contiene sia xxxService che xxxLogicList <br>
     * Controllo se esiste un metodo resetEmptyOnly() nella classe xxxService specifica <br>
     * Invoco il metodo API resetEmptyOnly() della interfaccia AIService <br>
     */
    protected Consumer<Object> bootReset = canonicalEntity -> {
        final AIResult result;
        final String canonicalEntityName = (String) canonicalEntity;
        final String canonicaName = canonicalEntityName.endsWith(SUFFIX_ENTITY) ? text.levaCoda(canonicalEntityName, SUFFIX_ENTITY) : canonicalEntityName;
        final String classeFinale = file.estraeClasseFinale(canonicaName);
        final String entityServicePrevista = classeFinale + SUFFIX_SERVICE;
        final AIService entityService = classService.getServiceFromEntityName(canonicalEntityName);
        final String packageName = file.estraeClasseFinale(canonicaName).toLowerCase();
        final String nameService;
        String message;
        Class entityClazz = null;
        int numRec;
        String type;
//        String collectionName;
//        Query query = new Query();

        try {
            entityClazz = Class.forName(canonicalEntityName);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "bootReset");
        }

        if (entityService == null) {
            message = String.format("Nel package %s manca la entityService specifica e non sono neanche riuscito a creare la EntityService generica", packageName);
            logger.log(AETypeLog.checkData, message);
            return;
        }

        nameService = entityService.getClass().getSimpleName();
        if (nameService.equals(TAG_GENERIC_SERVICE)) {
            message = String.format("Nel package %s non esiste la classe %s e usa EntityService. Non esiste il metodo %s()", packageName, entityServicePrevista, TAG_METHOD_BOOT_RESET);
            logger.log(AETypeLog.checkData, message);
            return;
        }

         //--Controlla che esista il metodo reset() nella sottoclasse specifica xxxService <br>
         //--Altrimenti i dati non possono essere ri-creati <br>
        try {
            if (entityService.getClass().getDeclaredMethod(TAG_METHOD_RESET) == null) {
                return;
            }
        } catch (Exception unErrore) {
        }

        if (mongo.isResetVuoto(entityClazz)) {
            result = entityService.reset();
        }
        else {
            result = AResult.errato( mongo.countReset(entityClazz));
        }


        if (result != null) {
            numRec = result.getValore();
            type = result.getValidationMessage();
            if (result.isValido()) {
                message = String.format("Nel package %s sono stati inseriti %d elementi %s col metodo %s.reset() ", packageName, numRec, type, nameService);
            }
            else {
                message = String.format("Nel package %s esistevano %d elementi creati col metodo %s.reset() ", packageName, numRec, nameService);
            }
            logger.log(AETypeLog.checkData, message);
        }
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
     * Check iniziale. Ad ogni avvio del programma spazzola tutte le collections <br>
     * Ognuna viene ricreata (mantenendo le entities che hanno reset=false) se:
     * - xxx->@AIEntity usaBoot=true,
     * - esiste xxxService.reset(),
     * - la collezione non contiene nessuna entity che abbia la property reset=true
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * L' ordine con cui vengono create le collections è significativo <br>
     *
     * @since java 8
     */
    public void resetData() {
        this.resetData("vaadflow14");
    }


    /**
     * Check iniziale. Ad ogni avvio del programma spazzola tutte le collections <br>
     * Ognuna viene ricreata (mantenendo le entities che hanno reset=false) se:
     * - xxx->@AIEntity usaBoot=true,
     * - esiste xxxService.reset(),
     * - la collezione non contiene nessuna entity che abbia la property reset=true
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * L' ordine con cui vengono create le collections è significativo <br>
     *
     * @param moduleName da controllare
     *
     * @since java 8
     */
    protected void resetData(final String moduleName) {
        List<String> allModulePackagesClasses;
        List<Object> allEntityClasses;
        List<Object> allResetEntityClasses;
        List<Object> allResetUsaBootEntityClasses;
        String message;
        Object[] matrice = null;

        //--spazzola tutta la directory package del modulo in esame e recupera
        //--tutte le classi contenute nella directory e nelle sue sottoclassi
        allModulePackagesClasses = file.getModuleSubFilesEntity(moduleName);

        //--seleziona le classes che estendono AEntity
        logger.log(AETypeLog.checkData, VUOTA);
        allEntityClasses = Arrays.asList(allModulePackagesClasses.stream().filter(checkEntity).sorted().toArray());
        message = String.format("In %s sono stati trovati %d packages con classi di tipo AEntity", moduleName, allEntityClasses.size() + 1);
        logger.log(AETypeLog.checkData, message);

        //--seleziona le Entity classes che estendono AREntity
        allResetEntityClasses = Arrays.asList(allEntityClasses.stream().filter(checkResetEntity).sorted().toArray());
        message = String.format("In %s sono stati trovati %d packages con classi di tipo AREntity da controllare", moduleName, allResetEntityClasses.size() + 1);
        logger.log(AETypeLog.checkData, message);

        //--seleziona le REntity classes che hanno @AIEntity usaBoot=true
        allResetUsaBootEntityClasses = Arrays.asList(allResetEntityClasses.stream().filter(checkUsaBoot).sorted().toArray());
        message = String.format("In %s sono stati trovati %d packages con classi di tipo AREntity che hanno usaBoot=true", moduleName, allResetUsaBootEntityClasses.size() + 1);
        logger.log(AETypeLog.checkData, message);

        //--elabora le entity classes che estendono AREntity
        //--eseguendo xxxService.bootReset (forEach=elaborazione)
        allResetUsaBootEntityClasses.stream().forEach(bootReset);
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
