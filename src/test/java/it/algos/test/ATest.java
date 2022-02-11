package it.algos.test;

import com.mongodb.*;
import com.mongodb.client.*;
import com.vaadin.flow.data.provider.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.service.*;
import it.algos.vaadflow14.wiki.*;
import org.bson.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;
import java.lang.reflect.Field;
import java.time.*;
import java.util.*;
import java.util.stream.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mar, 28-apr-2020
 * Time: 21:18
 * <p>
 * Classe astratta per i test <br>
 *
 * @see https://www.baeldung.com/parameterized-tests-junit-5
 */
@AIScript(sovraScrivibile = false)
public abstract class ATest {


    /**
     * The constant PIENA.
     */
    protected static final String PIENA = "Piena";

    protected static final String CONTENUTO = "contenuto";

    protected static final String CONTENUTO_DUE = "mariolino";

    /**
     * The constant ARRAY_STRING.
     */
    protected static final String[] ARRAY_SHORT_STRING = {CONTENUTO};

    /**
     * The constant LIST_STRING.
     */
    protected static final List<String> LIST_SHORT_STRING = new ArrayList(Arrays.asList(ARRAY_SHORT_STRING));

    protected static final String[] ARRAY_SHORT_STRING_DUE = {CONTENUTO_DUE};

    protected static final List<String> LIST_SHORT_STRING_DUE = new ArrayList(Arrays.asList(ARRAY_SHORT_STRING_DUE));

    protected final static String FIELD_NAME_NOTE = "note";

    protected final static String FIELD_NAME_ORDINE = "ordine";

    protected final static String FIELD_NAME_CODE = "code";

    protected final static String HEADER_ORDINE = "#";

    protected final static String HEADER_CODE = "code";

    protected final static String HEADER_NOME = "nome";

    protected final static String NAME_ORDINE = "ordine";

    protected final static String NAME_CODE = "code";

    protected final static String NAME_NOME = "nome";

    protected final static String NAME_ANNO = "anno";

    protected static final int WIDTH = 160;

    protected static final int WIDTH_WRAP = 40;

    protected static final String PAGINA_PIOZZANO = "Piozzano";

    protected static final String PAGINA_TEST = "Utente:Gac/T17";

    protected static final String PAGINA_NO_ASCI = "Roman Protasevič";

    protected static final String PAGINA_INESISTENTE = "Roman Protellino";

    protected static final String PAGINA_DISAMBIGUA = "Rossi";

    protected static final String PAGINA_REDIRECT = "Regno di Napoli (1805-1815)";

    protected static final String DATA_BASE_NAME = "vaadflow14";

    protected static Class<? extends AEntity> VIA_ENTITY_CLASS = Via.class;

    protected static Class<? extends AEntity> ANNO_ENTITY_CLASS = Anno.class;

    protected static Class<? extends AEntity> MESE_ENTITY_CLASS = Mese.class;

    protected static Class<? extends AEntity> GIORNO_ENTITY_CLASS = Giorno.class;

    protected static Class<? extends AEntity> COMPANY_ENTITY_CLASS = Company.class;

    protected static Class<? extends AEntity> STATO_ENTITY_CLASS = Stato.class;

    protected static Class ANNO_LOGIC_LIST = AnnoLogicList.class;

    protected static Class VIA_LIST_CLASS = ViaLogicList.class;

    protected static Class VIA_SERVICE_CLASS = ViaService.class;

    protected static Field FIELD_ORDINE;

    protected static Field FIELD_NOME;

    protected static String PATH = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard/";

    //    /**
    //     * The App context.
    //     */
    //    @Mock
    //    protected GenericApplicationContext appContext;


    @InjectMocks
    protected StaticContextAccessor staticContextAccessor;

    @InjectMocks
    protected TextService textService;

    @InjectMocks
    protected ArrayService arrayService;

    @InjectMocks
    protected DateService dateService;

    @InjectMocks
    protected AnnotationService annotationService;

    @InjectMocks
    protected ReflectionService reflectionService;

