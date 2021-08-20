package it.algos.test;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import it.algos.vaadwiki.wiki.query.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 16-ago-2021
 * Time: 14:45
 */
public abstract class WTest extends ATest {

    protected static final String PAGINA_UNO = "Roman Protasevič";

    protected static final String PAGINA_DUE = "Gaetano Anzalone";

    protected static final String PAGINA_TRE = "Bernart Arnaut d'Armagnac";

    protected static final String PAGINA_QUATTRO = "Francesco Maria Pignatelli";

    protected static final String PAGINA_CINQUE = "Colin Campbell (generale)";

    protected static final String PAGINA_SEI = "Edwin Hall";

    protected static final String PAGINA_SETTE = "Louis Winslow Austin";

    protected static final String PAGINA_DISAMBIGUA = "Rossi";

    protected static final String PAGINA_REDIRECT = "Regno di Napoli (1805-1815)";


    protected String didascalia;

    protected WrapBio wrap;

    protected Bio bio;


    @InjectMocks
    protected GiornoService giornoService;

    @InjectMocks
    protected AnnoService annoService;

    @InjectMocks
    protected AttivitaService attivitaService;

    @InjectMocks
    protected NazionalitaService nazionalitaService;

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

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUpStartUp() {
        super.setUpStartUp();

        wInitMocks();
        wFixRiferimentiIncrociati();

        //        //--abilita il bot
        //        queryLogin.urlRequest();
    }


    /**
     * Inizializzazione dei service
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     */
    protected void wInitMocks() {
        MockitoAnnotations.initMocks(giornoService);
        Assertions.assertNotNull(giornoService);

        MockitoAnnotations.initMocks(annoService);
        Assertions.assertNotNull(annoService);

        MockitoAnnotations.initMocks(attivitaService);
        Assertions.assertNotNull(attivitaService);

        MockitoAnnotations.initMocks(nazionalitaService);
        Assertions.assertNotNull(nazionalitaService);

        MockitoAnnotations.initMocks(didascaliaService);
        Assertions.assertNotNull(didascaliaService);

        MockitoAnnotations.initMocks(wikiBotService);
        Assertions.assertNotNull(wikiBotService);

        MockitoAnnotations.initMocks(bioUtilityService);
        Assertions.assertNotNull(bioUtilityService);

        MockitoAnnotations.initMocks(bioService);
        Assertions.assertNotNull(bioService);

        MockitoAnnotations.initMocks(elaboraService);
        Assertions.assertNotNull(elaboraService);

        MockitoAnnotations.initMocks(botLogin);
        Assertions.assertNotNull(botLogin);

        MockitoAnnotations.initMocks(queryLogin);
        Assertions.assertNotNull(queryLogin);

        MockitoAnnotations.initMocks(queryAssert);
        Assertions.assertNotNull(queryAssert);

        MockitoAnnotations.initMocks(queryCat);
        Assertions.assertNotNull(queryCat);

        MockitoAnnotations.initMocks(queryPages);
        Assertions.assertNotNull(queryPages);

        MockitoAnnotations.initMocks(queryTimestamp);
        Assertions.assertNotNull(queryTimestamp);

        MockitoAnnotations.initMocks(queryBio);
        Assertions.assertNotNull(queryBio);
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

        elaboraService.text = textService;
        elaboraService.giornoService = giornoService;
        elaboraService.annoService = annoService;
        giornoService.annotation = annotationService;
        giornoService.text = textService;
        giornoService.mongo = mongoService;
        annoService.text = textService;
        annoService.mongo = mongoService;
        annoService.annotation = annotationService;

        queryAssert.botLogin = botLogin;

        queryLogin.queryAssert = queryAssert;
        queryLogin.text = textService;
        queryLogin.wikiApi = wikiApiService;
        queryLogin.logger = loggerService;
        queryLogin.appContext = appContext;
        queryLogin.botLogin = botLogin;

        queryCat.wikiApi = wikiApiService;
        queryCat.wikiBot = wikiBotService;
        queryCat.logger = loggerService;
        queryCat.botLogin = botLogin;
        queryCat.queryAssert = queryAssert;

        queryPages.botLogin = botLogin;
        queryPages.queryAssert = queryAssert;
        queryPages.array = arrayService;

        queryTimestamp.botLogin = botLogin;
        queryTimestamp.queryAssert = queryAssert;
        queryTimestamp.array = arrayService;

        didascaliaService.text = textService;

        bioService.text = textService;
        bioService.annotation = annotationService;
        bioService.reflection = reflectionService;

        queryBio.text = textService;
        queryBio.wikiApi = wikiApiService;

        elaboraService.bioUtility = bioUtilityService;
        elaboraService.logger = loggerService;

        bioUtilityService.text = textService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUp() {
        super.setUp();
    }


    protected void print(final Bio bio, final String nomeCognome, final String attivitaNazionalita) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("NomeCognome: %s", nomeCognome));
        System.out.println(String.format("AttivitaNazionalita: %s", attivitaNazionalita));
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

}
