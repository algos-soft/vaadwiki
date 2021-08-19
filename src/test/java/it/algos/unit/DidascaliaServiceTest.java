package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.backend.enumeration.AETypeData.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.didascalia.*;
import static it.algos.vaadwiki.backend.enumeration.EDidascalia.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.query.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 08:39
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Didascalia - Elaborazione delle didascalie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DidascaliaServiceTest extends WTest {

    private static final String DATA_BASE_NAME = "vaadflow14";

    @InjectMocks
    public DidascaliaService service;
    //    @InjectMocks
    //    public Didascalia didascalia;

    //    @InjectMocks
    //    public DidascaliaService didascaliaService;

    @InjectMocks
    public GiornoService giornoService;

    //    @InjectMocks
    //    public DidascaliaAnnoNato annoNato;

    //    @InjectMocks
    //    public DidascaliaGiornoMorto giornoMorto;

    //    @InjectMocks
    //    public DidascaliaAnnoMorto annoMorto;

    //    @InjectMocks
    //    public DidascaliaListe standard;

    //    @InjectMocks
    //    public DidascaliaBiografie completa;

    @InjectMocks
    public AnnoService annoService;

    @InjectMocks
    public GiornoService giorno;

    @InjectMocks
    public AWikiBotService wikiBot;

    @InjectMocks
    public BioUtility bioUtility;

    @InjectMocks
    protected BioService bioService;

    //    @InjectMocks
    //    protected PageService pageService;

    @InjectMocks
    protected ElaboraService elaboraService;

    @InjectMocks
    protected ReflectionService reflection;

    @InjectMocks
    protected PreferenzaService pref;

    @InjectMocks
    protected DateService date;

//    @InjectMocks
//    protected AMongoService mongoService;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    QueryBio queryBio;

    /**
     * Classe di riferimento <br>
     */
    @InjectMocks
    DidascaliaGiornoNato didascaliaGiornoNato;

    @InjectMocks
    private GsonService gSonService;

    private String wikiTitle = "Adone Asinari";

    private String wikiTitleDue = "Sonia Todd";

    public static String[] PAGINE() {
        return new String[]{PAGINA_UNO, PAGINA_DUE, PAGINA_TRE, PAGINA_QUATTRO, PAGINA_CINQUE, PAGINA_SEI, PAGINA_SETTE};
    }

    @BeforeAll
    public void setUp() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);

        service.text = text;

        MockitoAnnotations.initMocks(queryBio);
        Assertions.assertNotNull(queryBio);
        queryBio.array = array;
        queryBio.text = text;
        queryBio.wikiApi = wikiApi;
        wikiApi.text = text;
        wikiApi.html = html;
        html.text = text;
        bioService.elaboraService = elaboraService;

        MockitoAnnotations.initMocks(wikiBot);
        Assertions.assertNotNull(wikiBot);

        MockitoAnnotations.initMocks(bioUtility);
        Assertions.assertNotNull(bioUtility);
        elaboraService.bioUtility = bioUtility;
        elaboraService.logger = logger;
        bioUtility.text = text;
        logger.text = text;
        logger.adminLogger = adminLogger;

        //        MockitoAnnotations.initMocks(api);
        //        MockitoAnnotations.initMocks(pageService);
        MockitoAnnotations.initMocks(bioService);
        MockitoAnnotations.initMocks(elaboraService);
        MockitoAnnotations.initMocks(annoService);
        MockitoAnnotations.initMocks(giornoService);
        MockitoAnnotations.initMocks(giorno);
        MockitoAnnotations.initMocks(reflection);