    @InjectMocks
    protected ALogService loggerService;

    @InjectMocks
    protected BeanService beanService;

    //    @Mock
    //    protected MongoTemplate mongoTemplate;

    @InjectMocks
    protected MongoService mongoServiceImpl;

    protected AIMongoService mongoService;

    protected MongoOperations mongoOp;

    @InjectMocks
    protected WebService webService;

    @InjectMocks
    protected AWikiApiService wikiApiService;

    @InjectMocks
    protected GeograficService geograficService;

    @InjectMocks
    protected FileService fileService;

    @InjectMocks
    protected MathService mathService;

    @InjectMocks
    protected GsonService gSonService;

    @InjectMocks
    protected ClassService classService;

    @InjectMocks
    protected UtilityService utilityService;

    @InjectMocks
    protected HtmlService htmlService;

    @InjectMocks
    protected PreferenzaService preferenzaService;

    @InjectMocks
    protected JSonService jSonService;

    @InjectMocks
    protected EnumerationService enumerationService;

    @InjectMocks
    protected ResourceService resourceService;

    @InjectMocks
    protected AFieldService fieldService;

    @InjectMocks
    protected CompanyService companyService;

    @Autowired
    protected FlowVar flowVar;

    @InjectMocks
    protected WrapFiltri wrapFiltri;

    protected Logger adminLogger;

    protected boolean previstoBooleano;

    protected boolean ottenutoBooleano;

    protected String sorgente;

    protected String sorgente2;

    protected String sorgente3;

    protected String previsto;

    protected String previsto2;

    protected String previsto3;

    protected String ottenuto;

    protected String ottenuto2;

    protected Class sorgenteClasse;

    protected Field sorgenteField;

    protected Field ottenutoField;

    protected Class previstoClasse;

    protected Class ottenutoClasse;

    protected LocalDateTime previstoDataTime;

    protected LocalDateTime ottenutoDataTime;

    protected LocalDate previstoData;

    protected LocalDate ottenutoData;

    protected LocalTime previstoOrario;

    protected LocalTime ottenutoOrario;

    protected List<String> sorgenteArray;

    protected List<String> previstoArray;

    protected List<String> ottenutoArray;

    protected List<Long> sorgenteArrayLong;

    protected List<Long> previstoArrayLong;

    protected List<Long> ottenutoArrayLong;

    protected List<Integer> previstoInteroArray;

    protected List<Integer> ottenutoInteroArray;

    protected String[] stringArray;

    protected String[] sorgenteMatrice;

    protected String[] previstoMatrice;

    protected String[] ottenutoMatrice;

    protected Integer[] sorgenteInteroMatrice;

    protected Integer[] previstoInteroMatrice;

    protected Integer[] ottenutoInteroMatrice;

    protected Map<String, Object> mappa;

    protected Map<String, String> mappaSorgente;

    protected Map<String, String> mappaPrevista;

    protected Map<String, String> mappaOttenuta;

    protected String tag;

    protected Object obj;

    protected int sorgenteIntero;

    protected int previstoIntero;

    protected int ottenutoIntero;

    protected double previstoDouble = 0;

    protected double ottenutoDouble = 0;

    protected int dividendo;

    protected int divisore;

    protected Class<? extends AEntity> entityClazz;

    protected AEntity entityBean;

    protected Query query;

    protected Document doc;

    protected BasicDBObject objectQuery;

    protected Sort sortSpring;

    protected QuerySortOrder sortVaadin;

    protected AFiltro filtro;

    protected List<AFiltro> listaFiltri;

    protected Map<String, AFiltro> mappaFiltri;

    protected List<Field> listaFields;

    protected List<String> listaStr;

    protected List<AEntity> listaBean;

    protected byte[] bytes;

    protected Class clazz;

    protected long inizio;

    protected int cicli;

    protected int caratteriVisibili;

    protected AIResult previstoRisultato;

    protected AIResult ottenutoRisultato;

    protected List<List<String>> listaGrezza;

    protected List<WrapDueStringhe> listaWrap;

    protected List<WrapTreStringhe> listaWrapTre;

