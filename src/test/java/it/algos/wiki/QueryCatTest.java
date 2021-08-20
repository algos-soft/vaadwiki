package it.algos.wiki;

import it.algos.test.*;
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
 * Date: dom, 25-lug-2021
 * Time: 22:18
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("QueryCat - Istanza per una query categoria.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryCatTest extends WTest {

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

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private QueryCat istanza;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        istanza = queryCat;
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
    @DisplayName("1 - Legge (come bot) una lista corta di pageid di una categoria wiki")
    void urlRequest1() {
        System.out.println("1 - Legge (come bot) una lista corta di pageid di una categoria wiki");

        sorgente = CAT_1435;
        previsto = JSON_SUCCESS;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);
        System.out.println(String.format("Risultato ottenuto in esattamente %s", dateService.deltaTextEsatto(inizio)));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Cerca di leggere (come bot) una lista di pageid di una categoria wiki inesistente")
    void urlRequest2() {
        System.out.println("2 - Cerca di leggere (come bot) una lista di pageid di una categoria wiki inesistente");

        sorgente = CAT_INESISTENTE;
        previsto = "Inesistente";
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getErrorCode());
        assertEquals(VUOTA, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Cerca di leggere (senza bot) una lista di pageid di una categoria wiki")
    void urlRequest3() {
        System.out.println("3 - Cerca di leggere (senza bot) una lista di pageid di una categoria wiki");

        //--tarocco -provvisoriamente- la mappa di botLogin
        Map cookiesValidi = botLogin.getCookies();
        Map mappaNewTaroccata = new HashMap();
        mappaNewTaroccata.put("key", "value");
        AIResult result = AResult.errato();
        result.setMappa(mappaNewTaroccata);
        botLogin.setResult(result);

        sorgente = CAT_1167;
        previsto = JSON_NO_BOT;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getErrorCode());
        printRisultato(ottenutoRisultato);

        //--ripristino la mappa di botLogin
        botLogin.getResult().setMappa(cookiesValidi);
    }



    @Test
    @Order(4)
    @DisplayName("4 - Legge (come bot) una lista media di pageid di una categoria wiki")
    void urlRequest4() {
        System.out.println("4 - Legge (come bot) una lista media di pageid di una categoria wiki");

        sorgente = CAT_1935;
        previsto = JSON_SUCCESS;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);
        System.out.println(String.format("Risultato ottenuto in esattamente %s", dateService.deltaTextEsatto(inizio)));
    }

//    @Test
    @Order(5)
    @DisplayName("5 - Legge (come bot) una lista lunga di pageid di una categoria wiki")
    void urlRequest5() {
        System.out.println("5 - Legge (come bot) una lista lunga di pageid di una categoria wiki");

        sorgente = CATEGORIA_BIO;
        previsto = JSON_SUCCESS;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato);
        System.out.println(String.format("Risultato ottenuto in esattamente %s", dateService.deltaTextEsatto(inizio)));
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