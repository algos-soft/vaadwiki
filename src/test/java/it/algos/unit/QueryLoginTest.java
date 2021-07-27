package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.wiki.query.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 08-lug-2021
 * Time: 19:01
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryLoginTest extends ATest {

    /**
     * Valori di controllo ricevuto <br>
     */
    public static final int LG_ID = 124123;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    private QueryLogin istanza;

    @InjectMocks
    private BotLogin botLogin;

    @InjectMocks
    private QueryAssert queryAssert;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(istanza);
        Assertions.assertNotNull(istanza);
        istanza.wikiApi = wikiApi;
        istanza.text = text;
        istanza.logger = logger;

        MockitoAnnotations.initMocks(botLogin);
        Assertions.assertNotNull(botLogin);
        istanza.botLogin = botLogin;

        MockitoAnnotations.initMocks(queryAssert);
        Assertions.assertNotNull(queryAssert);
        queryAssert.botLogin = botLogin;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        istanza = new QueryLogin();
        MockitoAnnotations.initMocks(istanza);
        Assertions.assertNotNull(istanza);
        istanza.wikiApi = wikiApi;
        istanza.text = text;
        istanza.logger = logger;
        istanza.botLogin = botLogin;

        MockitoAnnotations.initMocks(botLogin);
        Assertions.assertNotNull(botLogin);
        istanza.botLogin = botLogin;
        queryAssert.botLogin = botLogin;
        botLogin.setResult(null);
    }

    @Test
    @Order(1)
    @DisplayName("1 - urlRequest di queryLogin (errata)")
    void urlRequest() {
        System.out.println("1 - Valore errato di LG_NAME");

        String oldValue = istanza.LG_NAME;
        istanza.LG_NAME = "Valore errato";

        previsto = JSON_FAILED;
        ottenutoRisultato = istanza.urlRequest();
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getErrorCode());
        printRisultato(ottenutoRisultato);

        istanza.LG_NAME = oldValue;
    }

    @Test
    @Order(2)
    @DisplayName("2 - urlRequest di queryLogin (valida)")
    void urlRequest2() {
        System.out.println("2 - Valori validi");

        previsto = JSON_SUCCESS;
        ottenutoRisultato = istanza.urlRequest();
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(3)
    @DisplayName("3 -urlRequest di queryAssert (errata)")
    void urlRequest3() {
        System.out.println("3 - Manca il botLogin");
        queryAssert.botLogin = null;

        previsto = JSON_BOT_LOGIN;
        ottenutoRisultato = queryAssert.urlRequest();
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getErrorCode());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(4)
    @DisplayName("4 - urlRequest di queryAssert (errata)")
    void urlRequest4() {
        System.out.println("4 - Il botLogin non ha registrato nessuna chiamata di QueryLogin");

        previsto = JSON_NOT_QUERY_LOGIN;
        ottenutoRisultato = queryAssert.urlRequest();
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getErrorCode());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(5)
    @DisplayName("5 - urlRequest di queryAssert (errata)")
    void urlRequest5() {
        System.out.println("5 - La mappa di botLogin non è valida/corretta");
        //--tarocco la mappa di botLogin
        Map mappa = new HashMap();
        mappa.put("key", "value");
        AIResult result = AResult.errato();
        result.setMappa(mappa);
        queryAssert.botLogin.setResult(result);

        previsto = JSON_NO_BOT;
        ottenutoRisultato = queryAssert.urlRequest();
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getErrorCode());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(6)
    @DisplayName("6 - urlRequest di queryAssert (valida)")
    void urlRequest6() {
        System.out.println("6 - Collegato come bot");

        istanza.urlRequest();
        previsto = KEY_JSON_VALID;
        ottenutoRisultato = queryAssert.urlRequest();
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);
    }

    //    @Test
    @Order(7)
    @DisplayName("urlRequest")
    void urlRequest7() {
        sorgente = "Cookie: WMF-Last-Access=24-Jul-2021; WMF-Last-Access-Global=24-Jul-2021; itwikiss0-UserName=Gac; itwikiUserName=Gac; VEE=wikitext; wikiSince=1627143985406; itwikithanks-thanked=118106947%252C118140985%252C118146553%252C118160226%252C118160330%252C118174905%252C118183609%252C118194551%252C118214856%252C118218850%252C118229102%252C118264621%252C118265962%252C118275057%252C118304217%252C118342828%252C118418592%252C118454616%252C118493849%252C118504914%252C118504930%252C118504849%252C118525612%252C118531912%252C118679037%252C118695932%252C%252C118761957%252C118762882%252C118764939%252C118771904%252C118780381%252C118782338%252C118803117%252C118454783%252C118833381%252C118841772%252C118844048%252C118844642%252C118853221%252C118870680%252C118874040%252C118993644%252C119071987%252C119080526%252C119149399%252C119176205%252C119183664%252C119203546%252C119358933%252C119383926%252C119563099%252C119659505%252C119667715%252C119816797%252C119947285%252C119994968%252C120012663%252C119816846%252C120027822%252C120112944%252C120113385%252C120124280%252C120144984%252C120205959%252C120365382%252C120390182%252C120413421%252C120418138%252C120450321%252C120483848%252C120491021%252C120506952%252C120625811%252C120690691%252C120729375%252C120763947%252C120819845%252C120988304%252C120988594%252C120999015%252C121016125%252C121322626%252C121322990%252C121334562%252C121464917%252C121541280%252C121542585%252C121554543%252C121611991%252C121654976%252C121720788%252C121904180%252C122004342%252C122017880%252C122013053%252C122030106%252C122046871%252C122048634%252C122085057; wikiEditor-0-toolbar-section=advanced; wikiEditor-0-booklet-characters-page=symbols; itwikiwmE-sessionTickLastTickTime=1627145258722; itwikiwmE-sessionTickTickCount=38; cx_campaign_newarticle_hide=1; itwikircfilters-toplinks-collapsed-state=expanded; loginnotify_prevlogins=2021-8amzfv-8t95gfkij79vvdpy2jsfkmn36sob5f4; itwikiss0-UserID=399; itwikiUserID=399; centralauth_ss0-User=Gac; centralauth_User=Gac; centralauth_ss0-Token=95e58fce4cef8d7cf64a18d1e1632a9d; centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; ss0-itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; ss0-centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; GeoIP=IT:45:Gazzola:44.96:9.55:v4; itwikimwuser-sessionId=e81003ad326bdb8c7345; itwikiel-sessionId=420f19e61fed3bf6dc75";

        sorgente = "itwikiss0-UserName=Gac; itwikiUserName=Gac; VEE=wikitext; wikiSince=1627143985406; wikiEditor-0-toolbar-section=advanced; wikiEditor-0-booklet-characters-page=symbols; itwikiwmE-sessionTickLastTickTime=1627145258722; itwikiwmE-sessionTickTickCount=38; cx_campaign_newarticle_hide=1; itwikircfilters-toplinks-collapsed-state=expanded; loginnotify_prevlogins=2021-8amzfv-8t95gfkij79vvdpy2jsfkmn36sob5f4; itwikiss0-UserID=399; itwikiUserID=399; centralauth_ss0-User=Gac; centralauth_User=Gac; centralauth_ss0-Token=95e58fce4cef8d7cf64a18d1e1632a9d; centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; ss0-itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; ss0-centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; GeoIP=IT:45:Gazzola:44.96:9.55:v4; itwikimwuser-sessionId=e81003ad326bdb8c7345; itwikiel-sessionId=420f19e61fed3bf6dc75";

        sorgente = "itwikiss0-UserName=Gac; itwikiUserName=Gac;    itwikiwmE-sessionTickLastTickTime=1627145258722; itwikiwmE-sessionTickTickCount=38; cx_campaign_newarticle_hide=1; itwikircfilters-toplinks-collapsed-state=expanded; loginnotify_prevlogins=2021-8amzfv-8t95gfkij79vvdpy2jsfkmn36sob5f4; itwikiss0-UserID=399; itwikiUserID=399; centralauth_ss0-User=Gac; centralauth_User=Gac; centralauth_ss0-Token=95e58fce4cef8d7cf64a18d1e1632a9d; centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; ss0-itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; ss0-centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; GeoIP=IT:45:Gazzola:44.96:9.55:v4; itwikimwuser-sessionId=e81003ad326bdb8c7345; itwikiel-sessionId=420f19e61fed3bf6dc75";

        sorgente = "itwikiss0-UserName=Gac; itwikiUserName=Gac;   itwikiss0-UserID=399; itwikiUserID=399; centralauth_ss0-User=Gac; centralauth_User=Gac; centralauth_ss0-Token=95e58fce4cef8d7cf64a18d1e1632a9d; centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; ss0-itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; ss0-centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149;  itwikimwuser-sessionId=e81003ad326bdb8c7345; itwikiel-sessionId=420f19e61fed3bf6dc75";

        sorgente = "     centralauth_ss0-User=Gac; centralauth_User=Gac; centralauth_ss0-Token=95e58fce4cef8d7cf64a18d1e1632a9d; centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; ss0-itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; ss0-centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149;  itwikimwuser-sessionId=e81003ad326bdb8c7345; itwikiel-sessionId=420f19e61fed3bf6dc75";

        sorgente = "    centralauth_User=Gac;  centralauth_ss0-Token=95e58fce4cef8d7cf64a18d1e1632a9d; centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; ss0-itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac; ss0-centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149;   itwikiel-sessionId=420f19e61fed3bf6dc75 ";

        sorgente = "    centralauth_User=Gac;  centralauth_ss0-Token=95e58fce4cef8d7cf64a18d1e1632a9d; centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac;  centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149;  ";

        sorgente = "    centralauth_User=Gac;   centralauth_Token=95e58fce4cef8d7cf64a18d1e1632a9d; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac;  centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149;  ";
        sorgente = "    centralauth_User=Gac;       centralauth_Session=ee1c87fca3201a5b50bdb870c18c4149; ";

        //        sorgente=" itwikiUserName=Gac; itwikiSession=p4k2oivhnncrjo8unvse45ir0033jcac;";
        //        sorgente=" itwikiss0-UserName=Gac; itwikimwuser-sessionId=e81003ad326bdb8c7345;";
        //        ottenutoBooleano= queryAssert.urlRequest(sorgente);
        System.out.println(ottenuto);
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterEach
    void tearDownAll() {
    }

}