    protected List<WrapQuattro> listaWrapQuattro;

    protected WrapDueStringhe dueStringhe;

    protected WrapTreStringhe treStringhe;

    protected AETypeSerializing oldType;

    protected static String[] CANONICAL() {
        return new String[]{null, VUOTA, "CanonicalNameInesistente", VIA_ENTITY_CLASS.getCanonicalName(), VIA_ENTITY_CLASS.getCanonicalName() + JAVA_SUFFIX};
    }


    //--clazz
    //--esiste nel package
    protected static Stream<Arguments> SIMPLE() {
        return Stream.of(
                Arguments.of((Class) null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("NomeClasseInesistente", false),
                Arguments.of(VIA_ENTITY_CLASS.getSimpleName(), true),
                Arguments.of(VIA_ENTITY_CLASS.getSimpleName() + JAVA_SUFFIX, true),
                Arguments.of("via", true),
                Arguments.of("LogicList", false),
                Arguments.of("ViaLogicList", true),
                Arguments.of("AnnoLogicList", true)
        );
    }

    protected static String[] PATH() {
        return new String[]{null, VUOTA, "PathErrato", "/Users/gac/IdeaProjects/operativi/vaadwiki/src/main/java/backend/packages/anagrafica/via/Via", "/Users/gac/Documents/IdeaProjects/operativi/vaadwiki/src/main/java/it/algos/vaadflow14/backend/packages/anagrafica/via/Via", "/Users/gac/Documents/IdeaProjects/operativi/vaadwiki/src/main/java/it/algos/vaadflow14/backend/packages/anagrafica/via/Via.java"};
    }

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     */
    protected void setUpStartUp() {
        initMocks();
        fixRiferimentiIncrociati();

        FlowVar.projectNameDirectoryIdea = "vaadflow14";
        if (classService.getProjectName().equals(FlowVar.projectNameDirectoryIdea)) {
            FlowVar.projectNameModulo = "simple";
        }
        else {
            FlowVar.projectNameModulo = classService.getProjectName();
        }
    }


    /**
     * Inizializzazione dei service
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     */
    protected void initMocks() {
        MockitoAnnotations.initMocks(this);

        MockitoAnnotations.initMocks(staticContextAccessor);
        Assertions.assertNotNull(staticContextAccessor);
        staticContextAccessor.registerInstance();

        //        MockitoAnnotations.initMocks(appContext);
        //        Assertions.assertNotNull(appContext);

        MockitoAnnotations.initMocks(textService);
        Assertions.assertNotNull(textService);

        MockitoAnnotations.initMocks(arrayService);
        Assertions.assertNotNull(arrayService);

        MockitoAnnotations.initMocks(loggerService);
        Assertions.assertNotNull(loggerService);

        MockitoAnnotations.initMocks(dateService);
        Assertions.assertNotNull(dateService);

        adminLogger = LoggerFactory.getLogger("wam.admin");
        Assertions.assertNotNull(adminLogger);

        MockitoAnnotations.initMocks(annotationService);
        Assertions.assertNotNull(annotationService);

        MockitoAnnotations.initMocks(reflectionService);
        Assertions.assertNotNull(reflectionService);

        MockitoAnnotations.initMocks(beanService);
        Assertions.assertNotNull(beanService);

        MockitoAnnotations.initMocks(mongoServiceImpl);
        Assertions.assertNotNull(mongoServiceImpl);
        mongoService = mongoServiceImpl;

        //        MockitoAnnotations.initMocks(mongoTemplate);
        //        Assertions.assertNotNull(mongoTemplate);
        //        //        MockitoAnnotations.initMocks(mongoServiceImpl.mongoOp);
        //        //        Assertions.assertNotNull(mongoServiceImpl.mongoOp);
        //        mongoOp = mongoTemplate;
        //        mongoServiceImpl.mongoOp = mongoTemplate;

        MockitoAnnotations.initMocks(webService);
        Assertions.assertNotNull(webService);

        MockitoAnnotations.initMocks(wikiApiService);
        Assertions.assertNotNull(wikiApiService);

        MockitoAnnotations.initMocks(geograficService);
        Assertions.assertNotNull(geograficService);

        MockitoAnnotations.initMocks(fileService);
        Assertions.assertNotNull(fileService);

        MockitoAnnotations.initMocks(mathService);
        Assertions.assertNotNull(mathService);

        MockitoAnnotations.initMocks(gSonService);
        Assertions.assertNotNull(gSonService);

        MockitoAnnotations.initMocks(utilityService);
        Assertions.assertNotNull(utilityService);

        MockitoAnnotations.initMocks(htmlService);
        Assertions.assertNotNull(htmlService);

        MockitoAnnotations.initMocks(preferenzaService);
        Assertions.assertNotNull(preferenzaService);

        MockitoAnnotations.initMocks(jSonService);
        Assertions.assertNotNull(jSonService);

        MockitoAnnotations.initMocks(classService);
        Assertions.assertNotNull(classService);

        MockitoAnnotations.initMocks(enumerationService);
        Assertions.assertNotNull(enumerationService);

        MockitoAnnotations.initMocks(resourceService);
        Assertions.assertNotNull(resourceService);

        MockitoAnnotations.initMocks(fieldService);
        Assertions.assertNotNull(fieldService);

        MockitoAnnotations.initMocks(companyService);
        Assertions.assertNotNull(companyService);

        MockitoAnnotations.initMocks(wrapFiltri);
        Assertions.assertNotNull(wrapFiltri);
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito le referenze 'mockate' <br>
     * Nelle sottoclassi di testi devono essere regolati i riferimenti dei service specifici <br>
     */
    protected void fixRiferimentiIncrociati() {
        arrayService.text = textService;
        textService.array = arrayService;
        loggerService.text = textService;
        loggerService.adminLogger = adminLogger;
        annotationService.text = textService;
        annotationService.array = arrayService;
        annotationService.logger = loggerService;
        annotationService.reflection = reflectionService;
        annotationService.classService = classService;
        reflectionService.array = arrayService;
        reflectionService.text = textService;
        reflectionService.logger = loggerService;
        reflectionService.annotation = annotationService;
        reflectionService.classService = classService;
        gSonService.text = textService;
        gSonService.array = arrayService;
        jSonService.text = textService;
        jSonService.array = arrayService;
        beanService.mongo = mongoService;

        ((MongoService) mongoService).text = textService;
        ((MongoService) mongoService).array = arrayService;
        ((MongoService) mongoService).annotation = annotationService;
        ((MongoService) mongoService).reflection = reflectionService;
        ((MongoService) mongoService).logger = loggerService;
        ((MongoService) mongoService).date = dateService;
        ((MongoService) mongoService).classService = classService;
        ((MongoService) mongoService).fieldService = fieldService;
        ((MongoService) mongoService).fileService = fileService;

        webService.text = textService;
        webService.logger = loggerService;
        wikiApiService.text = textService;
        wikiApiService.web = webService;
        wikiApiService.logger = loggerService;
        wikiApiService.html = htmlService;
        fileService.text = textService;
        fileService.array = arrayService;
        fileService.logger = loggerService;
        fileService.math = mathService;
        sortSpring = null;

        classService.fileService = fileService;
        classService.text = textService;
        classService.logger = loggerService;
        classService.annotation = annotationService;
        classService.mongo = mongoService;

        //        classService.appContext = appContext;

        preferenzaService.mongo = mongoService;
        utilityService.text = textService;
        htmlService.text = textService;
        dateService.text = textService;
        ((MongoService) mongoService).gSonService = gSonService;
        gSonService.reflection = reflectionService;
        gSonService.annotation = annotationService;
        gSonService.logger = loggerService;
        gSonService.date = dateService;
        resourceService.fileService = fileService;
        resourceService.text = textService;
        utilityService.annotation = annotationService;
        wikiApiService.array = arrayService;
        enumerationService.text = textService;
        enumerationService.array = arrayService;
        dateService.math = mathService;

        ((MongoService) mongoService).fixProperties(classService.getProjectName());
        gSonService.fixProperties(classService.getProjectName());

        fieldService.annotation = annotationService;
        fieldService.array = arrayService;

        companyService.text = textService;
        companyService.logger = loggerService;
        companyService.annotation = annotationService;
        companyService.reflection = reflectionService;
        companyService.mongo = mongoService;
        companyService.beanService = beanService;

        wrapFiltri.text = textService;
        wrapFiltri.annotation = annotationService;
        wrapFiltri.reflection = reflectionService;

        try {
            FIELD_ORDINE = reflectionService.getField(VIA_ENTITY_CLASS, NAME_ORDINE);
            FIELD_NOME = reflectionService.getField(VIA_ENTITY_CLASS, NAME_NOME);
        } catch (AlgosException unErrore) {
            assertFalse(false);
        }
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     */
    protected void setUp() {
        inizio = System.currentTimeMillis();
        cicli = 1;
        ottenutoIntero = 0;
        caratteriVisibili = 80;
        previstoBooleano = false;
        ottenutoBooleano = false;
        sorgente = VUOTA;
        sorgente2 = VUOTA;
        ottenuto = VUOTA;
        previsto = VUOTA;
        previsto2 = VUOTA;
        previsto3 = VUOTA;
        previstoIntero = 0;
        sorgenteArray = null;
        previstoArray = null;
        ottenutoArray = null;
        sorgenteArrayLong = null;
        previstoArrayLong = null;
        ottenutoArrayLong = null;
        sorgenteClasse = null;
        sorgenteField = null;
        sorgenteMatrice = null;
        previstoMatrice = null;
        ottenutoMatrice = null;
        previstoInteroArray = null;
        ottenutoInteroArray = null;
        query = null;
        sortSpring = null;
        sortVaadin = null;
        filtro = null;
        listaFiltri = new ArrayList<>();
        mappa = new HashMap<>();
        listaBean = null;
        listaStr = null;
        listaFields = null;
        bytes = null;
        entityBean = null;
        clazz = null;
        previstoRisultato = null;
        ottenutoRisultato = null;
        listaStr = new ArrayList<>();
        wrapFiltri.entityClazz = null;
        mappaFiltri = null;
        wrapFiltri.setMappaFiltri(null);
        entityClazz = null;
    }

    protected String getTime() {
        return dateService.deltaTextEsatto(inizio);
    }

    protected void printOttenuto(final String sorgente, final String ottenuto) {
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }


    protected void printVuota(List<String> lista) {
        System.out.println(VUOTA);
        print(lista);
    }

    protected void print(List<String> lista) {
        if (lista != null && lista.size() > 0) {
            System.out.println(String.format("Ci sono %d elementi nella lista", lista.size()));
        }
        else {
            System.out.println("La lista è vuota");
        }
        System.out.println(VUOTA);
        if (arrayService.isAllValid(lista)) {
            for (String stringa : lista) {
                System.out.println(stringa);
            }
        }
    }


    protected void printList(List<List<String>> listaTable) {
        if (arrayService.isAllValid(listaTable)) {
            for (List<String> lista : listaTable) {
                System.out.println(VUOTA);
                if (arrayService.isAllValid(lista)) {
                    for (String stringa : lista) {
                        System.out.println(stringa);
                    }
                }
            }
        }
    }


    protected void printMappa(Map<String, List<String>> mappa) {
        List<String> lista;
        if (arrayService.isAllValid(mappa)) {
            for (String key : mappa.keySet()) {
                lista = mappa.get(key);
                System.out.println(VUOTA);
                if (arrayService.isAllValid(lista)) {
                    printVuota(lista);
                }
            }
        }
    }

    //    protected void printWrap(WrapPage wrap) {
    //        System.out.println(VUOTA);
    //        String message = VUOTA;
    //
    //        System.out.println(String.format("La query è: %s", wrap.getDomain()));
    //        System.out.println(String.format("Il title è: %s", wrap.getTitle()));
    //        if (wrap.isValida()) {
    //            System.out.println("Il wrap è valido");
    //            message = wrap.getType() == AETypePage.testoConTmpl ? "Usa solo il template come testo" : message;
    //            message = wrap.getType() == AETypePage.testoSenzaTmpl ? "Usa tutta la pagina come testo" : message;
    //            System.out.println(String.format("La pageid è: %s", wrap.getPageid()));
    //            System.out.println(String.format("Il timestamp è: %s", wrap.getTime()));
    //            System.out.println(message);
    //            if (wrap.getType() == AETypePage.testoConTmpl) {
    //                System.out.println(String.format("Il template è: %s", wrap.getTmpl().substring(0, Math.min(wrap.getTmpl().length(), WIDTH_WRAP))));
    //            }
    //            if (wrap.getType() == AETypePage.testoSenzaTmpl) {
    //                System.out.println(String.format("Il testo è: %s", wrap.getText().substring(0, Math.min(wrap.getText().length(), WIDTH_WRAP))));
    //            }
    //        }
    //        else {
    //            System.out.println("Il wrap non è valido");
    //            if (wrap.getType() == AETypePage.nonEsiste) {
    //                System.out.println("La pagina non esiste");
    //            }
    //            if (wrap.getType() == AETypePage.disambigua) {
    //                System.out.println("La pagina è una disambigua");
    //            }
    //            if (wrap.getType() == AETypePage.redirect) {
    //                System.out.println("La pagina è un redirect");
    //            }
    //        }
    //    }

    protected void printEntityBeanFromClazz(final String keyPropertyValue, final Class clazz, final AEntity entityBean) {
        if (clazz == null) {
            System.out.print("Non esiste la classe indicata");
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            return;
        }
        else {
            if (entityBean != null) {
                System.out.println(String.format("KeyId=%s: creata una entityBean (vuota) di classe %s%s%s", keyPropertyValue, clazz.getSimpleName(), FORWARD, entityBean));
            }
            else {
                System.out.println(String.format("KeyId=%s: non è stata creata nessuna entityBean di classe %s", keyPropertyValue, clazz.getSimpleName()));
            }
        }

        System.out.println(VUOTA);
    }


    protected void printEntityBeanFromProperty(final Class clazz, final String propertyName, final Serializable propertyValue, final AEntity entityBean, final int previstoIntero) {
        if (clazz == null) {
            System.out.print("Non esiste la classe indicata");
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            return;
        }
        else {
            System.out.print(String.format("%s%s%s: ", propertyName, UGUALE_SEMPLICE, propertyValue));

            if (entityBean != null) {
                if (entityBean.id != null && textService.isValid(entityBean.id)) {
                    System.out.println(String.format("recuperata una entityBean di classe %s%s%s", clazz.getSimpleName(), FORWARD, entityBean));
                    printMappa(entityBean);
                }
                else {
                    System.out.println(String.format("recuperata una entityBean (vuota) di classe %s%s%s", clazz.getSimpleName(), FORWARD, entityBean));
                    printMappa(entityBean);
                }
            }
            else {
                System.out.println(String.format("nel mongoDB non è stata trovata nessuna entityBean di classe %s", clazz.getSimpleName()));
            }
        }

        System.out.println(VUOTA);
    }

    protected void printWrap(List<WrapDueStringhe> listaWrap) {
        System.out.println("********");
        if (arrayService.isAllValid(listaWrap)) {
            for (WrapDueStringhe wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda());
            }
        }
    }

    protected void printWrapTre(List<WrapTreStringhe> listaWrap) {
        System.out.println(VUOTA);
        if (arrayService.isAllValid(listaWrap)) {
            for (WrapTreStringhe wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda() + SEP + wrap.getTerza());
            }
        }
    }

    protected void printWrapQuattro(List<WrapQuattro> listaWrap) {
        System.out.println(VUOTA);
        if (arrayService.isAllValid(listaWrap)) {
            for (WrapQuattro wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda() + SEP + wrap.getTerza());
            }
        }
    }

