package it.algos.test;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.packages.nomeDoppio.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import it.algos.vaadwiki.wiki.query.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 16-ago-2021
 * Time: 14:45
 */
public abstract class WTest extends ATest {

    public static final String CATEGORIA = "Categoria:";

    public static final String CAT_INESISTENTE = "Nati nel 3435";

    public static final String CAT_1167 = "Nati nel 1167";

    public static final String CAT_1435 = "Nati nel 1435";

    public static final String CAT_1591 = "Nati nel 1591";

    public static final String CAT_1935 = "Nati nel 1935";

    public static final int TOT_1935 = 1996;

    public static final String CAT_1713 = "Nati nel 1713";

    public static final String CAT_2020 = "Morti nel 2020";

    public static final int TOT_2020 = 2405;

    public static final String CAT_ROMANI = "Personaggi della storia romana";

    protected static final String PAGINA_UNO = "Roman Protasevič";

    protected static final String PAGINA_DUE = "Aldelmo di Malmesbury";

    protected static final String PAGINA_TRE = "Aelfric il grammatico";

    protected static final String PAGINA_QUATTRO = "Elfleda di Whitby";

    protected static final String PAGINA_CINQUE = "Werburga";

    protected static final String PAGINA_SEI = "Bernart Arnaut d'Armagnac";

    protected static final String PAGINA_SETTE = "Gaetano Anzalone";

    protected static final String PAGINA_OTTO = "Colin Campbell (generale)";

    protected static final String PAGINA_NOVE = "Louis Winslow Austin";

    protected static final String PAGINA_DIECI = "Maximilian Stadler";

    protected static final String PAGINA_DISAMBIGUA = "Rossi";

    protected static final String PAGINA_REDIRECT = "Regno di Napoli (1805-1815)";

    protected static final String DATA_BASE_NAME_WIKI = "vaadwiki";

    protected static final String TMPL_UNO = "{{Bio\n" +
            "|Nome = Annie\n" +
            "|Cognome = Proulx\n" +
            "|PostCognomeVirgola = all'anagrafe '''Edna Annie Proulx'''\n" +
            "|PreData = /pruː/\n" +
            "|Sesso = F\n" +
            "|LuogoNascita = Norwich\n" +
            "|LuogoNascitaLink = Norwich (Connecticut)\n" +
            "|GiornoMeseNascita = 22 agosto\n" +
            "|AnnoNascita = 1935\n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Epoca = 1900\n" +
            "|Attività = scrittrice\n" +
            "|Nazionalità = statunitense\n" +
            "|PostNazionalità = e di origini [[Canada|canadesi]], vincitrice del [[Premio Pulitzer per la narrativa]] con il romanzo ''[[Avviso ai naviganti (romanzo)|Avviso ai naviganti]]''\n" +
            "|Immagine = 2018-us-nationalbookfestival-annie-proulx.jpg\n" +
            "|Didascalia = Annie Proulx al National Book Festival 2018\n" +
            "}}";

    protected static final String TMPL_DUE = "{{Bio\n" +
            "|Nome = Alain Fabien Maurice Marcel\n" +
            "|Cognome = Delon\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Sceaux\n" +
            "|LuogoNascitaLink = Sceaux (Hauts-de-Seine)\n" +
            "|GiornoMeseNascita = 8 novembre\n" +
            "|AnnoNascita = 1935\n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Epoca = 1900\n" +
            "|Epoca2 = 2000\n" +
            "|Attività = attore\n" +
            "|Attività2 = regista\n" +
            "|Attività3 = produttore cinematografico\n" +
            "|Nazionalità = francese\n" +
            "|NazionalitàNaturalizzato = svizzero\n" +
            "|PostNazionalità = dal 1999 con doppia cittadinanza\n" +
            "|Immagine = Delon Le Guépard (cropped).jpg\n" +
            "|Didascalia = Alain Delon nel film ''[[Il Gattopardo (film)|Il Gattopardo]]'' ([[1963]])\n" +
            "}}";

