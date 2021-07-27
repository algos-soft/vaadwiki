package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
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
 * Date: dom, 25-lug-2021
 * Time: 22:18
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("QueryCatTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryCatTest extends ATest {

    public static final String CAT_INESISTENTE = "Nati nel 3435";

    public static final String CAT_1167 = "Nati nel 1167";

    public static final String CAT_1435 = "Nati nel 1435";

    public static final int TOT_1435 = 33;

    public static final String CAT_1591 = "Nati nel 1591";

    public static final String CAT_1935 = "Nati nel 1935";

    public static final int TOT_1935 = 1990;

    public static final String CAT_1713 = "Nati nel 1713";

    public static final String CAT_2020 = "Morti nel 2020";

    public static final int TOT_2020 = 2392;

    public static final String CAT_ROMANI = "Personaggi della storia romana";

    private static final String CATEGORIA_BIO = "BioBot";

    @InjectMocks
    public BotLogin botLogin;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    QueryCat istanza;

    @InjectMocks
    private QueryLogin queryLogin;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(istanza);
        Assertions.assertNotNull(istanza);
        istanza.text = text;
        istanza.logger = logger;
        istanza.wikiApi = wikiApi;

        MockitoAnnotations.initMocks(queryLogin);
        Assertions.assertNotNull(queryLogin);
        queryLogin.text = text;

        MockitoAnnotations.initMocks(botLogin);
        Assertions.assertNotNull(botLogin);
        istanza.botLogin = botLogin;
        queryLogin.botLogin = botLogin;

        assertTrue(queryLogin.urlRequest().isValido());
        assertTrue(queryLogin.isLoginValido());
        assertEquals(JSON_SUCCESS, queryLogin.getStatus());
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
    @DisplayName("Primo test")
    void urlRequest() {
        System.out.println("1 - Legge (come bot) una lista di pageid di una categoria wiki");

        sorgente = CAT_INESISTENTE;
        previstoIntero = 0;
        ottenutoArrayLong = istanza.urlRequest(sorgente).getLista();
        ottenuto = istanza.getUrlResponse();
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' non esiste", sorgente));
        System.out.println(String.format("UrlResponse: %s", istanza.getUrlResponse()));

        sorgente = CAT_1167;
        previstoIntero = 6;
        ottenutoArrayLong = istanza.urlRequest(sorgente).getLista();
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati in %s", sorgente, ottenutoArrayLong.size(), getTime()));
        System.out.println(String.format("UrlResponse: %s", istanza.getUrlResponse()));
        print10(ottenutoArrayLong);

        sorgente = CAT_1435;
        previstoIntero = TOT_1435;
        ottenutoArrayLong = istanza.urlRequest(sorgente).getLista();
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati in %s", sorgente, ottenutoArrayLong.size(), getTime()));
        System.out.println(String.format("UrlResponse: %s", istanza.getUrlResponse()));
        print10(ottenutoArrayLong);

        sorgente = CAT_1935;
        previstoIntero = TOT_1935;
        ottenutoArrayLong = istanza.urlRequest(sorgente).getLista();
        assertNotNull(ottenutoArrayLong);
        assertEquals(previstoIntero, ottenutoArrayLong.size());
        System.out.println(VUOTA);
        System.out.println(String.format("La categoria '%s' contiene %s pageIds recuperati in %s", sorgente, text.format(ottenutoArrayLong.size()), date.deltaText(inizio)));
        System.out.println(String.format("UrlResponse: %s", istanza.getUrlResponse()));
        print10(ottenutoArrayLong);

    }

    void print10(List<Long> lista) {
        int max = Math.min(10, lista.size());

        System.out.print("PageIds (primi 10): ");
        for (int k = 0; k < max - 1; k++) {
            System.out.print(lista.get(k));
            System.out.print(VIRGOLA_SPAZIO);
        }
        System.out.print(lista.get(max - 1));
        System.out.println(VUOTA);
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