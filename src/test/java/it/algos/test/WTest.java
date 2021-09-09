package it.algos.test;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.packages.prenome.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import it.algos.vaadwiki.wiki.query.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

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

    protected static final String PAGINA_OTTO = "Sergio Ferrero";

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

    @InjectMocks
    protected GiornoService giornoService;

    @InjectMocks
    protected AnnoService annoService;

    @InjectMocks
    protected AttivitaService attivitaService;

    @InjectMocks
    protected NazionalitaService nazionalitaService;

    @InjectMocks
    protected PrenomeService prenomeService;

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
        wCreaBio();
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

        MockitoAnnotations.initMocks(prenomeService);
        Assertions.assertNotNull(prenomeService);

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
        attivitaService.mongo = mongoService;
        nazionalitaService.mongo = mongoService;

        queryAssert.botLogin = botLogin;

        queryLogin.queryAssert = queryAssert;
        queryLogin.text = textService;
        queryLogin.wikiApi = wikiApiService;
        queryLogin.logger = loggerService;
        queryLogin.appContext = appContext;
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
        queryBio.appContext = appContext;

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
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUp() {
        super.setUp();

        listaWrapBio = null;
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
        System.out.println(String.format("Numeric value: %s", textService.format(result.getValue())));
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

}