    protected void printRisultato(AIResult result) {
        List lista = result.getLista();
        lista = lista != null && lista.size() > 20 ? lista.subList(0, 10) : lista;

        System.out.println(VUOTA);
        System.out.println("Risultato");
        System.out.println(String.format("Status: %s", result.isValido() ? "true" : "false"));
        System.out.println(String.format("Query: %s", result.getQueryType()));
        System.out.println(String.format("Title: %s", result.getWikiTitle()));
        System.out.println(String.format("Preliminary url: %s", result.getUrlPreliminary()));
        System.out.println(String.format("Secondary url: %s", result.getUrlRequest()));
        System.out.println(String.format("Preliminary response: %s", result.getPreliminaryResponse()));
        System.out.println(String.format("Token: %s", result.getToken()));
        System.out.println(String.format("Secondary response: %s", result.getResponse()));
        System.out.println(String.format("Message code: %s", result.getCodeMessage()));
        System.out.println(String.format("Message: %s", result.getMessage()));
        System.out.println(String.format("Error code: %s", result.getErrorCode()));
        System.out.println(String.format("Error message: %s", result.getErrorMessage()));
        System.out.println(String.format("Valid message: %s", result.getValidMessage()));
        System.out.println(String.format("Numeric value: %s", textService.format(result.getIntValue())));
        System.out.println(String.format("List value: %s", lista));
        System.out.println(String.format("Map value: %s", result.getMappa()));
        System.out.println(String.format("Risultato ottenuto in %s", dateService.deltaText(inizio)));
    }

