package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import org.json.simple.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 19-lug-2021
 * Time: 13:02
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("JSonService - Elaborazione testi JSON.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JSonServiceTest extends ATest {


    public static final String SORGENTE_UNO = "{\"error\":{\"code\":\"missingtitle\",\"info\":\"The page you specified doesn't exist.\",\"docref\":\"See https://it.wikipedia.org/w/api.php for API usage. Subscribe to the mediawiki-api-announce mailing list at &lt;https://lists.wikimedia.org/mailman/listinfo/mediawiki-api-announce&gt; for notice of API deprecations and breaking changes.\"},\"servedby\":\"mw2330\"}";


    private JSONObject objectAll;

    private JSONObject objectJson;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private JSonService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = jSonService;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        objectAll = null;
    }

    @Test
    @Order(1)
    @DisplayName("1 - Costruisce l'oggetto (mappa) principale JSON dal testo di risposta ad una query")
    void getObjectJSON() {
        sorgente = SORGENTE_UNO;

        objectAll = service.getObjectJSON(null);
        assertNotNull(objectAll);
        assertEquals(0, objectAll.size());

        objectAll = service.getObjectJSON(VUOTA);
        assertNotNull(objectAll);
        assertEquals(0, objectAll.size());

        previstoIntero = 2;
        objectAll = service.getObjectJSON(sorgente);
        assertNotNull(objectAll);
        assertEquals(previstoIntero, objectAll.size());

        System.out.println("Risposta della query");
        System.out.println(objectAll);

        printMappaJson(objectAll, "all");

    }


    @Test
    @Order(2)
    @DisplayName("2 - Conta quanti elementi contiene la mappa principale JSON di risposta ad una query")
    void getJSONSize() {
        System.out.println("Conta quanti elementi contiene la mappa principale JSON di risposta ad una query");
        System.out.println(VUOTA);
        sorgente = SORGENTE_UNO;

        previstoIntero = 0;
        ottenutoIntero = service.getJSONSize(null);
        assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = 0;
        ottenutoIntero = service.getJSONSize(VUOTA);
        assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = 2;
        ottenutoIntero = service.getJSONSize(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %s elementi nella mappa principale JSON", ottenutoIntero));
    }


    @Test
    @Order(3)
    @DisplayName("3 - Estrae un elemento (JSONObject) dalla mappa principale JSON di risposta ad una query")
    void getObj() {
        System.out.println("Estrae un elemento (JSONObject) dalla mappa principale JSON di risposta ad una query");

        objectJson = service.getObj(null, null);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        objectJson = service.getObj(VUOTA, VUOTA);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        objectJson = service.getObj(VUOTA, null);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        objectJson = service.getObj(null, VUOTA);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        objectJson = service.getObj(sorgente, VUOTA);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        objectJson = service.getObj(VUOTA, JSON_ERROR);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        objectJson = service.getObj(sorgente, null);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        objectJson = service.getObj(null, JSON_ERROR);
        assertNotNull(objectJson);
        assertEquals(0, objectJson.size());

        sorgente = SORGENTE_UNO;
        objectJson = service.getObj(sorgente, JSON_ERROR);
        assertNotNull(objectJson);
        printMappaJson(objectJson, JSON_ERROR);
    }


    void printMappaJson(JSONObject objectAll, String tag) {
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %s elementi (chiavi) nella mappa JSON: %s", objectAll.size(), tag));
        for (Object key : objectAll.keySet()) {
            System.out.print(key + DUE_PUNTI_SPAZIO);
            System.out.println(objectAll.get(key));
        }
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
    @AfterAll
    void tearDownAll() {
    }

}