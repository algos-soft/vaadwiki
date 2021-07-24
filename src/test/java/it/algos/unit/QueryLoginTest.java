package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.wiki.*;
import static it.algos.vaadwiki.backend.service.AWikiBotService.*;
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
@Tag("QueryLoginTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryLoginTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    private QueryLogin istanza;

    @InjectMocks
    private QueryBot queryBot;

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

        MockitoAnnotations.initMocks(queryBot);
        Assertions.assertNotNull(queryBot);
        queryBot.text = text;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }

    @Test
    @Order(1)
    @DisplayName("urlRequest")
    void urlRequest() {
        istanza.urlRequest();
        ottenutoBooleano = istanza.isLoginValido();
        assertTrue(ottenutoBooleano);
        System.out.println(String.format("Collegamento: %s", ottenutoBooleano));
        System.out.println(String.format("%s: %d", AWikiApiService.LOGIN_USER_ID, istanza.getLguserid()));
        System.out.println(String.format("%s: %s", AWikiApiService.LOGIN_USER_NAME, istanza.getLgusername()));

        String url = WIKI_PARSE + PAGINA_TEST + WIKI_QUERY_BOT;
        ottenutoRisultato = web.legge(url);
        Map mappaUno = istanza.getCookiesFromPreliminary();
        Map mappaDue = istanza.getCookiesFromSecondary();
        String token=istanza.getLgtoken();
        mappaDue.put("itwikiToken",token);
//        queryBot = appContext.getBean(QueryBot.class, mappa);
        queryBot.setTestoPost(istanza.getTestoPost());
        queryBot.urlRequest(mappaDue);
        boolean collegato = queryBot.isBot();
        int a = 87;

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