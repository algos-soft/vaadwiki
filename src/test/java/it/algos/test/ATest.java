package it.algos.test;

import com.mongodb.*;
import com.vaadin.flow.data.provider.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wiki.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.slf4j.*;
import org.springframework.context.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Field;
import java.time.*;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mar, 28-apr-2020
 * Time: 21:18
 * <p>
 * Classe astratta per i test <br>
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

    protected static Class<? extends AEntity> VIA_ENTITY_CLASS = Via.class;

    protected static Class<? extends AEntity> ANNO_ENTITY_CLASS = Anno.class;

    protected static Class<? extends AEntity> MESE_ENTITY_CLASS = Mese.class;

    protected static Class<? extends AEntity> COMPANY_ENTITY_CLASS = Company.class;

    protected static Class ANNO_LOGIC_LIST = AnnoLogicList.class;

    protected static Class VIA_LIST_CLASS = ViaLogicList.class;

    protected static Class VIA_SERVICE_CLASS = ViaService.class;

    protected static Field FIELD_ORDINE;

    protected static Field FIELD_NOME;

    protected static String PATH = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard/";

    /**
     * The App context.
     */
    @Mock
    protected ApplicationContext appContext;

    @InjectMocks
    protected TextService text;

    @InjectMocks
    protected ArrayService array;

    @InjectMocks
    protected DateService date;

    @InjectMocks
    protected AnnotationService annotation;

    @InjectMocks
    protected ReflectionService reflection;

    @InjectMocks
    protected ALogService logger;

    @InjectMocks
    protected BeanService bean;

    @InjectMocks
    protected AMongoService mongo;

    @InjectMocks
    protected WebService web;

    @InjectMocks
    protected AWikiApiService wikiApi;

    @InjectMocks
    protected AGeograficService geografic;

    @InjectMocks
    protected FileService file;

    @InjectMocks
    protected MathService math;

    @InjectMocks
    protected GsonService gSonService;

    @InjectMocks
    protected ClassService classService;

    @InjectMocks
    protected UtilityService utilityService;

    @InjectMocks
    protected HtmlService html;

    @InjectMocks
    protected PreferenzaService preferenzaService;

    @InjectMocks
    protected JSonService jSonService;

    protected Logger adminLogger;

    /**
     * The Previsto booleano.
     */
    protected boolean previstoBooleano;

    /**
     * The Ottenuto booleano.
     */
    protected boolean ottenutoBooleano;

    /**
     * The Sorgente.
     */
    protected String sorgente;

    /**
     * The Sorgente.
     */
    protected String sorgente2;

    /**
     * The Sorgente.
     */
    protected String sorgente3;

    /**
     * The Previsto.
     */
    protected String previsto;

    /**
     * The Previsto.
     */
    protected String previsto2;

    /**
     * The Previsto.
     */
    protected String previsto3;

    /**
     * The Ottenuto.
     */
    protected String ottenuto;

    /**
     * The Ottenuto.
     */
    protected String ottenuto2;

    /**
     * The Sorgente classe.
     */
    protected Class sorgenteClasse;

    /**
     * The Sorgente field.
     */
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

    protected AEntity entityBean;

    protected Query query;

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

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     */
    protected void setUpStartUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(appContext);
        Assertions.assertNotNull(appContext);

        MockitoAnnotations.initMocks(text);
        Assertions.assertNotNull(text);

        MockitoAnnotations.initMocks(array);
        Assertions.assertNotNull(array);

        MockitoAnnotations.initMocks(logger);
        Assertions.assertNotNull(logger);

        MockitoAnnotations.initMocks(date);
        Assertions.assertNotNull(date);

        adminLogger = LoggerFactory.getLogger("wam.admin");
        Assertions.assertNotNull(adminLogger);

        MockitoAnnotations.initMocks(annotation);
        Assertions.assertNotNull(annotation);

        MockitoAnnotations.initMocks(reflection);
        Assertions.assertNotNull(reflection);

        MockitoAnnotations.initMocks(bean);
        Assertions.assertNotNull(bean);

        MockitoAnnotations.initMocks(mongo);
        Assertions.assertNotNull(mongo);

        MockitoAnnotations.initMocks(web);
        Assertions.assertNotNull(web);

        MockitoAnnotations.initMocks(wikiApi);
        Assertions.assertNotNull(wikiApi);

        MockitoAnnotations.initMocks(geografic);
        Assertions.assertNotNull(geografic);

        MockitoAnnotations.initMocks(file);
        Assertions.assertNotNull(file);

        MockitoAnnotations.initMocks(math);
        Assertions.assertNotNull(math);

        MockitoAnnotations.initMocks(gSonService);
        Assertions.assertNotNull(gSonService);

        MockitoAnnotations.initMocks(utilityService);
        Assertions.assertNotNull(utilityService);

        MockitoAnnotations.initMocks(html);
        Assertions.assertNotNull(html);

        MockitoAnnotations.initMocks(preferenzaService);
        Assertions.assertNotNull(preferenzaService);

        MockitoAnnotations.initMocks(jSonService);
        Assertions.assertNotNull(jSonService);

        array.text = text;
        text.array = array;
        logger.text = text;
        logger.adminLogger = adminLogger;
        annotation.text = text;
        annotation.array = array;
        annotation.logger = logger;
        annotation.reflection = reflection;
        reflection.array = array;
        reflection.text = text;
        gSonService.text = text;
        gSonService.array = array;
        jSonService.text = text;
        jSonService.array = array;
        bean.mongo = mongo;
        mongo.text = text;
        mongo.annotation = annotation;
        mongo.reflection = reflection;
        web.text = text;
        web.logger = logger;
        wikiApi.text = text;
        wikiApi.web = web;
        wikiApi.logger = logger;
        wikiApi.html = html;
        file.text = text;
        file.array = array;
        file.logger = logger;
        date.math = math;
        sortSpring = null;
        classService.fileService = file;
        classService.text = text;
        classService.logger = logger;
        classService.annotation = annotation;
        preferenzaService.mongo = mongo;
        utilityService.text = text;
        html.text = text;
        date.text = text;
        sorgenteArray = null;
        previstoArray = null;
        ottenutoArray = null;
        sorgenteArrayLong = null;
        previstoArrayLong = null;
        ottenutoArrayLong = null;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     */
    protected void setUp() {
        inizio = System.currentTimeMillis();
        cicli = 1;
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
        sorgenteClasse = null;
        sorgenteField = null;
        sorgenteMatrice = null;
        previstoMatrice = null;
        ottenutoMatrice = null;
        previstoArray = null;
        ottenutoArray = null;
        previstoInteroArray = null;
        ottenutoInteroArray = null;
        query = null;
        sortSpring = null;
        sortVaadin = null;
        filtro = null;
        listaFiltri = new ArrayList<>();
        mappaFiltri = new HashMap<>();
        mappa = new HashMap<>();
        listaBean = null;
        listaStr = null;
        listaFields = null;
        bytes = null;
        FIELD_ORDINE = reflection.getField(VIA_ENTITY_CLASS, NAME_ORDINE);
        FIELD_NOME = reflection.getField(VIA_ENTITY_CLASS, NAME_NOME);
        entityBean = null;
        clazz = null;
        previstoRisultato = null;
        ottenutoRisultato = null;
    }

    protected String getTime() {
        return date.deltaTextEsatto(inizio);
    }

    protected void printVuota(List<String> lista) {
        System.out.println(VUOTA);
        print(lista);
    }

    protected void print(List<String> lista) {
        if (array.isAllValid(lista)) {
            for (String stringa : lista) {
                System.out.println(stringa);
            }
        }
    }


    protected void printList(List<List<String>> listaTable) {
        if (array.isAllValid(listaTable)) {
            for (List<String> lista : listaTable) {
                System.out.println(VUOTA);
                if (array.isAllValid(lista)) {
                    for (String stringa : lista) {
                        System.out.println(stringa);
                    }
                }
            }
        }
    }


    protected void printMappa(Map<String, List<String>> mappa) {
        List<String> lista;
        if (array.isAllValid(mappa)) {
            for (String key : mappa.keySet()) {
                lista = mappa.get(key);
                System.out.println(VUOTA);
                if (array.isAllValid(lista)) {
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



    protected void printWrap(List<WrapDueStringhe> listaWrap) {
        System.out.println("********");
        if (array.isAllValid(listaWrap)) {
            for (WrapDueStringhe wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda());
            }
        }
    }

    protected void printWrapTre(List<WrapTreStringhe> listaWrap) {
        System.out.println(VUOTA);
        if (array.isAllValid(listaWrap)) {
            for (WrapTreStringhe wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda() + SEP + wrap.getTerza());
            }
        }
    }

    protected void printWrapQuattro(List<WrapQuattro> listaWrap) {
        System.out.println(VUOTA);
        if (array.isAllValid(listaWrap)) {
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
        System.out.println(String.format("Numeric value: %s", text.format(result.getValue())));
        System.out.println(String.format("List value: %s", lista));
        System.out.println(String.format("Map value: %s", result.getMappa()));
        System.out.println(String.format("Risultato ottenuto in %s", date.deltaText(inizio)));
    }

}// end of class