    protected void printCollection(final Class clazz, final boolean esiste) {
        printCollection(clazz != null ? clazz.getSimpleName() : VUOTA, esiste);
    }


    protected void printCollection(final String simpleName, final boolean esiste) {
        if (textService.isEmpty(simpleName)) {
            System.out.println(String.format("Esistenza: manca la classe"));
            return;
        }

        if (esiste) {
            System.out.println(String.format("Esistenza: la collezione '%s' %s", simpleName, "esiste"));
        }
        else {
            System.out.println(String.format("Esistenza: la collezione '%s' non è valida (non ci sono entities)", simpleName));
        }
    }


    protected void printCollection(final Class clazz, final MongoCollection collection) {
        printCollection(clazz != null ? clazz.getSimpleName() : VUOTA, collection);
    }


    protected void printCollection(final String simpleName, final MongoCollection collection) {
        if (textService.isEmpty(simpleName)) {
            System.out.println(String.format("Collezione: manca la classe"));
            return;
        }

        if (collection != null) {
            System.out.println(String.format("Collezione: '%s'%s%s", simpleName, FORWARD, collection.toString()));
        }
        else {
            System.out.println(String.format("La collezione '%s' %s", simpleName, "non esiste"));
        }
    }

    protected void printCollectionValida(final Class clazz, final boolean valida) {
        printCollectionValida(clazz != null ? clazz.getSimpleName() : VUOTA, valida);
    }

