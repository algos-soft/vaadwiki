package it.algos.wiki;

import it.algos.test.*;
import static it.algos.wiki.QueryCatTest.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.query.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 28-lug-2021
 * Time: 21:45
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("QueryPages - Istanza per una query timestamp.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryTimestampTest extends WTest {


    private static final List<Long> LISTA_BREVE = List.of(Long.valueOf(876876), Long.valueOf(793444), Long.valueOf(22223), Long.valueOf(50030044));


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private QueryTimestamp istanza;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        istanza = queryTimestamp;
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
    @DisplayName("1 - Cerca di leggere (senza bot) una lista di pageIds da controllare")
    void urlRequest() {
        System.out.println("1 - Cerca di leggere (senza bot) una lista di pageIds da controllare");

        //--tarocco -provvisoriamente- la mappa di botLogin
        Map cookiesValidi = botLogin.getCookies();
        Map mappaNewTaroccata = new HashMap();
        mappaNewTaroccata.put("key", "value");
        AIResult result = AResult.errato();
        result.setMappa(mappaNewTaroccata);
        botLogin.setResult(result);

        sorgenteArrayLong = LISTA_BREVE;
        previsto = JSON_NO_BOT;
        ottenutoRisultato = istanza.urlRequest(sorgenteArrayLong);
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getErrorCode());
        assertEquals(VUOTA, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);

        //--ripristino la mappa di botLogin
        botLogin.getResult().setMappa(cookiesValidi);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Legge (con bot) una lista (breve) di pageIds da controllare")
    void urlRequest2() {
        System.out.println("2 - Legge (con bot) una lista (breve) di pageIds da controllare");

        sorgenteArrayLong = LISTA_BREVE;
        previsto = JSON_SUCCESS;
        ottenutoRisultato = istanza.urlRequest(sorgenteArrayLong);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Legge (con bot) una lista (media) di pageIds da controllare")
    void urlRequest3() {
        System.out.println("3 - Legge (con bot) una lista (media) di pageIds da controllare");

        sorgente = CAT_1935;
        previsto = JSON_SUCCESS;
        ottenutoRisultato = queryCat.urlRequest(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        sorgenteArrayLong = ottenutoRisultato.getLista();
        previsto = JSON_SUCCESS;
        ottenutoRisultato = istanza.urlRequest(sorgenteArrayLong);
//        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
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