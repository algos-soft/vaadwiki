package it.algos.integration;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadwiki.*;
import it.algos.vaadwiki.wiki.query.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;

import java.time.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 12-feb-2022
 * Time: 20:38
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@SpringBootTest(classes = {WikiApplication.class})
@DisplayName("Test Integration QueryWrite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllIntegration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryWriteIntegrationTest extends WTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private QueryWrite istanza;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        istanza = queryWrite;

        //--titolo della query
        queryType = istanza.getClass().getSimpleName();

        //--abilitato SOLO per i test Integration
        Assertions.assertNotNull(appContext);
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }

    @Test
    @Order(1)
    @DisplayName("1 - Tentativo di scrittura senza bot")
    void urlRequestSenzaBot() {
        sorgente = QueryWrite.WIKI_TITLE_DEBUG;
        sorgente2 = "Prova di scrittura";

        ottenutoRisultato = istanza.urlRequest(sorgente, sorgente2);
        Assertions.assertNotNull(ottenutoRisultato);
        Assertions.assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato,queryType);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Abilita il bot per i test successivi")
    void abilitaBot() {
        String tag = "collegamento";
        String tag2 = "botLogin";
        Map<String, Object> mappa;

        queryLogin.urlRequest();

        mappa = botLogin.getCookies();
        System.out.println(String.format("Abilitato il %s come UserType=%s", tag, botLogin.getUserType()));
        System.out.println(String.format("Abilitato il %s con Userid=%s", tag, botLogin.getUserid()));
        System.out.println(String.format("Abilitato il %s con Username=%s", tag, botLogin.getUsername()));
        System.out.println(String.format("Nel %s ci sono %d cookies", tag2, mappa.size()));

        System.out.println(VUOTA);
        for (String key : mappa.keySet()) {
            System.out.print(key);
            System.out.print(FORWARD);
            System.out.print(mappa.get(key));
            System.out.println();
        }

    }

    @Test
    @Order(3)
    @DisplayName("3 - Tentativo di scrivere una pagina senza titolo")
    void urlRequestSenzaTitolo() {
        sorgente = VUOTA;
        sorgente2 = VUOTA;

        ottenutoRisultato = istanza.urlRequest(sorgente, sorgente2);
        Assertions.assertNotNull(ottenutoRisultato);
        Assertions.assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato,queryType);
    }

    @Test
    @Order(4)
    @DisplayName("4 - Tentativo di scrivere una pagina senza testo")
    void urlRequestSenzaTesto() {
        sorgente = QueryWrite.WIKI_TITLE_DEBUG;
        sorgente2 = VUOTA;

        ottenutoRisultato = istanza.urlRequest(sorgente, sorgente2);
        Assertions.assertNotNull(ottenutoRisultato);
        Assertions.assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato,queryType);
    }

    @Test
    @Order(5)
    @DisplayName("5 - Scrittura con bot eseguita due volte")
    void urlRequestConBot() {
        System.out.println(String.format("Scrittura con bot eseguita due volte"));
        System.out.println(VUOTA);

        sorgente = QueryWrite.WIKI_TITLE_DEBUG;
        sorgente2 = "Prova di scrittura delle " + LocalDateTime.now();

        ottenutoRisultato = istanza.urlRequest(sorgente, sorgente2);
        Assertions.assertNotNull(ottenutoRisultato);
        Assertions.assertTrue(ottenutoRisultato.isValido());
        System.out.println(String.format("La prima volta esegue correttamente"));
        printRisultato(ottenutoRisultato,queryType);
        System.out.println(VUOTA);
        System.out.println(VUOTA);

        ottenutoRisultato = istanza.urlRequest(sorgente, sorgente2);
        Assertions.assertNotNull(ottenutoRisultato);
        Assertions.assertTrue(ottenutoRisultato.isValido());
        System.out.println(String.format("La seconda volta trova lo stesso contenuto e NON modifica"));
        printRisultato(ottenutoRisultato,queryType);
    }

    @Test
    @Order(6)
    @DisplayName("6 - Scrittura con bot and summary")
    void urlRequestConBotSummary() {
        sorgente = QueryWrite.WIKI_TITLE_DEBUG;
        sorgente2 = "Prova di scrittura delle " + LocalDateTime.now();
        sorgente3 = "Test di scrittura";

        ottenutoRisultato = istanza.urlRequest(sorgente, sorgente2, sorgente3);
        Assertions.assertNotNull(ottenutoRisultato);
        Assertions.assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato,queryType);
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