    protected void printCollectionValida(final String simpleName, final boolean valida) {
        if (textService.isEmpty(simpleName)) {
            System.out.println(String.format("Validità: manca la classe"));
            return;
        }

        if (valida) {
            System.out.println(String.format("Validità: la collezione '%s' ha una o più entities", simpleName));
        }
        else {
            System.out.println(String.format("Validità: la collezione '%s' non è valida (non ci sono entities)", simpleName));
        }
    }


    protected void printCollectionVuota(final Class clazz, final boolean vuota) {
        printCollectionVuota(clazz != null ? clazz.getSimpleName() : VUOTA, vuota);
    }

    protected void printCollectionVuota(final String simpleName, final boolean vuota) {
        if (textService.isEmpty(simpleName)) {
            System.out.println(String.format("Vuota: manca la classe"));
            return;
        }

        if (vuota) {
            System.out.println(String.format("Vuota: la collezione '%s' è vuota", simpleName));
        }
        else {
            System.out.println(String.format("Vuota: la collezione '%s' non è vuota", simpleName));
        }
    }

    protected void printCollection(final Class clazz, final String status) {
        System.out.println(String.format("La collezione '%s' %s", clazz != null ? clazz.getSimpleName() : VUOTA, status));
    }

    protected void printCollection(final String collectionName, final String status) {
        System.out.println(String.format("La collezione '%s' %s", collectionName, status));
    }

