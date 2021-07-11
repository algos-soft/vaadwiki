package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.wrapper.AResult;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 27-nov-2020
 * Time: 15:02
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("WrapperTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AWrapperTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    AResult result;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(result);
        Assertions.assertNotNull(result);
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
    @DisplayName("Result")
    void getLabelHost() {
        sorgente = "Messaggio di errore";
        sorgente2 = "Avviso per specificare il tipo di risultato positivo";
        sorgenteIntero = 87;

//        result = AResult.valido();
//        assertTrue(result.isValido());
//        assertEquals(VUOTA, result.getErrorMessage());
//
//        result = AResult.errato(sorgente);
//        assertFalse(result.isValido());
//        assertEquals(sorgente, result.getErrorMessage());
//
//        result = AResult.valido(sorgente2);
//        assertTrue(result.isValido());
//        assertEquals(VUOTA, result.getErrorMessage());
//        assertEquals(sorgente2, result.getValidationMessage());
//
//        result = AResult.valido(sorgente2,sorgenteIntero);
//        assertTrue(result.isValido());
//        assertEquals(VUOTA, result.getErrorMessage());
//        assertEquals(sorgente2, result.getValidationMessage());
//        assertEquals(sorgenteIntero, result.getValore());
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