//        MockitoAnnotations.initMocks(mongoService);
        MockitoAnnotations.initMocks(text);
        //        MockitoAnnotations.initMocks(didascalia);
        MockitoAnnotations.initMocks(giornoNato);
        MockitoAnnotations.initMocks(annoNato);
        MockitoAnnotations.initMocks(giornoMorto);
        MockitoAnnotations.initMocks(annoMorto);
        MockitoAnnotations.initMocks(standard);
        //        MockitoAnnotations.initMocks(completa);
        //        MockitoAnnotations.initMocks(didascaliaService);
        MockitoAnnotations.initMocks(pref);
        MockitoAnnotations.initMocks(date);
        //        api.pageService = pageService;
        //        api.text = text;
        //        pageService.api = api;
        //        pageService.text = text;
        //        pageService.bioService = bioService;
        //        pageService.elaboraService = elaboraService;
        elaboraService.text = text;
        elaboraService.annoService = annoService;
        //        elaboraService.libBio = libBio;
        //        libBio.giorno = giorno;
        bioService.text = text;
        //        libBio.mongo = mongoService;
        //        mongoService.mongoOp = mongoOperations;
        //        mongoService.reflection = reflection;
        //        mongoService.text = text;
        //        didascalia.text = text;
        //        giornoNato.annoService = annoService;
        //        giornoNato.text = text;
        //        annoNato.text = text;
        //        giornoMorto.text = text;
        //        annoMorto.text = text;
        //        standard.text = text;
        //        completa.text = text;
        //        didascalia.didascaliaCompleta = completa;
        //        didascalia.didascaliaGiornoNato = giornoNato;
        //        didascalia.didascaliaAnnoNato = annoNato;
        //        didascalia.didascaliaGiornoMorto = giornoMorto;
        //        didascalia.didascaliaAnnoMorto = annoMorto;
        //        didascalia.didascaliaStandard = standard;
        //        bio = api.leggeBio(wikiTitle);
        //        didascaliaService.wikiTitle = wikiTitle;
        //        didascaliaService.pref = pref;
        //        didascaliaService.date = date;
        //        didascaliaService.api = api;
        //        didascaliaService.text = text;
        text.mongo = mongo;
        annoService.mongo = mongo;
        mongo.text = text;

        for (ParBio parBio : ParBio.values()) {
            parBio.setText(text);
            parBio.setWikiBot(wikiBot);
            parBio.setElabora(elaboraService);
        }

        MockitoAnnotations.initMocks(gSonService);
        Assertions.assertNotNull(gSonService);

        mongo.fixProperties(DATA_BASE_NAME);
        gSonService.fixProperties(DATA_BASE_NAME);
    }// end of method

    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        didascalia = VUOTA;
        wrap = null;
        bio = null;
    }

    private Giorno creaGiorno(String giornoText) {
        Giorno giorno = null;
        giorno = new Giorno();
        //        giorno.titolo = giornoText;
        return giorno;
    }// end of method

    //    private Anno creaAnno(int annoInt) {
    //        Anno anno = null;
    //        anno = new Anno();
    //        anno.titolo = "" + annoInt;
    //        return anno;
    //    }// end of method

    //    private Attivita creaAttivita(String attivitaText) {
    //        Attivita attivita = null;
    //        attivita = new Attivita();
    //        attivita.singolare = attivitaText;
    //        return attivita;
    //    }// end of method

    //    private Nazionalita creaNazionalita(String nazionalitaText) {
    //        Nazionalita nazionalita = null;
    //        nazionalita = new Nazionalita();
    //        nazionalita.singolare = nazionalitaText;
    //        return nazionalita;
    //    }// end of method


    private Bio creaBio() {
        Bio entity = null;
        //        Giorno giorno = null;
        //        Anno anno = creaAnno(1963);
        //        Attivita attivita = creaAttivita("attrice");
        //        Nazionalita nazionalita = creaNazionalita("australiana");
        //
        //        entity = new Bio();
        //        entity.pageid = 29999;
        //        entity.wikiTitle = wikiTitle;
        //        entity.nome = "Sonia";
        //        entity.cognome = "Todd";
        //        entity.sesso = "F";
        //        entity.luogoNato = "Adelaide";
        //        entity.luogoNatoLink = "Adelaide (Australia)";
        //        entity.giornoNascita = giorno;
        //        entity.annoNascita = anno;
        //        entity.attivita = attivita;
        //        entity.nazionalita = nazionalita;

        return entity;
    }

    @Test
    @Order(1)
    @DisplayName("1 - Nome e cognome semplice")
    void getNomeCognome() {
        System.out.println("1 - Nome e cognome semplice");

        sorgente = "Sigurd Ribbung";
        sorgente2 = "Sigurd";
        sorgente3 = "Ribbung";
        previsto = "[[Sigurd Ribbung]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);

        wrap = queryBio.urlRequest(sorgente).getWrap();
        assertNotNull(wrap);
        assertTrue(wrap.isValido());
        bio = bioService.newEntity(wrap);
        bio = elaboraService.esegue(bio);
        ottenuto = service.getNomeCognome(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("Lo stesso passando da WrapBio e Bio");
        print(bio, ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Nome doppio e cognome semplice")
    void getNomeCognome2() {
        System.out.println("2 - Nome doppio e cognome semplice");

        sorgente = "Bernart Arnaut d'Armagnac";
        sorgente2 = "Bernart";
        sorgente3 = "d'Armagnac";
        previsto = "[[Bernart Arnaut d'Armagnac]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);
    }

    @Test
    @Order(3)
    @DisplayName("3 - Nome doppio e cognome semplice")
    void getNomeCognome3() {
        System.out.println("3 - Nome doppio e cognome semplice");

        sorgente = "Francesco Maria Pignatelli";
        sorgente2 = "Francesco Maria";
        sorgente3 = "Pignatelli";
        previsto = "[[Francesco Maria Pignatelli]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);
    }

    @Test
    @Order(4)
    @DisplayName("4 - Titolo disambiguato")
    void getNomeCognome4() {
        System.out.println("4 - Titolo disambiguato");

        sorgente = "Colin Campbell (generale)";
        sorgente2 = "Colin";
        sorgente3 = "Campbell";
        previsto = "[[Colin Campbell (generale)|Colin Campbell]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);
    }

    @ParameterizedTest
    @MethodSource(value = "PAGINE")
    @Order(5)
    @DisplayName("5 - Didascalie varie")
    void testWithStringParameter(String wikiTitle) {
        System.out.println("5 - Didascalie varie");

        sorgente = wikiTitle;
        wrap = queryBio.urlRequest(sorgente).getWrap();
        assertNotNull(wrap);
        assertTrue(wrap.isValido());
        bio = bioService.newEntity(wrap);
        bio = elaboraService.esegue(bio);
        previsto = text.setDoppieQuadre(bio.wikiTitle);
        ottenuto = service.getNomeCognome(bio);
        ottenuto2 = service.getAttivitaNazionalita(bio);
        assertTrue(text.isValid(ottenuto));
        System.out.println(VUOTA);
        print(bio, ottenuto,ottenuto2);
    }

    //    @Test
    public void download() {
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + wikiTitle);
        System.out.println("Senza chiave");
        System.out.println("*************");
        //        for (EADidascalia dida : EADidascalia.values()) {
        //            ottenuto = didascalia.esegue(bio, dida, false);
        //            System.out.println(dida.name() + ": " + ottenuto);
        //        }// end of for cycle
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        //        for (EADidascalia dida : EADidascalia.values()) {
        //            //            ottenuto = didascalia.esegue(bio, dida);
        //            System.out.println(dida.name() + ": " + ottenuto);
        //        }// end of for cycle
        System.out.println("*************");

    }// end of single test


    /**
     * Test con uscita sul terminale di Idea
     */
    //    @Test
    public void esegueTestDidascalie() {
        System.out.println("");
        System.out.println("Algos");
        System.out.println("");
        System.out.println("Tipi possibili di discalie");
        System.out.println("Esempio '" + wikiTitle + "'");
        System.out.println("");
        Bio bio = creaBio();
        //        for (EADidascalia type : EADidascalia.values()) {
        //            ottenuto = didascaliaService.getBaseSenza(bio, type);
        //            if (text.isValid(ottenuto)) {
        //                System.out.println(type.name() + ": " + ottenuto);
        //            }
        //            else {
        //                System.out.println(type.name() + ": Manca");
        //            }// end of if/else cycle
        //        }// end of for cycle
        System.out.println("");
    }// end of single test

    //    /**
    //     * Pagina completa con uscita su pagina utente
    //     */
    //    @Test
    //    public void esegueTestUplod() {
    //        didascaliaService.esegue();
    //    }// end of single test


}// end of class