    protected void printCount(final Class clazz, final int totRecords) {
        System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali", clazz != null ? clazz.getSimpleName() : VUOTA, totRecords));
    }

    protected void printCount(final String collectionName, final int totRecords) {
        System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali", collectionName, totRecords));
    }


    protected void printError(final AlgosException unErrore) {
        System.out.println(VUOTA);
        System.out.println("Errore");
        if (unErrore.getCause() != null) {
            System.out.println(String.format("Cause %s %s", FlowCost.FORWARD, unErrore.getCause().getClass().getSimpleName()));
        }
        System.out.println(String.format("Message %s %s", FlowCost.FORWARD, unErrore.getMessage()));
        if (unErrore.getEntityBean() != null) {
            System.out.println(String.format("EntityBean %s %s", FlowCost.FORWARD, unErrore.getEntityBean().toString()));
        }
        if (unErrore.getClazz() != null) {
            System.out.println(String.format("Clazz %s %s", FlowCost.FORWARD, unErrore.getClazz().getSimpleName()));
        }
        if (textService.isValid(unErrore.getMethod())) {
            System.out.println(String.format("Method %s %s()", FlowCost.FORWARD, unErrore.getMethod()));
        }
    }

    protected void printError(final Exception unErrore) {
        System.out.println(VUOTA);
        System.out.println("Errore");
        if (unErrore == null) {
            return;
        }

        if (unErrore instanceof AlgosException erroreAlgos) {
            System.out.println(String.format("Class %s %s", FlowCost.FORWARD, erroreAlgos.getClazz().getSimpleName()));
            System.out.println(String.format("Method %s %s", FlowCost.FORWARD, erroreAlgos.getMethod()));
            System.out.println(String.format("Message %s %s", FlowCost.FORWARD, erroreAlgos.getMessage()));
        }
        else {
            System.out.println(String.format("Class %s %s", FlowCost.FORWARD, unErrore.getCause()!=null?unErrore.getCause().getClass().getSimpleName():VUOTA));
            System.out.println(String.format("Message %s %s", FlowCost.FORWARD, unErrore.getMessage()));
            System.out.println(String.format("Cause %s %s", FlowCost.FORWARD, unErrore.getCause()));
        }
    }