    protected static final String TMPL_TRE = "{{Bio\n" +
            "|Nome = Andrea Giacomo\n" +
            "|Cognome = Viterbi\n" +
            "|PostCognome = anglicizzato in '''Andrew James Viterbi'''\n" +
            "|ForzaOrdinamento = Viterbi ,Andrew\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Bergamo\n" +
            "|GiornoMeseNascita = 9 marzo\n" +
            "|AnnoNascita = 1935\n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Epoca = 1900\n" +
            "|Epoca2 = 2000\n" +
            "|Attività = ingegnere\n" +
            "|Attività2 = imprenditore\n" +
            "|Attività3 = accademico\n" +
            "|Nazionalità = statunitense\n" +
            "|PostNazionalità = delle [[telecomunicazione|telecomunicazioni]] di origine [[italia]]na, noto per l'[[algoritmo di Viterbi|algoritmo]] che porta il suo nome\n" +
            "|Immagine = 10-08ViterbiBIG.jpg\n" +
            "|Didascalia = Andrew Viterbi nel [[2005]].\n" +
            "}}";

    private static int max = 175;

    protected String didascalia;

    protected WrapBio wrap;

    protected List<WrapBio> listaWrapBio;

    protected Bio bio;

    protected Bio bioTmplUno;

    protected Bio bioTmplDue;

    protected Bio bioTmplTre;

    protected Bio bioUno;

    protected Bio bioDue;

    protected Bio bioTre;

    protected String queryType = VUOTA;

    protected Attivita attivita = null;

    @Autowired
    protected ApplicationContext appContext;

    @InjectMocks
    protected GiornoService giornoService;

    @InjectMocks
    protected AnnoService annoService;

    @InjectMocks
    protected AttivitaService attivitaService;

    @InjectMocks
    protected NazionalitaService nazionalitaService;

    @InjectMocks
    protected NomeDoppioService prenomeService;

    @InjectMocks
    protected DidascaliaService didascaliaService;

    @InjectMocks
    protected WikiBotService wikiBotService;

    @InjectMocks
    protected BioUtility bioUtilityService;

    @InjectMocks
    protected BioService bioService;

    @InjectMocks
    protected ElaboraService elaboraService;

    @InjectMocks
    protected BotLogin botLogin;

    @InjectMocks
    protected QueryLogin queryLogin;

    @InjectMocks
    protected QueryAssert queryAssert;

    @InjectMocks
    protected QueryCat queryCat;

    @InjectMocks
    protected QueryPages queryPages;

    @InjectMocks
    protected QueryTimestamp queryTimestamp;

    @InjectMocks
    protected QueryBio queryBio;

    @InjectMocks
    protected QueryWrite queryWrite;

    private Attivita attivitaAbate;

    private Attivita attivitaBadessa;

    private Attivita attivitaAccademico;

    private Attivita attivitaAccademica;

    private Attivita attivitaAgronomo;

    private Attivita attivitaAforista;

    private Attivita attivitaConduttriceTelevisiva;

