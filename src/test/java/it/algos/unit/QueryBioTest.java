package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.wiki.*;
import it.algos.vaadwiki.wiki.query.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-ago-2021
 * Time: 09:30
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test QueryBio")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryBioTest extends ATest {

    public static final String PAGINA_ESISTENTE_MAIUSCOLA = "Pippoz";

    public static final String PAGINA_INESISTENTE_MINUSCOLA = "pippoz";

    public static final String PAGINA_INESISTENTE_CON_SPAZI = "Questa pagina non esiste";

    @InjectMocks
    public AWikiApiService wikiApi;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    QueryBio istanza;


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
        istanza.array = array;
        istanza.text = text;
        istanza.wikiApi = wikiApi;

        MockitoAnnotations.initMocks(wikiApi);
        Assertions.assertNotNull(wikiApi);
        wikiApi.text = text;
        wikiApi.web = web;
        wikiApi.jSonService = jSonService;
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
    @DisplayName("1 - Cerca di leggere una pagina inesistente")
    void urlRequest() {
        ottenutoRisultato = istanza.urlRequest(PAGINA_INESISTENTE);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Legge una pagina di redirect")
    void urlRequest2() {
    }

    @Test
    @Order(3)
    @DisplayName("3 - Legge una pagina di disambigua")
    void urlRequest3() {
    }

    @Test
    @Order(4)
    @DisplayName("4 - Legge una pagina SENZA tmpl Bio")
    void urlRequest4() {
    }

    @Test
    @Order(5)
    @DisplayName("5 - Legge una pagina con tmpl Bio")
    void urlRequest5() {
        //        System.out.println("5 - Legge (con bot) una lista (breve) di pagine");

        //        sorgenteArrayLong = LISTA_BREVE;
        //        previsto = JSON_SUCCESS;
        //        ottenutoRisultato = istanza.urlRequest(sorgenteArrayLong);
        //        assertTrue(ottenutoRisultato.isValido());
        //        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        //        printRisultato(ottenutoRisultato);
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