    protected void printDoc(final Document doc) {
        String key;
        Object value;
        System.out.println(VUOTA);

        if (doc != null) {
            System.out.println(String.format("Il documento contiene %s parametri chiave=valore, più keyID e classe", doc.size() - 2));
            for (Map.Entry<String, Object> mappa : doc.entrySet()) {
                key = mappa.getKey();
                value = mappa.getValue();
                System.out.println(String.format("%s: %s", key, value));
            }
        }
        else {
            System.out.println(String.format("Nessun documento trovato"));
        }
    }

    protected void printMappa(final AEntity entityBean) {
        Class clazz;

        if (entityBean == null) {
            System.out.print("La entityBean è nulla");
            return;
        }

        clazz = entityBean.getClass();
        System.out.println(VUOTA);
        System.out.println(String.format("Creata una entityBean di classe %s%s%s", clazz.getSimpleName(), FORWARD, entityBean));
        try {
            mappa = reflectionService.getMappaEntity(entityBean);
            for (String key : mappa.keySet()) {
                System.out.print(key);
                System.out.print(UGUALE_SPAZIATO);
                System.out.println(mappa.get(key));
            }

        } catch (AlgosException unErrore) {
            System.out.println(String.format("%s %s %s", unErrore.getMessage(), getClass(), "printMappa"));
            try {
                mappa = reflectionService.getMappaEntity(entityBean);
            } catch (AlgosException unErrore2) {
            }

        }

        System.out.println(VUOTA);
    }

}// end of class