    //--titolo
    //--pagina valida
    protected static Stream<Arguments> PAGINE() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of(PAGINA_UNO, false),
                Arguments.of(PAGINA_DUE, true),
                Arguments.of(PAGINA_TRE, true),
                Arguments.of(PAGINA_QUATTRO, true),
                Arguments.of(PAGINA_CINQUE, true),
                Arguments.of(PAGINA_SEI, true),
                Arguments.of(PAGINA_SETTE, true),
                Arguments.of(PAGINA_OTTO, true),
                Arguments.of(PAGINA_NOVE, true),
                Arguments.of(PAGINA_DIECI, true)
        );
    }

    //--titolo
    //--pagina o categoria esistente
    protected static Stream<Arguments> PAGINE_DUE() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of(PAGINA_INESISTENTE, false),
                Arguments.of(PAGINA_PIOZZANO, true),
                Arguments.of(CAT_INESISTENTE, false),
                Arguments.of(CAT_1167, true),
                Arguments.of(CATEGORIA + CAT_INESISTENTE, false),
                Arguments.of(CATEGORIA + CAT_1167, true)
        );
    }

    //--titolo categoria
    //--categoria esistente
    //--numero di pagine
    //--risultatoEsatto
    //--offset
    protected static Stream<Arguments> CATEGORIE() {
        return Stream.of(
                Arguments.of(null, false, 0, false, 0),
                Arguments.of(VUOTA, false, 0, false, 0),
                Arguments.of(CAT_INESISTENTE, false, 0, false, 0),
                Arguments.of(CAT_1167, true, 6, true, 1),
                Arguments.of(CAT_1435, true, 33, true, 1),
                Arguments.of(CAT_1935, true, TOT_1935, true, 1),
                Arguments.of(CAT_ROMANI, true, 78, true, 1)
        );
    }

    //--titolo categoria
    //--categoria esistente (per l'userType specificato)
    //--AETypeUser userType
    //--numero di pagine
    protected static Stream<Arguments> CATEGORIE_TYPE() {
        return Stream.of(
                Arguments.of(null, false, null, 0),
                Arguments.of(VUOTA, false, null, 0),
                Arguments.of(CAT_INESISTENTE, false, null, 0),
                Arguments.of(CAT_1167, true, null, 0),
                Arguments.of(CAT_1167, true, AETypeUser.anonymous, 0),
                Arguments.of(CAT_1167, false, AETypeUser.user, 0),
                Arguments.of(CAT_1167, false, AETypeUser.bot, 0),
                Arguments.of(CAT_1435, true, null, 0),
                Arguments.of(CAT_1435, true, AETypeUser.anonymous, 0),
                Arguments.of(CAT_1435, false, AETypeUser.user, 0),
                Arguments.of(CAT_1435, false, AETypeUser.bot, 0)
        );
    }

    //--attivita
    //--AETypeAttivita
    private Stream<Arguments> ATTIVITA() {
        return Stream.of(
                Arguments.of(attivitaBadessa, ListaAttivita.AETypeAttivita.singolare),
                Arguments.of(attivitaBadessa, ListaAttivita.AETypeAttivita.plurale),
                Arguments.of(attivitaAbate, ListaAttivita.AETypeAttivita.singolare),
                Arguments.of(attivitaAbate, ListaAttivita.AETypeAttivita.plurale)
                //                Arguments.of(attivitaAccademico, ListaAttivita.AETypeAttivita.singolare),
                //                Arguments.of(attivitaAccademico, ListaAttivita.AETypeAttivita.plurale),
                //                Arguments.of(attivitaAccademica, ListaAttivita.AETypeAttivita.singolare),
                //                Arguments.of(attivitaAccademica, ListaAttivita.AETypeAttivita.plurale),
                //                Arguments.of(attivitaAgronomo, ListaAttivita.AETypeAttivita.singolare),
                //                Arguments.of(attivitaAgronomo, ListaAttivita.AETypeAttivita.plurale),
                //                Arguments.of(attivitaAforista, ListaAttivita.AETypeAttivita.singolare),
                //                Arguments.of(attivitaAforista, ListaAttivita.AETypeAttivita.plurale),
                //                Arguments.of(attivitaConduttriceTelevisiva, ListaAttivita.AETypeAttivita.singolare)

        );
    }

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUpStartUp() {
        super.setUpStartUp();

        wInitMocks();
        wFixRiferimentiIncrociati();
        wCreaBio();
        wCreaEntity();
    }


    /**
     * Inizializzazione dei service
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     */
    protected void wInitMocks() {
        MockitoAnnotations.initMocks(giornoService);
        assertNotNull(giornoService);

        MockitoAnnotations.initMocks(annoService);
        assertNotNull(annoService);

        MockitoAnnotations.initMocks(attivitaService);
        assertNotNull(attivitaService);

        MockitoAnnotations.initMocks(nazionalitaService);
        assertNotNull(nazionalitaService);

        MockitoAnnotations.initMocks(prenomeService);
        assertNotNull(prenomeService);

        MockitoAnnotations.initMocks(didascaliaService);
        assertNotNull(didascaliaService);

        MockitoAnnotations.initMocks(wikiBotService);
        assertNotNull(wikiBotService);

        MockitoAnnotations.initMocks(bioUtilityService);
        assertNotNull(bioUtilityService);

        MockitoAnnotations.initMocks(bioService);
        assertNotNull(bioService);

        MockitoAnnotations.initMocks(elaboraService);
        assertNotNull(elaboraService);

        MockitoAnnotations.initMocks(botLogin);
        assertNotNull(botLogin);

        MockitoAnnotations.initMocks(queryLogin);
        assertNotNull(queryLogin);

        MockitoAnnotations.initMocks(queryAssert);
        assertNotNull(queryAssert);

        MockitoAnnotations.initMocks(queryCat);
        assertNotNull(queryCat);

        MockitoAnnotations.initMocks(queryPages);
        assertNotNull(queryPages);

        MockitoAnnotations.initMocks(queryTimestamp);
        assertNotNull(queryTimestamp);

        MockitoAnnotations.initMocks(queryBio);
        assertNotNull(queryBio);

        MockitoAnnotations.initMocks(queryWrite);
        assertNotNull(queryWrite);
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito le referenze 'mockate' <br>
     * Nelle sottoclassi di testi devono essere regolati i riferimenti dei service specifici <br>
     */
    protected void wFixRiferimentiIncrociati() {
        wikiBotService.text = textService;
        wikiBotService.web = webService;
        wikiBotService.jSonService = jSonService;
        wikiBotService.logger = loggerService;
        wikiBotService.wikiApi = wikiApiService;
        wikiBotService.date = dateService;
        wikiBotService.login = botLogin;
        wikiBotService.bioService = bioService;

        elaboraService.text = textService;
        elaboraService.giornoService = giornoService;
        elaboraService.annoService = annoService;
        giornoService.text = textService;
        giornoService.mongo = mongoService;
        giornoService.annotation = annotationService;
        annoService.text = textService;
        annoService.mongo = mongoService;
        annoService.annotation = annotationService;
        attivitaService.text = textService;
        attivitaService.mongo = mongoService;
        attivitaService.annotation = annotationService;
        nazionalitaService.text = textService;
        nazionalitaService.mongo = mongoService;
        nazionalitaService.annotation = annotationService;

        queryAssert.botLogin = botLogin;

        queryLogin.queryAssert = queryAssert;
        queryLogin.text = textService;
        queryLogin.wikiApi = wikiApiService;
        queryLogin.logger = loggerService;
        //        queryLogin.appContext = appContext;
        queryLogin.botLogin = botLogin;

        queryCat.text = textService;
        queryCat.wikiApi = wikiApiService;
        queryCat.wikiBot = wikiBotService;
        queryCat.logger = loggerService;
        queryCat.botLogin = botLogin;
        queryCat.queryAssert = queryAssert;

        queryPages.text = textService;
        queryPages.botLogin = botLogin;
        queryPages.queryAssert = queryAssert;
        queryPages.array = arrayService;
        queryPages.wikiApi = wikiApiService;

        queryTimestamp.text = textService;
        queryTimestamp.botLogin = botLogin;
        queryTimestamp.queryAssert = queryAssert;
        queryTimestamp.array = arrayService;

        queryWrite.text = textService;
        queryWrite.wikiApi = wikiApiService;
        queryWrite.botLogin = botLogin;
        queryWrite.queryAssert = queryAssert;
        queryWrite.array = arrayService;
        queryWrite.logger = loggerService;

        didascaliaService.text = textService;

        bioService.text = textService;
        bioService.annotation = annotationService;
        bioService.reflection = reflectionService;
        bioService.elaboraService = elaboraService;
        bioService.mongo = mongoService;
        bioService.logger = loggerService;
        bioService.date = dateService;

        queryBio.text = textService;
        queryBio.wikiApi = wikiApiService;
        //        queryBio.appContext = appContext;

        elaboraService.bioUtility = bioUtilityService;
        elaboraService.logger = loggerService;
        elaboraService.attivitaService = attivitaService;
        elaboraService.nazionalitaService = nazionalitaService;
        elaboraService.prenomeService = prenomeService;
        elaboraService.wikiBotService = wikiBotService;

        prenomeService.annotation = annotationService;
        prenomeService.reflection = reflectionService;
        prenomeService.logger = loggerService;
        prenomeService.mongo = mongoService;

        bioUtilityService.text = textService;

        for (ParBio par : ParBio.values()) {
            par.setText(textService);
            par.setWikiBot(wikiBotService);
            par.setElabora(elaboraService);
        }
    }

    /**
     * Crea alcune bio per i test, senza passare da mongoDB <br>
     */
    protected void wCreaBio() {
        FlowVar.typeSerializing = AETypeSerializing.gson;
        long pageId;

        pageId = 211334;
        sorgente = "Fabio Cudicini";
        sorgente2 = TMPL_UNO;
        bioTmplUno = bioService.newEntity(pageId, sorgente2, sorgente2);
        bioUno = elaboraService.esegue(bioTmplUno);

        pageId = 427;
        sorgente = "Alain Delon";
        sorgente2 = TMPL_DUE;
        bioTmplDue = bioService.newEntity(pageId, sorgente, sorgente2);
        bioDue = elaboraService.esegue(bioTmplDue);

        pageId = 14926;
        sorgente = "Andrew Viterbi";
        sorgente2 = TMPL_TRE;
        bioTmplTre = bioService.newEntity(pageId, sorgente, sorgente2);
        bioTre = elaboraService.esegue(bioTmplTre);
    }


    /**
     * Crea alcune istanze specifiche di ogni test <br>
     */
    protected void wCreaEntity() {
        try {
            attivitaAbate = attivitaService.findByKey("abate");
            assertNotNull(attivitaAbate);

            attivitaBadessa = attivitaService.findByKey("badessa");
            assertNotNull(attivitaBadessa);

            attivitaAccademico = attivitaService.findByKey("accademico");
            assertNotNull(attivitaAccademico);

            attivitaAccademica = attivitaService.findByKey("accademica");
            assertNotNull(attivitaAccademica);

            attivitaAgronomo = attivitaService.findByKey("agronomo");
            assertNotNull(attivitaAgronomo);

            attivitaAforista = attivitaService.findByKey("aforista");
            assertNotNull(attivitaAforista);

            attivitaConduttriceTelevisiva = attivitaService.findByKey("conduttrice televisiva");
            assertNotNull(attivitaConduttriceTelevisiva);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            System.out.println(VUOTA);
        }
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUp() {
        super.setUp();
        FlowVar.projectNameModulo = "vaadwiki";

        listaWrapBio = null;
    }

    protected Bio getBio(String wikiTitle) {
        FlowVar.typeSerializing = AETypeSerializing.gson;
        Bio bio = null;
        clazz = Bio.class;
        String propertyName = "wikiTitle";

        try {
            bio = (Bio) mongoService.find(clazz, propertyName, wikiTitle);
        } catch (Exception unErrore) {
            System.out.println(String.format("Non sono riuscito a recuperare la bio %s", wikiTitle));
        }

        return bio;
    }

    protected void print(final Bio bio, final String nomeCognome, final String attivitaNazionalita) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("NomeCognome: %s", nomeCognome));
        System.out.println(String.format("AttivitaNazionalita: %s", attivitaNazionalita));
    }

    protected void print(final Bio bio, final String nomeCognome, final String attivitaNazionalita, String natoMorto) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("NomeCognome: %s", nomeCognome));
        System.out.println(String.format("AttivitaNazionalita: %s", attivitaNazionalita));
        System.out.println(String.format("Luogo-anno-nascita-morte: %s", natoMorto));
    }

    protected void print(final Bio bio, final String nomeCognome, final String attivitaNazionalita, String natoMorto, String didascaliaLista) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("NomeCognome: %s", nomeCognome));
        System.out.println(String.format("AttivitaNazionalita: %s", attivitaNazionalita));
        System.out.println(String.format("Luogo-anno-nascita-morte: %s", natoMorto));
        System.out.println(String.format("Didascalia lista: %s", didascaliaLista));
    }

    protected void print(final Bio bio, final String ottenuto) {
        print(bio.wikiTitle, bio.nome, bio.cognome, ottenuto);
    }

    protected void print(final String sorgente, final String sorgente2, final String sorgente3, final String ottenuto) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("Nome: %s", sorgente2));
        System.out.println(String.format("Cognome: %s", sorgente3));
        System.out.println(String.format("Didascalia: %s", ottenuto));
    }

    protected void printWrapBio(WrapBio wrap) {
        System.out.println("WrapBio");
        System.out.println(VUOTA);
        System.out.println("Wrap valido: " + wrap.isValido());
        System.out.println("Titolo:" + SPAZIO + wrap.getTitle());
        System.out.println("PageId:" + SPAZIO + wrap.getPageid());
        System.out.println("Type:" + SPAZIO + wrap.getType());
        System.out.println("Timestamp:" + SPAZIO + wrap.getTime());
        System.out.println("Template:" + SPAZIO + wrap.getTemplBio());
    }

    protected void printRisultato(AIResult result, String titolo) {
        boolean miniWrap = false;
        boolean wrapBio = false;
        List lista = result.getLista();
        List<Long> listaPagesIds = null;
        List<String> listaTitles = null;
        lista = lista != null && lista.size() > 20 ? lista.subList(0, 5) : lista;

        if (lista != null && lista.get(0) instanceof MiniWrap) {
            miniWrap = true;
            listaPagesIds = new ArrayList<>();
            for (Object obj : lista) {
                listaPagesIds.add(((MiniWrap) obj).getPageid());
            }
        }

        if (lista != null && lista.get(0) instanceof WrapBio) {
            wrapBio = true;
            listaTitles = new ArrayList<>();
            for (Object obj : lista) {
                listaTitles.add(((WrapBio) obj).getTitle());
            }
        }

        System.out.println(VUOTA);
        System.out.println(String.format("Risultato %s", titolo));
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
        if (miniWrap || wrapBio) {
            if (miniWrap) {
                System.out.println(String.format("List value: %s ...", listaPagesIds));
            }
            if (wrapBio) {
                System.out.println(String.format("List value: %s ...", listaTitles));
            }
        }
        else {
            System.out.println(String.format("List value: %s ...", lista));
        }
        System.out.println(String.format("Map value: %s", result.getMappa()));
        System.out.println(String.format("Risultato ottenuto in %s", dateService.deltaText(inizio)));
    }

    protected void printRisultato(AIResult result) {
        printRisultato(result, VUOTA);
    }

    protected void print10long(List<Long> lista) {
        int max = Math.min(10, lista.size());

        System.out.print("PageIds (primi 10): ");
        for (int k = 0; k < max - 1; k++) {
            System.out.print(lista.get(k));
            System.out.print(VIRGOLA_SPAZIO);
        }
        System.out.print(lista.get(max - 1));
        System.out.println(VUOTA);
    }

    protected void print10Mini(List<MiniWrap> lista) {
        int max = Math.min(5, lista.size());

        System.out.print("MiniWrap con pageId e lastModifica (primi 5): ");
        for (int k = 0; k < max - 1; k++) {
            System.out.print(QUADRA_INI);
            System.out.print(lista.get(k).getPageid());
            System.out.print(SPAZIO);
            System.out.print(lista.get(k).getLastModifica());
            System.out.print(QUADRA_END);
            System.out.print(VIRGOLA_SPAZIO);
        }
        System.out.println(VUOTA);
    }

    protected void print10Bio(List<WrapBio> lista) {
        int max = Math.min(5, lista.size());

        System.out.print("WrapBio (primi 5): ");
        for (int k = 0; k < max - 1; k++) {
            System.out.print(QUADRA_INI);
            System.out.print(lista.get(k).getPageid());
            System.out.print(SPAZIO);
            System.out.print(lista.get(k).getTitle());
            System.out.print(QUADRA_END);
            System.out.print(VIRGOLA_SPAZIO);
        }
        System.out.println(VUOTA);
    }

    protected String getMax(String message) {
        message = message.length() < max ? message : message.substring(0, Math.min(max, message.length()));
        if (message.contains(A_CAPO)) {
            message = message.replaceAll(A_CAPO, SPAZIO);
        }

        return message;
    }

    protected void printResultBase(final AIResult result) {
        String message;

        if (result == null) {
            return;
        }

        if (result.getWebTitle() == null) {
            message = "(null)";
        }
        else {
            if (result.getWebTitle().equals(VUOTA)) {
                message = "(vuota)";
            }
            else {
                message = result.getWebTitle();
            }
        }

        System.out.println(VUOTA);
        System.out.println(String.format("Result di: %s", message));
        System.out.println(VUOTA);
        System.out.println(String.format("Risultato: %s", result.isValido() ? "valido" : "errato"));
        System.out.println(String.format("PageId: %s", result.getLongValue()));
        System.out.println(String.format("WikiTitle: %s", result.getWikiTitle()));
        System.out.println(String.format("Url: %s", result.getUrlRequest()));
        System.out.println(String.format("ErrorCode: %s", result.getErrorCode()));
        System.out.println(String.format("ErrorMessage: %s", result.getErrorMessage()));
        System.out.println(String.format("ValidMessage: %s", result.getValidMessage()));
        System.out.println(String.format("Response: %s", getMax(result.getResponse())));
        System.out.println(String.format("WikiText: %s", getMax(result.getWikiText())));
        System.out.println(String.format("WikiBio: %s", getMax(result.getWikiBio())));
    }

}
