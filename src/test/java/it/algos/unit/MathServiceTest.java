package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 13-ago-2020
 * Time: 19:23
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("MathService - Utility matematiche.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MathServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private MathService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = mathService;
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


    @Test
    @Order(1)
    @DisplayName("Divisione double")
    void divisione() {
        previstoDouble = 3.5;
        double dividendo = 7;
        double divisore = 2;

        ottenutoDouble = service.divisione(dividendo, divisore);
        assertEquals(previstoDouble, ottenutoDouble);
    }


    @Test
    @Order(2)
    @DisplayName("Divisione interi")
    void divisione2() {
        previstoDouble = 3.5;
        dividendo = 7;
        divisore = 2;
        ottenutoDouble = service.divisione(dividendo, divisore);
        assertEquals(previstoDouble, ottenutoDouble);

        previstoDouble = 18;
        dividendo = 450;
        divisore = 25;
        ottenutoDouble = service.divisione(dividendo, divisore);
        assertEquals(previstoDouble, ottenutoDouble);

        previstoIntero = 18;
        dividendo = 450;
        divisore = 25;
        ottenutoIntero = (int) service.divisione(dividendo, divisore);
        assertEquals(previstoIntero, ottenutoIntero);
    }


    @Test
    @Order(3)
    @DisplayName("Percentuale")
    void percentuale() {
        dividendo = 8;
        divisore = 200;
        previstoDouble = 4;
        ottenutoDouble = service.percentuale(dividendo, divisore);
        assertNotNull(ottenutoDouble);
        assertEquals(previstoDouble, ottenutoDouble);
    }


    @Test
    @Order(4)
    @DisplayName("Percentuale due decimali")
    void percentuale2() {
        dividendo = 8;
        divisore = 200;
        previsto = "4,00%";
        ottenuto = service.percentualeDueDecimali(dividendo, divisore);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        dividendo = 8;
        divisore = 19;
        previsto = "42,11%";
        ottenuto = service.percentualeDueDecimali(dividendo, divisore);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        dividendo = 8;
        divisore = 190;
        previsto = "4,21%";
        ottenuto = service.percentualeDueDecimali(dividendo, divisore);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        dividendo = 8;
        divisore = 1900;
        previsto = "0,42%";
        ottenuto = service.percentualeDueDecimali(dividendo, divisore);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        dividendo = 8;
        divisore = 19000;
        previsto = "0,04%";
        ottenuto = service.percentualeDueDecimali(dividendo, divisore);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(5)
    @DisplayName("Numero di cicli")
    void numCicli() {
        int totale = 100;
        int blocco = 25;
        previstoIntero = 4;
        ottenutoIntero = service.numCicli(totale, blocco);
        assertEquals(previstoIntero, ottenutoIntero);
        stampa(totale, blocco, ottenutoIntero);

        totale = 90;
        blocco = 25;
        previstoIntero = 4;
        ottenutoIntero = service.numCicli(totale, blocco);
        assertEquals(previstoIntero, ottenutoIntero);
        stampa(totale, blocco, ottenutoIntero);

        totale = 90;
        blocco = 90;
        previstoIntero = 1;
        ottenutoIntero = service.numCicli(totale, blocco);
        assertEquals(previstoIntero, ottenutoIntero);
        stampa(totale, blocco, ottenutoIntero);

        totale = 70;
        blocco = 90;
        previstoIntero = 1;
        ottenutoIntero = service.numCicli(totale, blocco);
        assertEquals(previstoIntero, ottenutoIntero);
        stampa(totale, blocco, ottenutoIntero);
    }


    @Test
    @Order(6)
    @DisplayName("Divisibile esatto")
    void divisibileEsatto() {
        divisore = 4;

        dividendo = 1;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);

        dividendo = 2;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);

        dividendo = 3;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);

        dividendo = 4;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertTrue(ottenutoBooleano);

        dividendo = 5;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);

        dividendo = 6;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);

        dividendo = 7;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);

        dividendo = 8;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertTrue(ottenutoBooleano);

        dividendo = 9;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);

        dividendo = 10;
        ottenutoBooleano = service.divisibileEsatto(dividendo, divisore);
        assertFalse(ottenutoBooleano);
    }


    private void stampa(int totale, int blocco, int cicli) {
        System.out.println("Per spazzolare " + totale + " elementi a blocchi di " + blocco + " occorrono " + cicli + " cicli");
    }


}